package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.COURT_ROOM_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.QueryBuilder;

public class CourtRoomQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        return termQuery(COURT_ROOM_PATH, valueOf(queryParams[0]));
    }
}
