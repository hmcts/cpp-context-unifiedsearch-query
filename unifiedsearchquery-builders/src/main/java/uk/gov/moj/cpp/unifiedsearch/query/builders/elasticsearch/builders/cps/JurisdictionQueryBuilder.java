package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.JURISDICTION_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.QueryBuilder;

public class JurisdictionQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        final String[] jurisdiction = clean(queryParams[0]);
        return termsQuery(JURISDICTION_PATH, jurisdiction);
    }
}
