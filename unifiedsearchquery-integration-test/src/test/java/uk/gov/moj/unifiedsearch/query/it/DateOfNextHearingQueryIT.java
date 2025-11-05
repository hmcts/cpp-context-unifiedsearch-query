package uk.gov.moj.unifiedsearch.query.it;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.HearingDateSearchDataIngester.DATE_OF_NEXT_HEARING_2019_04_19;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertLaaCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.LaaSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.HearingDay;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.HearingSummary;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.probation.ProbationDefendantDetailsSummary;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.HearingDateSearchDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.AssertProbationDefendantDetails;
import uk.gov.moj.unifiedsearch.query.it.util.LaaSearchApiClient;
import uk.gov.moj.unifiedsearch.query.it.util.ProbationDefendantDetailsSearchApiClient;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
public class DateOfNextHearingQueryIT {

    private static final String DATE_OF_NEXT_HEARING = "dateOfNextHearing";
    private LaaSearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static HearingDateSearchDataIngester hearingDateSearchDataIngester = new HearingDateSearchDataIngester();
    private final ProbationDefendantDetailsSearchApiClient probationDefendantDetailsSearchApiClient = ProbationDefendantDetailsSearchApiClient.getInstance();

    @BeforeAll
    public static void setup() throws IOException {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        hearingDateSearchDataIngester.loadHearingDateData();
    }

    @Test
    public void shouldReturnMultipleResultsForDateFromQuery() throws IOException {
        final String dateOfNextHearing = DATE_OF_NEXT_HEARING_2019_04_19;
        final CaseSearchResponse caseListResults = submitSearch(dateOfNextHearing);

        final CaseDocument expectedCaseDocument1 = hearingDateSearchDataIngester.getIndexDocumentAt(0);
        final CaseDocument expectedCaseDocument2 = hearingDateSearchDataIngester.getIndexDocumentAt(3);

        assertCases(caseListResults, asList(expectedCaseDocument1, expectedCaseDocument2));


        assertThat(getHearingDatesFromHearingDays(caseListResults.getCases().get(0).getHearingSummary()), hasItem(dateOfNextHearing));
        assertThat(getHearingDatesFromHearingDays(caseListResults.getCases().get(0).getHearingSummary()), hasItem(dateOfNextHearing));

        assertThat(getHearingDatesFromHearingDays(caseListResults.getCases().get(1).getHearingSummary()), hasItem(dateOfNextHearing));
    }

    @Test
    public void shouldReturnProbationDefendantDetailsResultsForDateFromQuery() throws IOException {
        final String dateOfNextHearing = DATE_OF_NEXT_HEARING_2019_04_19;

        final Map<String, String> parameters = ImmutableMap.of(DATE_OF_NEXT_HEARING, dateOfNextHearing, "defendantName", "joe");

        final List<ProbationDefendantDetailsSummary> caseListResults =  probationDefendantDetailsSearchApiClient.searchCases(parameters).getCases();

        final CaseDocument expectedCaseDocument1 = hearingDateSearchDataIngester.getIndexDocumentAt(0);

        AssertProbationDefendantDetails.assertCase(caseListResults.get(0), expectedCaseDocument1);


        assertThat(getHearingDatesFromHearingDays(caseListResults.get(0).getHearingSummary()), hasItem(dateOfNextHearing));
        assertThat(getHearingDatesFromHearingDays(caseListResults.get(0).getHearingSummary()), hasItem(dateOfNextHearing));
    }

    private CaseSearchResponse submitSearch(final String dateOfNextHearing) throws IOException {

        final Map<String, String> parameters = ImmutableMap.of(DATE_OF_NEXT_HEARING, dateOfNextHearing, "defendantName", "joe");

        return searchApiClient.searchCases(parameters);
    }


    private List<String> getHearingDatesFromHearingDays(final List<HearingSummary> hearings) {
        return hearings.stream()
                .map(HearingSummary::getHearingDays).
                        flatMap(Collection::stream).
                        map(HearingDay::getSittingDay).
                        map(s -> s.substring(0, 10))
                .collect(toList());
    }

}
