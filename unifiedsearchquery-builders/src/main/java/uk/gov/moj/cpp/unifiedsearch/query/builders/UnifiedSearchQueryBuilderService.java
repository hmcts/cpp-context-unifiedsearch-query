package uk.gov.moj.cpp.unifiedsearch.query.builders;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public interface UnifiedSearchQueryBuilderService<T> {

    Query.Builder builder(final T queryParameters);
}
