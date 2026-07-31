package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static java.lang.String.valueOf;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.nestedQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.LINKED_CASES_ID_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.LINKED_CASES_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.MANUALLY_LINKED_CASES_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;


import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;


public class ExcludeAutomaticallyLinkedCasesQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {

        final BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        final BoolQuery.Builder subQuery = new BoolQuery.Builder();
        subQuery.must(nestedQuery(LINKED_CASES_NESTED_PATH,termQuery(MANUALLY_LINKED_CASES_PATH,"false")).build());
        subQuery.must(nestedQuery(LINKED_CASES_NESTED_PATH,termQuery(LINKED_CASES_ID_PATH,valueOf(queryParams[0]))).build());
        return convertBuilder(boolQueryBuilder.mustNot(subQuery.build())).build();

    }
}
