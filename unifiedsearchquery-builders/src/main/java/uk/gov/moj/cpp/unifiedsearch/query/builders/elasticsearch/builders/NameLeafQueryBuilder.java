package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static co.elastic.clients.elasticsearch._types.query_dsl.Operator.And;
import static co.elastic.clients.elasticsearch._types.query_dsl.Operator.Or;
import static com.google.common.collect.ImmutableList.of;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.matchQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.nestedQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTY_NESTED_PATH;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

/**
 * The nameQueryParam passed to {@link #NameLeafQueryBuilder(String, List)} can contain any number
 * of words
 *
 * The crime_case_index structure contains firstName, middleName and lastName fields (at both
 * Parties and Parties.Aliases levels) as well as organisationName
 *
 * If nameQueryParam contains 1 word then we assume this is a lastName but also check firstName If
 * nameQueryParam contains 2 words then we assume this is firstName/lastName If nameQueryParam
 * contains 3 words then we assume this is firstName/middleName/lastName
 *
 * We always search the organisationName field for the whole nameQueryParam using an AND operator in
 * the match() to cater for org names comprising more than one word
 *
 * Note that a multi_match of type "cross_fields" is used to perform the query and this will ensure
 * all words are searched for across all 4 fields so this will catch all permutations not catered
 * for above, e.g. lastName/firstName
 */
public class NameLeafQueryBuilder {

    private static final String PARTY_FIRST_NAME_FIELD = "parties.firstName";
    private static final String PARTY_FIRST_NAME_NGRAM_FIELD = "parties.firstName.ngrammed";
    private static final String PARTY_MIDDLE_NAME_FIELD = "parties.middleName";
    private static final String PARTY_LAST_NAME_FIELD = "parties.lastName";
    private static final String PARTY_LAST_NAME_NGRAM_FIELD = "parties.lastName.ngrammed";
    private static final String PARTY_ORGANISATION_NAME_FIELD = "parties.organisationName";
    private static final List<String> allPartyNamesFields = asList(PARTY_FIRST_NAME_FIELD, PARTY_LAST_NAME_FIELD, PARTY_MIDDLE_NAME_FIELD, PARTY_ORGANISATION_NAME_FIELD);
    private static final List<String> lastNameAndOrganisationNameFields = asList(PARTY_LAST_NAME_FIELD, PARTY_ORGANISATION_NAME_FIELD);
    private static final List<String> firstAndMiddleNameFields = asList(PARTY_FIRST_NAME_FIELD, PARTY_MIDDLE_NAME_FIELD);

    private static final String PARTY_ALIAS_NESTED_PATH = "parties.aliases";
    private static final String PARTY_ALIAS_FIRST_NAME_FIELD = "parties.aliases.firstName";
    private static final String PARTY_ALIAS_FIRST_NAME_NGRAM_FIELD = "parties.aliases.firstName.ngrammed";
    private static final String PARTY_ALIAS_MIDDLE_NAME_FIELD = "parties.aliases.middleName";
    private static final String PARTY_ALIAS_LAST_NAME_FIELD = "parties.aliases.lastName";
    private static final String PARTY_ALIAS_LAST_NAME_NGRAM_FIELD = "parties.aliases.lastName.ngrammed";
    private static final String PARTY_ALIAS_ORGANISATION_NAME_FIELD = "parties.aliases.organisationName";

    private static final List<String> allPartyAliasNamesFields = asList(PARTY_ALIAS_FIRST_NAME_FIELD, PARTY_ALIAS_LAST_NAME_FIELD, PARTY_ALIAS_MIDDLE_NAME_FIELD, PARTY_ALIAS_ORGANISATION_NAME_FIELD);
    private static final List<String> lastNameAndOrganisationNameAliasFields = asList(PARTY_ALIAS_LAST_NAME_FIELD, PARTY_ALIAS_ORGANISATION_NAME_FIELD);

    private static final String FUZZINESS_AUTO = "AUTO";
    private final List<Query> additionalPartyQueryBuilders;

