package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.matchQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.POSTCODE;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class PostCodeQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParam) {
        final Query.Builder builder = new Query.Builder();
        builder.match(matchQuery(POSTCODE, String.valueOf(queryParam[0]) ).operator(Operator.And).build());
        return builder.build();
    }
}


