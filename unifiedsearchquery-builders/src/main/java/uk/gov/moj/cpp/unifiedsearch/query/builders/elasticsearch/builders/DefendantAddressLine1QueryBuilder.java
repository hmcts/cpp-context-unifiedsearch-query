package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.matchQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.ADDRESS1_INDEX;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class DefendantAddressLine1QueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final Query.Builder builder = new Query.Builder();
        builder.match(matchQuery(ADDRESS1_INDEX, String.valueOf(queryParams[0])).operator(Operator.And).build());
        return builder.build();
    }
}
