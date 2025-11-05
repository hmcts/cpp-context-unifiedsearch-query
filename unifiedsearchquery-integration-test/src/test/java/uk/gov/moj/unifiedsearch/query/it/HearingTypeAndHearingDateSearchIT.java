package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Collections.emptyList;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.HearingTypeAndHearingDateIngester.SEARCHED_HEARING_DATE;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.HearingTypeAndHearingDateIngester.SEARCHED_HEARING_TYPE_UUID;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.HearingTypeAndHearingDateIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class HearingTypeAndHearingDateSearchIT {

    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static HearingTypeAndHearingDateIngester hearingTypeAndHearingIngester = new HearingTypeAndHearingDateIngester();
    private static final String HEARING_TYPE_ID = "hearingTypeId";
    private static final String HEARING_DATE_FROM = "hearingDateFrom";
    private static final String HEARING_DATE_TO = "hearingDateTo";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        hearingTypeAndHearingIngester.loadCases();
    }

    @Test
    public void shouldReturnNothingWhenSearchingByHearingTypeAndBadDates() throws IOException {
        final CaseSearchResponse result = searchApiClient.searchCases(of(HEARING_TYPE_ID, SEARCHED_HEARING_TYPE_UUID,
                HEARING_DATE_FROM, "2011-11-11",
                HEARING_DATE_TO, "2011-11-11"));
        assertCases(result, emptyList());
    }


    @Test
    public void shouldReturnSearchResponseWhenSearchingByHearingTypeAndHearingDates() throws IOException {

        final CaseSearchResponse result = searchApiClient.searchCases(of(HEARING_TYPE_ID, SEARCHED_HEARING_TYPE_UUID,
                HEARING_DATE_FROM, SEARCHED_HEARING_DATE,
                HEARING_DATE_TO, SEARCHED_HEARING_DATE));
        final List<CaseDocument> expectedCaseDocuments = hearingTypeAndHearingIngester.getExpectedCases();

        assertCases(result, expectedCaseDocuments);
    }
}
