package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.google.common.collect.ImmutableList.of;
import static java.util.Arrays.asList;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.matchQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.multiMatchQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTY_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.nestedQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.utils.QueryBuilderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;


/**
 * Not in use
 * <p>
 * The crime_case_index structure contains lastName, organisationName and lastName fields (at both
 * Parties and Parties.Aliases levels) as well as organisationName
 * <p>
 * If nameQueryParam contains 1 word or 2 word  we assume that this can be last name or organisation
 * name
 * <p>
 * Note that a multi_match of type "cross_fields" is used to perform the query and this will ensure
 * all words are searched for across all 2 fields so this will catch all permutations not catered
 * for above, e.g. lastName/lastName
 */
public class LastOrOrganisationNameLeafQueryBuilder {

    private static final String PARTY_LAST_NAME_FIELD = "parties.lastName";
    private static final String PARTY_LAST_NAME_NGRAM_FIELD = "parties.lastName.ngrammed";
    private static final String PARTY_ORGANISATION_NAME_FIELD = "parties.organisationName";

    private static final List<String> allPartyNamesFields = asList(PARTY_LAST_NAME_FIELD, PARTY_ORGANISATION_NAME_FIELD);

    private static final String PARTY_ALIAS_NESTED_PATH = "parties.aliases";
    private static final String PARTY_ALIAS_LAST_NAME_FIELD = "parties.aliases.lastName";
    private static final String PARTY_ALIAS_LAST_NAME_NGRAM_FIELD = "parties.aliases.lastName.ngrammed";

    private static final String PARTY_ALIAS_ORGANISATION_NAME_FIELD = "parties.aliases.organisationName";

    private static final List<String> allPartyAliasNamesFields = asList(PARTY_ALIAS_LAST_NAME_FIELD, PARTY_ALIAS_ORGANISATION_NAME_FIELD);

    private static final String FUZZINESS_AUTO = "AUTO";
    private final List<Query> additionalPartyQueryBuilders;

    private String allNames;

    public LastOrOrganisationNameLeafQueryBuilder(final String allNames, final List<Query> additionalPartyQueryBuilders) {

        this.additionalPartyQueryBuilders = additionalPartyQueryBuilders == null ? of() : additionalPartyQueryBuilders;
        this.allNames = allNames.trim();

    }

    public Query.Builder nestedPartiesBuilder() {

        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();
        booleanQueryBuilder.should(getMustMatchQueryBuilderForParties().build());
        booleanQueryBuilder.should(getMustMatchWithFuzzinessQueryBuilderForParties().build());
        booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(convertBuilder(getMultiMatchQueryBuilderForParties().operator(Operator.Or))).build());

