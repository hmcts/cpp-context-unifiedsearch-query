package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import java.util.List;

import co.elastic.clients.elasticsearch._types.query_dsl.DisMaxQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

//Not in use
public class LastOrOrganisationNameSearchQueryBuilder implements ElasticSearchQueryBuilder {


    @Override
    public Query getQueryBuilderBy(final Object... nameQueryParam) {

        final String allNames = valueOf(nameQueryParam[0]);
        final List<Query> additionalPartyQueryBuilders = (List<Query>) nameQueryParam[1];
        final LastOrOrganisationNameLeafQueryBuilder lastOrOrganisationNameLeafQueryBuilder = new LastOrOrganisationNameLeafQueryBuilder(allNames, additionalPartyQueryBuilders);
        return namesQueryCompositeBuilder(lastOrOrganisationNameLeafQueryBuilder);
    }

    private Query namesQueryCompositeBuilder(final LastOrOrganisationNameLeafQueryBuilder lastOrOrganisationNameLeafQueryBuilder) {
        final DisMaxQuery.Builder disMaxQueryBuilder = new DisMaxQuery.Builder();

        disMaxQueryBuilder.queries(lastOrOrganisationNameLeafQueryBuilder.nestedPartiesBuilder().build());

        disMaxQueryBuilder.queries(lastOrOrganisationNameLeafQueryBuilder.nestedPartyAliasesBuilder().build());

        disMaxQueryBuilder.tieBreaker(0.0);
        disMaxQueryBuilder.boost(1.2F);

        return convertBuilder(disMaxQueryBuilder).build();
    }
}
