package uk.gov.moj.unifiedsearch.query.it;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.ADDRESS_LINE_1;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.CROWN_OR_MAGISTRATES;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PROCEEDINGS_CONCLUDED;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;
import static uk.gov.moj.unifiedsearch.query.it.util.defendantCase.AssertDefendantCases.assertDefendantCases;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.defendant.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.AddressLine1DataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PartyAddressLine1IT {
    private static final String PAGE_SIZE = "pageSize";

    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static AddressLine1DataIngester partyIngester = new AddressLine1DataIngester();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        partyIngester.loadAddressLineCases();
    }

/*The following Search criteria should return the index entry:
14, Excel’s Street      - As Is
14, Excel’s St          - As Is with Synonym

14 Excels Street        - As Is With Without Synonym And No comma And No Punctuations
14 Excels St            - As Is With With Synonym And No comma and No Punctuations

14, Excels Street       - As Is With Without Synonym And comma And No Punctuations
14, Excels St           - As Is With With Synonym And comma and No Punctuations

14 excels street        - lower Case Without Synonym And No comma And No Punctuations
14 excels st            - lower Case With Synonym And No comma And No Punctuations

14, excels street       - lower Case Without Synonym And with comma And No Punctuations
14, excels st           - lower Case With Synonym And with comma And No Punctuations

14, excel’s street      - lower Case Without Synonym And With Comma And With Punctuations
14, excel’s st          - lower Case With Synonym And With Comma And With Punctuations

*/
    @Test
    public void shouldReturnSearchResponseWhenSearchingByAsIs() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES,"true");
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(ADDRESS_LINE_1, "14, Excel’s Street");
        parameters.putIfAbsent(PAGE_SIZE, "10");

        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        assertThat(result.getCases().size(),is(1));

        final List<CaseDocument> expectedCaseDocuments = partyIngester.getExpectedCasesForAddressLinesStreet();
        assertDefendantCases(result, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByAsIsWithSynonym() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES,"true");
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(ADDRESS_LINE_1, "14, Excel’s St");
        parameters.putIfAbsent(PAGE_SIZE, "10");

        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        assertThat(result.getCases().size(),is(1));

        final List<CaseDocument> expectedCaseDocuments = partyIngester.getExpectedCasesForAddressLinesStreet();
        assertDefendantCases(result, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByAsIsWithoutSynonymAndNoCommaAndNoPunctuations() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES,"true");
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(ADDRESS_LINE_1, "14 Excels Street");
        parameters.putIfAbsent(PAGE_SIZE, "10");

        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        assertThat(result.getCases().size(),is(1));

        final List<CaseDocument> expectedCaseDocuments = partyIngester.getExpectedCasesForAddressLinesStreet();
        assertDefendantCases(result, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByAsIsWithSynonymAndNoCommaAndNoPunctuations() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES,"true");
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(ADDRESS_LINE_1, "14 Excels St");
        parameters.putIfAbsent(PAGE_SIZE, "10");

        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        assertThat(result.getCases().size(),is(1));

        final List<CaseDocument> expectedCaseDocuments = partyIngester.getExpectedCasesForAddressLinesStreet();
        assertDefendantCases(result, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByAsIsWithoutSynonymAndCommaAndNoPunctuations() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES,"true");
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(ADDRESS_LINE_1, "14, Excels Street");
        parameters.putIfAbsent(PAGE_SIZE, "10");

        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        assertThat(result.getCases().size(),is(1));

        final List<CaseDocument> expectedCaseDocuments = partyIngester.getExpectedCasesForAddressLinesStreet();
        assertDefendantCases(result, expectedCaseDocuments);
    }


    @Test
    public void shouldReturnSearchResponseWhenSearchingByAsIsWithSynonymAndCommaAndNoPunctuations() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES,"true");
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(ADDRESS_LINE_1, "14, Excels St");
        parameters.putIfAbsent(PAGE_SIZE, "10");

        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        assertThat(result.getCases().size(),is(1));

        final List<CaseDocument> expectedCaseDocuments = partyIngester.getExpectedCasesForAddressLinesStreet();
        assertDefendantCases(result, expectedCaseDocuments);
    }


    @Test
    public void shouldReturnSearchResponseWhenSearchingByLowerCaseWithoutSynonymAndNoCommaAndNoPunctuations() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES,"true");
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(ADDRESS_LINE_1, "14 Excels Street");
        parameters.putIfAbsent(PAGE_SIZE, "10");

        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        assertThat(result.getCases().size(),is(1));

        final List<CaseDocument> expectedCaseDocuments = partyIngester.getExpectedCasesForAddressLinesStreet();
        assertDefendantCases(result, expectedCaseDocuments);
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByLowerCaseWithSynonymAndNoCommaAndNoPunctuations() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES,"true");
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(ADDRESS_LINE_1, "14 Excels St");
        parameters.putIfAbsent(PAGE_SIZE, "10");

        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        assertThat(result.getCases().size(),is(1));

        final List<CaseDocument> expectedCaseDocuments = partyIngester.getExpectedCasesForAddressLinesStreet();
        assertDefendantCases(result, expectedCaseDocuments);
    }


    @Test
    public void shouldReturnSearchResponseWhenSearchingByLowerCaseWithoutSynonymAndWithCommaAndNoPunctuations() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES,"true");
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(ADDRESS_LINE_1, "14, Excels Street");
        parameters.putIfAbsent(PAGE_SIZE, "10");

        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        assertThat(result.getCases().size(),is(1));

        final List<CaseDocument> expectedCaseDocuments = partyIngester.getExpectedCasesForAddressLinesStreet();
        assertDefendantCases(result, expectedCaseDocuments);
    }


    @Test
    public void shouldReturnSearchResponseWhenSearchingByLowerCaseWithSynonymAndWithCommaAndNoPunctuations() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES,"true");
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(ADDRESS_LINE_1, "14, Excels St");
        parameters.putIfAbsent(PAGE_SIZE, "10");

        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        assertThat(result.getCases().size(),is(1));

        final List<CaseDocument> expectedCaseDocuments = partyIngester.getExpectedCasesForAddressLinesStreet();
        assertDefendantCases(result, expectedCaseDocuments);
    }


    @Test
    public void shouldReturnSearchResponseWhenSearchingByLowerCaseWithoutSynonymAndWithCommaAndWithPunctuations() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES,"true");
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(ADDRESS_LINE_1, "14, excel’s street");
        parameters.putIfAbsent(PAGE_SIZE, "10");

        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        assertThat(result.getCases().size(),is(1));

        final List<CaseDocument> expectedCaseDocuments = partyIngester.getExpectedCasesForAddressLinesStreet();
        assertDefendantCases(result, expectedCaseDocuments);
    }



    @Test
    public void shouldReturnSearchResponseWhenSearchingByLowerCaseWithSynonymAndWithCommaAndWithPunctuations() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(CROWN_OR_MAGISTRATES,"true");
        parameters.putIfAbsent(PROCEEDINGS_CONCLUDED, "false");
        parameters.putIfAbsent(ADDRESS_LINE_1, "14, excel’s st");
        parameters.putIfAbsent(PAGE_SIZE, "10");

        final CaseSearchResponse result = searchApiClient.searchDefendantCases(parameters);
        assertThat(result.getCases().size(),is(1));

        final List<CaseDocument> expectedCaseDocuments = partyIngester.getExpectedCasesForAddressLinesStreet();
        assertDefendantCases(result, expectedCaseDocuments);
    }



}
