package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.HearingTypeAndHearingDateIngester.SEARCHED_HEARING_TYPE_UUID;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.HearingTypeAndIsBoxHearingIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class HearingTypeAndIsBoxHearingSearchIT {

    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static HearingTypeAndIsBoxHearingIngester hearingTypeAndIsBoxHearingIngester = new HearingTypeAndIsBoxHearingIngester();
    private static final String HEARING_TYPE_ID = "hearingTypeId";
    private static final String BOX_HEARING = "boxWorkHearing";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        hearingTypeAndIsBoxHearingIngester.loadCases();
    }

    @Test
    public void shouldReturnNothingWhenSearchingHearingTypeAndNonBoxHearing() throws IOException {
        final CaseSearchResponse result = searchApiClient.searchCases(of(HEARING_TYPE_ID, SEARCHED_HEARING_TYPE_UUID,
                BOX_HEARING, FALSE.toString()));
        assertCases(result, hearingTypeAndIsBoxHearingIngester.getNonBoxHearingSearchedCases());
    }


    @Test
    public void shouldReturnSearchResponseWhenSearchingByHearingTypeAndBoxHearing() throws IOException {

        final CaseSearchResponse result = searchApiClient.searchCases(of(HEARING_TYPE_ID, SEARCHED_HEARING_TYPE_UUID,
                BOX_HEARING, TRUE.toString()));
        assertCases(result, hearingTypeAndIsBoxHearingIngester.getExpectedCases());
    }
}
