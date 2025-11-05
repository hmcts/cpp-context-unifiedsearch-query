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
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.CjsAreaCodeDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CjsAreaCodeIT {

    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private CpsSearchApiClient cpsSearchApiClient = getInstance();
    private static CjsAreaCodeDataIngester cjsAreaCodeDataIngester = new CjsAreaCodeDataIngester();
    private static final String CJS_AREA = "cjsAreaCode";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        cjsAreaCodeDataIngester.loadCaseDocuments();
    }

    @Test
    public void shouldReturnSingleResultForCpsQuery() throws Exception {
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(of(CJS_AREA, ESSEX));

        assertCases(caseSearchResponse, cjsAreaCodeDataIngester.getCasesForCjsAreaEssex());
    }

    @Test
    public void shouldQueryByCjsAreaIsCourtOfAppealGetMultipleResults() throws Exception {
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(of(CJS_AREA, HERTFORDSHIRE));

        assertCases(caseSearchResponse, cjsAreaCodeDataIngester.getCasesForCjsAreaHertfordshire());
    }

    @Test
    public void shouldReturnNothingWhereCjsAreaIsNotFound() throws IOException {
        final List<Case> cases = cpsSearchApiClient.searchCases(of(CJS_AREA, "US")).getCases();

        assertThat(cases, hasSize(0));
    }

    @Test
    public void shouldReturnAllResultsWhereCjsAreaIsEmpty() throws IOException {
        final Map<String, String> parameters = of(CJS_AREA, "");

        cpsSearchApiClient.searchCasesAndValidateStatusCode(parameters, SC_OK);
    }
}
