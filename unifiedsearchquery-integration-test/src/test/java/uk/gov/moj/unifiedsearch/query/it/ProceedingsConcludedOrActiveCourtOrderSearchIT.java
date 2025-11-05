package uk.gov.moj.unifiedsearch.query.it;

import static com.google.common.collect.ImmutableMap.of;
import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.COURT_ORDER_VALIDITY_DATE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.CROWN_OR_MAGISTRATES;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PROCEEDINGS_CONCLUDED;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.defendant.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.CourtOrdersDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ProceedingsConcludedOrActiveCourtOrderSearchIT {
    private static final String PAGE_SIZE = "pageSize";

    private SearchApiClient searchApiClient = getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private static CourtOrdersDataIngester courtOrdersDataIngester = new CourtOrdersDataIngester();

    @BeforeAll
    public static void setup() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
        courtOrdersDataIngester.loadCases();
    }

    @Test
    public void shouldReturnSearchResponseWhenProceedingsNotConcludedOrHasActiveCourtOrder() throws IOException {
        final LocalDate orderDate = LocalDate.now().plusDays(3);
        final Map<String, String> parameters = of(PROCEEDINGS_CONCLUDED, "false",
                COURT_ORDER_VALIDITY_DATE, orderDate.toString(),
                CROWN_OR_MAGISTRATES, "true",
                PAGE_SIZE, "10");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchDefendantCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(3L));
        assertThat(caseSearchResponse.getCases(), hasSize(3));
    }

    @Test
    public void shouldReturnSearchResponseWhenProceedingsConcludedAndHasNoActiveCourtOrder() throws IOException {
        final LocalDate orderDate = LocalDate.now().plusDays(11);
        final Map<String, String> parameters = of(PROCEEDINGS_CONCLUDED, "false",
                COURT_ORDER_VALIDITY_DATE, orderDate.toString(),
                CROWN_OR_MAGISTRATES, "true",
                PAGE_SIZE, "10");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchDefendantCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(1L));
        assertThat(caseSearchResponse.getCases(), hasSize(1));
    }

    @Test
    public void shouldReturnSearchResponseWhenProceedingsNotConcludedOrHasAtLeastOneActiveCourtOrder() throws IOException {
        final LocalDate orderDate = LocalDate.now().plusDays(21);
        final Map<String, String> parameters = of(PROCEEDINGS_CONCLUDED, "false",
                COURT_ORDER_VALIDITY_DATE, orderDate.toString(),
                CROWN_OR_MAGISTRATES, "true",
                PAGE_SIZE, "10");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchDefendantCases(parameters);

        assertThat(caseSearchResponse.getTotalResults(), is(1L));
        assertThat(caseSearchResponse.getCases(), hasSize(1));
    }
}
