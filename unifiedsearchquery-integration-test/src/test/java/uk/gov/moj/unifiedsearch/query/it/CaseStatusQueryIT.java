package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static java.lang.String.join;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.ingestors.ReferenceSearchDataIngesterWithAddress;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CaseStatusQueryIT {

    private final SearchApiClient searchApiClient = getInstance();
    private static final ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static final ReferenceSearchDataIngesterWithAddress dataIngesterWithAddress = new ReferenceSearchDataIngesterWithAddress();
    private static final String CASE_STATUS = "caseStatus";
    private static final String ACTIVE_CASE_STATUS = "ACTIVE";
    private static final String INACTIVE_CASE_STATUS = "INACTIVE";
    private static final String CASE_REFERENCE = "caseReference";
    private static final String TFL_CASE_REFERENCE = "TFL9876543";

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        dataIngesterWithAddress.loadCases();
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByCaseStatus() throws IOException {

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(CASE_STATUS, ACTIVE_CASE_STATUS));

        assertThat(caseSearchResponse.getTotalResults(), is(7L));
        assertThat(caseSearchResponse.getCases().size(), is(7));
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByTwoCaseStatuses() throws IOException {

        final String caseStatusParameters = join(",", ACTIVE_CASE_STATUS, INACTIVE_CASE_STATUS);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(CASE_STATUS, caseStatusParameters));
        assertThat(caseSearchResponse.getCases().size(), is(10));
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByCaseStatusAndCaseReference() throws IOException {

        final Map<String, String> parameters = of(CASE_STATUS, ACTIVE_CASE_STATUS, CASE_REFERENCE, TFL_CASE_REFERENCE);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);
        CaseDocument caseDocument = dataIngesterWithAddress.getIndexDocumentAt(10);
        Case responseCase = caseSearchResponse.getCases().get(0);
        assertThat(caseSearchResponse.getCases().size(), is(1));
        assertThat(caseDocument.getCaseStatus(), is(responseCase.getCaseStatus()));
        assertThat(caseDocument.getCaseReference(), is(responseCase.getCaseReference()));
        assertThat(caseDocument.getProsecutingAuthority(), is(responseCase.getProsecutingAuthority()));
    }

    @Test
    public void shouldReturnSearchResponseWhenSearchingByInactiveCaseStatus() throws IOException {

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(of(CASE_STATUS, INACTIVE_CASE_STATUS));

        assertThat(caseSearchResponse.getTotalResults(), is(4L));
        assertThat(caseSearchResponse.getCases().size(), is(4));
    }
}