    private String allNames;
    private String firstName;
    private String middleName;
    private String lastName;
    private String organisationName;
    private String partyFirstAndOrMiddleName;
    private boolean hasOneName;
    private boolean hasTwoNames;

    /**
     * By default, we are searching on the name field that contains fully concatenated value
     */
    private boolean inSingleNameFieldMode = true;

    public NameLeafQueryBuilder(final String allNames, final List<Query> additionalPartyQueryBuilders) {

        this.additionalPartyQueryBuilders = additionalPartyQueryBuilders == null ? of() : additionalPartyQueryBuilders;

        this.allNames = allNames.trim();

        final String[] names = allNames.trim().split("\\s");

        organisationName = allNames;
        if (names.length == 1) {
            lastName = names[0];
            hasOneName = true;
        } else if (names.length == 2) {
            firstName = names[0];
            lastName = names[1];
            hasTwoNames = true;
        } else {
            firstName = names[0];
            middleName = names[1];
            lastName = names[2];
        }
    }

    public NameLeafQueryBuilder(final String partyFirstAndOrMiddleName, final String partyLastNameOrOrganisationName, final List<Query> additionalPartyQueryBuilders) {
        this.additionalPartyQueryBuilders = additionalPartyQueryBuilders == null ? of() : additionalPartyQueryBuilders;
        inSingleNameFieldMode = false;
        firstName = EMPTY;
        middleName = EMPTY;
        allNames = partyLastNameOrOrganisationName;// used for cross fields

        organisationName = partyLastNameOrOrganisationName;
        lastName = partyLastNameOrOrganisationName;

        this.partyFirstAndOrMiddleName = partyFirstAndOrMiddleName.trim();
        final String[] firstAndOrMiddleNames = this.partyFirstAndOrMiddleName.split("\\s");

        if (firstAndOrMiddleNames.length == 1) {
            firstName = firstAndOrMiddleNames[0];
        } else if (firstAndOrMiddleNames.length >= 2) {
            firstName = firstAndOrMiddleNames[0];
            middleName = firstAndOrMiddleNames[1];
        }

        if (!partyLastNameOrOrganisationName.isEmpty() && (!firstName.isEmpty() || !middleName.isEmpty())) {
            hasTwoNames = true;
        } else if (!partyLastNameOrOrganisationName.isEmpty()) {
            hasOneName = true;
        }
    }


