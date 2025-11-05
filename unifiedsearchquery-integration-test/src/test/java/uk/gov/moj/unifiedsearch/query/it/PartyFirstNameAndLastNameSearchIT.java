package uk.gov.moj.unifiedsearch.query.it;

import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTY_FIRST_NAME_AND_OR_MIDDLE_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTY_LAST_NAME_OR_ORGANISATION_NAME;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.PartyFirstAndLastNameIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PartyFirstNameAndLastNameSearchIT {
    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static PartyFirstAndLastNameIngester partyIngester = new PartyFirstAndLastNameIngester();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        partyIngester.loadCases();
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingPartyFirstNameAndLastName() throws IOException {
        final Map<String, String> parameters = ImmutableMap.of(PARTY_FIRST_NAME_AND_OR_MIDDLE_NAME, "Joe", PARTY_LAST_NAME_OR_ORGANISATION_NAME, "Doe");

        final CaseSearchResponse result = searchApiClient.searchCases(parameters);
        final List<CaseDocument> expectedCaseDocuments = partyIngester.getJoeDoe();
        assertCases(result, expectedCaseDocuments);
    }
}
