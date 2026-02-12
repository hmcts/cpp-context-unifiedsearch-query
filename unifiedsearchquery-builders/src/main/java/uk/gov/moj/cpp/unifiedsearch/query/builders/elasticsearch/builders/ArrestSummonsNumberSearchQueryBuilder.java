package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;

import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTIES_ASN_REFERENCE_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class ArrestSummonsNumberSearchQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final String asnQueryParam = valueOf(queryParams[0]);
        return termQuery(PARTIES_ASN_REFERENCE_PATH, asnQueryParam).build();
    }
}
