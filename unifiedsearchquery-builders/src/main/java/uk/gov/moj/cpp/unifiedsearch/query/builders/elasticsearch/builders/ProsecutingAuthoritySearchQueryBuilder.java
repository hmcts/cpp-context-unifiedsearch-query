package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PROSECUTING_AUTHORITY;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class ProsecutingAuthoritySearchQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParam) {
        return convertBuilder((new BoolQuery.Builder())
                .filter(termQuery(PROSECUTING_AUTHORITY, valueOf(queryParam[0])).build())).build();
    }
}
