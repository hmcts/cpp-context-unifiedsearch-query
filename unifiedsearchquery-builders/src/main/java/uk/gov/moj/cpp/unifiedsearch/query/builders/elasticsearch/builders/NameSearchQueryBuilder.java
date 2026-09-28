package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import java.util.List;

import co.elastic.clients.elasticsearch._types.query_dsl.DisMaxQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class NameSearchQueryBuilder implements ElasticSearchQueryBuilder {


    @Override
    public Query getQueryBuilderBy(final Object... nameQueryParam) {

        NameLeafQueryBuilder nameLeafQueryBuilder;
        if (nameQueryParam.length > 2) {
            nameLeafQueryBuilder = new NameLeafQueryBuilder(valueOf(nameQueryParam[0]), valueOf(nameQueryParam[1]), (List<Query>) nameQueryParam[2]);
        } else {
            nameLeafQueryBuilder = new NameLeafQueryBuilder(valueOf(nameQueryParam[0]), (List<Query>) nameQueryParam[1]);
        }

        return namesQueryCompositeBuilder(nameLeafQueryBuilder);
    }

    private Query namesQueryCompositeBuilder(final NameLeafQueryBuilder nameLeafQueryBuilder) {
        final DisMaxQuery.Builder disMaxQueryBuilder = new DisMaxQuery.Builder();

        disMaxQueryBuilder.queries(nameLeafQueryBuilder.nestedPartiesBuilder().build());

        disMaxQueryBuilder.queries(nameLeafQueryBuilder.nestedPartyAliasesBuilder().build());

        disMaxQueryBuilder.tieBreaker(0.0);
        disMaxQueryBuilder.boost(1.2F);

        return convertBuilder(disMaxQueryBuilder).build();
    }
}
