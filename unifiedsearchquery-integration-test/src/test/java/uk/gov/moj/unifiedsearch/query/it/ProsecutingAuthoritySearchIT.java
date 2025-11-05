package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Collections.emptyList;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.ProsecutingAuthority.DVLA;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.ProsecutingAuthority.TFL;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.ProsecutingAuthority.TVL;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.ProsecutingAuthoritySearchDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ProsecutingAuthoritySearchIT {
    private static final String PROSECUTING_AUTHORITY = "prosecutingAuthority";
    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static ProsecutingAuthoritySearchDataIngester prosecutingAuthoritySearchDataIngester = new ProsecutingAuthoritySearchDataIngester();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        prosecutingAuthoritySearchDataIngester.loadCases();
    }


    @Test
    public void shouldFindNothingIfIncorrectProsecutingAuthorityIsProvided() throws IOException {

        final Map<String, String> parameters = of(PROSECUTING_AUTHORITY, "INCORRECT");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, emptyList());
    }

    @Test
    public void shouldFindNothingWhenSearchingForDvla() throws IOException {
        final Map<String, String> parameters = of(PROSECUTING_AUTHORITY, DVLA.name());

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, emptyList());
    }


    @Test
    public void shouldFindDataWhenSearchingForTfl() throws IOException {
        final List<CaseDocument> expectedTflCases = prosecutingAuthoritySearchDataIngester.getTflCases();
        final Map<String, String> parameters = of(PROSECUTING_AUTHORITY, TFL.name());

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, expectedTflCases);
    }

    @Test
    public void shouldFindNothingWhenSearchingForPartialProsecutingAuthority() throws IOException {
        final Map<String, String> parameters = of(PROSECUTING_AUTHORITY, "TF");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, emptyList());
    }

    @Test
    public void shouldFindDataWhenSearchingForTflCaseInsensitive() throws IOException {
        final List<CaseDocument> expectedTflCases = prosecutingAuthoritySearchDataIngester.getTflCases();
        final Map<String, String> parameters = of(PROSECUTING_AUTHORITY, "tFl");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, expectedTflCases);
    }

    @Test
    public void shouldFindDataWhenSearchingForTvl() throws IOException {
        final List<CaseDocument> expectedTvlCases = prosecutingAuthoritySearchDataIngester.getTvlCases();
        final Map<String, String> parameters = of(PROSECUTING_AUTHORITY, TVL.name());

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);
        assertCases(caseSearchResponse, expectedTvlCases);
    }

    @Test
    public void shouldReturnNothingWhenProsecutingAuthorityIsEmpty() throws IOException {
        final Map<String, String> parameters = of(PROSECUTING_AUTHORITY, "");
        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }

}
