package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.util.Collections.singletonList;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ReferenceSearchDataIngester.TFL_CASE_REFERENCE;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCase.assertCase;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.ReferenceSearchDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CaseReferenceSearchIT {

    private final SearchApiClient searchApiClient = getInstance();
    private static final ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static final ReferenceSearchDataIngester referenceSearchDataHelper = new ReferenceSearchDataIngester();
    private static final String CASE_REFERENCE = "caseReference";
    private static final String SORT_ASC = "asc";
    private static final String SORT_DESC = "desc";
    private static final String SORT_BY_APPOINTMENT_DATE = "sortByAppointmentDate";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        referenceSearchDataHelper.loadCases();
    }

    @Test
    public void shouldFindNothingIfPartialReferenceIsProvided() throws IOException {
        final Map<String, String> parameters = of(CASE_REFERENCE, TFL_CASE_REFERENCE.substring(0, TFL_CASE_REFERENCE.length() - 2));

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(0L));
        assertThat(caseSearchResponse.getCases(), hasSize(0));
    }

    @Test
    public void shouldFindNothingIfIncorrectReferenceIsProvided() throws IOException {

        final Map<String, String> parameters = of(CASE_REFERENCE, TFL_CASE_REFERENCE + "INCORRECT");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(0L));
        assertThat(caseSearchResponse.getCases(), hasSize(0));
    }


    @Test
    public void shouldReturnSearchResponseWhenSearchingByCaseReference() throws IOException {

        final CaseDocument secondCaseDocument = referenceSearchDataHelper.getIndexDocumentAt(1);

        final Map<String, String> parameters = of(CASE_REFERENCE, secondCaseDocument.getCaseReference());

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(1L));
        assertThat(caseSearchResponse.getCases(), hasSize(1));
        final Case firstCase = caseSearchResponse.getCases().get(0);
        assertCase(firstCase, secondCaseDocument);
    }

    @Test
    public void shouldReturnSearchResponseForCaseInsensitiveApplicationReference() throws IOException {

        final CaseDocument secondCaseDocument = referenceSearchDataHelper.getIndexDocumentAt(1);

        final Map<String, String> parameters = of(CASE_REFERENCE, secondCaseDocument.getCaseReference().toLowerCase());

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(1L));
        assertThat(caseSearchResponse.getCases(), hasSize(1));
        final Case firstCase = caseSearchResponse.getCases().get(0);

        assertCase(firstCase, secondCaseDocument);
    }


    @Test
    public void shouldReturnSearchResponseForCaseInsensitiveCaseReference() throws IOException {

        final CaseDocument secondCaseDocument = referenceSearchDataHelper.getIndexDocumentAt(1);

        final Map<String, String> parameters = of(CASE_REFERENCE, secondCaseDocument.getApplications().get(0).getApplicationReference().toLowerCase());

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(1L));
        assertThat(caseSearchResponse.getCases(), hasSize(1));
        final Case firstCase = caseSearchResponse.getCases().get(0);

        assertCase(firstCase, secondCaseDocument);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByApplicationReference() throws IOException {

        final CaseDocument firstCaseDocument = referenceSearchDataHelper.getIndexDocumentAt(0);
        final String applicationReference = firstCaseDocument.getApplications().get(0).getApplicationReference();

        final Map<String, String> parameters = of(CASE_REFERENCE, applicationReference);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertCases(caseSearchResponse, singletonList(firstCaseDocument));
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByStandaloneApplicationReference() throws IOException {

        final CaseDocument standaloneApplicationCaseDocument = referenceSearchDataHelper.getIndexDocumentAt(2);
        final String standaloneApplicationReference = standaloneApplicationCaseDocument.getApplications().get(0).getApplicationReference();
        final Map<String, String> parameters = of(CASE_REFERENCE, standaloneApplicationReference);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(1L));
        assertThat(caseSearchResponse.getCases(), hasSize(1));
        final Case firstCase = caseSearchResponse.getCases().get(0);

        assertCase(firstCase, standaloneApplicationCaseDocument);
    }

    @Test
    public void shouldFindNothingIfNoReferenceIsProvided() throws IOException {

        final Map<String, String> parameters = of(CASE_REFERENCE, "");

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }


    @Test
    public void shouldReturnSearchResponseWhenSearchingByCaseReferenceSortByAppointmentDateAsc() throws IOException {

        final CaseDocument sixthCaseDocument = referenceSearchDataHelper.getIndexDocumentAt(5);
        final CaseDocument seventhCaseDocument = referenceSearchDataHelper.getIndexDocumentAt(6);
        final CaseDocument eighthCaseDocument = referenceSearchDataHelper.getIndexDocumentAt(7);


        final Map<String, String> parameters = of(CASE_REFERENCE, seventhCaseDocument.getCaseReference(), SORT_BY_APPOINTMENT_DATE, SORT_ASC);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(3L));
        assertThat(caseSearchResponse.getCases(), hasSize(3));
        final Case firstCase = caseSearchResponse.getCases().get(0);
        final Case secondCase = caseSearchResponse.getCases().get(1);
        final Case thirdCase = caseSearchResponse.getCases().get(2);
        assertCase(firstCase, seventhCaseDocument);
        assertCase(secondCase, eighthCaseDocument);
        assertCase(thirdCase, sixthCaseDocument);

    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByCaseReferenceSortByAppointmentDateDesc() throws IOException {

        final CaseDocument secondCaseDocument = referenceSearchDataHelper.getIndexDocumentAt(1);

        final Map<String, String> parameters = of(CASE_REFERENCE, secondCaseDocument.getCaseReference(), SORT_BY_APPOINTMENT_DATE, SORT_DESC);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(1L));
        assertThat(caseSearchResponse.getCases(), hasSize(1));
        final Case firstCase = caseSearchResponse.getCases().get(0);
        assertCase(firstCase, secondCaseDocument);
    }

    @Test
    public void shouldReturnSearchResponseWithSourceSystemReferenceWhenCaseReference() throws IOException {

        final CaseDocument secondCaseDocument = referenceSearchDataHelper.getIndexDocumentAt(8);

        final Map<String, String> parameters = of(CASE_REFERENCE, secondCaseDocument.getCaseReference(), SORT_BY_APPOINTMENT_DATE, SORT_DESC);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(1L));
        assertThat(caseSearchResponse.getCases(), hasSize(1));
        final Case firstCase = caseSearchResponse.getCases().get(0);
        assertCase(firstCase, secondCaseDocument);
    }
}
