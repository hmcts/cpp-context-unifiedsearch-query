package uk.gov.moj.unifiedsearch.query.it;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.CROWN_OR_MAGISTRATES;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PROCEEDINGS_CONCLUDED;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;
import static uk.gov.moj.unifiedsearch.query.it.util.defendantCase.AssertDefendantCases.assertDefendantCases;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.defendant.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.CrownOrMagistratesDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PartyIsCrownOrMagistratesIT {
    private static final String PAGE_SIZE = "pageSize";

    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static CrownOrMagistratesDataIngester crownOrMagistratesDataIngester = new CrownOrMagistratesDataIngester();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        crownOrMagistratesDataIngester.loadCrownOrMagistratesCases();
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByCrownOrMagistratsIsTrue() throws IOException {
        final Map<String, String> parameters = new HashMap();

        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES, "true");
        parameters.putIfAbsent(PAGE_SIZE, "10");
        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        assertThat(result.getCases().size(),is(6));
        final List<CaseDocument> expectedCaseDocuments = crownOrMagistratesDataIngester.getExpectedCasesAC8_WhenCrownOrMagistratesIsTrue();
        assertDefendantCases(result, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByCrownOrMagistratsIsFalse() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES, "false");
        parameters.putIfAbsent(PAGE_SIZE, "12");
        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        assertThat(result.getCases().size(),is(12));
        final List<CaseDocument> expectedCaseDocuments = crownOrMagistratesDataIngester.getExpectedCasesAC8_WhenCrownOrMagistratesIsFalse();
        assertDefendantCases(result, expectedCaseDocuments);
    }
}
