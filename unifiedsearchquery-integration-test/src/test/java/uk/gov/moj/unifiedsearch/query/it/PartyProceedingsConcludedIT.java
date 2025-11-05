package uk.gov.moj.unifiedsearch.query.it;

import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.CROWN_OR_MAGISTRATES;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PROCEEDINGS_CONCLUDED;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;
import static uk.gov.moj.unifiedsearch.query.it.util.defendantCase.AssertDefendantCases.assertDefendantCases;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.defendant.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.ProceedingsConcludedDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PartyProceedingsConcludedIT {
    private static final String PAGE_SIZE = "pageSize";

    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static ProceedingsConcludedDataIngester partyIngester = new ProceedingsConcludedDataIngester();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        partyIngester.loadProceedingsConcludedCases();
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByProceedingsConcludedFalse() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES,"true");
        parameters.putIfAbsent(PAGE_SIZE, "10");

        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        final List<CaseDocument> expectedCaseDocuments = partyIngester.getExpectedCasesForProceedingsConcludedWhenFalse();
        assertDefendantCases(result, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByProceedingsConcludedTrue() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "true");
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES,"true");
        parameters.putIfAbsent(PAGE_SIZE, "10");

        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        final List<CaseDocument> expectedCaseDocuments = partyIngester.getExpectedCasesForProceedingsConcludedWhenTrue();
        assertDefendantCases(result, expectedCaseDocuments);
    }
}
