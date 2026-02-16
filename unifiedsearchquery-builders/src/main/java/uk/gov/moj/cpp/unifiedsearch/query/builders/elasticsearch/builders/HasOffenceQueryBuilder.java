package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.valueOf;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.OFFENCES_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.nestedQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.ExistsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class HasOffenceQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final boolean hasOffence = parseBoolean(valueOf(queryParams[0]));
        final BoolQuery.Builder offencesInnerBoolWrapper =new BoolQuery.Builder();
        final ExistsQuery.Builder existsQueryBuilder = new ExistsQuery.Builder();
        if (hasOffence) {
            offencesInnerBoolWrapper.must(existsQueryBuilder.field(OFFENCES_NESTED_PATH).build());
        } else {
            offencesInnerBoolWrapper.mustNot(existsQueryBuilder.field(OFFENCES_NESTED_PATH).build());
        }
        final Query.Builder queryBuilder = new Query.Builder();
        queryBuilder.bool(offencesInnerBoolWrapper.build());
        return convertBuilder(nestedQuery(OFFENCES_NESTED_PATH,  queryBuilder)).build();
    }
}
