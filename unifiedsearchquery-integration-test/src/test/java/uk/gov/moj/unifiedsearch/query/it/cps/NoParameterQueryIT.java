package uk.gov.moj.unifiedsearch.query.it.cps;

import static com.google.common.collect.ImmutableMap.of;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.CjsAreaCodeDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class NoParameterQueryIT {

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
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(of("", ""));

        assertCases(caseSearchResponse, cjsAreaCodeDataIngester.getCases());
    }

}
