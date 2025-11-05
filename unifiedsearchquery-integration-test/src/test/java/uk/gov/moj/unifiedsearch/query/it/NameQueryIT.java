package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.NAME_ALIAS;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.caseListForNameQuery;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.caseListForOrganisationNameQuery;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.caseListForPartyNameQueryWithAlias;
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


public class NameQueryIT {


    private static final String PARTY_NAME = "partyName";
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
    public void shouldReturnNameHitsFromParties() throws Exception {

        final int testDocumentCount = 12;

        final List<CaseDocument> caseList = caseListForNameQuery();
        loadCaseDocuments(caseList);

        final List<Case> caseListResults = submitSearch("John Allen Smith");

        assertThat(caseListResults.size(), is(testDocumentCount));

        for (int index = 0; index < testDocumentCount; index++) {
            assertSamePartyNames(caseListResults.get(index), caseList.get(index));
        }

    }

    @Test
    public void shouldReturnNameHitsFromPartiesIgnoringCase() throws Exception {

        final int testDocumentCount = 12;

        final List<CaseDocument> caseList = caseListForNameQuery();
        loadCaseDocuments(caseList);

        final List<Case> caseListResults = submitSearch("john allen smith");

        assertThat(caseListResults.size(), is(testDocumentCount));

        for (int index = 0; index < testDocumentCount; index++) {
            assertSamePartyNames(caseListResults.get(index), caseList.get(index));
        }

    }


    @Test
    public void shouldReturnOrganisationHitsFromPartiesMarkAndSpencer() throws Exception {

        final List<CaseDocument> caseList = caseListForOrganisationNameQuery();
        loadCaseDocuments(caseList);

        final List<Case> caseListResults = submitSearch("Mark and spencer");

        assertThat(caseListResults.size(), is(5));

        assertPartyOrganisationName(caseListResults.get(0), asList("marks and spencer"));
        assertPartyOrganisationName(caseListResults.get(1), asList("mark and spencer"));

        final List<String> expectedOrganisationNamesInAnyOrderList = asList("spencer Boyd", "mark Boyd", "mark");
        assertPartyOrganisationName(caseListResults.get(2), expectedOrganisationNamesInAnyOrderList);
        assertPartyOrganisationName(caseListResults.get(3), expectedOrganisationNamesInAnyOrderList);
        assertPartyOrganisationName(caseListResults.get(4), expectedOrganisationNamesInAnyOrderList);

    }

    @Test
    public void shouldReturnOrganisationHitsFromPartiesMarksAndSpencers() throws Exception {

        final List<CaseDocument> caseList = caseListForOrganisationNameQuery();
        loadCaseDocuments(caseList);

        final List<Case> caseListResults = submitSearch("Marks and spencers");

        assertThat(caseListResults.size(), is(5));

        assertPartyOrganisationName(caseListResults.get(0), asList("marks and spencer"));
        assertPartyOrganisationName(caseListResults.get(1), asList("mark and spencer"));

        final List<String> expectedOrganisationNamesInAnyOrderList = asList("spencer Boyd", "mark Boyd", "mark");
        assertPartyOrganisationName(caseListResults.get(2), expectedOrganisationNamesInAnyOrderList);
        assertPartyOrganisationName(caseListResults.get(3), expectedOrganisationNamesInAnyOrderList);
        assertPartyOrganisationName(caseListResults.get(4), expectedOrganisationNamesInAnyOrderList);

    }

    @Test
    public void shouldReturnCaseWhenAliasIsSearched() throws IOException {
        final List<CaseDocument> expectedPartiesWithAlias = caseListForPartyNameQueryWithAlias();
        loadCaseDocuments(expectedPartiesWithAlias);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_NAME, NAME_ALIAS));

        assertCases(caseSearchResponse, expectedPartiesWithAlias);
    }

    @Test
    public void shouldReturnCaseWhenAliasIsSearchedAndAliasFlagIsNotProvided() throws IOException {
        final List<CaseDocument> partiesWithAlias = caseListForPartyNameQueryWithAlias();
        loadCaseDocuments(partiesWithAlias);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_NAME, NAME_ALIAS));

        assertCases(caseSearchResponse, partiesWithAlias);
    }

    @Test
    public void shouldReturnNothingWhenEmptyAliasIsSearched() throws IOException {
        final List<CaseDocument> partiesWithAlias = caseListForPartyNameQueryWithAlias();
        loadCaseDocuments(partiesWithAlias);

        final Map<String, String> parameters = of(PARTY_NAME, "");

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }


    private void assertPartyOrganisationName(final Case retrievedCase, final List<String> expectedIn) {

        final Party party = retrievedCase.getParties().get(0);

        assertThat(expectedIn, hasItem(party.getOrganisationName()));

    }


    private void assertSamePartyNames(final Case retrievedCase, final CaseDocument expected) {

        final PartyDocument expectedParty = expected.getParties().get(0);
        final Party party = retrievedCase.getParties().get(0);

        assertThat(party.getFirstName(), is(expectedParty.getFirstName()));
        assertThat(party.getMiddleName(), is(expectedParty.getMiddleName()));
        assertThat(party.getLastName(), is(expectedParty.getLastName()));
    }


    private List<Case> submitSearch(final String partyName) throws Exception {

        final Map<String, String> parameters = of(PARTY_NAME, partyName, "pageSize", "30");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();
    }
}
