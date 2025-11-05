package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PNC_ID_INDEX;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.QueryBuilder;

public class PncIdQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        final String pncId = valueOf(queryParams[0]);
        return termQuery(PNC_ID_INDEX, pncId);
    }
}


