package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;

import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.FIRST_NAME_INDEX;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class FirstNameSearchQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final String firstName = valueOf(queryParams[0]);
        return termQuery(FIRST_NAME_INDEX, firstName).build();
    }
}
