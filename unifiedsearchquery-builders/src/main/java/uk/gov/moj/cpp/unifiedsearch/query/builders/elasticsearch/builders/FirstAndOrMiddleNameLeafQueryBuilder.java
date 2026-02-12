package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static co.elastic.clients.elasticsearch._types.query_dsl.Operator.And;
import static co.elastic.clients.elasticsearch._types.query_dsl.Operator.Or;
import static com.google.common.collect.ImmutableList.of;
import static java.util.Arrays.asList;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTY_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.multiMatchQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.matchQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.nestedQuery;

import uk.gov.moj.cpp.unifiedsearch.query.builders.utils.QueryBuilderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;

/**
 * Not in use
 * <p>
 * The crime_case_index structure contains firstName, middleName and lastName fields (at both
 * Parties and Parties.Aliases levels) as well as organisationName
 * <p>
 * If nameQueryParam contains 1 word then we assume this is a firstName If nameQueryParam contains 2
 * words then we assume this is firstName/middleName
 * <p>
 * Note that a multi_match of type "cross_fields" is used to perform the query and this will ensure
 * all words are searched for across all 2 fields so this will catch all permutations not catered
 * for above, e.g. lastName/firstName
 */
public class FirstAndOrMiddleNameLeafQueryBuilder {

    private static final String PARTY_FIRST_NAME_FIELD = "parties.firstName";
    private static final String PARTY_FIRST_NAME_NGRAM_FIELD = "parties.firstName.ngrammed";
    private static final String PARTY_MIDDLE_NAME_FIELD = "parties.middleName";

    private static final List<String> allPartyNamesFields = asList(PARTY_FIRST_NAME_FIELD, PARTY_MIDDLE_NAME_FIELD);

    private static final String PARTY_ALIAS_NESTED_PATH = "parties.aliases";

    private static final String PARTY_ALIAS_FIRST_NAME_FIELD = "parties.aliases.firstName";
    private static final String PARTY_ALIAS_FIRST_NAME_NGRAM_FIELD = "parties.aliases.firstName.ngrammed";

    private static final String PARTY_ALIAS_MIDDLE_NAME_FIELD = "parties.aliases.middleName";

    private static final List<String> allPartyAliasNamesFields = asList(PARTY_ALIAS_FIRST_NAME_FIELD, PARTY_ALIAS_MIDDLE_NAME_FIELD);

    private static final String FUZZINESS_AUTO = "AUTO";
    public static final String F = "^2.0f";
    private final List<Query> additionalPartyQueryBuilders;

    private String allNames;
    private String firstName;
    private String middleName;
    private boolean hasOneName;
    private boolean hasTwoNames;

    public FirstAndOrMiddleNameLeafQueryBuilder(final String allNames, final List<Query> additionalPartyQueryBuilders) {

        this.additionalPartyQueryBuilders = additionalPartyQueryBuilders == null ? of() : additionalPartyQueryBuilders;
        this.allNames = allNames.trim();

        final String[] names = allNames.trim().split("\\s");
        if (names.length == 1) {
            firstName = names[0];
            hasOneName = true;
        } else if (names.length == 2) {
            firstName = names[0];
            middleName = names[1];
            hasTwoNames = true;
        }
    }