    public Query.Builder nestedPartiesBuilder() {

        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();
        booleanQueryBuilder.should(getMustMatchQueryBuilderForParties().build());
        booleanQueryBuilder.should(getMustMatchWithFuzzinessQueryBuilderForParties().build());
        if (!allNames.isEmpty()) {
            booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(convertBuilder(getMultiMatchQueryBuilderForParties().operator(And).boost(0.4F))).build());
            booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(convertBuilder(getMultiMatchQueryBuilderForParties().operator(Or))).build());
        }

        if (!inSingleNameFieldMode && !firstName.isEmpty() && !middleName.isEmpty()) {
            booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(convertBuilder(getMultiMatchQueryBuilderForFirstAndMiddleName().operator(And).boost(0.1F))).build());
            booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(convertBuilder(getMultiMatchQueryBuilderForFirstAndMiddleName().operator(Or))).build());
        }

        final List<Query.Builder> prefixQueryBuilderList = getMatchNgramQueryBuilderForPartiesList();
        if (enableORQuery()) {
            prefixQueryBuilderList.forEach(queryBuilder -> booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(queryBuilder).build()));
        } else {
            prefixQueryBuilderList.forEach(queryBuilder -> booleanQueryBuilder.must(wrapWithAdditionalPartyQueryBuilders(queryBuilder).build()));
        }

        final MatchQuery.Builder organisationMatchBuilder = matchQueryBuilder(organisationName, PARTY_ORGANISATION_NAME_FIELD);
        organisationMatchBuilder.fuzziness(FUZZINESS_AUTO);
        organisationMatchBuilder.boost(2.0f);

        booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(convertBuilder(organisationMatchBuilder)).build());

        return convertBuilder(nestedQuery(PARTY_NESTED_PATH, convertBuilder(booleanQueryBuilder)));
    }

    public Query.Builder nestedPartyAliasesBuilder() {

        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();

        booleanQueryBuilder.should(getMustMatchQueryBuilderForPartyAliases().build());
        booleanQueryBuilder.should(getMustMatchWithFuzzinessQueryBuilderForPartyAliases().build());

        if (!allNames.isEmpty()) {
            booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(convertBuilder(getMultiMatchQueryBuilderForPartyAliases().operator(And).boost(0.4F))).build());
            booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(convertBuilder(getMultiMatchQueryBuilderForPartyAliases().operator(Or))).build());
        }

        final List<Query.Builder> prefixQueryBuilderList = getMatchNgramQueryBuilderForPartyAliasesList();
        if (enableORQuery()) {
            prefixQueryBuilderList.forEach(queryBuilder -> booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(queryBuilder).build()));
        } else {
            prefixQueryBuilderList.forEach(queryBuilder -> booleanQueryBuilder.must(wrapWithAdditionalPartyQueryBuilders(queryBuilder).build()));
        }

        final MatchQuery.Builder organisationMatchBuilder = matchQueryBuilder(organisationName, PARTY_ALIAS_ORGANISATION_NAME_FIELD);
        organisationMatchBuilder.fuzziness(FUZZINESS_AUTO);
        organisationMatchBuilder.boost(2.0f);
        booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(convertBuilder(organisationMatchBuilder)).build());

        final NestedQuery.Builder partyAliasesNestedQueryBuilder = nestedQuery(PARTY_ALIAS_NESTED_PATH, convertBuilder(booleanQueryBuilder));

        return convertBuilder(nestedQuery(PARTY_NESTED_PATH, convertBuilder(partyAliasesNestedQueryBuilder)));
    }

    private boolean enableORQuery() {
        return inSingleNameFieldMode || hasOneName;
    }


    private Query.Builder getMustMatchQueryBuilderForParties() {
        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();

        this.additionalPartyQueryBuilders.forEach(booleanQueryBuilder::must);

        final List<Query.Builder> mustMatchQueryBuilderList = getMustMatchQueryBuilderForPartiesList();
        mustMatchQueryBuilderList.forEach(b -> booleanQueryBuilder.must(b.build()));

        booleanQueryBuilder.boost(3.0F);

        return convertBuilder(booleanQueryBuilder);
    }

    private Query.Builder getMustMatchWithFuzzinessQueryBuilderForParties() {
        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();
        this.additionalPartyQueryBuilders.forEach(booleanQueryBuilder::must);

        final List<Query.Builder> mustMatchQueryBuilderList = getMustMatchQueryBuilderForPartiesList();

        mustMatchQueryBuilderList.stream()
                .map(q -> q.build().match())
                .forEach(mqb -> {
                    MatchQuery.Builder builder = new MatchQuery.Builder();
                    builder.field(mqb.field());
                    builder.query(mqb.query());
                    builder.boost(mqb.boost());
                    builder.fuzziness(FUZZINESS_AUTO);
                    booleanQueryBuilder.must(builder.build());
                });

        booleanQueryBuilder.boost(2.0F);

        return convertBuilder(booleanQueryBuilder);
    }

    private Query.Builder getMustMatchQueryBuilderForPartyAliases() {
        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();
        this.additionalPartyQueryBuilders.forEach(booleanQueryBuilder::must);
        final List<Query.Builder> mustMatchQueryBuilderList = getMustMatchQueryBuilderForPartyAliasesList();
        mustMatchQueryBuilderList.forEach(b -> booleanQueryBuilder.must(b.build()));

        booleanQueryBuilder.boost(3.0F);

        return convertBuilder(booleanQueryBuilder);
    }

    private Query.Builder getMustMatchWithFuzzinessQueryBuilderForPartyAliases() {
        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();
        this.additionalPartyQueryBuilders.forEach(booleanQueryBuilder::must);
        final List<Query.Builder> mustMatchQueryBuilderList = getMustMatchQueryBuilderForPartyAliasesList();

        mustMatchQueryBuilderList.stream()
                .map(q -> q.build().match())
                .forEach(mqb -> {
                    MatchQuery.Builder builder = new MatchQuery.Builder();
                    builder.field(mqb.field());
                    builder.query(mqb.query());
                    builder.boost(mqb.boost());
                    builder.fuzziness(FUZZINESS_AUTO);
                    booleanQueryBuilder.must(builder.build());
                });


        booleanQueryBuilder.boost(2.0F);

        return convertBuilder(booleanQueryBuilder);
    }


    private MultiMatchQuery.Builder getMultiMatchQueryBuilderForParties() {
        List<String> fields = inSingleNameFieldMode ? allPartyNamesFields : lastNameAndOrganisationNameFields;

        if (hasOneName){
            fields = fields.stream().filter(v -> !v.equals(PARTY_LAST_NAME_FIELD) && !v.equals(PARTY_ORGANISATION_NAME_FIELD) ).toList();
        }
        final MultiMatchQuery.Builder allNameFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(allNames, fields);
        if (hasOneName) {
            allNameFieldsMultiMatchQuery.fields(PARTY_LAST_NAME_FIELD+"^2.0f");
            allNameFieldsMultiMatchQuery.fields(PARTY_ORGANISATION_NAME_FIELD+"^2.0f");
        }

        allNameFieldsMultiMatchQuery.boost(0.3F);


        return allNameFieldsMultiMatchQuery;
    }

    private MultiMatchQuery.Builder getMultiMatchQueryBuilderForFirstAndMiddleName() {
        final MultiMatchQuery.Builder allNameFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(partyFirstAndOrMiddleName, firstAndMiddleNameFields);

        allNameFieldsMultiMatchQuery.fields(PARTY_FIRST_NAME_FIELD+"^1.10f");

        allNameFieldsMultiMatchQuery.boost(0.3F);


        return allNameFieldsMultiMatchQuery;
    }

    private MultiMatchQuery.Builder getMultiMatchQueryBuilderForPartyAliases() {
        List<String> fields = inSingleNameFieldMode ? allPartyAliasNamesFields : lastNameAndOrganisationNameAliasFields;
        if (hasOneName) {
            fields = fields.stream().filter(v -> !PARTY_ALIAS_LAST_NAME_FIELD.equals(v) && !PARTY_ALIAS_ORGANISATION_NAME_FIELD.equals(v)).toList();
        }
        final MultiMatchQuery.Builder partyAliasFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(allNames, fields);
        if (hasOneName) {
            partyAliasFieldsMultiMatchQuery.fields(asList(PARTY_ALIAS_LAST_NAME_FIELD+"^2.0f",PARTY_ALIAS_ORGANISATION_NAME_FIELD+"^2.0f" ));
        }

        partyAliasFieldsMultiMatchQuery.boost(0.3F);

        return partyAliasFieldsMultiMatchQuery;
    }

    private List<Query.Builder> getMustMatchQueryBuilderForPartiesList() {

        return getMustMatchQueryBuilderList(PARTY_FIRST_NAME_FIELD,
                PARTY_MIDDLE_NAME_FIELD,
                PARTY_LAST_NAME_FIELD
        );

    }

    private List<Query.Builder> getMustMatchQueryBuilderForPartyAliasesList() {

        return getMustMatchQueryBuilderList(PARTY_ALIAS_FIRST_NAME_FIELD,
                PARTY_ALIAS_MIDDLE_NAME_FIELD,
                PARTY_ALIAS_LAST_NAME_FIELD
        );

    }

    private List<Query.Builder> getMatchNgramQueryBuilderForPartiesList() {

        return getMatchNgramQueryBuilderList(PARTY_FIRST_NAME_NGRAM_FIELD,
                PARTY_LAST_NAME_NGRAM_FIELD
        );

    }


    private List<Query.Builder> getMatchNgramQueryBuilderForPartyAliasesList() {

        return getMatchNgramQueryBuilderList(PARTY_ALIAS_FIRST_NAME_NGRAM_FIELD,
                PARTY_ALIAS_LAST_NAME_NGRAM_FIELD
        );
    }

    private List<Query.Builder> getMustMatchQueryBuilderList(final String firstNameField, final String middleNameField, final String lastNameField) {

        if (!inSingleNameFieldMode) {
            return getMustMatchQueryBuilderListForSingleField(firstNameField, middleNameField, lastNameField);
        }

        final List<Query.Builder> queryBuilderList = new ArrayList<>();

        if (hasOneName) {
            queryBuilderList.add(convertBuilder(matchQueryBuilder(lastName, firstNameField)));
            queryBuilderList.add(convertBuilder(matchQueryBuilder(lastName, lastNameField)));
        } else if (hasTwoNames) {
            queryBuilderList.add(convertBuilder(matchQueryBuilder(firstName, firstNameField)));
            queryBuilderList.add(convertBuilder(matchQueryBuilder(lastName, lastNameField)));
        } else {
            queryBuilderList.add(convertBuilder(matchQueryBuilder(firstName, firstNameField)));
            queryBuilderList.add(convertBuilder(matchQueryBuilder(middleName, middleNameField)));
            queryBuilderList.add(convertBuilder(matchQueryBuilder(lastName, lastNameField)));
        }

        return queryBuilderList;
    }

    private List<Query.Builder> getMustMatchQueryBuilderListForSingleField(final String firstNameField, final String middleNameField, final String lastNameField) {

        final List<Query.Builder> queryBuilderList = new ArrayList<>();

        if (!firstName.isEmpty()) {
            queryBuilderList.add(convertBuilder(matchQueryBuilder(firstName, firstNameField)));
        }

        if (!middleName.isEmpty()) {
            queryBuilderList.add(convertBuilder(matchQueryBuilder(middleName, middleNameField)));
        }

        if (!lastName.isEmpty()) {
            queryBuilderList.add(convertBuilder(matchQueryBuilder(lastName, lastNameField)));
        }

        return queryBuilderList;
    }

    private List<Query.Builder> getMatchNgramQueryBuilderList(final String firstNameField, final String lastNameField) {

        if (!inSingleNameFieldMode) {
            return getMatchNgramQueryBuilderListForMultiFieldSearch(firstNameField, lastNameField);
        }

        final List<Query.Builder> queryBuilderList = new ArrayList<>();

        if (hasOneName) {
            queryBuilderList.add(convertBuilder(matchQueryBuilder(lastName, firstNameField, Optional.of(1.7f))));
            queryBuilderList.add(convertBuilder(matchQueryBuilder(lastName, lastNameField, Optional.of(1.5f))));
        } else {
            queryBuilderList.add(convertBuilder(matchQueryBuilder(firstName, firstNameField, Optional.of(1.7f))));
            queryBuilderList.add(convertBuilder(matchQueryBuilder(lastName, lastNameField, Optional.of(1.5f))));
        }

        return queryBuilderList;
    }

    private List<Query.Builder> getMatchNgramQueryBuilderListForMultiFieldSearch(final String firstNameField, final String lastNameField) {

        final List<Query.Builder> queryBuilderList = new ArrayList<>();

        if (!lastName.isEmpty()) {
            queryBuilderList.add(convertBuilder(matchQueryBuilder(lastName, lastNameField, Optional.of(1.7f))));
        }

        if (!firstName.isEmpty()) {
            queryBuilderList.add(convertBuilder(matchQueryBuilder(firstName, firstNameField, Optional.of(1.5f))));
        }

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

        final MultiMatchQuery.Builder builder = new MultiMatchQuery.Builder();
        builder.query(query).fields(fields).type(TextQueryType.CrossFields);
        return builder;
    }

    private Query.Builder wrapWithAdditionalPartyQueryBuilders(final Query.Builder queryBuilder) {
        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();
        this.additionalPartyQueryBuilders.forEach(booleanQueryBuilder::must);
        booleanQueryBuilder.must(queryBuilder.build());
        final Query.Builder builder = new Query.Builder();
        builder.bool(booleanQueryBuilder.build());
        return builder;
    }
}
