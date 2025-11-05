package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.disMaxQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import java.util.List;

import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

//Not in use
public class FirstAndOrMiddleNameSearchQueryBuilder implements ElasticSearchQueryBuilder {


    @Override
    public QueryBuilder getQueryBuilderBy(final Object... nameQueryParam) {

        final String allNames = valueOf(nameQueryParam[0]);
        final List<QueryBuilder> additionalPartyQueryBuilders = (List<QueryBuilder>) nameQueryParam[1];
        final FirstAndOrMiddleNameLeafQueryBuilder nameLeafQueryBuilder = new FirstAndOrMiddleNameLeafQueryBuilder(allNames, additionalPartyQueryBuilders);
        return namesQueryCompositeBuilder(nameLeafQueryBuilder);

    }

    private QueryBuilder namesQueryCompositeBuilder(final FirstAndOrMiddleNameLeafQueryBuilder firstAndOrMiddleNameLeafQueryBuilder) {
        final DisMaxQueryBuilder disMaxQueryBuilder = disMaxQuery();

        disMaxQueryBuilder.add(firstAndOrMiddleNameLeafQueryBuilder.nestedPartiesBuilder());

        disMaxQueryBuilder.add(firstAndOrMiddleNameLeafQueryBuilder.nestedPartyAliasesBuilder());

        disMaxQueryBuilder.tieBreaker(0.0F);
        disMaxQueryBuilder.boost(1.2F);

        return disMaxQueryBuilder;
    }
}
