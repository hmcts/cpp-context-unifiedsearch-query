# CLAUDE.md — cpp-context-unifiedsearch-query

Context-specific notes for the **Java 25 / WildFly 40 / Elasticsearch 9.2.2** upgrade (25.104.x). See the platform-wide guidance in the workspace root `CLAUDE.md`.

## ⚠️ BEHAVIOUR CHANGE — `pageSize=0` (watch API Tests)

During the ES 9.2.2 upgrade we changed how `pageSize=0` is handled on the search endpoints:

- **Before:** `pageSize=0` → Elasticsearch `size:0` → the query matched documents but returned an **empty result list**.
- **Now:** `pageSize=0` (and an **absent** `pageSize`, which the framework materialises as `0`) → **uses the default page size of 10**, returning results.
- **Unchanged:** a **negative** `pageSize` is still **rejected** as invalid (`BadRequestException "Invalid page size"`).

Implemented in `getPageSize()` of `CpsQueryParameters` and `QueryParameters` (`return pageSize == 0 ? 10 : pageSize`).

**Why:** the RAML-generated resource does not translate the RAML `default:` into a JAX-RS `@DefaultValue`, and the framework's parameter pipeline materialises an absent numeric query param as `0` — so an absent `pageSize` was reaching ES as `size:0` and returning nothing. Absent and explicit-`0` are indistinguishable by the time our code sees them, so the contract was set to "`0` means default page".

**API Test impact:** API Tests exercise real contexts across boundaries. **If an API Test involving unifiedsearch-query starts returning results where it previously got an empty list (a caller sending `pageSize=0`), this change is the cause.** Such a test should be updated to the new contract (send an explicit positive `pageSize` if it needs a specific page, or expect the default page for `pageSize=0`).

## ES 9.2.2 client notes

- The unified-search client migrated from `RestHighLevelClient` to `co.elastic.clients:elasticsearch-java` (in `cpp-platform-libraries`, cherry-picked from Java-17 DD-41592).
- `httpcore5` is pinned to `5.3.6` in the platform BOM (elasticsearch-rest-client 9.2.2 pins 5.2.1, which clashes with `httpclient5 5.5.1` → `NoSuchMethodError` on real ES HTTP calls; only shows in ITs, not embedded-ES unit tests).
- `CpsCaseSortBy.findByKeyNameOrDefault` uses `computeIfAbsent` (an unknown `orderBy` returns the `URN` default, not null — previously `putIfAbsent` returned null → NPE/500).
- ITs run against a real ES 9.2.2 container (`cpp-developers-docker`), with embedded ES 9.2.2 for unit tests via `elasticsearch-maven-plugin`.
