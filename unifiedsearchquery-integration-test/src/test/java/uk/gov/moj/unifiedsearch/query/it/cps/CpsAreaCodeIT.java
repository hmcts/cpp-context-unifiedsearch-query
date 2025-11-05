package uk.gov.moj.unifiedsearch.query.it.cps;

import static com.google.common.collect.ImmutableMap.of;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.cps.CjsAreaCodeDataIngester.ESSEX;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.cps.CjsAreaCodeDataIngester.HERTFORDSHIRE;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.CpsAreaCodeDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CpsAreaCodeIT {

    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private CpsSearchApiClient cpsSearchApiClient = getInstance();
    private static CpsAreaCodeDataIngester cpsAreaCodeDataIngester = new CpsAreaCodeDataIngester();
    private static final String CPS_AREA = "cpsAreaCode";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        cpsAreaCodeDataIngester.loadCaseDocuments();
    }

    @Test
    public void shouldReturnSingleResultForCpsQuery() throws Exception {
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(of(CPS_AREA, ESSEX));

        assertCases(caseSearchResponse, cpsAreaCodeDataIngester.getCasesForCpsAreaEssex());
    }

    @Test
    public void shouldQueryByCpsAreaIsCourtOfAppealGetMultipleResults() throws Exception {
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(of(CPS_AREA, HERTFORDSHIRE));

        assertCases(caseSearchResponse, cpsAreaCodeDataIngester.getCasesForCpsAreaHertfordshire());
    }

    @Test
    public void shouldReturnNothingWhereCpsAreaIsNotFound() throws IOException {
        final List<Case> cases = cpsSearchApiClient.searchCases(of(CPS_AREA, "US")).getCases();

        assertThat(cases, hasSize(0));
    }

    @Test
    public void shouldReturnAllResultsWhereCpsAreaIsEmpty() throws IOException {
        final Map<String, String> parameters = of(CPS_AREA, "");

        cpsSearchApiClient.searchCasesAndValidateStatusCode(parameters, SC_OK);
    }
}
