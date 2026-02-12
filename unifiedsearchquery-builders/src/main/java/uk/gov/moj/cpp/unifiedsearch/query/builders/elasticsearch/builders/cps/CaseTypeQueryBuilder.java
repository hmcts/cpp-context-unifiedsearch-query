package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CASE_TYPE;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class CaseTypeQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParam) {
        final Query.Builder builder = new Query.Builder();
        builder.bool(b -> b.filter(termQuery(CASE_TYPE, String.valueOf(queryParam[0])).build()));
        return builder.build();
    }
}
