package uk.gov.moj.cpp.unifiedsearch.query.builders;

import org.elasticsearch.index.query.QueryBuilder;

public interface UnifiedSearchQueryBuilderService<T> {

    QueryBuilder builder(final T queryParameters);
}
