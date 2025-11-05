package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyNameAndDateOfBirthSearchDataIngester.PARTY_DATE_OF_BIRTH_1958;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.PartyNameAndDateOfBirthSearchDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PartyNameAndDateOfBirthSearchIT {

    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static PartyNameAndDateOfBirthSearchDataIngester partyNameAndDateOfBirthSearchDataIngester = new PartyNameAndDateOfBirthSearchDataIngester();
    private static final String PARTY_DATE_OF_BIRTH = "partyDateOfBirth";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        partyNameAndDateOfBirthSearchDataIngester.loadCases();
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByNameAndDateOfBirth() throws IOException {

        final CaseSearchResponse result = searchApiClient.searchCases(of(PARTY_DATE_OF_BIRTH, PARTY_DATE_OF_BIRTH_1958, "partyName", "Joe"));
        final List<CaseDocument> expectedCaseDocuments = partyNameAndDateOfBirthSearchDataIngester.getCasesWithJoeAndDateOfBirth58();

        assertCases(result, expectedCaseDocuments);
    }


}
