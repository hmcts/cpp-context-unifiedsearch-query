package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Collections.emptyList;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.NameSearchMostRelevantFirstIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class NameSearchMostRelevantFirstIT {

    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static NameSearchMostRelevantFirstIngester searchDataHelper = new NameSearchMostRelevantFirstIngester();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        searchDataHelper.loadCases();
    }

    @Test
    public void shouldFindNothingWhenOneNameDoesNotMatch() throws IOException {
        final Map<String, String> parameters = of("partyFirstAndOrMiddleName", "aaa", "partyLastNameOrOrganisationName", "Smith");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, emptyList());
    }

    @Test
    public void shouldFindByFirstCharOfFirstName() throws IOException {
        final Map<String, String> parameters = of("partyFirstAndOrMiddleName", "d", "partyLastNameOrOrganisationName", "Smith");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, searchDataHelper.getCasesWithDavidSmithUpdated());
    }

    @Test
    public void shouldFindSingleCharFirstName() throws IOException {
        final Map<String, String> parameters = of("partyFirstAndOrMiddleName", "y", "partyLastNameOrOrganisationName", "Smith");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, searchDataHelper.getCasesWithYSmith());
    }
}
