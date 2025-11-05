package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyNameAndAsnDataIngester.ASN_1;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyNameAndAsnDataIngester.FIRSTNAME_1;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.PartyNameAndAsnDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PartyNameAndAsnSearchIT {

    public static final String PARTY_NAME = "partyName";
    public static final String PARTY_ASN = "partyArrestSummonsNumber";

    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static PartyNameAndAsnDataIngester partyNameAndAsnDataIngester = new PartyNameAndAsnDataIngester();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        partyNameAndAsnDataIngester.loadCases();
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByNameAndAsn() throws IOException {
        final CaseSearchResponse result = searchApiClient.searchCases(of(PARTY_ASN, ASN_1,
                                                                         PARTY_NAME, FIRSTNAME_1));
        final CaseDocument expectedCaseDocuments = partyNameAndAsnDataIngester.getExpectedCases(0);

        assertThat(result.getTotalResults(), is(1L));
        assertCases(result, asList(expectedCaseDocuments));
    }

    @Test
    public void shouldReturnNoSearchResponseWhenSearchingByNameAndInvalidAsn() throws IOException {
        final CaseSearchResponse result = searchApiClient.searchCases(of(PARTY_ASN, "Invalid asn",
                                                                         PARTY_NAME, FIRSTNAME_1));
        assertThat(result.getTotalResults(), is(0L));
    }
}
