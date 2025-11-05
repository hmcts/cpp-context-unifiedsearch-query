package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_DAY_COURTCENTRE_REFERENCE_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.QueryBuilder;

public class CourtSearchQueryBuilder implements ElasticSearchQueryBuilder {
    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        final String courtIdParam = valueOf(queryParams[0]);

        return termQuery(HEARING_DAY_COURTCENTRE_REFERENCE_PATH, courtIdParam);

   }
}
