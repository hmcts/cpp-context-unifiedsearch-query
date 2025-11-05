package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.apache.lucene.search.join.ScoreMode.Avg;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATIONS_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATION_REFERENCE_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.CASE_REFERENCE;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

public class ReferenceSearchQueryBuilder implements ElasticSearchQueryBuilder {


    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParam) {
        final Object caseReferenceValue = queryParam[0];
        final List<QueryBuilder> applicationFilters = (List<QueryBuilder>) queryParam[1];

        final BoolQueryBuilder applicationInnerBoolWrapper = boolQuery()
                .must(new TermQueryBuilder(APPLICATION_REFERENCE_PATH, caseReferenceValue));

        applicationFilters.forEach(applicationInnerBoolWrapper::must);

        return boolQuery()
                .should(new TermQueryBuilder(CASE_REFERENCE, caseReferenceValue))
                .should(nestedQuery(APPLICATIONS_NESTED_PATH, applicationInnerBoolWrapper, Avg));
    }
}
