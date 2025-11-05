package uk.gov.moj.unifiedsearch.query.it.cps;

import static com.google.common.collect.ImmutableMap.of;
import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.lang.String.join;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.unifiedsearch.client.constant.CpsPartyType.DEFENDANT;
import static uk.gov.justice.services.unifiedsearch.client.constant.CpsPartyType.VICTIM;
import static uk.gov.justice.services.unifiedsearch.client.constant.CpsPartyType.WITNESS;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.PartyTypeDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PartyTypeQueryIT {

    private static final String INCORRECT_PARTY_TYPE = "some-incorrect-party-type";
    private CpsSearchApiClient searchApiClient = CpsSearchApiClient.getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static PartyTypeDataIngester partyTypeSearchDataIngester = new PartyTypeDataIngester();
    private static final String PARTY_TYPE = "partyTypes";
    private static final String FIRST_NAME = "firstName";
    private static final String DEFAULT_FIRST_NAME = "Joe";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        partyTypeSearchDataIngester.loadCases();
    }

    @Test
    public void shouldFindNothingIfIncorrectReferenceIsProvided() throws IOException {

        final Map<String, String> parameters = of(PARTY_TYPE, INCORRECT_PARTY_TYPE,
                FIRST_NAME, DEFAULT_FIRST_NAME);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(0L));
        assertThat(caseSearchResponse.getCases(), hasSize(0));
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByDefendant() throws IOException {

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_TYPE, DEFENDANT.name(),
                FIRST_NAME, DEFAULT_FIRST_NAME));

        final List<CaseDocument> expectedCaseDocuments = partyTypeSearchDataIngester.getCasesWithDefendant();

        assertCases(caseSearchResponse, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByAllPartyType() throws IOException {

        final String partyTypesParameter = join("   ,  ", DEFENDANT.name(), WITNESS.name(), VICTIM.name());

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_TYPE, partyTypesParameter,
                FIRST_NAME, DEFAULT_FIRST_NAME));

        final List<CaseDocument> expectedCaseDocuments = partyTypeSearchDataIngester.getAllCases();

        assertCases(caseSearchResponse, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByVictim() throws IOException {

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_TYPE, VICTIM.name(),
                FIRST_NAME, DEFAULT_FIRST_NAME));

        final List<CaseDocument> expectedCaseDocuments = partyTypeSearchDataIngester.getCasesWithVictim();

        assertCases(caseSearchResponse, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByRespondentCaseInsensitive() throws IOException {
        final String partyTypes = "viCtIm";

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_TYPE, partyTypes,
                FIRST_NAME, DEFAULT_FIRST_NAME));

        final List<CaseDocument> expectedCaseDocuments = partyTypeSearchDataIngester.getCasesWithVictim();

        assertCases(caseSearchResponse, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByWitness() throws IOException {

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(PARTY_TYPE, WITNESS.name(),
                FIRST_NAME, DEFAULT_FIRST_NAME));

        final List<CaseDocument> expectedCaseDocuments = partyTypeSearchDataIngester.getCasesWithWitness();

        assertCases(caseSearchResponse, expectedCaseDocuments);
    }


    @Test
    public void shouldReturnBadRequestWhenApplicantSearchParamIsEmpty() throws IOException {

        final Map<String, String> parameters = of(PARTY_TYPE, "");

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_OK);
    }
}