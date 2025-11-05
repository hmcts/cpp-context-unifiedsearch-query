package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.HearingTypeIngester.loadCaseDocuments;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.IsBoxHearingTypeIngester.caseListForIsBoxHearingQuery;
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

public class IsBoxHearingQueryIT {

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
    public void shouldQueryByIsBoxHearingTrue() throws Exception {

        final List<CaseDocument> caseDocuments = caseListForIsBoxHearingQuery();
        loadCaseDocuments(caseDocuments);

        validateForIsBoxHearing(true, caseDocuments, 3);
    }

    @Test
    public void shouldQueryByIsBoxHearingFalse() throws Exception {

        final List<CaseDocument> caseDocuments = caseListForIsBoxHearingQuery();
        loadCaseDocuments(caseDocuments);

        validateForIsBoxHearing(false, caseDocuments, 6);
    }

    private void validateForIsBoxHearing(final boolean isBoxHearing, final List<CaseDocument> caseDocuments, final int expectedCount) throws Exception {

        final List<Case> retrievedCases = submitSearch(isBoxHearing);

        assertThat(retrievedCases.size(), is(lessThanOrEqualTo(expectedCount)));

        validateReturnedCaseDocuments(retrievedCases,
                caseDocuments.stream()
                        .filter(cd -> matchesIsBoxHearing(cd, isBoxHearing))
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

    private boolean matchesIsBoxHearing(final CaseDocument caseDocument, final boolean isBoxHearing) {
        return isBoxHearing == caseDocument.getHearings().get(0).isIsBoxHearing();
    }

    private List<Case> submitSearch(final boolean isBoxHearing) throws Exception {

        final Map<String, String> parameters = of("boxWorkHearing", valueOf(isBoxHearing), "pageSize", "30");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();

    }
}
