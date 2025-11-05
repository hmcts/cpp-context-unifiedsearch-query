package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.LAST_NAME_INDEX;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.QueryBuilder;

public class LastNameSearchQueryBuilder implements ElasticSearchQueryBuilder {
    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        final String lastName = valueOf(queryParams[0]);
        return termQuery(LAST_NAME_INDEX, lastName);
    }
}
