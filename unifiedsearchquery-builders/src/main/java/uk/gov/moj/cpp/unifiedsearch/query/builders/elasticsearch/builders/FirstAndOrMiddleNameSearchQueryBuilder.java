package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;


import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import java.util.List;

import co.elastic.clients.elasticsearch._types.query_dsl.DisMaxQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

//Not in use
public class FirstAndOrMiddleNameSearchQueryBuilder implements ElasticSearchQueryBuilder {


    @Override
    public Query getQueryBuilderBy(final Object... nameQueryParam) {

        final String allNames = valueOf(nameQueryParam[0]);
        final List<Query> additionalPartyQueryBuilders = (List<Query>) nameQueryParam[1];
        final FirstAndOrMiddleNameLeafQueryBuilder nameLeafQueryBuilder = new FirstAndOrMiddleNameLeafQueryBuilder(allNames, additionalPartyQueryBuilders);
        return namesQueryCompositeBuilder(nameLeafQueryBuilder);

    }

    private Query namesQueryCompositeBuilder(final FirstAndOrMiddleNameLeafQueryBuilder firstAndOrMiddleNameLeafQueryBuilder) {
        final DisMaxQuery.Builder disMaxQueryBuilder = new DisMaxQuery.Builder();

        disMaxQueryBuilder.queries(firstAndOrMiddleNameLeafQueryBuilder.nestedPartiesBuilder().build());

        disMaxQueryBuilder.queries(firstAndOrMiddleNameLeafQueryBuilder.nestedPartyAliasesBuilder().build());

        disMaxQueryBuilder.tieBreaker(0.0);
        disMaxQueryBuilder.boost(1.2F);

        return convertBuilder(disMaxQueryBuilder).build();
    }
}
