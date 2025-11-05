package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTY_TYPE_NESTED_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.QueryBuilder;

public class PartyTypeSearchQueryBuilder implements ElasticSearchQueryBuilder {


    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        final String[] partyTypes = clean(queryParams[0]);
        return termsQuery(PARTY_TYPE_NESTED_PATH, partyTypes);
    }
}
