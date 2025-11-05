package uk.gov.moj.unifiedsearch.query.it;

import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.CROWN_OR_MAGISTRATES;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.LAST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PNC_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PROCEEDINGS_CONCLUDED;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_LAST_NAME_AC1;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_PNC_ID_1_2099_1234567L_AC1;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;
import static uk.gov.moj.unifiedsearch.query.it.util.defendantCase.AssertDefendantCases.assertDefendantCases;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.defendant.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.PartyIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PartyLastNameAndPncIdSearchIT {
    private static final String PAGE_SIZE = "pageSize";

    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static PartyIngester partyIngester = new PartyIngester();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        partyIngester.loadCases();
    }

    @Test
    public void shouldReturnOnlyDefendantPartiesWhenSearchingByLastNameAndPncId() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES,"true");
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(PNC_ID, EXPECTED_PARTY_PNC_ID_1_2099_1234567L_AC1);
        parameters.putIfAbsent(LAST_NAME, EXPECTED_PARTY_LAST_NAME_AC1);
        parameters.putIfAbsent(PAGE_SIZE, "10");

        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        final List<CaseDocument> expectedCaseDocuments = partyIngester.getExpectedCasesForPncIdAndLastNameForDefendant();
        assertDefendantCases(result, expectedCaseDocuments);
    }
}
