package uk.gov.moj.cpp.unifiedsearch.query.builders.service;

import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.nestedQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilderCache;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.search.InnerHits;

public abstract class AbstractQueryBuilderService {

    @Inject
    protected ElasticSearchQueryBuilderCache elasticSearchQueryBuilderCache;

    protected void addFilterToQueryIfPresent(final String param, final String queryBuilderCacheKey,
                                           final BoolQuery.Builder queryBuilder) {
        if (!param.isEmpty()) {
            queryBuilder.filter(getQueryBuilder(queryBuilderCacheKey, param));
        }
    }

    protected Query getQueryBuilder(final String queryBuilderCacheKey,
                                         final Object... params) {
        final ElasticSearchQueryBuilder unifiedSearchQueryBuilderBy = elasticSearchQueryBuilderCache
                .getQueryBuilderFromCacheBy(queryBuilderCacheKey);
        return unifiedSearchQueryBuilderBy.getQueryBuilderBy(params);
    }

    protected Query createAdditionalMustFilterWithInnerHits(final String nestedPath,
                                                                 final List<Query> allInnerQueries) {
        if (allInnerQueries.isEmpty()) {
            return null;
        }

        final BoolQuery.Builder innerBooleanQueryBuilder = new BoolQuery.Builder();
        allInnerQueries.forEach(innerBooleanQueryBuilder::must);
        return convertBuilder(nestedQuery(nestedPath, convertBuilder(innerBooleanQueryBuilder))
                .innerHits(InnerHits.of(i -> i.name("parties")))).build();
    }

    protected Query createAdditionalMustFilter(final String nestedPath,
                                                    final List<Query> allInnerQueries) {
        if (allInnerQueries.isEmpty()) {
            return null;
        }

        final BoolQuery.Builder innerBooleanQueryBuilder = new BoolQuery.Builder();
        allInnerQueries.forEach(innerBooleanQueryBuilder::must);
        return convertBuilder(nestedQuery(nestedPath, convertBuilder(innerBooleanQueryBuilder))).build();
    }

    protected List<Query> createFilters(final Map<String, Object> parameterAndValues) {
        return parameterAndValues.entrySet().stream().
                filter(entry -> !hasNoValue(entry.getValue())).
                map(entry -> getQueryBuilder(entry.getKey(), entry.getValue())).
                collect(toList());
    }


    protected void addMustToQuery(final String queryBuilderCacheKey, final BoolQuery.Builder queryBuilder, final Object... params) {
        queryBuilder.must(getQueryBuilder(queryBuilderCacheKey, params));
    }

    protected void addMustNotToQuery(final String queryBuilderCacheKey, final BoolQuery.Builder queryBuilder, final Object... params) {
        queryBuilder.mustNot(getQueryBuilder(queryBuilderCacheKey, params));
    }

    protected void addFilterToQuery(final String queryBuilderCacheKey, final BoolQuery.Builder queryBuilder, final Object... params) {
        queryBuilder.filter(getQueryBuilder(queryBuilderCacheKey, params));
    }

    protected boolean hasNoValue(final Object object) {
        if (object == null) {
            return true;
        }

        if (object instanceof String) {
            return object.toString().trim().isEmpty();
        }
        return false;
    }
}
