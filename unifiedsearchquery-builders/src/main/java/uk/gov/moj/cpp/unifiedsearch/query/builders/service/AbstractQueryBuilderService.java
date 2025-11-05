package uk.gov.moj.cpp.unifiedsearch.query.builders.service;

import static java.util.stream.Collectors.toList;
import static org.apache.lucene.search.join.ScoreMode.Avg;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilderCache;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.QueryBuilder;

public abstract class AbstractQueryBuilderService {

    @Inject
    protected ElasticSearchQueryBuilderCache elasticSearchQueryBuilderCache;

    protected void addFilterToQueryIfPresent(final String param, final String queryBuilderCacheKey,
                                           final BoolQueryBuilder queryBuilder) {
        if (!param.isEmpty()) {
            queryBuilder.filter(getQueryBuilder(queryBuilderCacheKey, param));
        }
    }

    protected QueryBuilder getQueryBuilder(final String queryBuilderCacheKey,
                                         final Object... params) {
        final ElasticSearchQueryBuilder unifiedSearchQueryBuilderBy = elasticSearchQueryBuilderCache
                .getQueryBuilderFromCacheBy(queryBuilderCacheKey);
        return unifiedSearchQueryBuilderBy.getQueryBuilderBy(params);
    }

    protected QueryBuilder createAdditionalMustFilterWithInnerHits(final String nestedPath,
                                                                 final List<QueryBuilder> allInnerQueries) {
        if (allInnerQueries.isEmpty()) {
            return null;
        }

        final BoolQueryBuilder innerBooleanQueryBuilder = boolQuery();
        allInnerQueries.forEach(innerBooleanQueryBuilder::must);
        return nestedQuery(nestedPath, innerBooleanQueryBuilder, Avg)
                .innerHit(new InnerHitBuilder("parties"));
    }

    protected QueryBuilder createAdditionalMustFilter(final String nestedPath,
                                                    final List<QueryBuilder> allInnerQueries) {
        if (allInnerQueries.isEmpty()) {
            return null;
        }

        final BoolQueryBuilder innerBooleanQueryBuilder = boolQuery();
        allInnerQueries.forEach(innerBooleanQueryBuilder::must);
        return nestedQuery(nestedPath, innerBooleanQueryBuilder, Avg);
    }

    protected List<QueryBuilder> createFilters(final Map<String, Object> parameterAndValues) {
        return parameterAndValues.entrySet().stream().
                filter(entry -> !hasNoValue(entry.getValue())).
                map(entry -> getQueryBuilder(entry.getKey(), entry.getValue())).
                collect(toList());
    }


    protected void addMustToQuery(final String queryBuilderCacheKey, final BoolQueryBuilder queryBuilder, final Object... params) {
        queryBuilder.must(getQueryBuilder(queryBuilderCacheKey, params));
    }

    protected void addMustNotToQuery(final String queryBuilderCacheKey, final BoolQueryBuilder queryBuilder, final Object... params) {
        queryBuilder.mustNot(getQueryBuilder(queryBuilderCacheKey, params));
    }

    protected void addFilterToQuery(final String queryBuilderCacheKey, final BoolQueryBuilder queryBuilder, final Object... params) {
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
