package uk.gov.moj.unifiedsearch.query.it.cps;

import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OPERATION_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.ORGANISATION;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.cps.OrganisationNameAndOperationNameIngester.COOL_OPERATION;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.cps.OrganisationNameAndOperationNameIngester.COOL_ORGANISATION;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.OrganisationNameAndOperationNameIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PartyOrganisationNameAndOperationNameQueryIT {

    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static OrganisationNameAndOperationNameIngester organisationNameAndOperationNameIngester = new OrganisationNameAndOperationNameIngester();
    private CpsSearchApiClient cpsSearchApiClient = getInstance();

    @BeforeAll
    public static void init() throws IOException {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        organisationNameAndOperationNameIngester.loadCases();
    }

    @Test
    public void shouldReturnCaseMatchingOrganisationName() throws IOException {
        final Map<String, String> parameters = ImmutableMap.of(ORGANISATION, COOL_ORGANISATION, OPERATION_NAME, COOL_OPERATION);
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, organisationNameAndOperationNameIngester.getExpectedCaseDocuments());
    }

}
