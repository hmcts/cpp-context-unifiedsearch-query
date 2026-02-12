package uk.gov.moj.cpp.unifiedsearch.query.api.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
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

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CpsCaseSearchServiceTest {

    @Mock
    private UnifiedSearchService unifiedSearchService;

    @Mock
    private Query.Builder boolQuery;

    @Mock
    private JsonObject response;

    @Mock
    private UnifiedSearchQueryBuilderService unifiedSearchQueryBuilderService;

    @InjectMocks
    private CpsCaseSearchService cpsCaseQueryService;

    private final String missing_last = "_last";

    @Captor
    private ArgumentCaptor<SortOptions> sortOptionsArgumentCaptor;

    @Test
    public void shouldReturnSearchCasesResult() {
        final CpsQueryParameters queryParameters = mock(CpsQueryParameters.class);
        final SortOptions sortOptions = SortOptions.of(s -> s
                .field(f -> f
                        .field(URN)
                        .missing(missing_last)
                        .order(SortOrder.Asc)
                )
        );

        when(unifiedSearchQueryBuilderService.builder(queryParameters))
                .thenReturn(boolQuery);
        when(unifiedSearchService.search(eq(boolQuery),
                eq(CPS_CASE_INDEX_NAME),
                eq(Case.class),
                eq(RESULT_HIT_NODE_NAME),
                anyInt(),
                anyInt(),
                sortOptionsArgumentCaptor.capture()))
                .thenReturn(response);

        final JsonObject caseQueryResult = cpsCaseQueryService.searchCases(queryParameters);
        assertThat(caseQueryResult, is(notNullValue()));
        SortOptions actual = sortOptionsArgumentCaptor.getValue();
        assertThat(actual.toString(), is(sortOptions.toString()));
        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }

    @Test
    public void shouldReturnSearchCasesResultSortByCaseStatus() {
        final CpsQueryParameters queryParameters = mock(CpsQueryParameters.class);
        final SortOptions sortOptions = SortOptions.of(s -> s
                .field(f -> f
                        .field(CpsCaseSortBy.STATUS.getFieldName())
                        .missing(missing_last)
                        .order(SortOrder.Desc)
                )
        );

        when(queryParameters.getOrderBy()).thenReturn(CpsCaseSortBy.STATUS.getKeyName());
        when(queryParameters.getOrder()).thenReturn(uk.gov.moj.cpp.unifiedsearch.query.common.constant.SortOrder.Desc);

        when(unifiedSearchQueryBuilderService.builder(queryParameters))
                .thenReturn(boolQuery);

        when(unifiedSearchService.search(eq(boolQuery),
                eq(CPS_CASE_INDEX_NAME),
                eq(Case.class),
                eq(RESULT_HIT_NODE_NAME),
                anyInt(),
                anyInt(),
                sortOptionsArgumentCaptor.capture()))
                .thenReturn(response);

        final JsonObject caseQueryResult = cpsCaseQueryService.searchCases(queryParameters);
        assertThat(caseQueryResult, is(notNullValue()));
        SortOptions actual = sortOptionsArgumentCaptor.getValue();
        assertThat(actual.toString(), is(sortOptions.toString()));
        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }

    @Test
    public void shouldReturnSearchCasesResultSortByDateOfBirth() {
        final CpsQueryParameters queryParameters = mock(CpsQueryParameters.class);

        final CpsCaseSortBy sortByDateOfBirth = CpsCaseSortBy.DATE_OF_BIRTH;
        final SortOptions sortOptions = SortOptions.of(s -> s
                .field(f -> f
                        .field(sortByDateOfBirth.getFieldName())
                        .missing(missing_last)
                        .order(SortOrder.Asc)
                        .nested(n -> n
                                .path(sortByDateOfBirth.getNestedPath().orElse(null))
                        )
                )
        );

        when(queryParameters.getOrderBy()).thenReturn(sortByDateOfBirth.getKeyName());
        when(queryParameters.getOrder()).thenReturn(uk.gov.moj.cpp.unifiedsearch.query.common.constant.SortOrder.Asc);

        when(unifiedSearchQueryBuilderService.builder(queryParameters))
                .thenReturn(boolQuery);

        when(unifiedSearchService.search(eq(boolQuery),
                eq(CPS_CASE_INDEX_NAME),
                eq(Case.class),
                eq(RESULT_HIT_NODE_NAME),
                anyInt(),
                anyInt(),
                sortOptionsArgumentCaptor.capture()))
                .thenReturn(response);

        final JsonObject caseQueryResult = cpsCaseQueryService.searchCases(queryParameters);
        assertThat(caseQueryResult, is(notNullValue()));
        SortOptions actual = sortOptionsArgumentCaptor.getValue();
        assertThat(actual.toString(), is(sortOptions.toString()));
        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }

    @Test
    public void shouldReturnSearchCasesResultSortByHearingType() {
        final CpsQueryParameters queryParameters = mock(CpsQueryParameters.class);

        final CpsCaseSortBy sortByHearingType = CpsCaseSortBy.HEARING_TYPE;
        final SortOptions sortOptions = SortOptions.of(s -> s
                .field(f -> {
                    f.field(sortByHearingType.getFieldName())
                     .missing(missing_last)
                     .order(SortOrder.Desc);
                    f.nested(n -> {
                        n.path(sortByHearingType.getNestedPath().orElse(null));
                        sortByHearingType.getNestedFilter().ifPresent(n::filter);
                        sortByHearingType.getNestedMaxChildren().ifPresent(n::maxChildren);
                        return n;
                    });
                    return f;
                })
        );

        when(queryParameters.getOrderBy()).thenReturn(sortByHearingType.getKeyName());
        when(queryParameters.getOrder()).thenReturn(uk.gov.moj.cpp.unifiedsearch.query.common.constant.SortOrder.Desc);

        when(unifiedSearchQueryBuilderService.builder(queryParameters))
                .thenReturn(boolQuery);

        when(unifiedSearchService.search(eq(boolQuery),
                eq(CPS_CASE_INDEX_NAME),
                eq(Case.class),
                eq(RESULT_HIT_NODE_NAME),
                anyInt(),
                anyInt(),
                sortOptionsArgumentCaptor.capture()))
                .thenReturn(response);

        final JsonObject caseQueryResult = cpsCaseQueryService.searchCases(queryParameters);
        assertThat(caseQueryResult, is(notNullValue()));
        SortOptions actual = sortOptionsArgumentCaptor.getValue();
        assertThat(actual.toString(), is(sortOptions.toString()));
        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }

    @Test
    public void shouldReturnSearchCasesResultSortByProsecutor() {
        final CpsQueryParameters queryParameters = mock(CpsQueryParameters.class);

        final CpsCaseSortBy sortByProsecutor = CpsCaseSortBy.PROSECUTOR;
        final SortOptions sortOptions = SortOptions.of(s -> s
                .field(f -> f
                        .field(sortByProsecutor.getFieldName())
                        .missing(missing_last)
                        .order(SortOrder.Asc)
                )
        );

        when(queryParameters.getOrderBy()).thenReturn(sortByProsecutor.getKeyName());
        when(queryParameters.getOrder()).thenReturn(uk.gov.moj.cpp.unifiedsearch.query.common.constant.SortOrder.Asc);

        when(unifiedSearchQueryBuilderService.builder(queryParameters))
                .thenReturn(boolQuery);

        when(unifiedSearchService.search(eq(boolQuery),
                eq(CPS_CASE_INDEX_NAME),
                eq(Case.class),
                eq(RESULT_HIT_NODE_NAME),
                anyInt(),
                anyInt(),
                sortOptionsArgumentCaptor.capture()))
                .thenReturn(response);

        final JsonObject caseQueryResult = cpsCaseQueryService.searchCases(queryParameters);
        assertThat(caseQueryResult, is(notNullValue()));
        SortOptions actual = sortOptionsArgumentCaptor.getValue();
        assertThat(actual.toString(), is(sortOptions.toString()));
        verify(unifiedSearchQueryBuilderService).builder(queryParameters);

    }

    @Test
    public void shouldReturnSearchCasesResultSortByOffenceDescription() {
        final CpsQueryParameters queryParameters = mock(CpsQueryParameters.class);

        final CpsCaseSortBy sortByOffence = CpsCaseSortBy.OFFENCE_DESCRIPTION;
        final SortOptions sortOptions = SortOptions.of(s -> s
                .field(f -> f
                        .field(sortByOffence.getFieldName())
                        .missing(missing_last)
                        .order(SortOrder.Desc)
                        .nested(n -> n
                                .path(sortByOffence.getNestedPath().orElse(null))
                        )
                )
        );

        when(queryParameters.getOrderBy()).thenReturn(sortByOffence.getKeyName());
        when(queryParameters.getOrder()).thenReturn(uk.gov.moj.cpp.unifiedsearch.query.common.constant.SortOrder.Desc);

        when(unifiedSearchQueryBuilderService.builder(queryParameters))
                .thenReturn(boolQuery);

        when(unifiedSearchService.search(eq(boolQuery),
                eq(CPS_CASE_INDEX_NAME),
                eq(Case.class),
                eq(RESULT_HIT_NODE_NAME),
                anyInt(),
                anyInt(),
                sortOptionsArgumentCaptor.capture()))
                .thenReturn(response);

        final JsonObject caseQueryResult = cpsCaseQueryService.searchCases(queryParameters);
        assertThat(caseQueryResult, is(notNullValue()));
        SortOptions actual = sortOptionsArgumentCaptor.getValue();
        assertThat(actual.toString(), is(sortOptions.toString()));
        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }

    @Test
    public void shouldReturnSearchCasesResultSortByDefendantLastName() {
        final CpsQueryParameters queryParameters = mock(CpsQueryParameters.class);

        final CpsCaseSortBy sortByOffence = CpsCaseSortBy.DEFENDANT_LASTNAME;
        final SortOptions sortOptions = SortOptions.of(s -> s
                .field(f -> f
                        .field(sortByOffence.getFieldName())
                        .missing(missing_last)
                        .order(SortOrder.Desc)
                        .nested(n -> n
                                .path(sortByOffence.getNestedPath().orElse(null))
                        )
                )
        );

        when(queryParameters.getOrderBy()).thenReturn(sortByOffence.getKeyName());
        when(queryParameters.getOrder()).thenReturn(uk.gov.moj.cpp.unifiedsearch.query.common.constant.SortOrder.Desc);

        when(unifiedSearchQueryBuilderService.builder(queryParameters))
                .thenReturn(boolQuery);

        when(unifiedSearchService.search(eq(boolQuery),
                eq(CPS_CASE_INDEX_NAME),
                eq(Case.class),
                eq(RESULT_HIT_NODE_NAME),
                anyInt(),
                anyInt(),
                sortOptionsArgumentCaptor.capture()))
                .thenReturn(response);

        final JsonObject caseQueryResult = cpsCaseQueryService.searchCases(queryParameters);
        assertThat(caseQueryResult, is(notNullValue()));
        SortOptions actual = sortOptionsArgumentCaptor.getValue();
        assertThat(actual.toString(), is(sortOptions.toString()));
        verify(unifiedSearchQueryBuilderService).builder(queryParameters);
    }
}