package uk.gov.moj.unifiedsearch.query.it;

import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.ADDRESS_LINE_1;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.COURT_ORDER_VALIDITY_DATE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.CROWN_OR_MAGISTRATES;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.CRO_NUMBER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.DATE_OF_BIRTH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.FIRST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.LAST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PNC_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PROCEEDINGS_CONCLUDED;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_ADDRESS_LINE_AC4;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_CRO_NUMBER_123456_60g_AC4;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_DOB_AC4;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_FIRST_NAME_AC4;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_LAST_NAME_AC4;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_PNC_ID_5555_8135792K_AC4;
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

public class PncIdAndCroNumberAndPartyFirstNameAndLastNameAndDOBAndDefendantAddressLine1SearchIT {
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
    public void shouldReturnSearchResponseWhenSearchingPncIdAndCroNumberAndPartyFirstNameAndLastNameAndDOBAndDefendantAddressLine1() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES, "true");
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(PNC_ID, EXPECTED_PARTY_PNC_ID_5555_8135792K_AC4);
        parameters.putIfAbsent(CRO_NUMBER, EXPECTED_PARTY_CRO_NUMBER_123456_60g_AC4);
        parameters.putIfAbsent(FIRST_NAME, EXPECTED_PARTY_FIRST_NAME_AC4);
        parameters.putIfAbsent(LAST_NAME, EXPECTED_PARTY_LAST_NAME_AC4);
        parameters.putIfAbsent(DATE_OF_BIRTH, EXPECTED_PARTY_DOB_AC4);
        parameters.putIfAbsent(ADDRESS_LINE_1, EXPECTED_PARTY_ADDRESS_LINE_AC4);
        parameters.putIfAbsent(PAGE_SIZE, "10");
        parameters.putIfAbsent(COURT_ORDER_VALIDITY_DATE, "2021-01-12");
        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        final List<CaseDocument> expectedCaseDocuments = partyIngester.getExpectedCasesForFNLNDOBADDPNCIDCRO();
        assertDefendantCases(result, expectedCaseDocuments);
    }
}
