package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.valueOf;

import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_VIRTUAL_BOX_HEARING_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class VirtualBoxHearingQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final boolean isBoxWork = parseBoolean(valueOf(queryParams[0]));
        return termQuery(IS_VIRTUAL_BOX_HEARING_PATH, isBoxWork).build();
    }
}
