package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static java.lang.String.valueOf;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.COURT_HOUSE_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class CourtHouseQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        return termQuery(COURT_HOUSE_PATH, valueOf(queryParams[0])).build();
    }
}
