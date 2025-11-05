package uk.gov.moj.unifiedsearch.query.it.cps;

import static com.google.common.collect.ImmutableMap.of;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCase.assertCase;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.WitnessCareUnitDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class WitnessCareUnitIT {

    private static final String WITNESS_CARE_UNIT = "witnessCareUnitCode";
    private CpsSearchApiClient searchApiClient = CpsSearchApiClient.getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static WitnessCareUnitDataIngester witnessCareUnitDataIngester = new WitnessCareUnitDataIngester();

    @BeforeAll
    public static void setupStubs() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        witnessCareUnitDataIngester.loadCaseDocuments();
    }


    @Test
    public void shouldReturnSingleResultForWitnesscareUnitQuery() throws Exception {

        final List<Case> caseListResults = submitSearch("Derby");

        final CaseDocument expectedCaseDocument = witnessCareUnitDataIngester.getCaseForWitnessCareUnit002();

        assertThat(caseListResults, hasSize(1));

        assertCase(caseListResults.get(0), expectedCaseDocument);
    }

    @Test
    public void shouldGetMultipleCases() throws Exception {

        final List<Case> caseListResults = submitSearch("London");
        final List<CaseDocument> expectedCaseDocumentList = witnessCareUnitDataIngester.getCasesForWitnessCareUnitUnitLondon();
        assertCases(caseListResults, expectedCaseDocumentList);
    }

    @Test
    public void shouldReturnNothingWhereWitnessCareUnitIsNotFound() throws IOException {
        final List<Case> caseListResults = submitSearch("US");

        assertThat(caseListResults, hasSize(0));
    }

    @Test
    public void shouldReturnBadRequestWhereWitnessCareUnitIsEmpty() throws IOException {
        final Map<String, String> parameters = of(WITNESS_CARE_UNIT, "");

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_OK);
    }

    private List<Case> submitSearch(final String witnessCareUnit) throws IOException {
        final Map<String, String> parameters = of(WITNESS_CARE_UNIT, witnessCareUnit);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();
    }
}
