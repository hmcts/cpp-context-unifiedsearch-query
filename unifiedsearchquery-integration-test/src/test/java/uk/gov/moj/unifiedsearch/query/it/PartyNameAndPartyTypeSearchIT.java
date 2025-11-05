package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.DEFENDANT;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.PartyTypeSearchDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PartyNameAndPartyTypeSearchIT {
    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static PartyTypeSearchDataIngester partyTypeSearchDataIngester = new PartyTypeSearchDataIngester();
    private static final String PARTY_TYPES = "partyTypes";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        partyTypeSearchDataIngester.loadCases();
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByDefendantAndName() throws IOException {

        final CaseSearchResponse result = searchApiClient.searchCases(of(PARTY_TYPES, DEFENDANT.name(),
                "partyName", "Joe"));
        final List<CaseDocument> expectedCaseDocuments = partyTypeSearchDataIngester.getCasesWithDefendantJoe();

        assertCases(result, expectedCaseDocuments);
    }
}
