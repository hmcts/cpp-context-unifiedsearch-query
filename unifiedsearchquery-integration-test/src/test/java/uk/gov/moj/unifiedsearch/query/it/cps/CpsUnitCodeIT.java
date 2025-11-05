package uk.gov.moj.unifiedsearch.query.it.cps;

import static com.google.common.collect.ImmutableMap.of;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.cps.CpsUnitCodeDataIngester.DERBY_CPS_UNIT;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.cps.CpsUnitCodeDataIngester.LONDON_NORTH_CPS_UNIT;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCase.assertCase;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.CpsUnitCodeDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CpsUnitCodeIT {

    private static final String CPS = "cpsUnitCode";
    private CpsSearchApiClient searchApiClient = CpsSearchApiClient.getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static CpsUnitCodeDataIngester cpsUnitCodeDataIngester = new CpsUnitCodeDataIngester();

    @BeforeAll
    public static void setupStubs() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        cpsUnitCodeDataIngester.loadCaseDocuments();
    }


    @Test
    public void shouldReturnSingleResultForCpsQuery() throws Exception {

        final List<Case> caseListResults = submitSearch(DERBY_CPS_UNIT);

        final CaseDocument expectedCaseDocument = cpsUnitCodeDataIngester.getCaseForCps002();

        assertThat(caseListResults, hasSize(1));

        assertCase(caseListResults.get(0), expectedCaseDocument);
    }

    @Test
    public void shouldGetMultipleCases() throws Exception {

        final List<Case> caseListResults = submitSearch(LONDON_NORTH_CPS_UNIT);
        final List<CaseDocument> expectedCaseDocumentList = cpsUnitCodeDataIngester.getCasesForCpsUnitLondon();
        assertCases(caseListResults, expectedCaseDocumentList);
    }

    @Test
    public void shouldReturnNothingWhereCpsIsNotFound() throws IOException {
        final List<Case> caseListResults = submitSearch("US");

        assertThat(caseListResults, hasSize(0));
    }

    @Test
    public void shouldReturnAllResultsWhereCpsIsEmpty() throws IOException {
        final Map<String, String> parameters = of(CPS, "");

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_OK);
    }

    private List<Case> submitSearch(final String asn) throws IOException {
        final Map<String, String> parameters = of(CPS, asn);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();
    }
}
