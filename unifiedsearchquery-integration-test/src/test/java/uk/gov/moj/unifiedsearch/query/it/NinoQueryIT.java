package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertLaaCase.assertCase;
import static uk.gov.moj.unifiedsearch.query.it.util.LaaSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.CaseSummary;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.probation.ProbationDefendantDetailsSummary;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.NinoSearchDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.AssertProbationDefendantDetails;
import uk.gov.moj.unifiedsearch.query.it.util.LaaSearchApiClient;
import uk.gov.moj.unifiedsearch.query.it.util.ProbationDefendantDetailsSearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class NinoQueryIT {

    private static final String NINO = "defendantNINO";
    private LaaSearchApiClient laaSearchApiClient = getInstance();
    private final ProbationDefendantDetailsSearchApiClient probationDefendantDetailsSearchApiClient = ProbationDefendantDetailsSearchApiClient.getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static NinoSearchDataIngester ninoSearchDataIngester = new NinoSearchDataIngester();

    @BeforeAll
    public static void setup() throws IOException {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        ninoSearchDataIngester.loadNinoData();
    }

    @Test
    public void shouldReturnSingleResultForNinoQuery() throws IOException {
        final List<CaseSummary> caseListResults = submitSearch("HJ123456S");

        final CaseDocument expectedCaseDocument = ninoSearchDataIngester.getIndexDocumentAt(3);

        assertThat(caseListResults, hasSize(1));

        assertCase(caseListResults.get(0), expectedCaseDocument);
    }

    @Test
    public void shouldReturnSingleResultForNinoQueryCaseInsensitive() throws IOException {
        final List<CaseSummary> caseListResults = submitSearch("hj123456S");
        final CaseDocument expectedCaseDocument = ninoSearchDataIngester.getIndexDocumentAt(3);
        assertThat(caseListResults, hasSize(1));
        assertCase(caseListResults.get(0), expectedCaseDocument);
    }

    @Test
    public void shouldReturnNothingWhereNinoIsNotFound() throws IOException {
        final String nino = "ZZ999999Z";
        final List<CaseSummary> caseListResults = submitSearch(nino);

        assertThat(caseListResults, hasSize(0));
    }

    @Test
    public void shouldReturnNothingWhereNinoIsEmpty() throws IOException {
        final Map<String, String> parameters = of(NINO, "");

        laaSearchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }

    @Test
    public void shouldReturnProbationDefendantDetailsSingleResultForNinoQuery() throws IOException {
        final Map<String, String> parameters = of(NINO, "HJ123456F");

        final List<ProbationDefendantDetailsSummary> caseSearchResponse =  probationDefendantDetailsSearchApiClient.searchCases(parameters).getCases();

        final CaseDocument expectedCaseDocument = ninoSearchDataIngester.getIndexDocumentAt(4);

        assertThat(caseSearchResponse, Matchers.hasSize(1));

        AssertProbationDefendantDetails.assertCase(caseSearchResponse.get(0), expectedCaseDocument);
    }

    private List<CaseSummary> submitSearch(final String nino) throws IOException {
        final Map<String, String> parameters = of(NINO, nino);

        final CaseSearchResponse caseSearchResponse = laaSearchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();
    }
}
