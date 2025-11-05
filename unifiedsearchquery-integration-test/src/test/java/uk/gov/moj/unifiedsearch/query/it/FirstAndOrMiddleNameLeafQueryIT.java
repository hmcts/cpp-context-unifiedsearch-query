package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.NAME_ALIAS;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.caseListForFirstAndMiddleNameQueryWithAlias;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.caseListForFirstNameQueryWithAlias;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.caseListForMiddleNameQueryWithAlias;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.caseListForNameQuery;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.loadCaseDocuments;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Party;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class FirstAndOrMiddleNameLeafQueryIT {


    private static final String PARTY_FIRST_AND_OR_MIDDLE_NAME = "partyFirstAndOrMiddleName";
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
    public void shouldReturnNameHitsFromPartiesWhenSearchingForFirstNameJohnOnly() throws Exception {

        final int testDocumentCount = 8;

        final List<CaseDocument> caseList = caseListForNameQuery();
        loadCaseDocuments(caseList);

        final List<Case> caseListResults = submitSearch("John");

        assertThat(caseListResults.size(), is(testDocumentCount));
    }

    @Test
    public void shouldNotReturnNameHitsFromPartiesWhenSearchingForNameThatIsOnlyInMiddlename() throws Exception {

        final List<CaseDocument> caseList = caseListForNameQuery();
        loadCaseDocuments(caseList);

        final List<Case> caseListResults = submitSearch("Allan");

        assertThat(caseListResults.size(), is(0));
    }

    @Test
    public void shouldReturnNameHitsFromPartiesWhenSearchingForFirstOrMiddleName() throws Exception {

        final int testDocumentCount = 8;

        final List<CaseDocument> caseList = caseListForNameQuery();
        loadCaseDocuments(caseList);

        final List<Case> caseListResults = submitSearch("John Allen");

        assertThat(caseListResults.size(), is(testDocumentCount));

        for (int index = 0; index < testDocumentCount; index++) {
            assertSamePartyNames(caseListResults.get(index), caseList.get(index));
        }

    }

    @Test
    public void shouldReturnNameHitsFromPartiesWhenSearchingForFirstOrMiddleNameIgnoringCase() throws Exception {

        final int testDocumentCount = 8;

        final List<CaseDocument> caseList = caseListForNameQuery();
        loadCaseDocuments(caseList);

        final List<Case> caseListResults = submitSearch("john allen");

        assertThat(caseListResults.size(), is(testDocumentCount));

        for (int index = 0; index < testDocumentCount; index++) {
            assertSamePartyNames(caseListResults.get(index), caseList.get(index));
        }

    }

    @Test
    public void shouldReturnCaseWhenAliasForFirstNameIsSearched() throws IOException {
        final List<CaseDocument> expectedPartiesWithAlias = caseListForFirstNameQueryWithAlias();
        loadCaseDocuments(expectedPartiesWithAlias);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_FIRST_AND_OR_MIDDLE_NAME, NAME_ALIAS));

        assertCases(caseSearchResponse, expectedPartiesWithAlias);
    }

    @Test
    public void shouldNotReturnCaseWhenAliasForMiddleNameIsSearched() throws Exception {
        final List<CaseDocument> expectedPartiesWithAlias = caseListForMiddleNameQueryWithAlias();
        loadCaseDocuments(expectedPartiesWithAlias);

        final List<Case> caseListResults = submitSearch("Allan");

        assertThat(caseListResults.size(), is(0));
    }

    @Test
    public void shouldReturnCaseWhenAliasForFirstAndMiddleNameIsSearched() throws IOException {
        final List<CaseDocument> expectedPartiesWithAlias = caseListForFirstAndMiddleNameQueryWithAlias();
        loadCaseDocuments(expectedPartiesWithAlias);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_FIRST_AND_OR_MIDDLE_NAME, NAME_ALIAS));

        assertCases(caseSearchResponse, expectedPartiesWithAlias);
    }


    @Test
    public void shouldReturnNothingWhenEmptyAliasIsSearched() throws IOException {
        final List<CaseDocument> partiesWithAlias = caseListForFirstNameQueryWithAlias();
        loadCaseDocuments(partiesWithAlias);

        final Map<String, String> parameters = of(PARTY_FIRST_AND_OR_MIDDLE_NAME, "");

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }


    private void assertSamePartyNames(final Case retrievedCase, final CaseDocument expected) {

        final PartyDocument expectedParty = expected.getParties().get(0);
        final Party party = retrievedCase.getParties().get(0);

        assertThat(party.getFirstName(), is(expectedParty.getFirstName()));
        assertThat(party.getMiddleName(), is(expectedParty.getMiddleName()));
        assertThat(party.getLastName(), is(expectedParty.getLastName()));
    }


    private List<Case> submitSearch(final String partyName) throws Exception {

        final Map<String, String> parameters = of(PARTY_FIRST_AND_OR_MIDDLE_NAME, partyName, "pageSize", "30");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();
    }
}
