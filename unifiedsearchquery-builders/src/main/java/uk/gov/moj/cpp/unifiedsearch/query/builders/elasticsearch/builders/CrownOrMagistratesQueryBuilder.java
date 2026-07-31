package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.valueOf;

import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_CROWN_COURT;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_MAGISTRATE_COURT;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class CrownOrMagistratesQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final boolean crownOrMagistrates = parseBoolean(valueOf(queryParams[0]));
        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();

        if (crownOrMagistrates) {
            /*should means: At least one of these clauses must match, like logical OR*/
            booleanQueryBuilder.should(termQuery(IS_CROWN_COURT, true).build());
            booleanQueryBuilder.should(termQuery(IS_MAGISTRATE_COURT, true).build());
        } else {
            booleanQueryBuilder.must(termQuery(IS_CROWN_COURT, false).build());
            booleanQueryBuilder.must(termQuery(IS_MAGISTRATE_COURT, false).build());
        }
        return convertBuilder(booleanQueryBuilder).build();
    }
}
