package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyNameAndPostcodeDataIngester.PARTY_POSTCODE_CR0_2AB;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.PartyNameAndPostcodeDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PartyNameAndPostcodeSearchIT {

    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static PartyNameAndPostcodeDataIngester partyNameAndPostcodeDataIngester = new PartyNameAndPostcodeDataIngester();
    private static final String PARTY_POSTCODE = "partyPostcode";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        partyNameAndPostcodeDataIngester.loadCases();
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByNameAndPostcode() throws IOException {

        final CaseSearchResponse result = searchApiClient.searchCases(of(PARTY_POSTCODE, PARTY_POSTCODE_CR0_2AB, "partyName", "Joe"));
        final List<CaseDocument> expectedCaseDocuments = partyNameAndPostcodeDataIngester.getExpectedCases();

        assertCases(result, expectedCaseDocuments);
    }
}
