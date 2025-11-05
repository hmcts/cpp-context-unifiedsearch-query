package uk.gov.moj.unifiedsearch.query.it.cps;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.HearingDateSearchDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class HearingDateQueryIT {

    private static final String HEARING_DATE_FROM = "hearingDateFrom";
    private static final String HEARING_DATE_TO = "hearingDateTo";
    private CpsSearchApiClient searchApiClient = CpsSearchApiClient.getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static HearingDateSearchDataIngester hearingDateSearchDataIngester = new HearingDateSearchDataIngester();

    @BeforeAll
    public static void setup() throws IOException {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        hearingDateSearchDataIngester.loadHearingDateData();
    }

    @Test
    public void shouldReturnMultipleResultsForDateFromQuery() throws IOException {
        final String hearingDateFrom = "2021-03-11";
        final Optional<String> hearingDateTo = of("2021-04-15");
        final List<Case> caseListResults = submitSearch(hearingDateFrom, hearingDateTo);
        assertThat(caseListResults, hasSize(1));
        assertCases(caseListResults, asList(hearingDateSearchDataIngester.getIndexDocumentAt(0)));
    }

    @Test
    public void shouldReturnSingleResultForDateFromQuery() throws IOException {
        final String hearingDateFrom = "2021-04-20";
        final Optional<String> hearingDateTo = empty();
        final List<Case> caseListResults = submitSearch(hearingDateFrom, hearingDateTo);
        assertThat(caseListResults, hasSize(1));
        assertCases(caseListResults, asList(hearingDateSearchDataIngester.getIndexDocumentAt(1)));
    }

    @Test
    public void shouldReturnNothingWhenNoMatchForDateFromQuery() throws IOException {
        final String hearingDateFrom = "2020-05-17";
        final Optional<String> hearingDateTo = empty();
        final List<Case> caseListResults = submitSearch(hearingDateFrom, hearingDateTo);
        assertThat(caseListResults, hasSize(0));
    }

    @Test
    public void shouldReturnNothingWhenNoMatchForDateToQuery() throws IOException {
        final String hearingDateFrom = "";
        final Optional<String> hearingDateTo = of("2020-05-17");
        final List<Case> caseListResults = submitSearch(hearingDateFrom, hearingDateTo);
        assertThat(caseListResults, hasSize(0));
    }

    @Test
    public void shouldReturnSingleResultForDateToQuery() throws IOException {
        final String hearingDateFrom = "";
        final Optional<String> hearingDateTo = of("2021-04-30");
        final List<Case> caseListResults = submitSearch(hearingDateFrom, hearingDateTo);
        assertThat(caseListResults, hasSize(1));
        assertCases(caseListResults, asList(hearingDateSearchDataIngester.getIndexDocumentAt(2)));
    }

    @Test
    public void shouldReturnMultipleResultsForDateFromAndDateToQuery() throws IOException {
        final String hearingDateFrom = "2021-04-15";
        final Optional<String> hearingDateTo = of("2021-06-04");
        final List<Case> caseListResults = submitSearch(hearingDateFrom, hearingDateTo);
        assertCases(caseListResults, hearingDateSearchDataIngester.getMultipleCaseDocument());
        assertThat(caseListResults, hasSize(2));
    }

    @Test
    public void shouldReturnAllDataWhenWithinDateFromAndDateToRange() throws IOException {
        final String hearingDateFrom = "2021-02-01";
        final Optional<String> hearingDateTo = of("2021-06-15");
        final List<Case> caseListResults = submitSearch(hearingDateFrom, hearingDateTo);
        assertCases(caseListResults, hearingDateSearchDataIngester.getCaseDataWithRange());
        assertThat(caseListResults, hasSize(3));
    }

    @Test
    public void shouldReturnAllResultsWhenDateFromAndDateToRangeAreEmpty() throws IOException {
        final String hearingDateFrom = "";
        final Optional<String> hearingDateTo = of("");

        final Map<String, String> parameters = updateMapIfHearingDateToIsPresent(hearingDateFrom, hearingDateTo);

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_OK);
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
        final String hearingDateFrom = "2021-04-20";
        final Optional<String> hearingDateTo = of("2021-04-20");

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

}
