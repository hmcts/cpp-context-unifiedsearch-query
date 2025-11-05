package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Collections.emptyList;
import static org.apache.http.HttpStatus.SC_OK;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationReferenceAndApplicationTypeIngester.SEARCHED_APPLICATION_REFERENCE;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationReferenceAndApplicationTypeIngester.SEARCHED_APPLICATION_TYPE;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.CONTEXT_USER_ID;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationReferenceAndApplicationTypeIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ApplicationReferenceAndApplicationTypeSearchIT {

    private SearchApiClient searchApiClient = SearchApiClient.getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static ApplicationReferenceAndApplicationTypeIngester ingester = new ApplicationReferenceAndApplicationTypeIngester();
    private static final String REFERENCE = "caseReference";
    private static final String APPLICATION_TYPE = "applicationType";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        ingester.loadCases();
    }

    @Test
    public void shouldReturnNothingWhenSearchingForWrongApplicationReference() throws IOException {
        final CaseSearchResponse result = searchApiClient.searchCases(of(REFERENCE, "aaaaa", APPLICATION_TYPE, SEARCHED_APPLICATION_TYPE));
        assertCases(result, emptyList());
    }

    @Test
    public void shouldReturnNothingWhenSearchingForWrongApplicationType() throws IOException {
        final CaseSearchResponse result = searchApiClient.searchCases(of(REFERENCE, SEARCHED_APPLICATION_REFERENCE, APPLICATION_TYPE, "Some random application type"));
        assertCases(result, emptyList());
    }


    @Test
    public void shouldReturnSearchResponseWhenSearchingForReferenceAndApplicationType() throws IOException {

        final CaseSearchResponse result = search(of(REFERENCE, SEARCHED_APPLICATION_REFERENCE, APPLICATION_TYPE, SEARCHED_APPLICATION_TYPE));
        assertCases(result, ingester.getExpectedCases());
    }

    private CaseSearchResponse search(Map<String, String> params) throws IOException {
        final Map<String, String> headers = of("CJSCPPUID", CONTEXT_USER_ID);
        return searchApiClient.searchCasesAndValidateStatusCode(params, headers, SC_OK);

    }
}
