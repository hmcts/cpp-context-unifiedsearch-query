package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertLaaCase.assertOffence;
import static uk.gov.moj.unifiedsearch.query.it.util.LaaSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.CaseSummary;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.OffenceSummary;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.OffenceDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.LaaSearchShouldExcludeDeletedOffencesIngester;
import uk.gov.moj.unifiedsearch.query.it.util.LaaSearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LaaSearchShouldExcludeDeletedOffences {

    private static final String NINO = "defendantNINO";
    private LaaSearchApiClient laaSearchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static LaaSearchShouldExcludeDeletedOffencesIngester searchIngester = new LaaSearchShouldExcludeDeletedOffencesIngester();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        searchIngester.loadCases();
        ;
    }

    @Test
    public void shouldExcludeDeletedOffencesFromLaaResponse() throws IOException {
        final List<CaseSummary> caseListResults = submitSearch("nino123");

        final OffenceDocument expectedOffence = searchIngester.getExpectedOffence();

        assertThat(caseListResults, hasSize(1));

        final List<OffenceSummary> offenceSummary = caseListResults.get(0).getDefendantSummary().get(0).getOffenceSummary();
        assertThat(offenceSummary.size(), CoreMatchers.is(1));
        assertOffence(offenceSummary.get(0), expectedOffence);
    }

    private List<CaseSummary> submitSearch(final String nino) throws IOException {
        final Map<String, String> parameters = of(NINO, nino);

        final CaseSearchResponse caseSearchResponse = laaSearchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();
    }
}
