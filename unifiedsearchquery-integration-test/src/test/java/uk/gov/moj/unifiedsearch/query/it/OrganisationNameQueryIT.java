package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.NameQueryDataIngester.loadCaseDocuments;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.OrganisationNameQueryDataIngester.caseListForOrganisationNameQuery;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Party;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


public class OrganisationNameQueryIT {

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
    public void shouldFindTwoCharName() throws Exception {

        final List<CaseDocument> caseList = caseListForOrganisationNameQuery();
        loadCaseDocuments(caseList);

        final List<Case> caseListResults = submitSearch("EY");

        assertThat(caseListResults.size(), is(1));

        final Party party0 = caseListResults.get(0).getParties().get(0);
        assertThat(party0.getOrganisationName(), is("EY"));
    }

    @Test
    public void shouldFindOrganisationWithNameOrganisation() throws Exception {

        final List<CaseDocument> caseList = caseListForOrganisationNameQuery();
        loadCaseDocuments(caseList);

        final List<Case> caseListResults = submitSearch("Organisation");

        assertThat(caseListResults.size(), is(3));

        final Party party0 = caseListResults.get(0).getParties().get(0);
        assertThat(party0.getOrganisationName(), is("Organisation"));

        final Party party1 = caseListResults.get(1).getParties().get(0);
        assertThat(party1.getOrganisationName(), is("OrganisationName"));

        final Party party2 = caseListResults.get(2).getParties().get(0);
        assertThat(party2.getOrganisationName(), is("Organisation_Name"));
    }

    @Disabled("Failing, will be fixed in SCUS-531")
    @Test
    public void shouldFindOrganisationWithNameOrganisation_Name() throws Exception {

        final List<CaseDocument> caseList = caseListForOrganisationNameQuery();
        loadCaseDocuments(caseList);

        final List<Case> caseListResults = submitSearch("Organisation_Name");

        assertThat(caseListResults.size(), is(3));

        final Party party0 = caseListResults.get(0).getParties().get(0);
        assertThat(party0.getOrganisationName(), is("Organisation_Name"));

        final Party party1 = caseListResults.get(1).getParties().get(0);
        assertThat(party1.getOrganisationName(), is("OrganisationName"));

        final Party party2 = caseListResults.get(2).getParties().get(0);
        assertThat(party2.getOrganisationName(), is("Organisation"));
    }

    @Disabled("Failing, will be fixed in SCUS-531")
    @Test
    public void shouldFindOrganisationWithNameOrganisationName() throws Exception {

        final List<CaseDocument> caseList = caseListForOrganisationNameQuery();
        loadCaseDocuments(caseList);

        final List<Case> caseListResults = submitSearch("OrganisationName");

        assertThat(caseListResults.size(), is(3));

        final Party party0 = caseListResults.get(0).getParties().get(0);
        assertThat(party0.getOrganisationName(), is("OrganisationName"));

        final Party party1 = caseListResults.get(1).getParties().get(0);
        assertThat(party1.getOrganisationName(), is("OrganisationName"));

        final Party party2 = caseListResults.get(2).getParties().get(0);
        assertThat(party2.getOrganisationName(), is("Organisation_Name"));
    }

    private List<Case> submitSearch(final String partyName) throws Exception {

        final Map<String, String> parameters = of(PARTY_LAST_NAME_OR_ORGANISATION_NAME, partyName, "pageSize", "30");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();
    }
}
