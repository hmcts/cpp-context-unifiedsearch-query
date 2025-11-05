package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
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
import uk.gov.moj.unifiedsearch.query.it.ingestors.DateOfBirthSearchDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DateOfBirthQueryIT {

    private static final String DATE_OF_BIRTH = "partyDateOfBirth";
    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static DateOfBirthSearchDataIngester dateOfBirthSearchDataIngester = new DateOfBirthSearchDataIngester();

    @BeforeAll
    public static void setup() throws IOException {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        dateOfBirthSearchDataIngester.loadDateOfBirthData();
    }

    @Test
    public void shouldReturnSingleResultForDateOfBirthQuery() throws IOException {
        final String dateOfBirth = "1981-02-27";
        final List<Case> caseListResults = submitSearch(dateOfBirth);

        final CaseDocument expectedCaseDocument1 = dateOfBirthSearchDataIngester.getIndexDocumentAt(3);

        assertThat(caseListResults, hasSize(1));

        assertCase(caseListResults.get(0), expectedCaseDocument1);
    }

    @Test
    public void shouldReturnMultipleResultsForDateOfBirthQuery() throws IOException {
        final String dateOfBirth = "1972-11-23";
        final List<Case> caseListResults = submitSearch(dateOfBirth);

        final CaseDocument expectedCaseDocument1 = dateOfBirthSearchDataIngester.getIndexDocumentAt(0);
        final CaseDocument expectedCaseDocument2 = dateOfBirthSearchDataIngester.getIndexDocumentAt(6);

        assertThat(caseListResults, hasSize(2));

        assertCases(caseListResults, asList(expectedCaseDocument1, expectedCaseDocument2));
    }

    @Test
    public void shouldReturnNothingWhereDateOfBirthIsNotFound() throws IOException {
        final String dateOfBirth = "1972-11-28";
        final List<Case> caseListResults = submitSearch(dateOfBirth);

        assertThat(caseListResults, hasSize(0));
    }

    @Test
    public void shouldReturnNothingWhereDateOfBirthIsEmpty() throws IOException {
        final Map<String, String> parameters = of("dateOfBirth", "");

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }

    @Test
    public void shouldReturnBadRequestWhenDateOfBirthFormatIsIncorrect() throws IOException {
        final String dateOfBirth = "28-11-1972";

        final Map<String, String> parameters = of(DATE_OF_BIRTH, dateOfBirth);

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }

    private List<Case> submitSearch(final String dateOfBirth) throws IOException {
        final Map<String, String> parameters = of(DATE_OF_BIRTH, dateOfBirth);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();
    }
}
