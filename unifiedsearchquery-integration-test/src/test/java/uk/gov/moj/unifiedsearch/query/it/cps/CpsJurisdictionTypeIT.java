package uk.gov.moj.unifiedsearch.query.it.cps;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCases.assertCases;
import static uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.JurisdictionTypeIngestor;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CpsJurisdictionTypeIT {

    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private CpsSearchApiClient cpsSearchApiClient = getInstance();
    private static JurisdictionTypeIngestor jurisdictionTypeIngestor = new JurisdictionTypeIngestor();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        jurisdictionTypeIngestor.loadCaseDocuments();
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsCourtOfAppealGetMultipleResults() throws Exception {
        final List<Case> cases = submitSearch("COURT_OF_APPEAL");

        assertThat(cases.size(), is(3));

        final CaseDocument expectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(2);

        final CaseDocument expectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(5);

        final CaseDocument expectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(8);

        assertCases(cases, asList(expectedCaseDocument1, expectedCaseDocument2, expectedCaseDocument3));
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsCourtOfAppealOrCrownAndGetMultipleResults() throws Exception {
        final List<Case> cases = submitSearch("COURT_OF_APPEAL , CROWN");

        assertThat(cases.size(), is(6));

        final CaseDocument expectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(0);
        final CaseDocument expectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(2);
        final CaseDocument expectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(3);
        final CaseDocument expectedCaseDocument4 = jurisdictionTypeIngestor.getIndexDocumentAt(5);
        final CaseDocument expectedCaseDocument5 = jurisdictionTypeIngestor.getIndexDocumentAt(6);
        final CaseDocument expectedCaseDocument6 = jurisdictionTypeIngestor.getIndexDocumentAt(8);

        assertCases(cases, asList(expectedCaseDocument1, expectedCaseDocument2, expectedCaseDocument3, expectedCaseDocument4, expectedCaseDocument5, expectedCaseDocument6));
    }

    @Test
    public void shouldQueryByJurisdictionTypeIsCourtOfAppealOrCrownOrMagistratesAndGetMultipleResults() throws Exception {
        final List<Case> cases = submitSearch("COURT_OF_APPEAL , CROWN, MAGISTRATES");

        assertThat(cases.size(), is(9));

        final CaseDocument expectedCaseDocument1 = jurisdictionTypeIngestor.getIndexDocumentAt(0);
        final CaseDocument expectedCaseDocument2 = jurisdictionTypeIngestor.getIndexDocumentAt(1);
        final CaseDocument expectedCaseDocument3 = jurisdictionTypeIngestor.getIndexDocumentAt(2);
        final CaseDocument expectedCaseDocument4 = jurisdictionTypeIngestor.getIndexDocumentAt(3);
        final CaseDocument expectedCaseDocument5 = jurisdictionTypeIngestor.getIndexDocumentAt(4);
        final CaseDocument expectedCaseDocument6 = jurisdictionTypeIngestor.getIndexDocumentAt(5);
        final CaseDocument expectedCaseDocument7 = jurisdictionTypeIngestor.getIndexDocumentAt(6);
        final CaseDocument expectedCaseDocument8 = jurisdictionTypeIngestor.getIndexDocumentAt(7);
        final CaseDocument expectedCaseDocument9 = jurisdictionTypeIngestor.getIndexDocumentAt(8);

        assertCases(cases, asList(expectedCaseDocument1, expectedCaseDocument2, expectedCaseDocument3, expectedCaseDocument4, expectedCaseDocument5, expectedCaseDocument6, expectedCaseDocument7, expectedCaseDocument8, expectedCaseDocument9));
    }

    @Test
    public void shouldReturnAllResultsWhereJurisdictionTypeIsEmpty() throws IOException {
        final Map<String, String> parameters = of("jurisdictionTypes", "");

        cpsSearchApiClient.searchCasesAndValidateStatusCode(parameters, SC_OK);
    }

    @Test
    public void shouldReturnNothingWhereJurisdictionTypeIsNotFound() throws IOException {
        final List<Case> caseListResults = submitSearch("US");

        assertThat(caseListResults, hasSize(0));
    }

    private List<Case> submitSearch(final String jurisdictionTypes) throws IOException {
        final CaseSearchResponse caseSearchResponse = cpsSearchApiClient.searchCases(ImmutableMap.of("jurisdictionTypes", jurisdictionTypes));
        return caseSearchResponse.getCases();
    }
}
