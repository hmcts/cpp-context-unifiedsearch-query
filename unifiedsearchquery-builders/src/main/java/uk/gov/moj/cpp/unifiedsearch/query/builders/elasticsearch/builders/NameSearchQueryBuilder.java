package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.lang.String.valueOf;
import static org.elasticsearch.index.query.QueryBuilders.disMaxQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import java.util.List;

import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

public class NameSearchQueryBuilder implements ElasticSearchQueryBuilder {


    @Override
    public QueryBuilder getQueryBuilderBy(final Object... nameQueryParam) {

        NameLeafQueryBuilder nameLeafQueryBuilder;
        if (nameQueryParam.length > 2) {
            nameLeafQueryBuilder = new NameLeafQueryBuilder(valueOf(nameQueryParam[0]), valueOf(nameQueryParam[1]), (List<QueryBuilder>) nameQueryParam[2]);
        } else {
            nameLeafQueryBuilder = new NameLeafQueryBuilder(valueOf(nameQueryParam[0]), (List<QueryBuilder>) nameQueryParam[1]);
        }

        return namesQueryCompositeBuilder(nameLeafQueryBuilder);
    }

    private QueryBuilder namesQueryCompositeBuilder(final NameLeafQueryBuilder nameLeafQueryBuilder) {
        final DisMaxQueryBuilder disMaxQueryBuilder = disMaxQuery();

        disMaxQueryBuilder.add(nameLeafQueryBuilder.nestedPartiesBuilder());

        disMaxQueryBuilder.add(nameLeafQueryBuilder.nestedPartyAliasesBuilder());

        disMaxQueryBuilder.tieBreaker(0.0F);
        disMaxQueryBuilder.boost(1.2F);

        return disMaxQueryBuilder;
    }
}
