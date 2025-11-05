package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyNameAndAddressDataIngester.SEARCHED_PARTY_ADDRESS;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.PartyNameAndAddressDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PartyNameAndAddressSearchIT {

    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static PartyNameAndAddressDataIngester partyNameAndAddressDataIngester = new PartyNameAndAddressDataIngester();
    private static final String PARTY_ADDRESS = "partyAddress";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        partyNameAndAddressDataIngester.loadCases();
    }

    @Test
    public void shouldReturnNothingWhenSearchingByNameAndBadAddress() throws IOException {

        final CaseSearchResponse result = searchApiClient.searchCases(of(PARTY_ADDRESS, "some random address", "partyName", "Joe"));
        assertCases(result, ImmutableList.of());
    }


    @Test
    public void shouldReturnSearchResponseWhenSearchingByNameAndAddress() throws IOException {

        final CaseSearchResponse result = searchApiClient.searchCases(of(PARTY_ADDRESS, SEARCHED_PARTY_ADDRESS, "partyName", "Joe"));
        final List<CaseDocument> expectedCaseDocuments = partyNameAndAddressDataIngester.getExpectedCases();

        assertCases(result, expectedCaseDocuments);
    }
}
