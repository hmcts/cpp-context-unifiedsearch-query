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
import org.elasticsearch.index.query.RegexpQueryBuilder;

public class ReferenceSearchQueryBuilder implements ElasticSearchQueryBuilder {


    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParam) {
        final String caseReferenceValue = queryParam[0].toString();
        final List<QueryBuilder> applicationFilters = (List<QueryBuilder>) queryParam[1];

        final String regexValue = " *" + caseReferenceValue + " *";

        final BoolQueryBuilder applicationInnerBoolWrapper = boolQuery()
                .must(new RegexpQueryBuilder(APPLICATION_REFERENCE_PATH, regexValue));

        applicationFilters.forEach(applicationInnerBoolWrapper::must);

        return boolQuery()
                .should(new RegexpQueryBuilder(CASE_REFERENCE, regexValue))
                .should(nestedQuery(APPLICATIONS_NESTED_PATH, applicationInnerBoolWrapper, Avg));
    }
}
