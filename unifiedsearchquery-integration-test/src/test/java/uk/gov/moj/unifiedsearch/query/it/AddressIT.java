package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.AddressSearchDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AddressIT {

    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static AddressSearchDataIngester addressSearchDataIngester = new AddressSearchDataIngester();
    private static final String PARTY_ADDRESS = "partyAddress";
    private static final String PARTY_POSTCODE = "partyPostcode";
    private static final String PAGE_SIZE = "pageSize";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        addressSearchDataIngester.loadCasesAddressData();
    }

    @Test
    public void shouldReturnMultipleResultsForSingleWordSearch() throws Exception {
        final Map<String, String> parameters = of(PARTY_ADDRESS, "Liverpool");
        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getCases(), hasSize(6));
    }

    @Test
    public void shouldReturnSingleResult() throws Exception {
        final Map<String, String> parameters = of(PARTY_ADDRESS, "130 Liverpool road");
        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getCases(), hasSize(1));
        assertThat(caseSearchResponse.getCases().get(0).getParties().get(0).getAddressLines(), is("130 Liverpool road Brighton Rhode Island"));
    }

    @Test
    public void shouldPerformCaseIntensiveSearch() throws Exception {
        final Map<String, String> parameters = of(PARTY_ADDRESS, "liverpool road");
        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getCases(), hasSize(1));
        assertThat(caseSearchResponse.getCases().get(0).getParties().get(0).getAddressLines(), is("130 Liverpool road Brighton Rhode Island"));
    }

    @Test
    public void shouldSearchBySynonyms() throws Exception {
        final Map<String, String> parameters = of(PARTY_ADDRESS, "liverpool rd.");
        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getCases(), hasSize(1));
        assertThat(caseSearchResponse.getCases().get(0).getParties().get(0).getAddressLines(), is("130 Liverpool road Brighton Rhode Island"));
    }

    @Test
    public void shouldPerformSearchWithMultipleQueryParams() throws Exception {
        final Map<String, String> parameters = of(PARTY_ADDRESS, "mitchley", PAGE_SIZE, "10");
        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getCases(), hasSize(2));

        final Map<String, String> parametersNew = of(PARTY_ADDRESS, "mitchley", PARTY_POSTCODE, "CR8", PAGE_SIZE, "10");
        final CaseSearchResponse caseSearchResponseNew = searchApiClient.searchCases(parametersNew);

        assertThat(caseSearchResponseNew.getCases(), hasSize(1));
        assertThat(caseSearchResponseNew.getCases().get(0).getParties().get(1).getAddressLines(), is("48 Mitchley Avenue Riddlesdown Purley"));

        final Map<String, String> parametersFinal = of(PARTY_ADDRESS, "mitchley", PARTY_POSTCODE, "CR2", PAGE_SIZE, "10");
        final CaseSearchResponse caseSearchResponseFinal = searchApiClient.searchCases(parametersFinal);

        assertThat(caseSearchResponseFinal.getCases(), hasSize(1));
        assertThat(caseSearchResponseFinal.getCases().get(0).getParties().get(1).getAddressLines(), is("44 Mitchley Avenue Riddlesdown Purley"));
    }

    @Test
    public void shouldResultIfNoResultsIfNoMatchesFound() throws Exception {
        final Map<String, String> parameters = of(PARTY_ADDRESS, "liverpool road", PARTY_POSTCODE, "CR8");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getCases(), hasSize(0));
    }

    @Test
    public void shouldReturnNoResultsIfNoAddressIsProvided() throws Exception {
        final Map<String, String> parameters = of(PARTY_ADDRESS, "");

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }

}
