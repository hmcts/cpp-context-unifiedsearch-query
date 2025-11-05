package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_CROWN_COURT;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_MAGISTRATE_COURT;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

public class CrownOrMagistratesQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        final boolean crownOrMagistrates = parseBoolean(valueOf(queryParams[0]));
        final BoolQueryBuilder booleanQueryBuilder = boolQuery();

        if (crownOrMagistrates) {
            /*should means: At least one of these clauses must match, like logical OR*/
            booleanQueryBuilder.should(termQuery(IS_CROWN_COURT, true));
            booleanQueryBuilder.should(termQuery(IS_MAGISTRATE_COURT, true));
        } else {
            booleanQueryBuilder.must(termQuery(IS_CROWN_COURT, false));
            booleanQueryBuilder.must(termQuery(IS_MAGISTRATE_COURT, false));
        }
        return booleanQueryBuilder;
    }
}
