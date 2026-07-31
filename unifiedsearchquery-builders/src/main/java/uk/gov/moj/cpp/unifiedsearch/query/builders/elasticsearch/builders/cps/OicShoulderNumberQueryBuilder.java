package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static java.lang.String.valueOf;

import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTIES_OIC_SHOULDER_NUMBER_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class OicShoulderNumberQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        return termQuery(PARTIES_OIC_SHOULDER_NUMBER_PATH, valueOf(queryParams[0])).build();
    }
}
