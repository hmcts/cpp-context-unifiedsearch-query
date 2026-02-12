package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;

import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.COURT_ORDER_END_DATE_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.COURT_ORDER_START_DATE_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.json.JsonData;


public class CourtOrderValidityDateQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final String courtOrderValidityDataQueryParam = valueOf(queryParams[0]);

        return convertBuilder((new BoolQuery.Builder())
                .must(getRangeGTEBuilderQuery(courtOrderValidityDataQueryParam).build())
                .must(getRangeLTEBuilderQuery(courtOrderValidityDataQueryParam).build())).build();
    }

    private RangeQuery.Builder getRangeGTEBuilderQuery(final String courtOrderValidityDataQueryParam) {
        RangeQuery.Builder builder = new RangeQuery.Builder();
        builder.untyped( u -> u
                .field(COURT_ORDER_START_DATE_PATH)
                .lte(JsonData.of(courtOrderValidityDataQueryParam)));
        return builder;
    }

    private RangeQuery.Builder getRangeLTEBuilderQuery(final String courtOrderValidityDataQueryParam) {
        RangeQuery.Builder builder = new RangeQuery.Builder();
        builder.untyped( u -> u
                .field(COURT_ORDER_END_DATE_PATH)
                .gte(JsonData.of(courtOrderValidityDataQueryParam)));
        return builder;
    }
}
