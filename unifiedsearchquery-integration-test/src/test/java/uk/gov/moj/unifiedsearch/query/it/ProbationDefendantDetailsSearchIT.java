package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ReferenceSearchDataIngester.TFL_CASE_REFERENCE;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertProbationDefendantDetails.assertCase;
import static uk.gov.moj.unifiedsearch.query.it.util.ProbationDefendantDetailsSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.probation.ProbationDefendantDetailsResponse;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.probation.ProbationDefendantDetailsSummary;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.ReferenceSearchDataIngesterWithAddress;
import uk.gov.moj.unifiedsearch.query.it.util.ProbationDefendantDetailsSearchApiClient;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ProbationDefendantDetailsSearchIT {

    private final ProbationDefendantDetailsSearchApiClient probationDefendantDetailsSearchApiClient = getInstance();
    private static final ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static final ReferenceSearchDataIngesterWithAddress referenceSearchDataHelper = new ReferenceSearchDataIngesterWithAddress();
    private static final String PROSECUTION_CASE_REFERENCE = "prosecutionCaseReference";
    private static final String ASN = "defendantASN";
    private static final String NINO = "defendantNINO";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        referenceSearchDataHelper.loadCases();
    }

    @Test
    public void shouldFindNothingIfPartialReferenceIsProvided() throws IOException {
        final Map<String, String> parameters = of(PROSECUTION_CASE_REFERENCE, TFL_CASE_REFERENCE.substring(0, TFL_CASE_REFERENCE.length() - 2));

        final ProbationDefendantDetailsResponse caseSearchResponse = probationDefendantDetailsSearchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(0L));
        assertThat(caseSearchResponse.getCases(), hasSize(0));
    }

    @Test
    public void shouldFindNothingIfIncorrectReferenceIsProvided() throws IOException {
        final Map<String, String> parameters = of(PROSECUTION_CASE_REFERENCE, TFL_CASE_REFERENCE + "INCORRECT");

        final ProbationDefendantDetailsResponse caseSearchResponse = probationDefendantDetailsSearchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(0L));
        assertThat(caseSearchResponse.getCases(), hasSize(0));
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByCaseReference() throws IOException {
        final CaseDocument secondCaseDocument = referenceSearchDataHelper.getIndexDocumentAt(1);

        final Map<String, String> parameters = of(PROSECUTION_CASE_REFERENCE, secondCaseDocument.getCaseReference());

        final ProbationDefendantDetailsResponse caseSearchResponse = probationDefendantDetailsSearchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(1L));
        assertThat(caseSearchResponse.getCases(), hasSize(1));
        final ProbationDefendantDetailsSummary firstCase = caseSearchResponse.getCases().get(0);
        assertCase(firstCase, secondCaseDocument);
    }

    @Test
    public void shouldFindNothingIfNoReferenceIsProvided() throws IOException {
        final Map<String, String> parameters = of(PROSECUTION_CASE_REFERENCE, "");

        final ProbationDefendantDetailsResponse caseSearchResponse = probationDefendantDetailsSearchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);

        assertThat(caseSearchResponse.getTotalResults(), is(0L));
        assertThat(caseSearchResponse.getCases(), hasSize(0));
    }

    @Test
    public void shouldFindNothingIfCaseStatusIsInActive() throws IOException {
        final CaseDocument inActiveCaseDocument = referenceSearchDataHelper.getIndexDocumentAt(3);

        final Map<String, String> parameters = of(PROSECUTION_CASE_REFERENCE, inActiveCaseDocument.getCaseReference());

        final ProbationDefendantDetailsResponse caseSearchResponse = probationDefendantDetailsSearchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(1L));
        assertThat(caseSearchResponse.getCases(), hasSize(0));
    }

}
