package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCase.assertCase;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.CourtSearchDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CourtQueryIT {

    private static final String COURT_ID = "courtId";
    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static CourtSearchDataIngester courtSearchDataIngester = new CourtSearchDataIngester();

    @BeforeAll
    public static void setup() throws IOException {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        courtSearchDataIngester.loadCourtData();
    }

    @Test
    public void shouldReturnSingleResultForCourtHouseQuery() throws IOException {
        final String courtId = courtSearchDataIngester.courtId3;

        final List<Case> caseListResults = submitSearch(courtId);

        final CaseDocument expectedCaseDocument1 = courtSearchDataIngester.getIndexDocumentAt(4);

        assertThat(caseListResults, hasSize(1));

        assertCase(caseListResults.get(0), expectedCaseDocument1);
    }

    @Test
    public void shouldReturnMultipleResultForCourtHouseQuery() throws IOException {
        final String courtId = courtSearchDataIngester.courtId1;

        final List<Case> caseListResults = submitSearch(courtId);

        final CaseDocument expectedCaseDocument1 = courtSearchDataIngester.getIndexDocumentAt(0);

        final CaseDocument expectedCaseDocument2 = courtSearchDataIngester.getIndexDocumentAt(2);

        final CaseDocument expectedCaseDocument3 = courtSearchDataIngester.getIndexDocumentAt(3);

        assertThat(caseListResults, hasSize(3));

        assertCases(caseListResults, asList(expectedCaseDocument1, expectedCaseDocument2, expectedCaseDocument3));
    }

    @Test
    public void shouldReturnMultipleResultForCourtCentreQuery() throws IOException {
        final String courtId = courtSearchDataIngester.courtId2;

        final List<Case> caseListResults = submitSearch(courtId);

        final CaseDocument expectedCaseDocument1 = courtSearchDataIngester.getIndexDocumentAt(0);

        final CaseDocument expectedCaseDocument2 = courtSearchDataIngester.getIndexDocumentAt(1);

        final CaseDocument expectedCaseDocument3 = courtSearchDataIngester.getIndexDocumentAt(2);

        final CaseDocument expectedCaseDocument4 = courtSearchDataIngester.getIndexDocumentAt(5);

        assertThat(caseListResults, hasSize(4));

        assertCases(caseListResults, asList(expectedCaseDocument1, expectedCaseDocument2, expectedCaseDocument3, expectedCaseDocument4));
    }

    @Test
    public void shouldReturnNoResultWhenCourtHouseQueryParamNotFound() throws IOException {
        final String courtId = randomUUID().toString();

        final List<Case> caseListResults = submitSearch(courtId);

        assertThat(caseListResults, hasSize(0));
    }

    @Test
    public void shouldReturnNoResultWhenCourtHouseQueryParamIsEmpty() throws IOException {
        final Map<String, String> parameters = of("courtId", " ");
        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }

    @Test
    public void shouldReturnBadRequestWhenCourtIdIsNotUUIDFormat() throws IOException {
        final String courtId = "ab746921-d839-4867-bcf9-b41db8ebc85";
        final Map<String, String> parameters = of("courtId", courtId);
        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }

    private List<Case> submitSearch(final String courtId) throws IOException {
        final Map<String, String> parameters = of(COURT_ID, courtId);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();
    }
}
