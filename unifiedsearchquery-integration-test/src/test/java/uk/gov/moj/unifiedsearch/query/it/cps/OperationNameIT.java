package uk.gov.moj.unifiedsearch.query.it.cps;

import static com.google.common.collect.ImmutableMap.of;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.OperationNameDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class OperationNameIT {

    private static final String OPERATION_NAME = "operationName";
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static OperationNameDataIngester operationNameDataIngester = new OperationNameDataIngester();
    private CpsSearchApiClient searchApiClient = CpsSearchApiClient.getInstance();

    @BeforeAll
    public static void setupStubs() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        operationNameDataIngester.loadCaseDocuments();
    }


    @Test
    public void shouldReturnSingleResultForOperationNameQuery() throws Exception {
        final List<Case> caseListResults = submitSearch("Whistle");
        final List<CaseDocument> expectedCaseDocument = operationNameDataIngester.getIndexDocumentAt(0);
        assertThat(caseListResults, hasSize(1));
        assertCases(caseListResults, expectedCaseDocument);
    }

    @Test
    public void shouldReturnResultForOperationNameQueryWithFirstWordOfPhrase() throws Exception {
        final List<Case> caseListResults = submitSearch("Shield");
        final List<CaseDocument> expectedCaseDocument = operationNameDataIngester.getIndexDocumentAt(5);
        assertThat(caseListResults, hasSize(1));
        assertCases(caseListResults, expectedCaseDocument);
    }

    @Test
    public void shouldReturnResultForOperationNameQueryWithPartialFirstWord() throws Exception {
        final List<Case> caseListResults = submitSearch("Shie");
        final List<CaseDocument> expectedCaseDocument = operationNameDataIngester.getIndexDocumentAt(5);
        assertThat(caseListResults, hasSize(1));
        assertCases(caseListResults, expectedCaseDocument);
    }

    @Test
    public void shouldReturnResultForOperationNameQueryWithLastWordOfPhrase() throws Exception {
        final List<Case> caseListResults = submitSearch("Connect");
        final List<CaseDocument> expectedCaseDocument = operationNameDataIngester.getIndexDocumentAt(6);
        assertThat(caseListResults, hasSize(1));
        assertCases(caseListResults, expectedCaseDocument);
    }

    @Test
    public void shouldGetMultipleCases() throws Exception {
        final List<Case> caseListResults = submitSearch("Chase");
        final List<CaseDocument> expectedCaseDocumentList = operationNameDataIngester.getMultipleCases();
        assertCases(caseListResults, expectedCaseDocumentList);
    }

    @Test
    public void shouldReturnNothingWhereOperationNameIsNotFound() throws IOException {
        final List<Case> caseListResults = submitSearch("US");
        assertThat(caseListResults, hasSize(0));
    }

    @Test
    public void shouldReturnBadRequestWhereOperationNameIsEmpty() throws IOException {
        final Map<String, String> parameters = of(OPERATION_NAME, "");
        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_OK);
    }

    private List<Case> submitSearch(final String operationName) throws IOException {
        final Map<String, String> parameters = of(OPERATION_NAME, operationName);
        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);
        return caseSearchResponse.getCases();
    }
}
