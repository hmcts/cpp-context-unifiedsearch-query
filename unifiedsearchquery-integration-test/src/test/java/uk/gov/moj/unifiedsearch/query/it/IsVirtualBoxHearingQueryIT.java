package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.lang.String.valueOf;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.IsVirtualBoxHearingTypeIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IsVirtualBoxHearingQueryIT {

    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private IsVirtualBoxHearingTypeIngester virtualBoxHearingTypeIngester = new IsVirtualBoxHearingTypeIngester();
    private SearchApiClient searchApiClient = getInstance();

    @BeforeAll
    public static void setupStubs() throws Exception {
        initializeStubbing();
    }

    @BeforeEach
    public void setup() throws Exception {
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        virtualBoxHearingTypeIngester.loadCaseDocuments();
    }

    @Test
    public void shouldQueryByIsBoxHearingTrue() throws Exception {
        final CaseSearchResponse searchResponse = submitSearch(true);
        assertCases(searchResponse, virtualBoxHearingTypeIngester.getExpectedVirtualBoxHearingCases());
    }

    @Test
    public void shouldQueryByIsBoxHearingFalse() throws Exception {
        final CaseSearchResponse searchResponse = submitSearch(false);
        assertCases(searchResponse, virtualBoxHearingTypeIngester.getExpectedNonVirtualBoxHearingCases());
    }

    private CaseSearchResponse submitSearch(final boolean isVirtualBoxHearing) throws Exception {
        final Map<String, String> parameters = of("boxWorkVirtualHearing", valueOf(isVirtualBoxHearing));
        return searchApiClient.searchCases(parameters);
    }
}
