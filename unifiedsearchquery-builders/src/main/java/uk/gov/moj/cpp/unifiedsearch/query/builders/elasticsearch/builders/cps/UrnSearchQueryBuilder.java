package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.URN;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

public class UrnSearchQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParam) {
        return boolQuery()
                .filter(new TermQueryBuilder(URN, valueOf(queryParam[0])));
    }
}
