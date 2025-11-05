package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PROCEEDINGS_CONCLUDED_INDEX;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

public class ProceedingsConcludedQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        final String proceedingsConcluded = valueOf(queryParams[0]);
        final BoolQueryBuilder booleanQueryBuilder = boolQuery();

        if ("false".equalsIgnoreCase(proceedingsConcluded)) {
            booleanQueryBuilder.should(termQuery(PROCEEDINGS_CONCLUDED_INDEX, proceedingsConcluded));
            booleanQueryBuilder.should(mustNot());
            return booleanQueryBuilder;
        }
        return boolQuery().must(termQuery(PROCEEDINGS_CONCLUDED_INDEX, proceedingsConcluded));
    }

    private QueryBuilder mustNot() {
        final BoolQueryBuilder booleanQueryBuilder2 = boolQuery();
        return  booleanQueryBuilder2.mustNot(existsQuery(PROCEEDINGS_CONCLUDED_INDEX));
    }
}
