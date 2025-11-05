package uk.gov.moj.unifiedsearch.query.it.cps;

import static com.google.common.collect.ImmutableMap.of;
import static org.apache.http.HttpStatus.SC_OK;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.cps.ExcludeAutomaticallyLinkedCasesDataIngester.linkedCaseId1;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.ExcludeAutomaticallyLinkedCasesDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ExcludeAutomaticallyLinkedCaseIT {

    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private CpsSearchApiClient cpsSearchApiClient = getInstance();
    private static ExcludeAutomaticallyLinkedCasesDataIngester excludeAutomaticallyLinkedCasesDataIngester = new ExcludeAutomaticallyLinkedCasesDataIngester();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        excludeAutomaticallyLinkedCasesDataIngester.loadCaseDocuments();
    }

    @Test
    public void shouldReturnCasesForExcludingLinkedCaseIdFalse() throws Exception {
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(of("excludeAutomaticallyLinkedCasesTo", linkedCaseId1));

        assertCases(caseSearchResponse, excludeAutomaticallyLinkedCasesDataIngester.getCasesWithLinkedIdWithManuallyLinkedFalse());
    }

    @Test
    public void shouldReturnAllCasesWhenLinkedCaseIdNotFound() throws IOException {
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(of("excludeAutomaticallyLinkedCasesTo", UUID.randomUUID().toString()));

        assertCases(caseSearchResponse, excludeAutomaticallyLinkedCasesDataIngester.getCases());
    }

    @Test
    public void shouldReturnAllResultsWhereLinkedCaseIdIsEmpty() throws IOException {
        final Map<String, String> parameters = of("excludeAutomaticallyLinkedCasesTo", "");

        cpsSearchApiClient.searchCasesAndValidateStatusCode(parameters, SC_OK);
    }
}
