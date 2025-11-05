package uk.gov.moj.unifiedsearch.query.it.cps;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.cps.CourtHouseDataIngestor.GUILDFORD;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.cps.CourtHouseDataIngestor.LEICESTER;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.CourtHouseDataIngestor;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CourtHouseQueryIT {

    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private CpsSearchApiClient cpsSearchApiClient = getInstance();
    private static CourtHouseDataIngestor courtHouseDataIngestor = new CourtHouseDataIngestor();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        courtHouseDataIngestor.loadCaseDocuments();
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsCourtOfAppealGetMultipleResults() throws Exception {
        final List<Case> cases = submitSearch(GUILDFORD);
        assertThat(cases.size(), is(3));
        assertCases(cases, courtHouseDataIngestor.getCaseDatas());
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsCourtOfAppealOrCrownAndGetMultipleResults() throws Exception {
        final List<Case> cases = submitSearch(LEICESTER);
        assertThat(cases.size(), is(1));
        assertCases(cases, asList(courtHouseDataIngestor.getIndexDocumentAt(1)));
    }

    @Test
    public void shouldReturnAllResultsWhereJurisdictionTypeIsEmpty() throws IOException {
        final Map<String, String> parameters = of("courtHouse", "");
        cpsSearchApiClient.searchCasesAndValidateStatusCode(parameters, SC_OK);
    }

    @Test
    public void shouldReturnNothingWhereJurisdictionTypeIsNotFound() throws IOException {
        final List<Case> caseListResults = submitSearch("US");
        assertThat(caseListResults, hasSize(0));
    }

    private List<Case> submitSearch(final String courtHouse) throws IOException {
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(ImmutableMap.of("courtHouse", courtHouse));
        return caseSearchResponse.getCases();
    }
}
