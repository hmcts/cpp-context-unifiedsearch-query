package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static java.lang.String.valueOf;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CJS_AREA;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class CjsAreaQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParam) {
        return convertBuilder((new BoolQuery.Builder())
                .filter(termQuery(CJS_AREA, valueOf(queryParam[0])).build())).build();
    }
}
