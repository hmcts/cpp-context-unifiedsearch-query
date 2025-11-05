package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertLaaCase.assertCase;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertLaaCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.LaaSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.CaseSummary;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.probation.ProbationDefendantDetailsSummary;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.AsnSearchDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.AssertProbationDefendantDetails;
import uk.gov.moj.unifiedsearch.query.it.util.LaaSearchApiClient;
import uk.gov.moj.unifiedsearch.query.it.util.ProbationDefendantDetailsSearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ASNQueryIT {

    private static final String ASN = "defendantASN";
    private LaaSearchApiClient laaSearchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static AsnSearchDataIngester asnSearchDataIngester = new AsnSearchDataIngester();
    private final ProbationDefendantDetailsSearchApiClient probationDefendantDetailsSearchApiClient = ProbationDefendantDetailsSearchApiClient.getInstance();

    @BeforeAll
    public static void setup() throws IOException {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        asnSearchDataIngester.loadAsnData();
    }

    @Test
    public void shouldReturnSingleResultForAsnQuery() throws IOException {

        final List<CaseSummary> caseListResults = submitSearch("ASN003");

        final CaseDocument expectedCaseDocument = asnSearchDataIngester.getCaseForAsn003();

        assertThat(caseListResults, hasSize(1));

        assertCase(caseListResults.get(0), expectedCaseDocument);
    }

    @Test
    public void shouldAsnQueryBeCaseInsensitive() throws IOException {

        final List<CaseSummary> caseListResults = submitSearch("Asn003");

        final CaseDocument expectedCaseDocument = asnSearchDataIngester.getCaseForAsn003();

        assertThat(caseListResults, hasSize(1));

        assertCase(caseListResults.get(0), expectedCaseDocument);
    }

    @Test
    public void shouldGetMultipleCases() throws IOException {

        final List<CaseSummary> caseListResults = submitSearch("TFL");

        final List<CaseDocument> expectedCaseDocuments = asnSearchDataIngester.getCasesForTflAsn();

        assertCases(caseListResults, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnNothingWhereAsnIsNotFound() throws IOException {
        final String asn = "some fake ASN";
        final List<CaseSummary> caseListResults = submitSearch(asn);

        assertThat(caseListResults, hasSize(0));
    }

    @Test
    public void shouldReturnNothingWhereAsnIsEmpty() throws IOException {
        final Map<String, String> parameters = of(ASN, "");

        laaSearchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }


    @Test
    public void shouldFindProbationDefendantDetailsWhenAsnIsPassed() throws IOException {


        final Map<String, String> parameters = of(ASN, "ASN004");

        final List<ProbationDefendantDetailsSummary> caseSearchResponse = probationDefendantDetailsSearchApiClient.searchCases(parameters).getCases();

        final CaseDocument expectedCaseDocument = asnSearchDataIngester.getCaseForAsn004();

        assertThat(caseSearchResponse, Matchers.hasSize(1));

        AssertProbationDefendantDetails.assertCase(caseSearchResponse.get(0), expectedCaseDocument);
    }

    private List<CaseSummary> submitSearch(final String asn) throws IOException {
        final Map<String, String> parameters = of(ASN, asn);

        final CaseSearchResponse caseSearchResponse = laaSearchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();
    }
}
