package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static java.lang.String.valueOf;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.matchQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OPERATION_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OPERATION_NAME_PATH_NGRAMMED;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class OperationNameQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParam) {

        return convertBuilder((new BoolQuery.Builder())
                .should(matchQuery(OPERATION_NAME, valueOf(queryParam[0])).build())
                .should(matchQuery(OPERATION_NAME_PATH_NGRAMMED, valueOf(queryParam[0])).build())).build();
    }
}
