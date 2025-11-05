package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.lang.String.join;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.APPLICANT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.DEFENDANT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.RESPONDENT;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.PartyTypeSearchDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PartyTypeSearchIT {

    private static final String INCORRECT_PARTY_TYPE = "some-incorrect-party-type";
    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static PartyTypeSearchDataIngester partyTypeSearchDataIngester = new PartyTypeSearchDataIngester();
    private static final String PARTY_TYPES = "partyTypes";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        partyTypeSearchDataIngester.loadCases();
    }

    @Test
    public void shouldFindNothingIfIncorrectReferenceIsProvided() throws IOException {

        final Map<String, String> parameters = of(PARTY_TYPES, INCORRECT_PARTY_TYPE);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(0L));
        assertThat(caseSearchResponse.getCases(), hasSize(0));
    }


    @Test
    public void shouldReturnSearchResponseWhenSearchingByRespondent() throws IOException {

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_TYPES, RESPONDENT.name()));

        final List<CaseDocument> expectedCaseDocuments = partyTypeSearchDataIngester.getCasesWithRespondent();

        assertCases(caseSearchResponse, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByRespondentAndInvalidPartyType() throws IOException {

        final String partyTypes = join(",", RESPONDENT.name(), INCORRECT_PARTY_TYPE);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_TYPES, partyTypes));

        final List<CaseDocument> expectedCaseDocuments = partyTypeSearchDataIngester.getCasesWithRespondent();

        assertCases(caseSearchResponse, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByRespondentCaseInsensitive() throws IOException {
        final String partyTypes = "ReSpoNDent";

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_TYPES, partyTypes));

        final List<CaseDocument> expectedCaseDocuments = partyTypeSearchDataIngester.getCasesWithRespondent();

        assertCases(caseSearchResponse, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByApplicant() throws IOException {

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_TYPES, APPLICANT.name()));

        final List<CaseDocument> expectedCaseDocuments = partyTypeSearchDataIngester.getCasesWithApplicant();

        assertCases(caseSearchResponse, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByDefendant() throws IOException {

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_TYPES, DEFENDANT.name()));

        final List<CaseDocument> expectedCaseDocuments = partyTypeSearchDataIngester.getCasesWithDefendant();

        assertCases(caseSearchResponse, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByApplicantDefendantAndRespondent() throws IOException {

        final String partyTypesParameter = join(",", APPLICANT.name(), DEFENDANT.name(), RESPONDENT.name());

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_TYPES, partyTypesParameter));

        final List<CaseDocument> expectedCaseDocuments = partyTypeSearchDataIngester.getAllCases();

        assertCases(caseSearchResponse, expectedCaseDocuments);
    }


    @Test
    public void shouldReturnSearchResponseWhenSearchingByApplicantDefendantAndRespondentWithSpace() throws IOException {

        final String partyTypesParameter = join("   ,  ", APPLICANT.name(), DEFENDANT.name(), RESPONDENT.name());

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_TYPES, partyTypesParameter));

        final List<CaseDocument> expectedCaseDocuments = partyTypeSearchDataIngester.getAllCases();

        assertCases(caseSearchResponse, expectedCaseDocuments);
    }


    @Test
    public void shouldReturnNothingWhenApplicantSearchParamIsEmpty() throws IOException {

        final Map<String, String> parameters = of(PARTY_TYPES, "");

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }
}
