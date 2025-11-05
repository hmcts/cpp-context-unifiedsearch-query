package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.CASE_STATUS_PARAM;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.QueryBuilder;

public class CaseStatusQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        final String[] caseStatuses = clean(queryParams[0]);
        return termsQuery(CASE_STATUS_PARAM, caseStatuses);
    }

    @Override
    public String[] clean(final Object queryParam) {
        return valueOf(queryParam).replaceAll("[^a-zA-Z0-9,_]", "")
                .split(",");
    }
}
