package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;

import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_DAY_COURTCENTRE_REFERENCE_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class CourtSearchQueryBuilder implements ElasticSearchQueryBuilder {
    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final String courtIdParam = valueOf(queryParams[0]);

        return termQuery(HEARING_DAY_COURTCENTRE_REFERENCE_PATH, courtIdParam).build();

   }
}
