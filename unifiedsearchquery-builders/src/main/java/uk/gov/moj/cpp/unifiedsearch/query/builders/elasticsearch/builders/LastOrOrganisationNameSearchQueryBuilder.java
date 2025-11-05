package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.disMaxQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import java.util.List;

import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

//Not in use
public class LastOrOrganisationNameSearchQueryBuilder implements ElasticSearchQueryBuilder {


    @Override
    public QueryBuilder getQueryBuilderBy(final Object... nameQueryParam) {

        final String allNames = valueOf(nameQueryParam[0]);
        final List<QueryBuilder> additionalPartyQueryBuilders = (List<QueryBuilder>) nameQueryParam[1];
        final LastOrOrganisationNameLeafQueryBuilder lastOrOrganisationNameLeafQueryBuilder = new LastOrOrganisationNameLeafQueryBuilder(allNames, additionalPartyQueryBuilders);
        final QueryBuilder queryBuilder = namesQueryCompositeBuilder(lastOrOrganisationNameLeafQueryBuilder);
        return queryBuilder;
    }

    private QueryBuilder namesQueryCompositeBuilder(final LastOrOrganisationNameLeafQueryBuilder lastOrOrganisationNameLeafQueryBuilder) {
        final DisMaxQueryBuilder disMaxQueryBuilder = disMaxQuery();

        disMaxQueryBuilder.add(lastOrOrganisationNameLeafQueryBuilder.nestedPartiesBuilder());

        disMaxQueryBuilder.add(lastOrOrganisationNameLeafQueryBuilder.nestedPartyAliasesBuilder());

        disMaxQueryBuilder.tieBreaker(0.0F);
        disMaxQueryBuilder.boost(1.2F);

        return disMaxQueryBuilder;
    }
}
