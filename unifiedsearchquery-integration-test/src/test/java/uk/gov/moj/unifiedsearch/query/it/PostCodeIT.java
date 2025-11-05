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
import uk.gov.moj.unifiedsearch.query.it.ingestors.PostCodeSearchDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PostCodeIT {

    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static PostCodeSearchDataIngester postCodeSearchDataIngester = new PostCodeSearchDataIngester();
    private static final String PARTY_POSTCODE = "partyPostcode";
    private static final String PARTY_NAME = "partyName";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        postCodeSearchDataIngester.loadPostCodeData();
    }

    @Test
    public void shouldReturnMultipleResultsForSingleWordSearch() throws Exception {
        final Map<String, String> parameters = of(PARTY_POSTCODE, "TG14", PARTY_NAME, "TEST");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getCases(), hasSize(5));
    }

    @Test
    public void shouldReturnSingleResult() throws Exception {
        final Map<String, String> parameters = of(PARTY_POSTCODE, "TG14 T67", PARTY_NAME, "TEST");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getCases(), hasSize(1));
        assertThat(caseSearchResponse.getCases().get(0).getParties().get(0).getPostCode(), is("TG14 T67"));
    }

    @Test
    public void shouldPerformCaseAndSpaceIntensiveSearch() throws Exception {
        final Map<String, String> parameters = of(PARTY_POSTCODE, "tg14t67", PARTY_NAME, "TEST");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getCases(), hasSize(1));
        assertThat(caseSearchResponse.getCases().get(0).getParties().get(0).getPostCode(), is("TG14 T67"));
    }

    @Test
    public void shouldReturnNothingWhenPostCodeIsEmpty() throws Exception {
        final Map<String, String> parameters = of(PARTY_POSTCODE, "");
        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }


    @Test
    public void shouldReturnBadRequestWhenPostCodeParameterProvidedWithNoOtherParameter() throws Exception {
        final Map<String, String> parameters = of(PARTY_POSTCODE, "TG14 67T");
        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }
}

