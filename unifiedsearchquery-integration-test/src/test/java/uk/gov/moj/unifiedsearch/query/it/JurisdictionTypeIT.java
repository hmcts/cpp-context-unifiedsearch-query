package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCase.assertCase;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.JurisdictionTypeIngestor;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class JurisdictionTypeIT {
    private static final String IS_SJP = "_is_sjp";
    private static final String IS_MAGISTRATE_COURT = "_is_magistrates";
    private static final String IS_CROWN_COURT = "_is_crown";
    private static final String SORT_ASC = "asc";
    private static final String SORT_DESC = "desc";
    private static final String SORT_BY_SJP_NOTICE_SERVED = "sortBySjpNoticeServed";

    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private SearchApiClient searchApiClient = getInstance();
    private static JurisdictionTypeIngestor jurisdictionTypeIngestor = new JurisdictionTypeIngestor();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        jurisdictionTypeIngestor.loadCaseDocuments();
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsSjpEqualsTrueAndGetMultipleResults() throws Exception {
        final List<Case> retrievedCases = validateForJurisdictionType("true", "false", "false");

        assertThat(retrievedCases.size(), is(4));

        final CaseDocument expectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(0);

        final CaseDocument expectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(3);

        final CaseDocument expectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(4);

        final CaseDocument expectedCaseDocument4 = jurisdictionTypeIngestor.getIndexDocumentAt(6);

        assertCases(retrievedCases, asList(expectedCaseDocument1, expectedCaseDocument2, expectedCaseDocument3, expectedCaseDocument4));
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsSjpEqualsTrueAndGetMultipleResultsSortedByAsc() throws Exception {
        final List<Case> retrievedCases = submitSearch("true", "false", "false", SORT_ASC);

        assertThat(retrievedCases.size(), is(4));

        final CaseDocument orderedByAscExpectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(6);

        final CaseDocument orderedByAscExpectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(0);

        final CaseDocument orderedByAscExpectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(4);

        final CaseDocument orderedByAscExpectedCaseDocument4 = jurisdictionTypeIngestor.getIndexDocumentAt(3);

        assertCase(retrievedCases.get(0), orderedByAscExpectedCaseDocument1);
        assertCase(retrievedCases.get(1), orderedByAscExpectedCaseDocument2);
        assertCase(retrievedCases.get(2), orderedByAscExpectedCaseDocument3);
        assertCase(retrievedCases.get(3), orderedByAscExpectedCaseDocument4);
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsSjpEqualsTrueAndGetMultipleResultsSortedByAscWithPaddedSpace() throws Exception {
        final List<Case> retrievedCases = submitSearch("true", "false", "false", "asc ");

        assertThat(retrievedCases.size(), is(4));

        final CaseDocument orderedByAscExpectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(6);

        final CaseDocument orderedByAscExpectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(0);

        final CaseDocument orderedByAscExpectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(4);

        final CaseDocument orderedByAscExpectedCaseDocument4 = jurisdictionTypeIngestor.getIndexDocumentAt(3);

        assertCase(retrievedCases.get(0), orderedByAscExpectedCaseDocument1);
        assertCase(retrievedCases.get(1), orderedByAscExpectedCaseDocument2);
        assertCase(retrievedCases.get(2), orderedByAscExpectedCaseDocument3);
        assertCase(retrievedCases.get(3), orderedByAscExpectedCaseDocument4);
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsSjpEqualsTrueAndGetMultipleResultsSortedByAscInUppercase() throws Exception {
        final List<Case> retrievedCases = submitSearch("true", "false", "false", "ASC");

        assertThat(retrievedCases.size(), is(4));

        final CaseDocument orderedByAscExpectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(6);

        final CaseDocument orderedByAscExpectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(0);

        final CaseDocument orderedByAscExpectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(4);

        final CaseDocument orderedByAscExpectedCaseDocument4 = jurisdictionTypeIngestor.getIndexDocumentAt(3);

        assertCase(retrievedCases.get(0), orderedByAscExpectedCaseDocument1);
        assertCase(retrievedCases.get(1), orderedByAscExpectedCaseDocument2);
        assertCase(retrievedCases.get(2), orderedByAscExpectedCaseDocument3);
        assertCase(retrievedCases.get(3), orderedByAscExpectedCaseDocument4);
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsSjpEqualsTrueAndGetMultipleResultsSortedByDesc() throws Exception {
        final List<Case> retrievedCases = submitSearch("true", "false", "false", SORT_DESC);

        assertThat(retrievedCases.size(), is(4));

        final CaseDocument orderedByDescExpectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(3);

        final CaseDocument orderedByDescExpectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(4);

        final CaseDocument orderedByDescExpectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(0);

        final CaseDocument orderedByDescExpectedCaseDocument4 = jurisdictionTypeIngestor.getIndexDocumentAt(6);

        assertCase(retrievedCases.get(0), orderedByDescExpectedCaseDocument1);
        assertCase(retrievedCases.get(1), orderedByDescExpectedCaseDocument2);
        assertCase(retrievedCases.get(2), orderedByDescExpectedCaseDocument3);
        assertCase(retrievedCases.get(3), orderedByDescExpectedCaseDocument4);
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsMagistrateEqualsTrueAndGetMultipleResults() throws Exception {
        final List<Case> retrievedCases = validateForJurisdictionType("false", "true", "false");

        assertThat(retrievedCases.size(), is(4));

        final CaseDocument expectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(1);

        final CaseDocument expectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(3);

        final CaseDocument expectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(5);

        final CaseDocument expectedCaseDocument4 = jurisdictionTypeIngestor.getIndexDocumentAt(6);

        assertCases(retrievedCases, asList(expectedCaseDocument1, expectedCaseDocument2, expectedCaseDocument3, expectedCaseDocument4));
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsCrownCourtEqualsTrueAndGetMultipleResults() throws Exception {
        final List<Case> retrievedCases = validateForJurisdictionType("false", "false", "true");

        assertThat(retrievedCases.size(), is(4));

        final CaseDocument expectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(2);

        final CaseDocument expectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(4);

        final CaseDocument expectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(5);

        final CaseDocument expectedCaseDocument4 = jurisdictionTypeIngestor.getIndexDocumentAt(6);

        assertCases(retrievedCases, asList(expectedCaseDocument1, expectedCaseDocument2, expectedCaseDocument3, expectedCaseDocument4));
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsSjpAndIsCrownCourtEqualsTrueAndGetMultipleResultsSortedByAsc() throws Exception {
        final List<Case> retrievedCases = submitSearch("true", "false", "true", SORT_ASC);

        assertThat(retrievedCases.size(), is(6));

        final CaseDocument orderedByAscExpectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(2);

        final CaseDocument orderedByAscExpectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(6);

        final CaseDocument orderedByAscExpectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(0);

        final CaseDocument orderedByAscExpectedCaseDocument4 = jurisdictionTypeIngestor.getIndexDocumentAt(4);

        final CaseDocument orderedByAscExpectedCaseDocument5 = jurisdictionTypeIngestor.getIndexDocumentAt(3);

        final CaseDocument orderedByAscExpectedCaseDocument6 = jurisdictionTypeIngestor.getIndexDocumentAt(5);

        assertCase(retrievedCases.get(0), orderedByAscExpectedCaseDocument1);
        assertCase(retrievedCases.get(1), orderedByAscExpectedCaseDocument2);
        assertCase(retrievedCases.get(2), orderedByAscExpectedCaseDocument3);
        assertCase(retrievedCases.get(3), orderedByAscExpectedCaseDocument4);
        assertCase(retrievedCases.get(4), orderedByAscExpectedCaseDocument5);
        assertCase(retrievedCases.get(5), orderedByAscExpectedCaseDocument6);
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsSjpAndIsCrownCourtEqualsTrueAndGetMultipleResultsSortedByDesc() throws Exception {
        final List<Case> retrievedCases = submitSearch("true", "false", "true", SORT_DESC);

        assertThat(retrievedCases.size(), is(6));

        final CaseDocument orderedByDescExpectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(5);

        final CaseDocument orderedByDescExpectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(3);

        final CaseDocument orderedByDescExpectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(4);

        final CaseDocument orderedByDescExpectedCaseDocument4 = jurisdictionTypeIngestor.getIndexDocumentAt(0);

        final CaseDocument orderedByDescExpectedCaseDocument5 = jurisdictionTypeIngestor.getIndexDocumentAt(6);

        final CaseDocument orderedByDescExpectedCaseDocument6 = jurisdictionTypeIngestor.getIndexDocumentAt(2);

        assertCase(retrievedCases.get(0), orderedByDescExpectedCaseDocument1);
        assertCase(retrievedCases.get(1), orderedByDescExpectedCaseDocument2);
        assertCase(retrievedCases.get(2), orderedByDescExpectedCaseDocument3);
        assertCase(retrievedCases.get(3), orderedByDescExpectedCaseDocument4);
        assertCase(retrievedCases.get(4), orderedByDescExpectedCaseDocument5);
        assertCase(retrievedCases.get(5), orderedByDescExpectedCaseDocument6);
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsSjpAndIsCrownCourtEqualsTrue() throws Exception {
        final List<Case> retrievedCases = validateForJurisdictionType("true", "false", "true");

        assertThat(retrievedCases.size(), is(6));

        final CaseDocument expectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(0);

        final CaseDocument expectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(2);

        final CaseDocument expectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(3);

        final CaseDocument expectedCaseDocument4 = jurisdictionTypeIngestor.getIndexDocumentAt(4);

        final CaseDocument expectedCaseDocument5 = jurisdictionTypeIngestor.getIndexDocumentAt(5);

        final CaseDocument expectedCaseDocument6 = jurisdictionTypeIngestor.getIndexDocumentAt(6);

        assertCases(retrievedCases, asList(expectedCaseDocument1, expectedCaseDocument2, expectedCaseDocument3, expectedCaseDocument4, expectedCaseDocument5, expectedCaseDocument6));
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsSjpAndIsMagistrateEqualsTrue() throws Exception {
        final List<Case> retrievedCases = validateForJurisdictionType("true", "true", "false");

        assertThat(retrievedCases.size(), is(6));

        final CaseDocument expectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(0);

        final CaseDocument expectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(1);

        final CaseDocument expectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(3);

        final CaseDocument expectedCaseDocument4 = jurisdictionTypeIngestor.getIndexDocumentAt(4);

        final CaseDocument expectedCaseDocument5 = jurisdictionTypeIngestor.getIndexDocumentAt(5);

        final CaseDocument expectedCaseDocument6 = jurisdictionTypeIngestor.getIndexDocumentAt(6);

        assertCases(retrievedCases, asList(expectedCaseDocument1, expectedCaseDocument2, expectedCaseDocument3, expectedCaseDocument4, expectedCaseDocument5, expectedCaseDocument6));
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsMagistrateAndIsCrownCourtEqualsTrue() throws Exception {
        final List<Case> retrievedCases = validateForJurisdictionType("false", "true", "true");

        assertThat(retrievedCases.size(), is(6));

        final CaseDocument expectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(1);

        final CaseDocument expectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(2);

        final CaseDocument expectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(3);

        final CaseDocument expectedCaseDocument4 = jurisdictionTypeIngestor.getIndexDocumentAt(4);

        final CaseDocument expectedCaseDocument5 = jurisdictionTypeIngestor.getIndexDocumentAt(5);

        final CaseDocument expectedCaseDocument6 = jurisdictionTypeIngestor.getIndexDocumentAt(6);

        assertCases(retrievedCases, asList(expectedCaseDocument1, expectedCaseDocument2, expectedCaseDocument3, expectedCaseDocument4, expectedCaseDocument5, expectedCaseDocument6));
    }

    @Test
    public void shouldQueryByAllJurisdictionTypeEqualsTrue() throws Exception {
        final List<Case> retrievedCases = validateForJurisdictionType("true", "true", "true");

        assertThat(retrievedCases.size(), is(7));

        final CaseDocument expectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(0);

        final CaseDocument expectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(1);

        final CaseDocument expectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(2);

        final CaseDocument expectedCaseDocument4 = jurisdictionTypeIngestor.getIndexDocumentAt(3);

        final CaseDocument expectedCaseDocument5 = jurisdictionTypeIngestor.getIndexDocumentAt(4);

        final CaseDocument expectedCaseDocument6 = jurisdictionTypeIngestor.getIndexDocumentAt(5);

        final CaseDocument expectedCaseDocument7 = jurisdictionTypeIngestor.getIndexDocumentAt(6);

        assertCases(retrievedCases,
                asList(expectedCaseDocument1,
                        expectedCaseDocument2,
                        expectedCaseDocument3,
                        expectedCaseDocument4,
                        expectedCaseDocument5,
                        expectedCaseDocument6,
                        expectedCaseDocument7));
    }

    @Test
    public void shouldReturnAllCasesWhenNoJurisdictionTypeSpecifiedToFilterBy() throws Exception {
        final List<Case> retrievedCases = validateForJurisdictionType("false", "false", "false");

        assertThat(retrievedCases.size(), is(8));

        final CaseDocument expectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(0);

        final CaseDocument expectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(1);

        final CaseDocument expectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(2);

        final CaseDocument expectedCaseDocument4 = jurisdictionTypeIngestor.getIndexDocumentAt(3);

        final CaseDocument expectedCaseDocument5 = jurisdictionTypeIngestor.getIndexDocumentAt(4);

        final CaseDocument expectedCaseDocument6 = jurisdictionTypeIngestor.getIndexDocumentAt(5);

        final CaseDocument expectedCaseDocument7 = jurisdictionTypeIngestor.getIndexDocumentAt(6);

        final CaseDocument expectedCaseDocument8 = jurisdictionTypeIngestor.getIndexDocumentAt(7);

        assertCases(retrievedCases,
                asList(expectedCaseDocument1,
                        expectedCaseDocument2,
                        expectedCaseDocument3,
                        expectedCaseDocument4,
                        expectedCaseDocument5,
                        expectedCaseDocument6,
                        expectedCaseDocument7,
                        expectedCaseDocument8));
    }

    @Test
    public void shouldReturnBadRequestWhenIncorrectValueType() throws Exception {
        final Map<String, String> parameters = of(IS_SJP, "test", IS_MAGISTRATE_COURT, "test", IS_CROWN_COURT, "test");

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }

    @Test
    public void shouldReturnBadRequestWhenJurisdictionTypeIsEmpty() throws IOException {
        final Map<String, String> parameters = of(IS_SJP, "", IS_MAGISTRATE_COURT, "", IS_CROWN_COURT, "");

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }

    @Test
    public void shouldReturnBadRequestWhenJurisdictionTypeSortOrderIsIncorrect() throws IOException {
        final Map<String, String> parameters = of(IS_SJP, "true", IS_MAGISTRATE_COURT, "false", IS_CROWN_COURT, "false", SORT_BY_SJP_NOTICE_SERVED,"unknown");

        searchApiClient.searchCasesAndValidateStatusCode(parameters, SC_BAD_REQUEST);
    }

    private List<Case> validateForJurisdictionType(final String sjp, final String magistrateCourt, final String crownCourt) throws Exception {
        return submitSearch(sjp, magistrateCourt, crownCourt, null);
    }

    private List<Case> submitSearch(final String sjp, final String magistrateCourt, final String crownCourt, final String sortBySjpNoticeServed) throws Exception {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("sjp", sjp);
        parameters.put("magistrateCourt", magistrateCourt);
        parameters.put("crownCourt", crownCourt);
        parameters.put("pageSize", "30");
        if (sortBySjpNoticeServed != null) {
            parameters.put("sortBySjpNoticeServed", sortBySjpNoticeServed);
        }

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        return caseSearchResponse.getCases();
    }
}
