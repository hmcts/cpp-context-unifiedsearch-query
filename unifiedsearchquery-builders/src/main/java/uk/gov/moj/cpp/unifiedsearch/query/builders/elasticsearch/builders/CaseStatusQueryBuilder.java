package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.CASE_STATUS_PARAM;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termsQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class CaseStatusQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final String[] caseStatuses = clean(queryParams[0]);
        return termsQuery(CASE_STATUS_PARAM, caseStatuses).build();
    }

    @Override
    public String[] clean(final Object queryParam) {
        return valueOf(queryParam).replaceAll("[^a-zA-Z0-9,_]", "")
                .split(",");
    }
}
