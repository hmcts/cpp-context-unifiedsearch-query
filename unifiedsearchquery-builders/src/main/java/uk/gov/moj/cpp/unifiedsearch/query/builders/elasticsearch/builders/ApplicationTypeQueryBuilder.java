package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATION_TYPE_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.QueryBuilder;

public class ApplicationTypeQueryBuilder implements ElasticSearchQueryBuilder {


    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        return termQuery(APPLICATION_TYPE_PATH, valueOf(queryParams[0]));
    }
}
