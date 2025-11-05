package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_TYPE_ID_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.QueryBuilder;

public class HearingTypeQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        final String hearingType = valueOf(queryParams[0]);
        return termQuery(HEARING_TYPE_ID_PATH, hearingType);

    }
}
