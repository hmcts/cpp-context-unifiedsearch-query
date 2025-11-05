package uk.gov.moj.cpp.unifiedsearch.query.api.service;

import static org.elasticsearch.search.sort.SortBuilders.fieldSort;
import static org.elasticsearch.search.sort.SortOrder.ASC;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.moj.cpp.unifiedsearch.query.api.service.CaseSearchService.APPOINTMENT_DATE;
import static uk.gov.moj.cpp.unifiedsearch.query.api.service.CaseSearchService.CASE_REFERENCE;
import static uk.gov.moj.cpp.unifiedsearch.query.api.service.CaseSearchService.COURT_PROCEEDINGS_INITIATED;
import static uk.gov.moj.cpp.unifiedsearch.query.api.service.CaseSearchService.DEFENDANTS;
import static uk.gov.moj.cpp.unifiedsearch.query.api.service.CaseSearchService.HEARINGS;
import static uk.gov.moj.cpp.unifiedsearch.query.api.service.CaseSearchService.PROSECUTION_CASE_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.api.service.CaseSearchService.RESULT_HIT_NODE_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.api.service.CaseSearchService.RESULT_INNER_HIT_NODE_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.api.service.CaseSearchService.SJP_NOTICE_SERVED;
import static uk.gov.moj.cpp.unifiedsearch.query.api.service.CaseSearchService.STATUS;
import static uk.gov.moj.cpp.unifiedsearch.query.api.service.CaseSearchService.TOTAL_RESULTS;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.CRIME_CASE_INDEX_NAME;

import uk.gov.justice.services.unifiedsearch.client.search.UnifiedSearchService;
import uk.gov.moj.cpp.unifiedsearch.query.builders.UnifiedSearchQueryBuilderService;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.QueryParameters;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.defendant.ProbationDefendantDetails;