        final List<Query.Builder> prefixQueryBuilderList = getMatchNgramQueryBuilderForPartiesList();
        prefixQueryBuilderList.forEach(queryBuilder -> booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(queryBuilder).build()));

        return convertBuilder(nestedQuery(PARTY_NESTED_PATH, convertBuilder(booleanQueryBuilder)));
    }

    public Query.Builder nestedPartyAliasesBuilder() {

        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();

        booleanQueryBuilder.should(getMustMatchQueryBuilderForPartyAliases().build());
        booleanQueryBuilder.should(getMustMatchWithFuzzinessQueryBuilderForPartyAliases().build());
        booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(convertBuilder(getMultiMatchQueryBuilderForPartyAliases().operator(Operator.Or))).build());

        final List<Query.Builder> prefixQueryBuilderList = getMatchNgramQueryBuilderForPartyAliasesList();
        prefixQueryBuilderList.forEach(queryBuilder -> booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(queryBuilder).build()));

        final NestedQuery.Builder partyAliasesNestedQueryBuilder = nestedQuery(PARTY_ALIAS_NESTED_PATH, convertBuilder(booleanQueryBuilder));

        return convertBuilder(nestedQuery(PARTY_NESTED_PATH, convertBuilder(partyAliasesNestedQueryBuilder)));
    }

    private Query.Builder getMustMatchQueryBuilderForParties() {
        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();

        this.additionalPartyQueryBuilders.forEach(b -> booleanQueryBuilder.must(b));

        final List<Query.Builder> mustMatchQueryBuilderList = getMustMatchQueryBuilderForPartiesList();
        mustMatchQueryBuilderList.forEach(b-> booleanQueryBuilder.must(b.build()));

        booleanQueryBuilder.boost(3.0F);

        return convertBuilder(booleanQueryBuilder);
    }

    private Query.Builder getMustMatchWithFuzzinessQueryBuilderForParties() {
        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();
        this.additionalPartyQueryBuilders.forEach(b -> booleanQueryBuilder.must(b));

        final List<Query.Builder> mustMatchQueryBuilderList = getMustMatchQueryBuilderForPartiesList();

        mustMatchQueryBuilderList.stream()
                .map(q -> q.build().match())
                .forEach(mqb -> {
                    final MatchQuery.Builder builder = QueryBuilderUtils.copyQuery(mqb);
                    builder.fuzziness(FUZZINESS_AUTO);
                    booleanQueryBuilder.must(builder.build());
                });

        booleanQueryBuilder.boost(2.0F);

        return convertBuilder(booleanQueryBuilder);
    }

    private Query.Builder getMustMatchQueryBuilderForPartyAliases() {
        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();
        this.additionalPartyQueryBuilders.forEach( b-> booleanQueryBuilder.must(b));
        final List<Query.Builder> mustMatchQueryBuilderList = getMustMatchQueryBuilderForPartyAliasesList();
        mustMatchQueryBuilderList.forEach(b -> booleanQueryBuilder.must(b.build()));

        booleanQueryBuilder.boost(3.0F);

        return convertBuilder(booleanQueryBuilder);
    }

    private Query.Builder getMustMatchWithFuzzinessQueryBuilderForPartyAliases() {
        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();
        this.additionalPartyQueryBuilders.forEach(b -> booleanQueryBuilder.must(b));
        final List<Query.Builder> mustMatchQueryBuilderList = getMustMatchQueryBuilderForPartyAliasesList();

        mustMatchQueryBuilderList.stream()
                .map(q -> q.build().match())
                .forEach(mqb -> {
                    final MatchQuery.Builder builder = QueryBuilderUtils.copyQuery(mqb);
                    builder.fuzziness(FUZZINESS_AUTO);
                    booleanQueryBuilder.must(builder.build());
                });


        booleanQueryBuilder.boost(2.0F);

        return convertBuilder(booleanQueryBuilder);
    }


    private MultiMatchQuery.Builder getMultiMatchQueryBuilderForParties() {
        final MultiMatchQuery.Builder allNameFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(allNames, allPartyNamesFields);
        allNameFieldsMultiMatchQuery.fields(PARTY_LAST_NAME_FIELD+"^2.0f");
        allNameFieldsMultiMatchQuery.fields(PARTY_ORGANISATION_NAME_FIELD+"^2.0f");
        allNameFieldsMultiMatchQuery.boost(0.3F);
        return allNameFieldsMultiMatchQuery;
    }

    private MultiMatchQuery.Builder getMultiMatchQueryBuilderForPartyAliases() {
        final MultiMatchQuery.Builder partyAliasFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(allNames, allPartyAliasNamesFields);
        partyAliasFieldsMultiMatchQuery.fields(PARTY_ALIAS_LAST_NAME_FIELD+"^2.0f");
        partyAliasFieldsMultiMatchQuery.fields(PARTY_ALIAS_ORGANISATION_NAME_FIELD+"^2.0f");
        partyAliasFieldsMultiMatchQuery.boost(0.3F);
        return partyAliasFieldsMultiMatchQuery;
    }

    private List<Query.Builder> getMustMatchQueryBuilderForPartiesList() {

        return getMustMatchQueryBuilderList(PARTY_LAST_NAME_FIELD, PARTY_ORGANISATION_NAME_FIELD);

    }

    private List<Query.Builder> getMustMatchQueryBuilderForPartyAliasesList() {

        return getMustMatchQueryBuilderList(PARTY_ALIAS_LAST_NAME_FIELD, PARTY_ALIAS_ORGANISATION_NAME_FIELD);

    }

    private List<Query.Builder> getMatchNgramQueryBuilderForPartiesList() {

        return getMatchNgramQueryBuilderList(PARTY_LAST_NAME_NGRAM_FIELD);
    }

    private List<Query.Builder> getMatchNgramQueryBuilderForPartyAliasesList() {

        return getMatchNgramQueryBuilderList(PARTY_ALIAS_LAST_NAME_NGRAM_FIELD);
    }

    private List<Query.Builder> getMustMatchQueryBuilderList(final String lastNameField, final String orgNameField) {

        final List<Query.Builder> queryBuilderList = new ArrayList<>();
        queryBuilderList.add(convertBuilder(matchQueryBuilder(allNames, lastNameField)));
        queryBuilderList.add(convertBuilder(matchQueryBuilder(allNames, orgNameField)));
        return queryBuilderList;
    }

    private List<Query.Builder> getMatchNgramQueryBuilderList(final String fieldName) {

        final List<Query.Builder> queryBuilderList = new ArrayList<>();

        queryBuilderList.add(convertBuilder(matchQueryBuilder(allNames, fieldName, Optional.of(1.7f))));

        return queryBuilderList;
    }

    private MatchQuery.Builder matchQueryBuilder(final String query, final String field) {
        return matchQuery(field, query);
    }

    private MatchQuery.Builder matchQueryBuilder(final String query, final String field, final Optional<Float> boost) {
        final MatchQuery.Builder builder = matchQuery(field, query);
        boost.ifPresent(builder::boost);
        return builder;
    }

    private MultiMatchQuery.Builder crossFieldsMultiMatchQueryBuilder(final String query, final List<String> fields) {

        final MultiMatchQuery.Builder builder = multiMatchQuery(query, fields);
        builder.type(TextQueryType.CrossFields);

        return builder;
    }

    private Query.Builder wrapWithAdditionalPartyQueryBuilders(final Query.Builder queryBuilder) {
        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();
        this.additionalPartyQueryBuilders.forEach(b -> booleanQueryBuilder.must(b));
        booleanQueryBuilder.must(queryBuilder.build());
        return convertBuilder(booleanQueryBuilder);
    }
}
