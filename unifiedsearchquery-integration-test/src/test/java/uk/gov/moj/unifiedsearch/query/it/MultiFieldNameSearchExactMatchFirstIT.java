package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Collections.emptyList;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.MultiFieldNameSearchExactMatchFirstIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MultiFieldNameSearchExactMatchFirstIT {

    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static MultiFieldNameSearchExactMatchFirstIngester searchDataHelper = new MultiFieldNameSearchExactMatchFirstIngester();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        searchDataHelper.loadCases();
    }

    @Test
    public void shouldFindJoeAsTopResult() throws IOException {
        final Map<String, String> parameters = of("partyFirstAndOrMiddleName", "Joe", "partyLastNameOrOrganisationName", "Smith");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, searchDataHelper.getCasesWithJoeSmith());
    }


    @Test
    public void shouldFindTamaraAsTopResult() throws IOException {
        final Map<String, String> parameters = of("partyFirstAndOrMiddleName", "Tamara", "partyLastNameOrOrganisationName", "Smith");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, searchDataHelper.getCasesWithTamaraSmith());
    }

    @Test
    public void shouldFinEricAsTopResult() throws IOException {
        final Map<String, String> parameters = of("partyFirstAndOrMiddleName", "Eric", "partyLastNameOrOrganisationName", "Smith");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, searchDataHelper.getCasesWithEricSmith());
    }

    @Test
    public void shouldFindNothingWhenWeSwapFirstAndLastNam() throws IOException {
        final Map<String, String> parameters = of("partyFirstAndOrMiddleName", "Smith", "partyLastNameOrOrganisationName", "Joe");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, emptyList());
    }

    @Test
    public void shouldFindwithPartialName() throws IOException {
        final Map<String, String> parameters = of("partyFirstAndOrMiddleName", "Tama", "partyLastNameOrOrganisationName", "Smith");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, searchDataHelper.getCasesWithTamaraSmith());
    }

    @Test
    public void shouldFindWithMiddleName() throws IOException {
        final Map<String, String> parameters = of("partyFirstAndOrMiddleName", "Tamara Olive", "partyLastNameOrOrganisationName", "Smith");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, searchDataHelper.getCasesWithTamaraSmith());
    }

    @Test
    public void shouldFindwithFirstNameOnly() throws IOException {
        final Map<String, String> parameters = of("partyFirstAndOrMiddleName", "Tamara");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);


        assertCases(caseSearchResponse, searchDataHelper.getCasesWithTamaraSmith());
    }

    @Test
    public void shouldFindNothingWhenUsingFirstNAmeAsLastName() throws IOException {
        final Map<String, String> parameters = of("partyLastNameOrOrganisationName", "Joe");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, emptyList());
    }

    @Test
    public void shouldFindAllByLastName() throws IOException {
        final Map<String, String> parameters = of("partyLastNameOrOrganisationName", "Smith");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, searchDataHelper.getCasesWithSmiths());
    }
}
