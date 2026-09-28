package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTY_TYPE_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termsQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class PartyTypeSearchQueryBuilder implements ElasticSearchQueryBuilder {


    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final String[] partyTypes = clean(queryParams[0]);
        return termsQuery(PARTY_TYPE_NESTED_PATH, partyTypes).build();
    }
}
