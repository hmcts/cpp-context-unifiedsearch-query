package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;



import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.matchQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class AddressQueryBuilder implements ElasticSearchQueryBuilder {

    private static final String ADDRESS_LINES = "parties.addressLines";

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        Query.Builder builder = new Query.Builder();
        builder.match(matchQuery(ADDRESS_LINES, String.valueOf(queryParams[0])).operator(Operator.And).build());
        return builder.build();
    }
}
