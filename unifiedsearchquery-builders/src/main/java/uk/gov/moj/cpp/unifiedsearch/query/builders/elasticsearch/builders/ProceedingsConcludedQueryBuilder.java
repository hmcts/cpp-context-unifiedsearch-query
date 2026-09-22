package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;


import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PROCEEDINGS_CONCLUDED_INDEX;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class ProceedingsConcludedQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final Boolean proceedingsConcluded = (Boolean)queryParams[0];
        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();

        if (!proceedingsConcluded) {
            booleanQueryBuilder.should(termQuery(PROCEEDINGS_CONCLUDED_INDEX, proceedingsConcluded).build());
            booleanQueryBuilder.should(mustNot().build());
            return convertBuilder(booleanQueryBuilder).build();
        }
        return convertBuilder(booleanQueryBuilder.must(termQuery(PROCEEDINGS_CONCLUDED_INDEX, proceedingsConcluded).build())).build();
    }

    private Query.Builder mustNot() {
        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();
        booleanQueryBuilder.mustNot(m -> m.exists(e -> e.field(PROCEEDINGS_CONCLUDED_INDEX)));
        return convertBuilder(booleanQueryBuilder);
    }
}
