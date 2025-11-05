package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.HearingTypeIngester.HEARING_TYPE_ID_1;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.HearingTypeIngester.HEARING_TYPE_ID_2;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.HearingTypeIngester.HEARING_TYPE_ID_3;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.HearingTypeIngester.caseListForHearingTypeQuery;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.HearingTypeIngester.loadCaseDocuments;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCase.assertCase;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HearingTypeQueryIT {

    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();


    private SearchApiClient searchApiClient = getInstance();

    @BeforeAll
    public static void setupStubs() throws Exception {
        initializeStubbing();
    }

    @BeforeEach
    public void setup() throws Exception {
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
    }


    @Test
    public void shouldQueryByHearingTypeId() throws Exception {

        final List<CaseDocument> caseDocuments = caseListForHearingTypeQuery();
        loadCaseDocuments(caseDocuments);

        validateForHearingType(HEARING_TYPE_ID_1, caseDocuments);
        validateForHearingType(HEARING_TYPE_ID_2, caseDocuments);
        validateForHearingType(HEARING_TYPE_ID_3, caseDocuments);

    }


    @Test
    public void shouldReturnNoCaseForInvalidHearingType() throws Exception {

        final List<CaseDocument> caseDocuments = caseListForHearingTypeQuery();
        loadCaseDocuments(caseDocuments);

        final List<Case> retrievedCases = submitSearch(randomUUID().toString());

        assertThat(retrievedCases, is(empty()));

    }

    @Test
    public void shouldNotReturnResultsWhenHearingTypeIsEmpty() throws Exception {

        final List<CaseDocument> caseDocuments = caseListForHearingTypeQuery();
        loadCaseDocuments(caseDocuments);

        final Map<String, String> parameters = of("hearingTypeId", "");

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }

    @Test
    public void shouldReturnBadRequestWhenPageSizeIsNotValidNumber() throws Exception {

        final List<CaseDocument> caseDocuments = caseListForHearingTypeQuery();
        loadCaseDocuments(caseDocuments);

        final Map<String, String> parameters = of("hearingTypeId", randomUUID().toString(), "pageSize", "-1");

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }

    private void validateForHearingType(final String hearingTypeId, final List<CaseDocument> caseDocuments) throws Exception {

        final List<Case> retrievedCases = submitSearch(hearingTypeId);

        assertThat(retrievedCases.size(), is(3));

        validateReturnedCaseDocuments(retrievedCases,
                caseDocuments.stream()
                        .filter(cd -> matchesHearingType(cd, hearingTypeId))
                        .collect(toList()));
    }

    private void validateReturnedCaseDocuments(final List<Case> cases, final List<CaseDocument> sourceDocuments) {

        cases.forEach(caseIn -> {
            Optional<CaseDocument> caseDocument = caseDocumentFromCaseId(caseIn.getCaseId(), sourceDocuments);
            assertCase(caseIn, caseDocument.orElseThrow(() -> new AssertionError("No CaseDocument matches provided caseId !")));
        });

    }


    private Optional<CaseDocument> caseDocumentFromCaseId(final UUID caseId, final List<CaseDocument> sourceDocuments) {
        final String caseIdString = caseId.toString();
        return sourceDocuments.stream()
                .filter(cd -> caseIdString.equals(cd.getCaseId()))
                .findFirst();
    }

    private boolean matchesHearingType(final CaseDocument caseDocument, final String hearingType) {
        return hearingType.equals(caseDocument.getHearings().get(0).getHearingTypeId());
    }


    private List<Case> submitSearch(final String hearingType) throws Exception {

        final Map<String, String> parameters = of("hearingTypeId", hearingType, "pageSize", "30");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();

    }


}
