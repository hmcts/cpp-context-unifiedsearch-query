package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTIES_NINO_REFERENCE_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.QueryBuilder;

public class NinoSearchQueryBuilder implements ElasticSearchQueryBuilder {


    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        final String ninoQueryParam = valueOf(queryParams[0]);
        return termQuery(PARTIES_NINO_REFERENCE_PATH, ninoQueryParam);
    }
}