import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.NestedSortBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CaseSearchServiceTest {

    @Mock
    private UnifiedSearchService unifiedSearchService;

    @Mock
    private JsonNumber totalResults;

    @Mock
    private BoolQueryBuilder boolQueryBuilder;

    @Mock
    private JsonObject response;

    @Mock
    private UnifiedSearchQueryBuilderService unifiedSearchQueryBuilderService;

    @InjectMocks
    private CaseSearchService caseQueryService;

    private String resultHitNodeName;

    private final String missing_last = "_last";


    @BeforeEach
    public void setUp() {
        resultHitNodeName = "cases";
    }

    @Test
    public void shouldReturnSearchCasesResult() {

        final QueryParameters queryParameters = mock(QueryParameters.class);


        when(unifiedSearchQueryBuilderService.builder(queryParameters)).thenReturn(boolQueryBuilder);
        when(unifiedSearchService.search(boolQueryBuilder,
                CRIME_CASE_INDEX_NAME,
                uk.gov.moj.cpp.unifiedsearch.query.api.domain.response.indexdoc2apiresponse.Case.class,
                resultHitNodeName,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                null))
                .thenReturn(response);

        final JsonObject caseQueryResult = caseQueryService.searchCases(queryParameters);

        assertThat(caseQueryResult, is(notNullValue()));

        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }

    @Test
    public void shouldReturnDefendantDetails() {

        final QueryParameters queryParameters = mock(QueryParameters.class);
        final JsonObject party = Json.createObjectBuilder().add("defendantId", UUID.randomUUID().toString()).build();
        final JsonArray parties = Json.createArrayBuilder().add(party).build();
        final JsonObject case1 = Json.createObjectBuilder()
                .add(PROSECUTION_CASE_ID, UUID.randomUUID().toString())
                .add(CASE_REFERENCE, "caseReference")
                .add(DEFENDANTS, parties)
                .add(STATUS, "ACTIVE")
                .build();
        final JsonObject case2 = Json.createObjectBuilder()
                .add(PROSECUTION_CASE_ID, UUID.randomUUID().toString())
                .add(CASE_REFERENCE, "caseReference")
                .add(DEFENDANTS, parties)
                .add(STATUS, "INACTIVE")
                .build();
        final JsonArray cases = Json.createArrayBuilder().add(case1).add(case2).build();

        when(unifiedSearchQueryBuilderService.builder(queryParameters)).thenReturn(boolQueryBuilder);
        when(unifiedSearchService.search(boolQueryBuilder,
                CRIME_CASE_INDEX_NAME,
                ProbationDefendantDetails.class,
                resultHitNodeName,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                null))
                .thenReturn(response);
        when(response.getJsonArray(RESULT_HIT_NODE_NAME)).thenReturn(cases);
        when(response.getJsonNumber(TOTAL_RESULTS)).thenReturn(totalResults);

        final JsonObject caseQueryResult = caseQueryService.searchProbationDefendantDetails(queryParameters);

        assertThat(caseQueryResult, is(notNullValue()));

        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }

    @Test
    public void shouldReturnSortedBySjpNoticeServedSearchCasesResult() {
        final QueryParameters queryParameters = mock(QueryParameters.class);
        final FieldSortBuilder fieldSortBuilder = fieldSort(SJP_NOTICE_SERVED)
                .missing(missing_last)
                .order(ASC);

        when(queryParameters.getSortBySjpNoticeServed()).thenReturn("asc");

        when(unifiedSearchQueryBuilderService.builder(queryParameters)).thenReturn(boolQueryBuilder);
        when(unifiedSearchService.search(boolQueryBuilder,
                CRIME_CASE_INDEX_NAME,
                uk.gov.moj.cpp.unifiedsearch.query.api.domain.response.indexdoc2apiresponse.Case.class,
                resultHitNodeName,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                fieldSortBuilder))
                .thenReturn(response);

        final JsonObject caseQueryResult = caseQueryService.searchCases(queryParameters);

        assertThat(caseQueryResult, is(notNullValue()));

        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }

    @Test
    public void shouldReturnSortedByAppointmentDateSearchCasesResult() {
        final QueryParameters queryParameters = mock(QueryParameters.class);
        final FieldSortBuilder fieldSortBuilder = fieldSort(APPOINTMENT_DATE).missing(missing_last).order(ASC).setNestedSort(new NestedSortBuilder(HEARINGS));

        when(queryParameters.getSortByAppointmentDate()).thenReturn("asc");

        when(unifiedSearchQueryBuilderService.builder(queryParameters)).thenReturn(boolQueryBuilder);
        when(unifiedSearchService.search(boolQueryBuilder,
                CRIME_CASE_INDEX_NAME,
                uk.gov.moj.cpp.unifiedsearch.query.api.domain.response.indexdoc2apiresponse.Case.class,
                resultHitNodeName,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                fieldSortBuilder))
                .thenReturn(response);

        final JsonObject caseQueryResult = caseQueryService.searchCases(queryParameters);

        assertThat(caseQueryResult, is(notNullValue()));

        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }

    @Test
    public void shouldReturnSortedSearchCasesResultForDefendantSearch() {
        final QueryParameters queryParameters = mock(QueryParameters.class);
        final FieldSortBuilder fieldSortBuilder = fieldSort(COURT_PROCEEDINGS_INITIATED).order(ASC);
        final JsonObject party = Json.createObjectBuilder().add("defendantId", UUID.randomUUID().toString()).build();
        final JsonArray parties = Json.createArrayBuilder().add(party).build();

        final JsonObject case1 = Json.createObjectBuilder()
                .add(PROSECUTION_CASE_ID, UUID.randomUUID().toString())
                .add(CASE_REFERENCE, "caseReference")
                .add(DEFENDANTS, parties)
                .build();
        final JsonArray cases = Json.createArrayBuilder().add(case1).build();
        when(unifiedSearchQueryBuilderService.builder(queryParameters)).thenReturn(boolQueryBuilder);

        when(unifiedSearchService.search(boolQueryBuilder,
                CRIME_CASE_INDEX_NAME,
                uk.gov.moj.cpp.unifiedsearch.query.api.domain.response.index2defendantcaseresponse.Case.class,
                resultHitNodeName,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                fieldSortBuilder,
                uk.gov.moj.cpp.unifiedsearch.query.api.domain.response.index2defendantcaseresponse.Party.class,
                RESULT_INNER_HIT_NODE_NAME
        ))
                .thenReturn(response);
        when(response.getJsonArray(RESULT_INNER_HIT_NODE_NAME)).thenReturn(parties);
        when(response.getJsonArray(RESULT_HIT_NODE_NAME)).thenReturn(cases);
        when(response.getJsonNumber(TOTAL_RESULTS)).thenReturn(totalResults);

        final JsonObject caseQueryResult = caseQueryService.searchDefendantCases(queryParameters);

        assertThat(caseQueryResult, is(notNullValue()));

        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }
}