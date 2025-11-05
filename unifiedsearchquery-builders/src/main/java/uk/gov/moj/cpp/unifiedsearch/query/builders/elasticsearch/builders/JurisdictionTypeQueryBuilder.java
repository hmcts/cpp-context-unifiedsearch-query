package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_CROWN_COURT;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_MAGISTRATE_COURT;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_SJP;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

public class JurisdictionTypeQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        final boolean sjp = parseBoolean(valueOf(queryParams[0]));
        final boolean magistrateCourt = parseBoolean(valueOf(queryParams[1]));
        final boolean crownCourt = parseBoolean(valueOf(queryParams[2]));

        final BoolQueryBuilder booleanQueryBuilder = boolQuery();

        addShouldClauseIfParamPresentAndTrue(IS_SJP, sjp, booleanQueryBuilder);
        addShouldClauseIfParamPresentAndTrue(IS_MAGISTRATE_COURT, magistrateCourt, booleanQueryBuilder);
        addShouldClauseIfParamPresentAndTrue(IS_CROWN_COURT, crownCourt, booleanQueryBuilder);

        return booleanQueryBuilder;
    }

    private void addShouldClauseIfParamPresentAndTrue(final String fieldName, final boolean queryParam, final BoolQueryBuilder boolQueryBuilder){
        if (queryParam) {
            boolQueryBuilder.should(termQuery(fieldName, true));
        }
    }
}
