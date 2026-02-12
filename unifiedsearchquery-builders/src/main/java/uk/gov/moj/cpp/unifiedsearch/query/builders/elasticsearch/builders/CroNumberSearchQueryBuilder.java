package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.CRO_NUMBER_INDEX;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;


public class CroNumberSearchQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final String croNumber = valueOf(queryParams[0]);
        return termQuery(CRO_NUMBER_INDEX, croNumber).build();
    }
}
