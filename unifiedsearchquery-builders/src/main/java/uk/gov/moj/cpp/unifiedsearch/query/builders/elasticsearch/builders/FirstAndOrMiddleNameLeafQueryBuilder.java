package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.google.common.collect.ImmutableList.of;
import static java.util.Arrays.asList;
import static org.elasticsearch.index.query.MultiMatchQueryBuilder.Type.CROSS_FIELDS;
import static org.elasticsearch.index.query.Operator.AND;
import static org.elasticsearch.index.query.Operator.OR;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTY_NESTED_PATH;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

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
    private final List<QueryBuilder> additionalPartyQueryBuilders;

    private String allNames;
    private String firstName;
    private String middleName;
    private boolean hasOneName;
    private boolean hasTwoNames;

    public FirstAndOrMiddleNameLeafQueryBuilder(final String allNames, final List<QueryBuilder> additionalPartyQueryBuilders) {

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

    public QueryBuilder nestedPartiesBuilder() {

        final BoolQueryBuilder booleanQueryBuilder = boolQuery();
        booleanQueryBuilder.should(getMustMatchQueryBuilderForParties());
        booleanQueryBuilder.should(getMustMatchWithFuzzinessQueryBuilderForParties());
        booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(getMultiMatchQueryBuilderForParties().operator(AND).boost(0.4F)));
        booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(getMultiMatchQueryBuilderForParties().operator(OR)));

        final List<QueryBuilder> prefixQueryBuilderList = getMatchNgramQueryBuilderForPartiesList();
        prefixQueryBuilderList.forEach(queryBuilder -> booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(queryBuilder)));

        return nestedQuery(PARTY_NESTED_PATH, booleanQueryBuilder, ScoreMode.Avg);
    }

    public QueryBuilder nestedPartyAliasesBuilder() {

        final BoolQueryBuilder booleanQueryBuilder = boolQuery();

        booleanQueryBuilder.should(getMustMatchQueryBuilderForPartyAliases());
        booleanQueryBuilder.should(getMustMatchWithFuzzinessQueryBuilderForPartyAliases());
        booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(getMultiMatchQueryBuilderForPartyAliases().operator(AND).boost(0.4F)));
        booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(getMultiMatchQueryBuilderForPartyAliases().operator(OR)));

        final List<QueryBuilder> prefixQueryBuilderList = getMatchNgramQueryBuilderForPartyAliasesList();
        prefixQueryBuilderList.forEach(queryBuilder -> booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(queryBuilder)));

        final NestedQueryBuilder partyAliasesNestedQueryBuilder = nestedQuery(PARTY_ALIAS_NESTED_PATH, booleanQueryBuilder, ScoreMode.Avg);

        return nestedQuery(PARTY_NESTED_PATH, partyAliasesNestedQueryBuilder, ScoreMode.Avg);
    }

    private QueryBuilder getMustMatchQueryBuilderForParties() {
        final BoolQueryBuilder booleanQueryBuilder = boolQuery();

        this.additionalPartyQueryBuilders.forEach(booleanQueryBuilder::must);

        final List<QueryBuilder> mustMatchQueryBuilderList = getMustMatchQueryBuilderForPartiesList();
        mustMatchQueryBuilderList.forEach(booleanQueryBuilder::must);

        booleanQueryBuilder.boost(3.0F);

        return booleanQueryBuilder;
    }

    private QueryBuilder getMustMatchWithFuzzinessQueryBuilderForParties() {
        final BoolQueryBuilder booleanQueryBuilder = boolQuery();
        this.additionalPartyQueryBuilders.forEach(booleanQueryBuilder::must);

        final List<QueryBuilder> mustMatchQueryBuilderList = getMustMatchQueryBuilderForPartiesList();

        mustMatchQueryBuilderList.stream()
                .map(MatchQueryBuilder.class::cast)
                .forEach(mqb -> {
                    mqb.fuzziness(FUZZINESS_AUTO);
                    booleanQueryBuilder.must(mqb);
                });

        booleanQueryBuilder.boost(2.0F);

        return booleanQueryBuilder;
    }

    private QueryBuilder getMustMatchQueryBuilderForPartyAliases() {
        final BoolQueryBuilder booleanQueryBuilder = boolQuery();
        this.additionalPartyQueryBuilders.forEach(booleanQueryBuilder::must);
        final List<QueryBuilder> mustMatchQueryBuilderList = getMustMatchQueryBuilderForPartyAliasesList();
        mustMatchQueryBuilderList.forEach(booleanQueryBuilder::must);

        booleanQueryBuilder.boost(3.0F);

        return booleanQueryBuilder;
    }

    private QueryBuilder getMustMatchWithFuzzinessQueryBuilderForPartyAliases() {
        final BoolQueryBuilder booleanQueryBuilder = boolQuery();
        this.additionalPartyQueryBuilders.forEach(booleanQueryBuilder::must);
        final List<QueryBuilder> mustMatchQueryBuilderList = getMustMatchQueryBuilderForPartyAliasesList();

        mustMatchQueryBuilderList.stream()
                .map(MatchQueryBuilder.class::cast)
                .forEach(mqb -> {
                    mqb.fuzziness(FUZZINESS_AUTO);
                    booleanQueryBuilder.must(mqb);
                });


        booleanQueryBuilder.boost(2.0F);

        return booleanQueryBuilder;
    }


    private MultiMatchQueryBuilder getMultiMatchQueryBuilderForParties() {
        if (hasOneName) {
            final MultiMatchQueryBuilder allNameFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(allNames, asList(PARTY_FIRST_NAME_FIELD));
            allNameFieldsMultiMatchQuery.field(PARTY_FIRST_NAME_FIELD, 2.0f);
            allNameFieldsMultiMatchQuery.boost(0.3F);
            return allNameFieldsMultiMatchQuery;
        } else {
            final MultiMatchQueryBuilder allNameFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(allNames, allPartyNamesFields);
            allNameFieldsMultiMatchQuery.field(PARTY_FIRST_NAME_FIELD, 2.0f);
            allNameFieldsMultiMatchQuery.field(PARTY_MIDDLE_NAME_FIELD, 2.0f);
            allNameFieldsMultiMatchQuery.boost(0.3F);
            return allNameFieldsMultiMatchQuery;
        }


    }

    private MultiMatchQueryBuilder getMultiMatchQueryBuilderForPartyAliases() {
        if (hasOneName) {
            final MultiMatchQueryBuilder partyAliasFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(allNames, asList(PARTY_FIRST_NAME_FIELD));
            partyAliasFieldsMultiMatchQuery.field(PARTY_ALIAS_FIRST_NAME_FIELD, 2.0f);
            partyAliasFieldsMultiMatchQuery.boost(0.3F);
            return partyAliasFieldsMultiMatchQuery;
        } else {
            final MultiMatchQueryBuilder partyAliasFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(allNames, allPartyAliasNamesFields);
            partyAliasFieldsMultiMatchQuery.field(PARTY_ALIAS_FIRST_NAME_FIELD, 2.0f);
            partyAliasFieldsMultiMatchQuery.field(PARTY_ALIAS_MIDDLE_NAME_FIELD, 2.0f);
            partyAliasFieldsMultiMatchQuery.boost(0.3F);
            return partyAliasFieldsMultiMatchQuery;
        }


    }

    private List<QueryBuilder> getMustMatchQueryBuilderForPartiesList() {

        return getMustMatchQueryBuilderList(PARTY_FIRST_NAME_FIELD, PARTY_MIDDLE_NAME_FIELD);

    }

    private List<QueryBuilder> getMustMatchQueryBuilderForPartyAliasesList() {

        return getMustMatchQueryBuilderList(PARTY_ALIAS_FIRST_NAME_FIELD, PARTY_ALIAS_MIDDLE_NAME_FIELD);

    }

    private List<QueryBuilder> getMatchNgramQueryBuilderForPartiesList() {

        return getMatchNgramQueryBuilderList(PARTY_FIRST_NAME_NGRAM_FIELD);
    }

    private List<QueryBuilder> getMatchNgramQueryBuilderForPartyAliasesList() {

        return getMatchNgramQueryBuilderList(PARTY_ALIAS_FIRST_NAME_NGRAM_FIELD);
    }

    private List<QueryBuilder> getMustMatchQueryBuilderList(final String firstNameField, final String middleNameField) {

        final List<QueryBuilder> queryBuilderList = new ArrayList<>();

        if (hasOneName) {
            queryBuilderList.add(matchQueryBuilder(firstName, firstNameField));
        } else if (hasTwoNames) {
            queryBuilderList.add(matchQueryBuilder(firstName, firstNameField));
            queryBuilderList.add(matchQueryBuilder(middleName, middleNameField));
        }

        return queryBuilderList;
    }

    private List<QueryBuilder> getMatchNgramQueryBuilderList(final String firstNameField) {

        final List<QueryBuilder> queryBuilderList = new ArrayList<>();

        queryBuilderList.add(matchQueryBuilder(firstName, firstNameField, Optional.of(1.7f)));

        return queryBuilderList;
    }

    private MatchQueryBuilder matchQueryBuilder(final String query, final String field) {
        return matchQuery(field, query);
    }

    private MatchQueryBuilder matchQueryBuilder(final String query, final String field, final Optional<Float> boost) {
        final MatchQueryBuilder builder = matchQuery(field, query);
        boost.ifPresent(builder::boost);
        return builder;
    }

    private MultiMatchQueryBuilder crossFieldsMultiMatchQueryBuilder(final String query, final List<String> fields) {

        final MultiMatchQueryBuilder builder = multiMatchQuery(query, fields.toArray(new String[0]));
        builder.type(CROSS_FIELDS);

        return builder;
    }

    private QueryBuilder wrapWithAdditionalPartyQueryBuilders(final QueryBuilder queryBuilder) {
        final BoolQueryBuilder booleanQueryBuilder = boolQuery();
        this.additionalPartyQueryBuilders.forEach(booleanQueryBuilder::must);
        booleanQueryBuilder.must(queryBuilder);
        return booleanQueryBuilder;
    }
}
