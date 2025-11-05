package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch;

import static java.lang.String.valueOf;

import org.elasticsearch.index.query.QueryBuilder;

public interface ElasticSearchQueryBuilder {
    QueryBuilder getQueryBuilderBy(final Object... queryParams);

    default String[] clean(final Object queryParam) {
        return valueOf(queryParam).replaceAll("\\s", "").split(",");
    }
}