    public Query.Builder nestedPartiesBuilder() {

        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();
        booleanQueryBuilder.should(getMustMatchQueryBuilderForParties(false).build());
        booleanQueryBuilder.should(getMustMatchQueryBuilderForParties(true).build());
        booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(convertBuilder(getMultiMatchQueryBuilderForParties().operator(And).boost(0.4F))).build());
        booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(convertBuilder(getMultiMatchQueryBuilderForParties().operator(Or))).build());

        final List<Query.Builder> prefixQueryBuilderList = getMatchNgramQueryBuilderForPartiesList();
        prefixQueryBuilderList.forEach(queryBuilder -> booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(queryBuilder).build()));

        return convertBuilder(nestedQuery(PARTY_NESTED_PATH, convertBuilder(booleanQueryBuilder)));
    }

    public Query.Builder nestedPartyAliasesBuilder() {

        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();

        booleanQueryBuilder.should(getMustMatchQueryBuilderForPartyAliases(false).build());
        booleanQueryBuilder.should(getMustMatchQueryBuilderForPartyAliases(true).build());
        booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(convertBuilder(getMultiMatchQueryBuilderForPartyAliases().operator(And).boost(0.4F))).build());
        booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(convertBuilder(getMultiMatchQueryBuilderForPartyAliases().operator(Or))).build());

        final List<Query.Builder> prefixQueryBuilderList = getMatchNgramQueryBuilderForPartyAliasesList();
        prefixQueryBuilderList.forEach(queryBuilder -> booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(queryBuilder).build()));

        final NestedQuery.Builder partyAliasesNestedQueryBuilder = nestedQuery(PARTY_ALIAS_NESTED_PATH, convertBuilder(booleanQueryBuilder));

        return convertBuilder(nestedQuery(PARTY_NESTED_PATH, convertBuilder(partyAliasesNestedQueryBuilder)));
    }

    private Query.Builder getMustMatchQueryBuilderForParties(final Boolean isFuzziness) {
        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();

        this.additionalPartyQueryBuilders.forEach( b-> booleanQueryBuilder.must(b));

        final List<Query.Builder> mustMatchQueryBuilderList = getMustMatchQueryBuilderForPartiesList();
        if (isFuzziness){
            mustMatchQueryBuilderList.stream()
                    .map(mqb -> mqb.build().match())
                    .forEach(mqb -> {
                        final MatchQuery.Builder builder = QueryBuilderUtils.copyQuery(mqb);
                        builder.fuzziness(FUZZINESS_AUTO);
                        booleanQueryBuilder.must(builder.build());
                    });

            booleanQueryBuilder.boost(2.0F);
        } else {
            mustMatchQueryBuilderList.forEach(b -> booleanQueryBuilder.must(b.build()));

            booleanQueryBuilder.boost(3.0F);
        }
        return convertBuilder(booleanQueryBuilder);
    }

    private Query.Builder getMustMatchQueryBuilderForPartyAliases(final boolean isFuzziness) {
        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();
        this.additionalPartyQueryBuilders.forEach( b-> booleanQueryBuilder.must(b));
        final List<Query.Builder> mustMatchQueryBuilderList = getMustMatchQueryBuilderForPartyAliasesList();
        if (isFuzziness){
            mustMatchQueryBuilderList.stream()
                    .map(q -> q.build().match())
                    .forEach(mqb -> {
                        final MatchQuery.Builder builder = QueryBuilderUtils.copyQuery(mqb);
                        builder.fuzziness(FUZZINESS_AUTO);
                        booleanQueryBuilder.must(builder.build());
                    });
            booleanQueryBuilder.boost(2.0F);
        } else {
            mustMatchQueryBuilderList.forEach(b -> booleanQueryBuilder.must(b.build()));

            booleanQueryBuilder.boost(3.0F);
        }
        return convertBuilder(booleanQueryBuilder);
    }

    private MultiMatchQuery.Builder getMultiMatchQueryBuilderForParties() {
        final MultiMatchQuery.Builder allNameFieldsMultiMatchQuery;
        if (hasOneName) {
            allNameFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(allNames, asList(PARTY_FIRST_NAME_FIELD));
        } else {
            allNameFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(allNames, allPartyNamesFields);
            allNameFieldsMultiMatchQuery.fields(PARTY_MIDDLE_NAME_FIELD+ F);
        }
        allNameFieldsMultiMatchQuery.fields(PARTY_FIRST_NAME_FIELD+ F);
        allNameFieldsMultiMatchQuery.boost(0.3F);
        return allNameFieldsMultiMatchQuery;
    }

    private MultiMatchQuery.Builder getMultiMatchQueryBuilderForPartyAliases() {
        final MultiMatchQuery.Builder partyAliasFieldsMultiMatchQuery;
        if (hasOneName) {
            partyAliasFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(allNames, asList(PARTY_FIRST_NAME_FIELD));
        } else {
            partyAliasFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(allNames, allPartyAliasNamesFields);
            partyAliasFieldsMultiMatchQuery.fields(PARTY_ALIAS_MIDDLE_NAME_FIELD+ F);
        }
        partyAliasFieldsMultiMatchQuery.fields(PARTY_ALIAS_FIRST_NAME_FIELD+ F);
        partyAliasFieldsMultiMatchQuery.boost(0.3F);
        return partyAliasFieldsMultiMatchQuery;
    }

    private List<Query.Builder> getMustMatchQueryBuilderForPartiesList() {

        return getMustMatchQueryBuilderList(PARTY_FIRST_NAME_FIELD, PARTY_MIDDLE_NAME_FIELD);

    }

    private List<Query.Builder> getMustMatchQueryBuilderForPartyAliasesList() {

        return getMustMatchQueryBuilderList(PARTY_ALIAS_FIRST_NAME_FIELD, PARTY_ALIAS_MIDDLE_NAME_FIELD);

    }

    private List<Query.Builder> getMatchNgramQueryBuilderForPartiesList() {

        return getMatchNgramQueryBuilderList(PARTY_FIRST_NAME_NGRAM_FIELD);
    }

    private List<Query.Builder> getMatchNgramQueryBuilderForPartyAliasesList() {

        return getMatchNgramQueryBuilderList(PARTY_ALIAS_FIRST_NAME_NGRAM_FIELD);
    }

    private List<Query.Builder> getMustMatchQueryBuilderList(final String firstNameField, final String middleNameField) {

        final List<Query.Builder> queryBuilderList = new ArrayList<>();

        if (hasOneName) {
            queryBuilderList.add(convertBuilder(matchQueryBuilder(firstName, firstNameField)));
        } else if (hasTwoNames) {
            queryBuilderList.add(convertBuilder(matchQueryBuilder(firstName, firstNameField)));
            queryBuilderList.add(convertBuilder(matchQueryBuilder(middleName, middleNameField)));
        }

        return queryBuilderList;
    }

    private List<Query.Builder> getMatchNgramQueryBuilderList(final String firstNameField) {

        final List<Query.Builder> queryBuilderList = new ArrayList<>();

        queryBuilderList.add(convertBuilder(matchQueryBuilder(firstName, firstNameField, Optional.of(1.7f))));

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
        this.additionalPartyQueryBuilders.forEach( b -> booleanQueryBuilder.must(b));
        booleanQueryBuilder.must(queryBuilder.build());
        return convertBuilder(booleanQueryBuilder);
    }
}
