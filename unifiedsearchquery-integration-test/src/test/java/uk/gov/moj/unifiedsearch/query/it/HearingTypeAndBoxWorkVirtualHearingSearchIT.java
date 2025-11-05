package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.HearingTypeAndHearingDateIngester.SEARCHED_HEARING_TYPE_UUID;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.HearingTypeAndIsVirtualBoxHearingIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class HearingTypeAndBoxWorkVirtualHearingSearchIT {

    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static final String HEARING_TYPE_ID = "hearingTypeId";
    private static final String BOX_WORK_VIRTUAL_HEARING = "boxWorkVirtualHearing";
    private final ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();
    private static HearingTypeAndIsVirtualBoxHearingIngester hearingTypeAndIsVirtualBoxHearingIngester = new HearingTypeAndIsVirtualBoxHearingIngester();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        hearingTypeAndIsVirtualBoxHearingIngester.loadCases();
    }

    @Test
    public void shouldReturnNothingWhenSearchingHearingTypeAndNonBoxVirtualHearing() throws Exception {
        final CaseSearchResponse result = searchApiClient.searchCases(of(HEARING_TYPE_ID, SEARCHED_HEARING_TYPE_UUID,
                BOX_WORK_VIRTUAL_HEARING, FALSE.toString()));
        assertCases(result, hearingTypeAndIsVirtualBoxHearingIngester.getNonVirtualBoxHearingSearchedCases());
    }


    @Test
    public void shouldReturnSearchResponseWhenSearchingByHearingTypeAndVirtualBoxHearing() throws Exception {
        final CaseSearchResponse result = searchApiClient.searchCases(of(HEARING_TYPE_ID, SEARCHED_HEARING_TYPE_UUID,
                BOX_WORK_VIRTUAL_HEARING, TRUE.toString()));
        assertCases(result, hearingTypeAndIsVirtualBoxHearingIngester.getExpectedVirtualBoxHearingCases());
    }
}
