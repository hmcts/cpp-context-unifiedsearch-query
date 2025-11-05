package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ReferenceSearchDataIngester.TFL_CASE_REFERENCE;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertLaaCase.assertCase;
import static uk.gov.moj.unifiedsearch.query.it.util.LaaSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.CaseSummary;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.ReferenceSearchDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.LaaSearchApiClient;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LaaCaseReferenceSearchIT {

    private final LaaSearchApiClient laaSearchApiClient = getInstance();
    private static final ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static final ReferenceSearchDataIngester referenceSearchDataHelper = new ReferenceSearchDataIngester();
    private static final String PROSECUTION_CASE_REFERENCE = "prosecutionCaseReference";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        referenceSearchDataHelper.loadCases();
    }

    @Test
    public void shouldFindNothingIfPartialReferenceIsProvided() throws IOException {
        final Map<String, String> parameters = of(PROSECUTION_CASE_REFERENCE, TFL_CASE_REFERENCE.substring(0, TFL_CASE_REFERENCE.length() - 2));

        final CaseSearchResponse caseSearchResponse = laaSearchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(0L));
        assertThat(caseSearchResponse.getCases(), hasSize(0));
    }

    @Test
    public void shouldFindNothingIfIncorrectReferenceIsProvided() throws IOException {
        final Map<String, String> parameters = of(PROSECUTION_CASE_REFERENCE, TFL_CASE_REFERENCE + "INCORRECT");

        final CaseSearchResponse caseSearchResponse = laaSearchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(0L));
        assertThat(caseSearchResponse.getCases(), hasSize(0));
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByCaseReference() throws IOException {
        final CaseDocument secondCaseDocument = referenceSearchDataHelper.getIndexDocumentAt(1);

        final Map<String, String> parameters = of(PROSECUTION_CASE_REFERENCE, secondCaseDocument.getCaseReference());

        final CaseSearchResponse caseSearchResponse = laaSearchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(1L));
        assertThat(caseSearchResponse.getCases(), hasSize(1));
        final CaseSummary firstCase = caseSearchResponse.getCases().get(0);
        assertThat(firstCase.getApplicationSummary().size(), is(2));
        assertThat(firstCase.getApplicationSummary().get(0).getSubjectSummary().getSubjectId(), notNullValue());
        assertThat(firstCase.getApplicationSummary().get(0).getSubjectSummary().getFirstName(), is("First_Name"));
        assertCase(firstCase, secondCaseDocument);
    }

    @Test
    public void shouldReturnSearchResponseForCaseInsensitiveCaseReference() throws IOException {
        final CaseDocument secondCaseDocument = referenceSearchDataHelper.getIndexDocumentAt(1);

        final Map<String, String> parameters = of(PROSECUTION_CASE_REFERENCE, secondCaseDocument.getApplications().get(0).getApplicationReference().toLowerCase());

        final CaseSearchResponse caseSearchResponse = laaSearchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(1L));
        assertThat(caseSearchResponse.getCases(), hasSize(1));
        final CaseSummary firstCase = caseSearchResponse.getCases().get(0);

        assertCase(firstCase, secondCaseDocument);
    }

    @Test
    public void shouldFindNothingIfNoReferenceIsProvided() throws IOException {
        final Map<String, String> parameters = of(PROSECUTION_CASE_REFERENCE, "");

        final CaseSearchResponse caseSearchResponse = laaSearchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);

        assertThat(caseSearchResponse.getTotalResults(), is(0L));
        assertThat(caseSearchResponse.getCases(), is(nullValue()));

    }

    @Test
    public void shouldFindNothingIfCaseReferenceProvidedHasNoOffences() throws IOException {
        final CaseDocument caseDocumentWithoutOffences = referenceSearchDataHelper.getIndexDocumentAt(3);

        final Map<String, String> parameters = of(PROSECUTION_CASE_REFERENCE, caseDocumentWithoutOffences.getCaseReference());

        final CaseSearchResponse caseSearchResponse = laaSearchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(0L));
        assertThat(caseSearchResponse.getCases(), hasSize(0));
    }

    @Test
    public void shouldFindNothingIfCaseReferenceProvidedHasEmptyOffences() throws IOException {
        final CaseDocument caseDocumentWithEmptyOffences = referenceSearchDataHelper.getIndexDocumentAt(4);

        final Map<String, String> parameters = of(PROSECUTION_CASE_REFERENCE, caseDocumentWithEmptyOffences.getCaseReference());

        final CaseSearchResponse caseSearchResponse = laaSearchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(0L));
        assertThat(caseSearchResponse.getCases(), hasSize(0));
    }

}
