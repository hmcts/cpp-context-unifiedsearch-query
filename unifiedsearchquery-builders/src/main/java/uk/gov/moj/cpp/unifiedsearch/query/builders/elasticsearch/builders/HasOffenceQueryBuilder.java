package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.valueOf;
import static org.apache.lucene.search.join.ScoreMode.Avg;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.OFFENCES_NESTED_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

public class HasOffenceQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        final boolean hasOffence = parseBoolean(valueOf(queryParams[0]));
        final BoolQueryBuilder offencesInnerBoolWrapper = boolQuery();

        if (hasOffence) {
            offencesInnerBoolWrapper.must(existsQuery(OFFENCES_NESTED_PATH));
        } else {
            offencesInnerBoolWrapper.mustNot(existsQuery(OFFENCES_NESTED_PATH));
        }
        return nestedQuery(OFFENCES_NESTED_PATH, offencesInnerBoolWrapper, Avg);
    }
}
