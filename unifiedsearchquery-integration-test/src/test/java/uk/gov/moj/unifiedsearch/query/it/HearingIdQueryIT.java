package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_ID;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.HearingDocumentMother.HEARING_ESTIMATED_DURATION;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.HearingIdIngester.HEARING_ID_1;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.HearingIdIngester.HEARING_ID_2;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.HearingIdIngester.HEARING_ID_3;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.HearingIdIngester.caseListForHearingIdQuery;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.HearingTypeIngester.loadCaseDocuments;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertLaaCase.assertCase;
import static uk.gov.moj.unifiedsearch.query.it.util.LaaSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.CaseSummary;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.util.LaaSearchApiClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HearingIdQueryIT {

    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private LaaSearchApiClient laaSearchApiClient = getInstance();

    private static final String PAGE_SIZE_PARAMETER = "pageSize";

    @BeforeAll
    public static void setupStubs() throws Exception {
        initializeStubbing();
    }

    @BeforeEach
    public void setup() throws Exception {
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
    }


    @Test
    public void shouldQueryByHearingId() throws Exception {
        final List<CaseDocument> caseDocuments = caseListForHearingIdQuery();
        loadCaseDocuments(caseDocuments);

        validateForHearingId(HEARING_ID_1, caseDocuments);
        validateForHearingId(HEARING_ID_2, caseDocuments);
        validateForHearingId(HEARING_ID_3, caseDocuments);
    }

    @Test
    public void shouldQueryByHearingIdAndPopulateEstimatedDuration() throws Exception {
        final List<CaseDocument> caseDocuments = caseListForHearingIdQuery();
        loadCaseDocuments(caseDocuments);

        final List<CaseSummary> retrievedCases = submitSearch(HEARING_ID_1);

        validateForHearingIdAndEstimatedDuration(HEARING_ID_1, caseDocuments);
        validateForHearingIdAndEstimatedDuration(HEARING_ID_2, caseDocuments);
        validateForHearingIdAndEstimatedDuration(HEARING_ID_3, caseDocuments);
    }


    @Test
    public void shouldReturnNoCaseForInvalidHearingId() throws Exception {
        final List<CaseDocument> caseDocuments = caseListForHearingIdQuery();
        loadCaseDocuments(caseDocuments);

        final List<CaseSummary> retrievedCases = submitSearch(randomUUID().toString());

        assertThat(retrievedCases, is(empty()));
    }

    @Test
    public void shouldReturnBadRequestWhenHearingIdIsEmpty() throws Exception {
        final List<CaseDocument> caseDocuments = caseListForHearingIdQuery();
        loadCaseDocuments(caseDocuments);

        final Map<String, String> parameters = of(HEARING_ID, EMPTY);

        laaSearchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }

    private void validateForHearingId(final String hearingId, final List<CaseDocument> caseDocuments) throws Exception {
        final List<CaseSummary> retrievedCases = submitSearch(hearingId);

        assertThat(retrievedCases.size(), is(1));

        validateReturnedCaseDocuments(retrievedCases,
                caseDocuments.stream()
                        .filter(caseDocument -> matchesHearingId(caseDocument, hearingId))
                        .collect(toList()));
    }

    private void validateForHearingIdAndEstimatedDuration(final String hearingId, final List<CaseDocument> caseDocuments) throws Exception {
        final List<CaseSummary> retrievedCases = submitSearch(hearingId);

        assertThat(retrievedCases.size(), is(1));

        validateReturnedCaseDocuments(retrievedCases,
                caseDocuments.stream()
                        .filter(caseDocument -> matchesHearingId(caseDocument, hearingId))
                        .filter(caseDocument -> caseDocument.getHearings().get(0).getEstimatedDuration().equals(HEARING_ESTIMATED_DURATION))
                        .collect(toList()));
    }

    private void validateReturnedCaseDocuments(final List<CaseSummary> cases, final List<CaseDocument> sourceDocuments) {
        cases.forEach(caseIn -> {
            Optional<CaseDocument> caseDocument = caseDocumentFromCaseId(caseIn.getProsecutionCaseId(), sourceDocuments);
            assertCase(caseIn, caseDocument.orElseThrow(() -> new AssertionError("No CaseDocument matches provided caseId !")));
        });
    }

    private Optional<CaseDocument> caseDocumentFromCaseId(final UUID caseId, final List<CaseDocument> sourceDocuments) {
        final String caseIdString = caseId.toString();

        return sourceDocuments.stream()
                .filter(caseDocument -> caseIdString.equals(caseDocument.getCaseId()))
                .findFirst();
    }

    private boolean matchesHearingId(final CaseDocument caseDocument, final String hearingId) {
        return hearingId.equals(caseDocument.getHearings().get(0).getHearingId());
    }

    private boolean matchesHearingEstimatedDuration(final CaseDocument caseDocument, final String estimatedDuration) {
        return estimatedDuration.equals(caseDocument.getHearings().get(0).getEstimatedDuration());
    }

    private List<CaseSummary> submitSearch(final String hearingId) throws Exception {
        final Map<String, String> parameters = of(HEARING_ID, hearingId, PAGE_SIZE_PARAMETER, "30");

        final CaseSearchResponse caseSearchResponse = laaSearchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();
    }

}
