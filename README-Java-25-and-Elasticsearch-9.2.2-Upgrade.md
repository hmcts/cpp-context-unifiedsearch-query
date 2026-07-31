# Java 25 / WildFly 40 / Elasticsearch 9.2.2 upgrade — guide for the unifiedsearch-query team

This branch (`dev/java-25-es-9.2.2`, draft PR **#27**) upgrades unifiedsearch-query to the **25.104.x** line
(Java 25 / WildFly 40 / Jakarta EE 11) **and** to **Elasticsearch 9.2.2**. It was prepared by the platform/framework
upgrade effort (ticket **PEG-3408**, mirroring the Java-17 ES 9.2 work in **DD-41592**) as a *proving* exercise.

**The decision to accept, finish and release these changes is yours.** This document explains what we changed, the
decisions we made and why, the gotchas we hit, and exactly what you need to do to take it over the line.

---

## Why

- Elasticsearch 9.2.2 is being rolled out on the Java-17 line; we needed to prove it also works on the Java-25 stack.
- Folded into the July 2026 security-hardening work (jackson `2.21.5` etc.).

## What changed (summary)

- **Parent** → `service-parent-pom:25.104.0-M8-SNAPSHOT` (Java 25 / WildFly 40 / Jakarta EE 11).
- **`javax.*` → `jakarta.*`** across all modules; `javax:javaee-api` → `jakarta.platform:jakarta.jakartaee-api`;
  `jakarta.xml.bind-api` override added to the RAML client-generator plugins; RESTEasy status-code constant fix in ITs.
- **Elasticsearch client** migrated from `RestHighLevelClient` to `co.elastic.clients:elasticsearch-java` +
  `elasticsearch-rest-client` (this lives in `cpp-platform-libraries` and is shared; cherry-picked from DD-41592).
- Two small **context-code fixes** (below).

---

## Decisions we made — please review these

### 1. ⚠️ `pageSize=0` now returns the default page (BEHAVIOUR CHANGE — API Test risk)

This is the decision most worth your attention.

- **Before:** `pageSize=0` → Elasticsearch `size:0` → matched documents but returned an **empty list**.
- **Now:** `pageSize=0` — **and an *absent* `pageSize`** — returns the **default page of 10**.
- **Unchanged:** a **negative** `pageSize` is still rejected (`BadRequestException "Invalid page size"`).

**Why we had to change it:** the framework materialises an *absent* numeric query param as `0` (verified for `int`
*and* `Integer` in the deployed bytecode), and the RAML `default:` is **not** emitted as a JAX-RS `@DefaultValue` by
the generator. So "absent" and "explicit 0" are indistinguishable by the time our code sees them — and an absent
`pageSize` was reaching ES as `size:0` and returning nothing. We chose the contract **`0` → default page of 10**
(a zero-size page is meaningless). Implemented in `getPageSize()` of `CpsQueryParameters` and `QueryParameters`.

**What you should do:** check your **API Tests** (the cross-context tests that use real contexts). If one that calls
unifiedsearch-query with `pageSize=0` previously asserted an empty list, it will now get the default page — update it
(send an explicit positive `pageSize`, or expect the default page). If you'd prefer a different contract, this is the
one decision we'd flag for a conversation before release.

### 2. `CpsCaseSortBy.findByKeyNameOrDefault`: `putIfAbsent` → `computeIfAbsent`

The original used `Map.putIfAbsent`, which **returns null** for a key that wasn't present — so an unrecognised
`orderBy` value produced a 500. Changed to `computeIfAbsent(key -> URN)` so an unknown key resolves to the `URN`
default (and is cached), which is what the method name promises.

### 3. Shared ES-client change lives in the platform, not here

The `RestHighLevelClient` → `co.elastic` migration is in `cpp-platform-libraries` (shared by all search contexts),
not in this repo. You inherit it via the platform version. `httpcore5` is pinned to `5.3.6` in the platform BOM
(ES 9.2.2 pulls `5.2.1`, which clashes with `httpclient5 5.5.1` → `NoSuchMethodError` — only shows up in ITs).

---

## Gotchas we hit (so you don't have to)

- **RAML `default:` on a query param is not honoured** by this generator, and the framework turns an absent numeric
  param into `0` — hence decision #1. `putOptional` omits nulls but doesn't recover the absent-vs-0 distinction.
- **Local Elasticsearch (dev docker)** needed ES-9 changes: new base image, dev TLS-off + security enabled, non-root
  file permissions, and the security-provisioning API path `_xpack/security` → `_security`. These are in
  `cpp-developers-docker` (already done); you only need an up-to-date local stack to run the ITs.
- ES 9 sorting on `keyword` / text `.keyword` fields works unchanged.

---

## Test evidence

- **Full-stack integration tests: 332 / 0 / 0** (3 skipped) against a live ES 9.2.2 container + WildFly 40 on JDK 25.
- 202 unit / embedded-ES tests green; platform ES-client migration 92 tests green.

---

## What you need to do to accept & release

1. **Review decision #1** (`pageSize=0`) with whoever owns the API Tests, and decisions #2–#3.
2. Wait for the **25.104.x framework/platform milestones to be released** to Artifactory (this PR is a **draft**
   because it currently references a `-SNAPSHOT` parent, so CI can't build it yet).
3. Bump the **parent** from `25.104.0-M8-SNAPSHOT` to the released milestone; likewise any other SNAPSHOT platform deps.
4. Run the build + ITs (`./runIntegrationTests.sh`) against a current local stack; expect the same green result.
5. Set the project version per the release scheme, mark PR **#27** ready, and release.

## References

- Draft PR: **#27** · Tickets: **PEG-3408**, **DD-41592**
- Living upgrade page: *Elasticsearch 9.2.2 Upgrade — Java 25* (Confluence, space PETTA, page 1990251336)
- AI-assistant notes for this repo: `CLAUDE.md` (same content, terser).
