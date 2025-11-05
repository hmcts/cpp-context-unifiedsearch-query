package uk.gov.moj.unifiedsearch.query.it.cps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_FIRST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_LAST_NAME;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.PartyNameIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PartyFirstAndLastNameQueryIT {

    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static PartyNameIngester partyNameIngester = new PartyNameIngester();
    private CpsSearchApiClient cpsSearchApiClient = getInstance();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        partyNameIngester.loadCases();
    }

    @Test
    public void shouldSearchByFirstNameAndLastName() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(PARTY_FIRST_NAME, "John");
        parameters.putIfAbsent(PARTY_LAST_NAME, "Wick");
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, partyNameIngester.getJohnWickCaseDocuments());
    }

    @Test
    public void shouldSearchByFirstName() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(PARTY_FIRST_NAME, "Johnie");
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, partyNameIngester.getJohnieSmithCaseDocuments());
    }

    @Test
    public void shouldSearchByLastName() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(PARTY_LAST_NAME, "Williams");
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, partyNameIngester.getDebbieWilliamsCaseDocuments());
    }

    @Test
    public void shouldReturnNoResultsWhenNoMatchFound() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(PARTY_FIRST_NAME, "John");
        parameters.putIfAbsent(PARTY_LAST_NAME, "Williams");
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getCases(), hasSize(0));
    }

    @Test
    public void shouldReturnResultsWhenSearchingForPartialFirstName() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(PARTY_FIRST_NAME, "De");
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, partyNameIngester.getDebbieWilliamsCaseDocuments());
    }

    @Test
    public void shouldReturnResultsWhenSearchingForPartialLastName() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(PARTY_LAST_NAME, "wil");
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, partyNameIngester.getDebbieWilliamsCaseDocuments());
    }
}
