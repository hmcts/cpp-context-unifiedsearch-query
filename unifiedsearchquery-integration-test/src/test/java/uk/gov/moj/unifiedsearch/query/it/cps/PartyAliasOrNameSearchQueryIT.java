package uk.gov.moj.unifiedsearch.query.it.cps;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.join;
import static uk.gov.justice.services.unifiedsearch.client.constant.CpsPartyType.DEFENDANT;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_FIRST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_LAST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_TYPES;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.PartyAliasOrNameIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PartyAliasOrNameSearchQueryIT {

    private static final String DEFENDANT_ALIAS = "DEFENDANT_ALIAS";
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static PartyAliasOrNameIngester aliasOrNameIngester = new PartyAliasOrNameIngester();
    private CpsSearchApiClient cpsSearchApiClient = getInstance();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        aliasOrNameIngester.loadCases();
    }

    @Test
    public void shouldSearchByPartyNameOrAliasForADefendant() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(PARTY_FIRST_NAME, "Bob");
        parameters.putIfAbsent(PARTY_LAST_NAME, "Marley");
        parameters.putIfAbsent(PARTY_TYPES, join(asList(DEFENDANT.name(), DEFENDANT_ALIAS), ","));

        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, aliasOrNameIngester.getBobCaseDocuments());
    }

    @Test
    public void shouldSearchByPartyNameForADefendant() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(PARTY_FIRST_NAME, "Johnie");
        parameters.putIfAbsent(PARTY_LAST_NAME, "Smith");
        parameters.putIfAbsent(PARTY_TYPES, join(asList(DEFENDANT.name()), ","));

        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, aliasOrNameIngester.getJohnieCaseDocuments());
    }

    @Test
    public void shouldSearchByPartyAliasNameForADefendant() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(PARTY_FIRST_NAME, "Roger");
        parameters.putIfAbsent(PARTY_LAST_NAME, "Smith");
        parameters.putIfAbsent(PARTY_TYPES, join(asList(DEFENDANT_ALIAS), ","));

        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, aliasOrNameIngester.getRogerCaseDocuments());
    }

    @Test
    public void shouldSearchByPartyAliasPartialNameForADefendant() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(PARTY_FIRST_NAME, "Ro");
        parameters.putIfAbsent(PARTY_LAST_NAME, "Sm");
        parameters.putIfAbsent(PARTY_TYPES, join(asList(DEFENDANT_ALIAS), ","));

        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, aliasOrNameIngester.getRogerCaseDocuments());
    }
}
