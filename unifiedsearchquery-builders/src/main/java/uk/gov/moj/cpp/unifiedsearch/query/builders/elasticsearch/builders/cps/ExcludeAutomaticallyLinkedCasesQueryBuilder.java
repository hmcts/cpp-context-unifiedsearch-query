package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static java.lang.String.valueOf;
import static org.apache.lucene.search.join.ScoreMode.Avg;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.LINKED_CASES_ID_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.LINKED_CASES_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.MANUALLY_LINKED_CASES_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class ExcludeAutomaticallyLinkedCasesQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {

        final BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        final BoolQueryBuilder subQuery = QueryBuilders.boolQuery();
        subQuery.must(nestedQuery(LINKED_CASES_NESTED_PATH,QueryBuilders.termQuery(MANUALLY_LINKED_CASES_PATH,"false"),Avg));
        subQuery.must(nestedQuery(LINKED_CASES_NESTED_PATH,QueryBuilders.termQuery(LINKED_CASES_ID_PATH,valueOf(queryParams[0])),Avg));
        return boolQueryBuilder.mustNot(subQuery);

    }
}
