package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;

import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_DAY_REFERENCE_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.json.JsonData;


public class HearingDateSearchQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final String hearingDateFromQueryParam = valueOf(queryParams[0]);
        final String hearingDateToQueryParam = valueOf(queryParams[1]);

        if (!hearingDateFromQueryParam.isEmpty() && hearingDateToQueryParam.isEmpty()) {
            return getTermBuilderQuery(hearingDateFromQueryParam).build();

        }

        if (!hearingDateToQueryParam.isEmpty() && hearingDateFromQueryParam.isEmpty()) {
            return getTermBuilderQuery(hearingDateToQueryParam).build();
        }

        return convertBuilder(getRangeBuilderQuery(hearingDateFromQueryParam, hearingDateToQueryParam)).build();
    }

    private RangeQuery.Builder getRangeBuilderQuery(final String hearingDateFromQueryParam, final String hearingDateToQueryParam) {
        RangeQuery.Builder builder = new RangeQuery.Builder();
        builder.untyped( u -> u
                .field(HEARING_DAY_REFERENCE_PATH)
                .gte(JsonData.of(hearingDateFromQueryParam))
                .lte(JsonData.of(hearingDateToQueryParam)));
        return builder;
    }

    private Query.Builder getTermBuilderQuery(final String queryParam) {
        return termQuery(HEARING_DAY_REFERENCE_PATH, queryParam);
    }
}
