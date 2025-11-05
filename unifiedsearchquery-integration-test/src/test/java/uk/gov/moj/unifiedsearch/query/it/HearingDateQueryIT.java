package uk.gov.moj.unifiedsearch.query.it;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCase.assertCase;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Hearing;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.HearingDateSearchDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class HearingDateQueryIT {

    private static final String HEARING_DATE_FROM = "hearingDateFrom";
    private static final String HEARING_DATE_TO = "hearingDateTo";
    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static HearingDateSearchDataIngester hearingDateSearchDataIngester = new HearingDateSearchDataIngester();

    @BeforeAll
    public static void setup() throws IOException {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        hearingDateSearchDataIngester.loadHearingDateData();
    }

    @Test
    public void shouldReturnMultipleResultsForDateFromQuery() throws IOException {
        final String hearingDateFrom = "2019-04-19";
        final Optional<String> hearingDateTo = empty();
        final List<Case> caseListResults = submitSearch(hearingDateFrom, hearingDateTo);

        final CaseDocument expectedCaseDocument1 = hearingDateSearchDataIngester.getIndexDocumentAt(0);
        final CaseDocument expectedCaseDocument2 = hearingDateSearchDataIngester.getIndexDocumentAt(3);

        assertThat(caseListResults, hasSize(2));

        assertCases(caseListResults, asList(expectedCaseDocument1, expectedCaseDocument2));

        assertThat(getHearingDatesForCase(caseListResults.get(0).getHearings()), hasItem(hearingDateFrom));
        assertThat(getHearingDatesForCase(caseListResults.get(1).getHearings()), hasItem(hearingDateFrom));
    }

    @Test
    public void shouldReturnSingleResultForDateFromQuery() throws IOException {
        final String hearingDateFrom = "2019-05-17";
        final Optional<String> hearingDateTo = empty();
        final List<Case> caseListResults = submitSearch(hearingDateFrom, hearingDateTo);

        final CaseDocument expectedCaseDocument1 = hearingDateSearchDataIngester.getIndexDocumentAt(3);

        assertThat(caseListResults, hasSize(1));

        assertCase(caseListResults.get(0), expectedCaseDocument1);

        assertThat(getHearingDatesForCase(caseListResults.get(0).getHearings()), hasItem(hearingDateFrom));
    }

    @Test
    public void shouldReturnNothingWhenNoMatchForDateFromQuery() throws IOException {
        final String hearingDateFrom = "2018-05-17";
        final Optional<String> hearingDateTo = empty();
        final List<Case> caseListResults = submitSearch(hearingDateFrom, hearingDateTo);

        assertThat(caseListResults, hasSize(0));
    }

    @Test
    public void shouldReturnNothingWhenNoMatchForDateToQuery() throws IOException {
        final String hearingDateFrom = "";
        final Optional<String> hearingDateTo = of("2018-05-17");
        final List<Case> caseListResults = submitSearch(hearingDateFrom, hearingDateTo);

        assertThat(caseListResults, hasSize(0));
    }

    @Test
    public void shouldReturnSingleResultForDateToQuery() throws IOException {
        final String hearingDateFrom = "";
        final Optional<String> hearingDateTo = of("2019-05-17");
        final List<Case> caseListResults = submitSearch(hearingDateFrom, hearingDateTo);

        final CaseDocument expectedCaseDocument1 = hearingDateSearchDataIngester.getIndexDocumentAt(3);

        assertThat(caseListResults, hasSize(1));

        assertCase(caseListResults.get(0), expectedCaseDocument1);

        assertThat(getHearingDatesForCase(caseListResults.get(0).getHearings()), hasItem(hearingDateTo.get()));
    }

    @Test
    public void shouldReturnMultipleResultsForDateFromAndDateToQuery() throws IOException {
        final String hearingDateFrom = "2019-04-20";
        final Optional<String> hearingDateTo = of("2019-04-26");
        final List<Case> caseListResults = submitSearch(hearingDateFrom, hearingDateTo);

        final CaseDocument expectedCaseDocument1 = hearingDateSearchDataIngester.getIndexDocumentAt(0);
        final CaseDocument expectedCaseDocument2 = hearingDateSearchDataIngester.getIndexDocumentAt(1);
        final CaseDocument expectedCaseDocument3 = hearingDateSearchDataIngester.getIndexDocumentAt(4);

        assertCases(caseListResults, asList(expectedCaseDocument1, expectedCaseDocument2, expectedCaseDocument3));

        assertThat(caseListResults, hasSize(3));
    }

    @Test
    public void shouldReturnAllDataWhenWithinDateFromAndDateToRange() throws IOException {
        final String hearingDateFrom = "2019-01-12";
        final Optional<String> hearingDateTo = of("2019-05-17");
        final List<Case> caseListResults = submitSearch(hearingDateFrom, hearingDateTo);

        final CaseDocument expectedCaseDocument1 = hearingDateSearchDataIngester.getIndexDocumentAt(0);
        final CaseDocument expectedCaseDocument2 = hearingDateSearchDataIngester.getIndexDocumentAt(1);
        final CaseDocument expectedCaseDocument3 = hearingDateSearchDataIngester.getIndexDocumentAt(2);
        final CaseDocument expectedCaseDocument4 = hearingDateSearchDataIngester.getIndexDocumentAt(3);
        final CaseDocument expectedCaseDocument5 = hearingDateSearchDataIngester.getIndexDocumentAt(4);

        assertCases(caseListResults, asList(expectedCaseDocument1, expectedCaseDocument2, expectedCaseDocument3, expectedCaseDocument4, expectedCaseDocument5));

        assertThat(caseListResults, hasSize(5));
    }

    @Test
    public void shouldReturnNoResultsWhenDateFromAndDateToRangeAreEmpty() throws IOException {
        final String hearingDateFrom = "";
        final Optional<String> hearingDateTo = of("");

        final Map<String, String> parameters = updateMapIfHearingDateToIsPresent(hearingDateFrom, hearingDateTo);

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }

    @Test
    public void shouldReturnBadRequestWhenDateFromIsGreaterThanDateTo() throws IOException {
        final String hearingDateFrom = "2019-05-17";
        final Optional<String> hearingDateTo = of("2019-01-12");

        final Map<String, String> parameters = updateMapIfHearingDateToIsPresent(hearingDateFrom, hearingDateTo);

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }

    @Test
    public void shouldReturnResultWhenDateFromIsEqualToDateTo() throws IOException {
        final String hearingDateFrom = "2019-05-17";
        final Optional<String> hearingDateTo = of("2019-05-17");

        final Map<String, String> parameters = updateMapIfHearingDateToIsPresent(hearingDateFrom, hearingDateTo);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_OK);

        final List<Case> cases = caseSearchResponse.getCases();

        assertThat(cases, hasSize(1));
    }

    private List<Case> submitSearch(final String hearingDateFrom, final Optional<String> hearingDateTo) throws IOException {

        final Map<String, String> parameters = updateMapIfHearingDateToIsPresent(hearingDateFrom, hearingDateTo);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();
    }

    private Map<String, String> updateMapIfHearingDateToIsPresent(final String hearingDateFrom, final Optional<String> hearingDateTo) {
        final Map<String, String> parameters = new HashMap<>();

        if (!hearingDateFrom.isEmpty()) {
            parameters.put(HEARING_DATE_FROM, hearingDateFrom);
        }

        hearingDateTo.ifPresent(s -> parameters.put(HEARING_DATE_TO, s));

        return parameters;
    }

    private List<String> getHearingDatesForCase(final List<Hearing> hearings) {

        return hearings.stream()
                .map(Hearing::getHearingDates)
                .flatMap(Collection::stream)
                .collect(toList());
    }
}
