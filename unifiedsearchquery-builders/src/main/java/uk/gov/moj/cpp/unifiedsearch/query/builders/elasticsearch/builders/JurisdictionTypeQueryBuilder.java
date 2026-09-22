package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.valueOf;

import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_CROWN_COURT;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_MAGISTRATE_COURT;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_SJP;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class JurisdictionTypeQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final boolean sjp = parseBoolean(valueOf(queryParams[0]));
        final boolean magistrateCourt = parseBoolean(valueOf(queryParams[1]));
        final boolean crownCourt = parseBoolean(valueOf(queryParams[2]));

        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();

        addShouldClauseIfParamPresentAndTrue(IS_SJP, sjp, booleanQueryBuilder);
        addShouldClauseIfParamPresentAndTrue(IS_MAGISTRATE_COURT, magistrateCourt, booleanQueryBuilder);
        addShouldClauseIfParamPresentAndTrue(IS_CROWN_COURT, crownCourt, booleanQueryBuilder);

        return convertBuilder(booleanQueryBuilder).build();
    }

    private void addShouldClauseIfParamPresentAndTrue(final String fieldName, final boolean queryParam, final BoolQuery.Builder boolQueryBuilder){
        if (queryParam) {
            boolQueryBuilder.should(termQuery(fieldName, true).build());
        }
    }
}
