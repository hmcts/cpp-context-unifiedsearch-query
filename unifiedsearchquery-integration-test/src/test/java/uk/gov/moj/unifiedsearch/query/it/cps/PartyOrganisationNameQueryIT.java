package uk.gov.moj.unifiedsearch.query.it.cps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.ORGANISATION;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.OrganisationNameIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PartyOrganisationNameQueryIT {

    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static OrganisationNameIngester organisationNameIngester = new OrganisationNameIngester();
    private CpsSearchApiClient cpsSearchApiClient = getInstance();

    @BeforeAll
    public static void init() throws IOException {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        organisationNameIngester.loadCases();
    }

    @Test
    public void shouldReturnCaseMatchingOrganisationName() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORGANISATION, "DRS Solutions");
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, organisationNameIngester.getDRSSolutionsCaseDocument());
    }

    @Test
    public void shouldReturnCaseMatchingPartialOrganisationName() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORGANISATION, "K");
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, organisationNameIngester.getKitesCaseDocument());
    }

    @Test
    public void shouldReturnNoCaseMatchingOrganisationName() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORGANISATION, "Test");
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);
        assertThat(caseSearchResponse.getCases(), hasSize(0));
    }

}
