package uk.gov.moj.unifiedsearch.query.it.cps;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CASE_STATUS;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.CaseStatusDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CpsCaseStatusQueryIT {

    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static CaseStatusDataIngester caseStatusDataIngester = new CaseStatusDataIngester();
    private CpsSearchApiClient cpsSearchApiClient = getInstance();

    @BeforeAll
    public static void init() throws IOException {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        caseStatusDataIngester.loadCaseDocuments();
    }

    @Test
    public void shouldReturnCasesMatchingCaseStatusLIV() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CASE_STATUS, "LIV");
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, caseStatusDataIngester.getCasesWithCaseStatusLIV());
    }

    @Test
    public void shouldReturnCasesMatchingCaseStatusAPP() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CASE_STATUS, "APP");
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, caseStatusDataIngester.getCasesWithCaseStatusAPP());
    }

    @Test
    public void shouldReturnCasesMatchingCaseStatusFIN() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CASE_STATUS, "FIN");
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, caseStatusDataIngester.getCasesWithCaseStatusFIN());
    }

    @Test
    public void shouldReturnCasesMatchingCaseStatusMON() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CASE_STATUS, "MON");
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, caseStatusDataIngester.getCasesWithCaseStatusMON());
    }

    @Test
    public void shouldReturnNoCasesNoMatchingCaseStatus() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CASE_STATUS, "AP");
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);
        assertThat(caseSearchResponse.getCases().size(), is(0));
    }
}
