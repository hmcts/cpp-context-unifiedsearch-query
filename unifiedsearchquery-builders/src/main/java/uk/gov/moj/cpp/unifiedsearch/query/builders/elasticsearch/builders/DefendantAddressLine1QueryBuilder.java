package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.elasticsearch.index.query.Operator.AND;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.ADDRESS1_INDEX;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.QueryBuilder;

public class DefendantAddressLine1QueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        return matchQuery(ADDRESS1_INDEX, queryParams[0]).operator(AND);
    }
}
