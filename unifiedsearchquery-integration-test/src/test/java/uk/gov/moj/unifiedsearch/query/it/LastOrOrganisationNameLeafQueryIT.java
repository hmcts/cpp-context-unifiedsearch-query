package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.NAME_ALIAS;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.caseListForFirstNameQueryWithAlias;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.caseListForLastNameQuery;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.caseListForLastNameQueryWithAlias;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.caseListForLastOrOrganisationNameQueryWithAlias;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.caseListForOrganisationNameQuery;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.caseListForOrganisationNameQueryWithAlias;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.loadCaseDocuments;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Party;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class LastOrOrganisationNameLeafQueryIT {

    private static final String PARTY_LAST_NAME_OR_ORGANISATION_NAME = "partyLastNameOrOrganisationName";
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
    public void shouldReturnOrganisationHitsFromPartiesMarkAndSpencer() throws Exception {

        final List<CaseDocument> caseList = caseListForOrganisationNameQuery();
        loadCaseDocuments(caseList);

        final List<CaseDocument> lastNamecaseList = caseListForLastNameQuery();
        loadCaseDocuments(lastNamecaseList);

        final List<Case> caseListResults = submitSearch("Marks and spencer");

        assertThat(caseListResults.size(), is(15));

        final Party party0 = caseListResults.get(0).getParties().get(0);
        assertThat(party0.getLastName(), is("Marks And Spencers"));

        final Party party1 = caseListResults.get(1).getParties().get(0);
        assertThat(party1.getLastName(), is("Mark And Spencer"));

        final Party party2 = caseListResults.get(2).getParties().get(0);
        assertThat(party2.getOrganisationName(), is("marks and spencer"));

        final Party party3 = caseListResults.get(3).getParties().get(0);
        assertThat(party3.getLastName(), is("Marks"));

        final Party party4 = caseListResults.get(4).getParties().get(0);
        assertThat(party4.getLastName(), is("Marks"));

        final Party party5 = caseListResults.get(5).getParties().get(0);
        assertThat(party5.getOrganisationName(), is("mark and spencer"));

        final Party party6 = caseListResults.get(6).getParties().get(0);
        assertThat(party6.getLastName(), is("Spencer"));

        final Party party7 = caseListResults.get(7).getParties().get(0);
        assertThat(party7.getLastName(), is("Spencer"));

        final Party party8 = caseListResults.get(8).getParties().get(0);
        assertThat(party8.getOrganisationName(), is("spencer Boyd"));

        final Party party9 = caseListResults.get(9).getParties().get(0);
        assertThat(party9.getLastName(), is("Spencers"));

        final Party party10 = caseListResults.get(10).getParties().get(0);
        assertThat(party10.getLastName(), is("Spencers"));
    }

    @Test
    public void shouldReturnOrganisationHitsFromPartiesMarksAndSpencers() throws Exception {

        final List<CaseDocument> caseList = caseListForOrganisationNameQuery();
        loadCaseDocuments(caseList);

        final List<CaseDocument> lastNamecaseList = caseListForLastNameQuery();
        loadCaseDocuments(lastNamecaseList);

        final List<Case> caseListResults = submitSearch("Marks and spencers");

        assertThat(caseListResults.size(), is(15));

        final Party party0 = caseListResults.get(0).getParties().get(0);
        assertThat(party0.getLastName(), is("Marks And Spencers"));

        final Party party1 = caseListResults.get(1).getParties().get(0);
        assertThat(party1.getLastName(), is("Mark And Spencer"));

        final Party party2 = caseListResults.get(2).getParties().get(0);
        assertThat(party2.getLastName(), is("Spencers"));

        final Party party3 = caseListResults.get(3).getParties().get(0);
        assertThat(party3.getLastName(), is("Marks"));

        final Party party4 = caseListResults.get(4).getParties().get(0);
        assertThat(party4.getLastName(), is("Spencers"));

        final Party party5 = caseListResults.get(5).getParties().get(0);
        assertThat(party5.getLastName(), is("Marks"));

        final Party party6 = caseListResults.get(6).getParties().get(0);
        assertThat(party6.getOrganisationName(), is("marks and spencer"));

        final Party party7 = caseListResults.get(7).getParties().get(0);
        assertThat(party7.getOrganisationName(), is("mark and spencer"));

    }

    @Test
    public void shouldReturnLastNameHitsFromPartiesMarkAndSpencer() throws Exception {

        final List<CaseDocument> caseList = caseListForOrganisationNameQuery();
        loadCaseDocuments(caseList);

        final List<CaseDocument> lastNamecaseList = caseListForLastNameQuery();
        loadCaseDocuments(lastNamecaseList);


        final List<Case> caseListResults = submitSearch("spencer");
        assertThat(caseListResults.size(), is(9));

        final Party party0 = caseListResults.get(0).getParties().get(0);
        assertThat(party0.getLastName(), is("Spencer"));

        final Party party1 = caseListResults.get(1).getParties().get(0);
        assertThat(party1.getLastName(), is("Spencer"));

        final Party party2 = caseListResults.get(2).getParties().get(0);
        assertThat(party2.getLastName(), is("Mark And Spencer"));

        final Party party3 = caseListResults.get(3).getParties().get(0);
        assertThat(party3.getOrganisationName(), is("spencer Boyd"));

        final Party party4 = caseListResults.get(4).getParties().get(0);
        assertThat(party4.getOrganisationName(), is("marks and spencer"));

        final Party party5 = caseListResults.get(5).getParties().get(0);
        assertThat(party5.getOrganisationName(), is("mark and spencer"));

        final Party party6 = caseListResults.get(6).getParties().get(0);
        assertThat(party6.getLastName(), is("Spencers"));

        final Party party7 = caseListResults.get(7).getParties().get(0);
        assertThat(party7.getLastName(), is("Spencers"));

        final Party party8 = caseListResults.get(8).getParties().get(0);
        assertThat(party8.getLastName(), is("Marks And Spencers"));
    }


    @Test
    public void shouldReturnOrganisationAndLastNameHitsFromPartiesMarkAndSpencerIgnoringCase() throws Exception {
        final List<CaseDocument> caseList = caseListForOrganisationNameQuery();
        loadCaseDocuments(caseList);

        final List<CaseDocument> lastNamecaseList = caseListForLastNameQuery();
        loadCaseDocuments(lastNamecaseList);

        final List<Case> caseListResults = submitSearch("marks And Spencers");

        assertThat(caseListResults.size(), is(15));

        final Party party0 = caseListResults.get(0).getParties().get(0);
        assertThat(party0.getLastName(), is("Marks And Spencers"));

        final Party party1 = caseListResults.get(1).getParties().get(0);
        assertThat(party1.getLastName(), is("Mark And Spencer"));

        final Party party2 = caseListResults.get(2).getParties().get(0);
        assertThat(party2.getLastName(), is("Spencers"));

        final Party party3 = caseListResults.get(3).getParties().get(0);
        assertThat(party3.getLastName(), is("Marks"));

        final Party party4 = caseListResults.get(4).getParties().get(0);
        assertThat(party4.getLastName(), is("Spencers"));

        final Party party5 = caseListResults.get(5).getParties().get(0);
        assertThat(party5.getLastName(), is("Marks"));

        final Party party6 = caseListResults.get(6).getParties().get(0);
        assertThat(party6.getOrganisationName(), is("marks and spencer"));

        final Party party7 = caseListResults.get(7).getParties().get(0);
        assertThat(party7.getOrganisationName(), is("mark and spencer"));

    }

    @Test
    public void shouldReturnCaseWhenAliasForLastNameIsSearched() throws IOException {
        final List<CaseDocument> expectedPartiesWithAlias = caseListForLastNameQueryWithAlias();
        loadCaseDocuments(expectedPartiesWithAlias);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_LAST_NAME_OR_ORGANISATION_NAME, NAME_ALIAS));

        assertCases(caseSearchResponse, expectedPartiesWithAlias);
    }

    @Test
    public void shouldReturnCaseWhenAliasForOrganisationNameIsSearched() throws IOException {
        final List<CaseDocument> expectedPartiesWithAlias = caseListForOrganisationNameQueryWithAlias();
        loadCaseDocuments(expectedPartiesWithAlias);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_LAST_NAME_OR_ORGANISATION_NAME, NAME_ALIAS));

        assertCases(caseSearchResponse, expectedPartiesWithAlias);
    }

    @Test
    public void shouldReturnCaseWhenAliasForLastNameAndOrganisationNameIsSearched() throws IOException {
        final List<CaseDocument> expectedPartiesWithAlias = caseListForLastOrOrganisationNameQueryWithAlias();
        loadCaseDocuments(expectedPartiesWithAlias);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_LAST_NAME_OR_ORGANISATION_NAME, NAME_ALIAS));

        assertCases(caseSearchResponse, expectedPartiesWithAlias);
    }


    @Test
    public void shouldReturnNothingWhenEmptyAliasIsSearched() throws IOException {
        final List<CaseDocument> partiesWithAlias = caseListForFirstNameQueryWithAlias();
        loadCaseDocuments(partiesWithAlias);

        final Map<String, String> parameters = of(PARTY_LAST_NAME_OR_ORGANISATION_NAME, "");

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }


    private List<Case> submitSearch(final String partyName) throws Exception {

        final Map<String, String> parameters = of(PARTY_LAST_NAME_OR_ORGANISATION_NAME, partyName, "pageSize", "30");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();
    }
}
