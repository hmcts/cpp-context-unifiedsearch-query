package uk.gov.moj.unifiedsearch.query.it.cps;

import static org.elasticsearch.search.sort.SortOrder.ASC;
import static org.elasticsearch.search.sort.SortOrder.DESC;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.HEARING_DATE_SORT_BY;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.ORDER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.ORDER_BY;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCase.assertCase;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.SortByHearingDateIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SortByHearingDateQueryIT {

    private CpsSearchApiClient searchApiClient = CpsSearchApiClient.getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static SortByHearingDateIngester sortByHearingDateIngester = new SortByHearingDateIngester();

    @BeforeEach
    public void setupStubs() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        sortByHearingDateIngester.loadCaseDocuments();
    }

    @Test
    public void shouldReturnCasesOrderedByHearingDateTimeDesc() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, HEARING_DATE_SORT_BY);
        parameters.putIfAbsent(ORDER, DESC.toString());

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), CoreMatchers.is(3L));
        assertCase(caseSearchResponse.getCases().get(0), sortByHearingDateIngester.getExpectedTopHearingDesc());
        assertCase(caseSearchResponse.getCases().get(2), sortByHearingDateIngester.getExpectedBottomHearingDec());
    }

    @Test
    public void shouldReturnCasesOrderedByHearingDateTimeAsc() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, HEARING_DATE_SORT_BY);
        parameters.putIfAbsent(ORDER, ASC.toString());

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), CoreMatchers.is(3L));
        assertCase(caseSearchResponse.getCases().get(0), sortByHearingDateIngester.getExpectedTopHearingAsc());
        assertCase(caseSearchResponse.getCases().get(2), sortByHearingDateIngester.getExpectedBottomTopHearingAsc());
    }

}
