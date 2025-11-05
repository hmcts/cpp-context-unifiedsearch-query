package uk.gov.moj.cpp.unifiedsearch.query.api.service;

import static org.elasticsearch.search.sort.SortBuilders.fieldSort;
import static org.elasticsearch.search.sort.SortOrder.ASC;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.RESULT_HIT_NODE_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.URN;

import uk.gov.justice.services.unifiedsearch.client.search.UnifiedSearchService;
import uk.gov.moj.cpp.unifiedsearch.query.builders.UnifiedSearchQueryBuilderService;
import uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSortBy;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.CpsQueryParameters;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Case;

import javax.json.JsonObject;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.NestedSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CpsCaseSearchServiceTest {

    @Mock
    private UnifiedSearchService unifiedSearchService;

    @Mock
    private BoolQueryBuilder boolQueryBuilder;

    @Mock
    private JsonObject response;

    @Mock
    private UnifiedSearchQueryBuilderService unifiedSearchQueryBuilderService;

    @InjectMocks
    private CpsCaseSearchService cpsCaseQueryService;

    private final String missing_last = "_last";

    @Test
    public void shouldReturnSearchCasesResult() {
        final CpsQueryParameters queryParameters = mock(CpsQueryParameters.class);
        final FieldSortBuilder fieldSortBuilder = fieldSort(URN).missing(missing_last).order(ASC);

        when(unifiedSearchQueryBuilderService.builder(queryParameters))
                .thenReturn(boolQueryBuilder);
        when(unifiedSearchService.search(boolQueryBuilder,
                CPS_CASE_INDEX_NAME,
                Case.class,
                RESULT_HIT_NODE_NAME,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                fieldSortBuilder))
                .thenReturn(response);

        final JsonObject caseQueryResult = cpsCaseQueryService.searchCases(queryParameters);
        assertThat(caseQueryResult, is(notNullValue()));
        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }

    @Test
    public void shouldReturnSearchCasesResultSortByCaseStatus() {
        final CpsQueryParameters queryParameters = mock(CpsQueryParameters.class);
        final FieldSortBuilder fieldSortBuilder = fieldSort(CpsCaseSortBy.STATUS.getFieldName()).missing(missing_last).order(SortOrder.DESC);

        when(queryParameters.getOrderBy()).thenReturn(CpsCaseSortBy.STATUS.getKeyName());
        when(queryParameters.getOrder()).thenReturn(SortOrder.DESC);

        when(unifiedSearchQueryBuilderService.builder(queryParameters))
                .thenReturn(boolQueryBuilder);

        when(unifiedSearchService.search(boolQueryBuilder,
                CPS_CASE_INDEX_NAME,
                Case.class,
                RESULT_HIT_NODE_NAME,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                fieldSortBuilder))
                .thenReturn(response);

        final JsonObject caseQueryResult = cpsCaseQueryService.searchCases(queryParameters);
        assertThat(caseQueryResult, is(notNullValue()));
        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }

    @Test
    public void shouldReturnSearchCasesResultSortByDateOfBirth() {
        final CpsQueryParameters queryParameters = mock(CpsQueryParameters.class);

        final CpsCaseSortBy sortByDateOfBirth = CpsCaseSortBy.DATE_OF_BIRTH;
        final FieldSortBuilder fieldSortBuilder = fieldSort(sortByDateOfBirth.getFieldName())
                .missing(missing_last)
                .order(SortOrder.ASC)
                .setNestedSort(new NestedSortBuilder(sortByDateOfBirth.getNestedPath().orElse(null)));

        when(queryParameters.getOrderBy()).thenReturn(sortByDateOfBirth.getKeyName());
        when(queryParameters.getOrder()).thenReturn(SortOrder.ASC);

        when(unifiedSearchQueryBuilderService.builder(queryParameters))
                .thenReturn(boolQueryBuilder);

        when(unifiedSearchService.search(boolQueryBuilder,
                CPS_CASE_INDEX_NAME,
                Case.class,
                RESULT_HIT_NODE_NAME,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                fieldSortBuilder))
                .thenReturn(response);

        final JsonObject caseQueryResult = cpsCaseQueryService.searchCases(queryParameters);
        assertThat(caseQueryResult, is(notNullValue()));
        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }

    @Test
    public void shouldReturnSearchCasesResultSortByHearingType() {
        final CpsQueryParameters queryParameters = mock(CpsQueryParameters.class);

        final CpsCaseSortBy sortByHearingType = CpsCaseSortBy.HEARING_TYPE;
        final FieldSortBuilder fieldSortBuilder = fieldSort(sortByHearingType.getFieldName())
                .missing(missing_last)
                .order(SortOrder.DESC)
                .setNestedSort(new NestedSortBuilder(sortByHearingType.getNestedPath().orElse(null)).
                        setFilter(sortByHearingType.getNestedFilter().orElse(null)).
                        setMaxChildren(sortByHearingType.getNestedMaxChildren().orElse(null)));

        when(queryParameters.getOrderBy()).thenReturn(sortByHearingType.getKeyName());
        when(queryParameters.getOrder()).thenReturn(SortOrder.DESC);

        when(unifiedSearchQueryBuilderService.builder(queryParameters))
                .thenReturn(boolQueryBuilder);

        when(unifiedSearchService.search(boolQueryBuilder,
                CPS_CASE_INDEX_NAME,
                Case.class,
                RESULT_HIT_NODE_NAME,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                fieldSortBuilder))
                .thenReturn(response);

        final JsonObject caseQueryResult = cpsCaseQueryService.searchCases(queryParameters);
        assertThat(caseQueryResult, is(notNullValue()));
        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }

    @Test
    public void shouldReturnSearchCasesResultSortByProsecutor() {
        final CpsQueryParameters queryParameters = mock(CpsQueryParameters.class);

        final CpsCaseSortBy sortByProsecutor = CpsCaseSortBy.PROSECUTOR;
        final FieldSortBuilder fieldSortBuilder = fieldSort(sortByProsecutor.getFieldName())
                .missing(missing_last)
                .order(SortOrder.ASC);

        when(queryParameters.getOrderBy()).thenReturn(sortByProsecutor.getKeyName());
        when(queryParameters.getOrder()).thenReturn(SortOrder.ASC);

        when(unifiedSearchQueryBuilderService.builder(queryParameters))
                .thenReturn(boolQueryBuilder);

        when(unifiedSearchService.search(boolQueryBuilder,
                CPS_CASE_INDEX_NAME,
                Case.class,
                RESULT_HIT_NODE_NAME,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                fieldSortBuilder))
                .thenReturn(response);

        final JsonObject caseQueryResult = cpsCaseQueryService.searchCases(queryParameters);
        assertThat(caseQueryResult, is(notNullValue()));
        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }

    @Test
    public void shouldReturnSearchCasesResultSortByOffenceDescription() {
        final CpsQueryParameters queryParameters = mock(CpsQueryParameters.class);

        final CpsCaseSortBy sortByOffence = CpsCaseSortBy.OFFENCE_DESCRIPTION;
        final FieldSortBuilder fieldSortBuilder = fieldSort(sortByOffence.getFieldName())
                .missing(missing_last)
                .order(SortOrder.DESC)
                .setNestedSort(new NestedSortBuilder(sortByOffence.getNestedPath().orElse(null)));

        when(queryParameters.getOrderBy()).thenReturn(sortByOffence.getKeyName());
        when(queryParameters.getOrder()).thenReturn(SortOrder.DESC);

        when(unifiedSearchQueryBuilderService.builder(queryParameters))
                .thenReturn(boolQueryBuilder);

        when(unifiedSearchService.search(boolQueryBuilder,
                CPS_CASE_INDEX_NAME,
                Case.class,
                RESULT_HIT_NODE_NAME,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                fieldSortBuilder))
                .thenReturn(response);

        final JsonObject caseQueryResult = cpsCaseQueryService.searchCases(queryParameters);
        assertThat(caseQueryResult, is(notNullValue()));
        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }

    @Test
    public void shouldReturnSearchCasesResultSortByDefendantLastName() {
        final CpsQueryParameters queryParameters = mock(CpsQueryParameters.class);

        final CpsCaseSortBy sortByOffence = CpsCaseSortBy.DEFENDANT_LASTNAME;
        final FieldSortBuilder fieldSortBuilder = fieldSort(sortByOffence.getFieldName())
                .missing(missing_last)
                .order(SortOrder.DESC)
                .setNestedSort(new NestedSortBuilder(sortByOffence.getNestedPath().orElse(null)));

        when(queryParameters.getOrderBy()).thenReturn(sortByOffence.getKeyName());
        when(queryParameters.getOrder()).thenReturn(SortOrder.DESC);

        when(unifiedSearchQueryBuilderService.builder(queryParameters))
                .thenReturn(boolQueryBuilder);

        when(unifiedSearchService.search(boolQueryBuilder,
                CPS_CASE_INDEX_NAME,
                Case.class,
                RESULT_HIT_NODE_NAME,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                fieldSortBuilder))
                .thenReturn(response);

        final JsonObject caseQueryResult = cpsCaseQueryService.searchCases(queryParameters);
        assertThat(caseQueryResult, is(notNullValue()));
        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }
}