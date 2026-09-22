package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.JURISDICTION_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termsQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class JurisdictionQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final String[] jurisdiction = clean(queryParams[0]);
        return termsQuery(JURISDICTION_PATH, jurisdiction).build();
    }
}
