package uk.gov.moj.unifiedsearch.query.it.cps;

import static java.lang.String.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.URN;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.UrnDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PageSizeAndStartFromQueryIT {

    private static final String PAGE_SIZE = "pageSize";
    private static final String START_FROM = "startFrom";

    private CpsSearchApiClient searchApiClient = CpsSearchApiClient.getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static UrnDataIngester urnDataIngester = new UrnDataIngester();

    @BeforeAll
    public static void setupStubs() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        urnDataIngester.loadCaseDocuments();
    }

    @Test
    public void shouldReturnSingleResultForUrnQuery() throws Exception {

        final List<Case> caseListResults = searchByPageSizeAndStartFrom(1, 0);

        assertThat(caseListResults, hasSize(1));
    }

    @Test
    public void shouldGet5Cases() throws Exception {

        final List<Case> caseListResults = searchByPageSizeAndStartFrom(5, 0);

        assertThat(caseListResults, hasSize(5));
    }

    @Test
    public void shouldGet1CasesAsThereIsOnly1CaseRemainedForSecondPage() throws Exception {

        final List<Case> caseListResults = searchByPageSizeAndStartFrom(5, 5);

        assertThat(caseListResults, hasSize(1));
    }

    @Test
    public void shouldGet6CasesIfThereAre6Cases() throws Exception {

        final List<Case> caseListResults = searchByPageSizeAndStartFrom(6, 0);

        assertThat(caseListResults, hasSize(6));
    }

    @Test
    public void shouldReturnNothingWhenThereIsNoCaseToShowForSecondPage() throws Exception {

        final List<Case> caseListResults = searchByPageSizeAndStartFrom(6, 6);

        assertThat(caseListResults, hasSize(0));
    }

    @Test
    public void shouldGet6Cases() throws Exception {

        final List<Case> caseListResults = searchByPageSizeAndStartFrom(10, 0);

        assertThat(caseListResults, hasSize(6));
    }

    @Test
    public void shouldReturnNothingWhenPageSizeIs0() throws IOException {
        final List<Case> caseListResults = searchByPageSizeAndStartFrom(0, 0);

        assertThat(caseListResults, hasSize(0));
    }


    private List<Case> searchByPageSizeAndStartFrom(final Integer pageSize, final Integer startFrom) throws IOException {
        final Map<String, String > parameters = new HashMap<>();
        parameters.putIfAbsent(PAGE_SIZE, valueOf(pageSize));
        parameters.putIfAbsent(START_FROM, valueOf(startFrom));
        parameters.putIfAbsent(URN, "URN009");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();
    }
}
