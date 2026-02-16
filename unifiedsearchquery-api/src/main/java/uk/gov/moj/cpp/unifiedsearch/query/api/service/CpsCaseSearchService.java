package uk.gov.moj.cpp.unifiedsearch.query.api.service;

import static java.util.Objects.isNull;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.RESULT_HIT_NODE_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.URN;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSortBy.findByKeyNameOrDefault;

import uk.gov.justice.services.unifiedsearch.UnifiedSearchName;
import uk.gov.justice.services.unifiedsearch.client.search.UnifiedSearchService;
import uk.gov.moj.cpp.unifiedsearch.query.builders.UnifiedSearchQueryBuilderService;
import uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSortBy;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.CpsQueryParameters;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Case;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import org.apache.commons.lang3.StringUtils;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;


@ApplicationScoped
public class CpsCaseSearchService implements BaseCaseSearchService {

    @Inject
    private UnifiedSearchService unifiedSearchService;

    @Inject
    @UnifiedSearchName(CPS_CASE_INDEX_NAME)
    private UnifiedSearchQueryBuilderService unifiedSearchQueryBuilderService;

    public JsonObject searchCases(final CpsQueryParameters queryParameters) {
        final Query.Builder queryBuilder = unifiedSearchQueryBuilderService.builder(queryParameters);
        final Optional<SortOptions> fieldSortBuilder = getSortBuilder(queryParameters);

        return unifiedSearchService.search(
                queryBuilder,
                CPS_CASE_INDEX_NAME,
                Case.class,
                RESULT_HIT_NODE_NAME,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                fieldSortBuilder.orElse(null));
    }

    private Optional<SortOptions> getSortBuilder(final CpsQueryParameters queryParameters) {
        if (StringUtils.isNotEmpty(queryParameters.getOrderBy())) {
            final CpsCaseSortBy cpsCaseSortBy = findByKeyNameOrDefault(queryParameters.getOrderBy());

            final String orderNameOrDefault = getOrderNameOrDefault(queryParameters.getOrder());
            return createFieldSort(orderNameOrDefault,
                    cpsCaseSortBy.getFieldName(),
                    cpsCaseSortBy.getNestedPath().orElse(null),
                    cpsCaseSortBy.getNestedFilter().orElse(null),
                    cpsCaseSortBy.getNestedMaxChildren().orElse(null));
        }

        return createFieldSort(SortOrder.Asc.name(), URN, null);
    }

    private String getOrderNameOrDefault(final uk.gov.moj.cpp.unifiedsearch.query.common.constant.SortOrder order) {
        return isNull(order) ? SortOrder.Asc.name() : order.getOrder();
    }
}
