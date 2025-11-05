package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.utils.HearingDateUtil.stringToDate;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_DAY_REFERENCE_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

public class HearingDateSearchQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        final String hearingDateFromQueryParam = valueOf(queryParams[0]);
        final String hearingDateToQueryParam = valueOf(queryParams[1]);

        if (!hearingDateFromQueryParam.isEmpty() && hearingDateToQueryParam.isEmpty()) {
            return getTermBuilderQuery(hearingDateFromQueryParam);

        }

        if (!hearingDateToQueryParam.isEmpty() && hearingDateFromQueryParam.isEmpty()) {
            return getTermBuilderQuery(hearingDateToQueryParam);
        }

        return getRangeBuilderQuery(hearingDateFromQueryParam, hearingDateToQueryParam);
    }

    private RangeQueryBuilder getRangeBuilderQuery(final String hearingDateFromQueryParam, final String hearingDateToQueryParam) {
        return rangeQuery(HEARING_DAY_REFERENCE_PATH).from(stringToDate(hearingDateFromQueryParam)).to(stringToDate(hearingDateToQueryParam));
    }

    private TermQueryBuilder getTermBuilderQuery(final String queryParam) {
        return termQuery(HEARING_DAY_REFERENCE_PATH, queryParam);
    }
}
