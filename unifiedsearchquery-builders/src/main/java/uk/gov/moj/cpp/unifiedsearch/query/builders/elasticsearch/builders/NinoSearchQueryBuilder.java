package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;


import static java.lang.String.valueOf;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTIES_NINO_REFERENCE_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class NinoSearchQueryBuilder implements ElasticSearchQueryBuilder {


    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final String ninoQueryParam = valueOf(queryParams[0]);
        return termQuery(PARTIES_NINO_REFERENCE_PATH, ninoQueryParam).build();
    }
}
