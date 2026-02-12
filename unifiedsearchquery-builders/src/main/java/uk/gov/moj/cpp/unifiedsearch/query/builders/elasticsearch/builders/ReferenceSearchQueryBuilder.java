package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.nestedQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATIONS_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATION_REFERENCE_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.CASE_REFERENCE;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import java.util.List;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;


public class ReferenceSearchQueryBuilder implements ElasticSearchQueryBuilder {


    @Override
    public Query getQueryBuilderBy(final Object... queryParam) {
        final String caseReferenceValue = String.valueOf(queryParam[0]);
        final List<Query> applicationFilters = (List<Query>) queryParam[1];

        final BoolQuery.Builder applicationInnerBoolWrapper = (new BoolQuery.Builder())
                .must(termQuery(APPLICATION_REFERENCE_PATH, caseReferenceValue).build());

        applicationFilters.forEach(applicationInnerBoolWrapper::must);

        return convertBuilder((new BoolQuery.Builder())
                .should(termQuery(CASE_REFERENCE, caseReferenceValue).build())
                .should(convertBuilder(nestedQuery(APPLICATIONS_NESTED_PATH, convertBuilder(applicationInnerBoolWrapper))).build())).build();
    }
}
