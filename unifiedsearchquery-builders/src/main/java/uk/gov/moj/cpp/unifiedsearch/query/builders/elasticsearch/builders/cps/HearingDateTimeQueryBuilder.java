package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static java.lang.String.valueOf;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.utils.HearingDateUtil.stringToDate;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.HEARING_DATE_TIME_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;

public class HearingDateTimeQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final String hearingDateFromQueryParam = valueOf(queryParams[0]);
        final String hearingDateToQueryParam = valueOf(queryParams[1]);

        if (!hearingDateFromQueryParam.isEmpty() && hearingDateToQueryParam.isEmpty()) {
            return termQuery(HEARING_DATE_TIME_PATH, hearingDateFromQueryParam).build();

        }

        if (!hearingDateToQueryParam.isEmpty() && hearingDateFromQueryParam.isEmpty()) {
            return termQuery(HEARING_DATE_TIME_PATH, hearingDateToQueryParam).build();
        }

        return getRangeBuilderQuery(hearingDateFromQueryParam, hearingDateToQueryParam).build();
    }

    private Query.Builder getRangeBuilderQuery(final String hearingDateFromQueryParam, final String hearingDateToQueryParam) {
        final Query.Builder builder = new Query.Builder();
        builder.range(r -> r
                .untyped(u -> u
                        .field(HEARING_DATE_TIME_PATH)
                        .gte(JsonData.of(stringToDate(hearingDateFromQueryParam)))
                        .lte(JsonData.of(stringToDate(hearingDateToQueryParam)))
                ));
        return builder;
    }
}
