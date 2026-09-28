package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;

import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATION_TYPE_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class ApplicationTypeQueryBuilder implements ElasticSearchQueryBuilder {


    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        return termQuery(APPLICATION_TYPE_PATH, valueOf(queryParams[0])).build();
    }
}
