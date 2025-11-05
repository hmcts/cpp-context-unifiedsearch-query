package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.elasticsearch.index.query.Operator.AND;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.QueryBuilder;

public class AddressQueryBuilder implements ElasticSearchQueryBuilder {

    private static final String ADDRESS_LINES = "parties.addressLines";

    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        return matchQuery(ADDRESS_LINES, queryParams[0]).operator(AND);
    }
}
