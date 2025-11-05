package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.COURT_ORDER_END_DATE_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.COURT_ORDER_START_DATE_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;

public class CourtOrderValidityDateQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        final String courtOrderValidityDataQueryParam = valueOf(queryParams[0]);

        return boolQuery()
                .must(getRangeGTEBuilderQuery(courtOrderValidityDataQueryParam))
                .must(getRangeLTEBuilderQuery(courtOrderValidityDataQueryParam));
    }

    private RangeQueryBuilder getRangeGTEBuilderQuery(final String courtOrderValidityDataQueryParam) {
        return rangeQuery(COURT_ORDER_START_DATE_PATH).lte(courtOrderValidityDataQueryParam);
    }

    private RangeQueryBuilder getRangeLTEBuilderQuery(final String courtOrderValidityDataQueryParam) {
        return rangeQuery(COURT_ORDER_END_DATE_PATH).gte(courtOrderValidityDataQueryParam);
    }
}
