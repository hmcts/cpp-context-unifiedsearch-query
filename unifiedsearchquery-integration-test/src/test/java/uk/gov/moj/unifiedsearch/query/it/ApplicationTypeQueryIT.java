package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.stream.Collectors.toList;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPEAL_AGAINST_CONVICTION;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPEAL_AGAINST_DECISION_THAT_AN_OFFENCE_HAS_A_TERRORIST_CONNECTION;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPEAL_AGAINST_DECISION_TO_MAKE_A_HOSPITAL_ORDER_OR_GUARDIANSHIP_ORDER_WITHOUT_CONVICTING_THE_DEFENDANT;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPEAL_AGAINST_DECISION_TO_REVOKE_A_COMMUNITY_ORDER_AND_DEAL_WITH_THE_DEFENDANT_ANOTHER_WAY;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPEAL_AGAINST_GRANT_OF_BAIL;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPEAL_AGAINST_REFUSAL_TO_EXTEND_A_CUSTODY_TIME_LIMIT;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPEAL_AGAINST_SENTENCE;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPLICATION_FOR_A_DOMESTIC_VIOLENCE_PROTECTION_ORDER;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPLICATION_TO_AMEND_INDICTMENT_BY_ADDING_OR_REMOVING_A_COURT_OR_COUNTS;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPLICATION_TO_PREFER_INDICTMENT_OUT_OF_TIME;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.caseListForApplicationTypeQuery;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.loadCaseDocuments;
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

public class ApplicationTypeQueryIT {

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
    public void shouldRetrieveCorrectCasesByApplicationType() throws Exception {

        final List<CaseDocument> caseDocuments = caseListForApplicationTypeQuery();

        loadCaseDocuments(caseDocuments);

        validateForApplicationType(APPLICATION_FOR_A_DOMESTIC_VIOLENCE_PROTECTION_ORDER, caseDocuments);
        validateForApplicationType(APPEAL_AGAINST_CONVICTION, caseDocuments);
        validateForApplicationType(APPEAL_AGAINST_SENTENCE, caseDocuments);
        validateForApplicationType(APPEAL_AGAINST_DECISION_TO_MAKE_A_HOSPITAL_ORDER_OR_GUARDIANSHIP_ORDER_WITHOUT_CONVICTING_THE_DEFENDANT, caseDocuments);
        validateForApplicationType(APPEAL_AGAINST_DECISION_TO_REVOKE_A_COMMUNITY_ORDER_AND_DEAL_WITH_THE_DEFENDANT_ANOTHER_WAY, caseDocuments);
        validateForApplicationType(APPEAL_AGAINST_DECISION_THAT_AN_OFFENCE_HAS_A_TERRORIST_CONNECTION, caseDocuments);
        validateForApplicationType(APPEAL_AGAINST_GRANT_OF_BAIL, caseDocuments);
        validateForApplicationType(APPEAL_AGAINST_REFUSAL_TO_EXTEND_A_CUSTODY_TIME_LIMIT, caseDocuments);
        validateForApplicationType(APPLICATION_TO_PREFER_INDICTMENT_OUT_OF_TIME, caseDocuments);
        validateForApplicationType(APPLICATION_TO_AMEND_INDICTMENT_BY_ADDING_OR_REMOVING_A_COURT_OR_COUNTS, caseDocuments);
    }

    @Test
    public void shouldReturnNoCaseForInvalidApplicationType() throws Exception {

        final List<CaseDocument> caseDocuments = caseListForApplicationTypeQuery();

        loadCaseDocuments(caseDocuments);

        final List<Case> retrievedCases = submitSearch("invalid type");

        assertThat(retrievedCases, is(empty()));

    }

    @Test
    public void shouldReturnNoCaseForEmptyApplicationType() throws Exception {

        final List<CaseDocument> caseDocuments = caseListForApplicationTypeQuery();

        loadCaseDocuments(caseDocuments);

        final Map<String, String> parameters = of("applicationType", "");

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }

    private void validateForApplicationType(final String applicationType, final List<CaseDocument> caseDocuments) throws Exception {

        final List<Case> retrievedCases = submitSearch(applicationType);

        assertThat(retrievedCases.size(), is(3));

        validateReturnedCaseDocuments(retrievedCases,
                caseDocuments.stream()
                        .filter(cd -> matchesApplicationType(cd, applicationType))
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

    private boolean matchesApplicationType(final CaseDocument caseDocument, final String applicationType) {
        return applicationType.equals(caseDocument.getApplications().get(0).getApplicationType());
    }

    private List<Case> submitSearch(final String applicationType) throws Exception {

        final Map<String, String> parameters = of("applicationType", applicationType, "pageSize", "30");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();
    }
}
