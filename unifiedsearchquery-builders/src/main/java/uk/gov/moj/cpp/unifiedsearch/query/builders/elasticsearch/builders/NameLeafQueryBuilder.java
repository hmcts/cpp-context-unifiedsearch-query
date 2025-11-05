package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.google.common.collect.ImmutableList.of;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
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
    private final List<QueryBuilder> additionalPartyQueryBuilders;

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

    public NameLeafQueryBuilder(final String allNames, final List<QueryBuilder> additionalPartyQueryBuilders) {

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

    public NameLeafQueryBuilder(final String partyFirstAndOrMiddleName, final String partyLastNameOrOrganisationName, final List<QueryBuilder> additionalPartyQueryBuilders) {
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


    public QueryBuilder nestedPartiesBuilder() {

        final BoolQueryBuilder booleanQueryBuilder = boolQuery();
        booleanQueryBuilder.should(getMustMatchQueryBuilderForParties());
        booleanQueryBuilder.should(getMustMatchWithFuzzinessQueryBuilderForParties());
        if (!allNames.isEmpty()) {
            booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(getMultiMatchQueryBuilderForParties().operator(AND).boost(0.4F)));
            booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(getMultiMatchQueryBuilderForParties().operator(OR)));
        }

        if (!inSingleNameFieldMode && !firstName.isEmpty() && !middleName.isEmpty()) {
            booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(getMultiMatchQueryBuilderForFirstAndMiddleName().operator(AND).boost(0.1F)));
            booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(getMultiMatchQueryBuilderForFirstAndMiddleName().operator(OR)));
        }

        final List<QueryBuilder> prefixQueryBuilderList = getMatchNgramQueryBuilderForPartiesList();
        if (enableORQuery()) {
            prefixQueryBuilderList.forEach(queryBuilder -> booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(queryBuilder)));
        } else {
            prefixQueryBuilderList.forEach(queryBuilder -> booleanQueryBuilder.must(wrapWithAdditionalPartyQueryBuilders(queryBuilder)));
        }

        final MatchQueryBuilder organisationMatchBuilder = matchQueryBuilder(organisationName, PARTY_ORGANISATION_NAME_FIELD);
        organisationMatchBuilder.fuzziness(FUZZINESS_AUTO);
        organisationMatchBuilder.boost(2.0f);

        booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(organisationMatchBuilder));

        return nestedQuery(PARTY_NESTED_PATH, booleanQueryBuilder, ScoreMode.Avg);
    }

    public QueryBuilder nestedPartyAliasesBuilder() {

        final BoolQueryBuilder booleanQueryBuilder = boolQuery();

        booleanQueryBuilder.should(getMustMatchQueryBuilderForPartyAliases());
        booleanQueryBuilder.should(getMustMatchWithFuzzinessQueryBuilderForPartyAliases());

        if (!allNames.isEmpty()) {
            booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(getMultiMatchQueryBuilderForPartyAliases().operator(AND).boost(0.4F)));
            booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(getMultiMatchQueryBuilderForPartyAliases().operator(OR)));
        }

        final List<QueryBuilder> prefixQueryBuilderList = getMatchNgramQueryBuilderForPartyAliasesList();
        if (enableORQuery()) {
            prefixQueryBuilderList.forEach(queryBuilder -> booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(queryBuilder)));
        } else {
            prefixQueryBuilderList.forEach(queryBuilder -> booleanQueryBuilder.must(wrapWithAdditionalPartyQueryBuilders(queryBuilder)));
        }

        final MatchQueryBuilder organisationMatchBuilder = matchQueryBuilder(organisationName, PARTY_ALIAS_ORGANISATION_NAME_FIELD);
        organisationMatchBuilder.fuzziness(FUZZINESS_AUTO);
        organisationMatchBuilder.boost(2.0f);
        booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(organisationMatchBuilder));

        final NestedQueryBuilder partyAliasesNestedQueryBuilder = nestedQuery(PARTY_ALIAS_NESTED_PATH, booleanQueryBuilder, ScoreMode.Avg);

        return nestedQuery(PARTY_NESTED_PATH, partyAliasesNestedQueryBuilder, ScoreMode.Avg);
    }

    private boolean enableORQuery() {
        return inSingleNameFieldMode || hasOneName;
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
        final List<String> fields = inSingleNameFieldMode ? allPartyNamesFields : lastNameAndOrganisationNameFields;
        final MultiMatchQueryBuilder allNameFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(allNames, fields);
        if (hasOneName) {
            allNameFieldsMultiMatchQuery.field(PARTY_LAST_NAME_FIELD, 2.0f);
            allNameFieldsMultiMatchQuery.field(PARTY_ORGANISATION_NAME_FIELD, 2.0f);
        }

        allNameFieldsMultiMatchQuery.boost(0.3F);


        return allNameFieldsMultiMatchQuery;
    }

    private MultiMatchQueryBuilder getMultiMatchQueryBuilderForFirstAndMiddleName() {
        final MultiMatchQueryBuilder allNameFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(partyFirstAndOrMiddleName, firstAndMiddleNameFields);

        allNameFieldsMultiMatchQuery.field(PARTY_FIRST_NAME_FIELD, 1.10f);

        allNameFieldsMultiMatchQuery.boost(0.3F);


        return allNameFieldsMultiMatchQuery;
    }

    private MultiMatchQueryBuilder getMultiMatchQueryBuilderForPartyAliases() {
        final List<String> fields = inSingleNameFieldMode ? allPartyAliasNamesFields : lastNameAndOrganisationNameAliasFields;
        final MultiMatchQueryBuilder partyAliasFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(allNames, fields);
        if (hasOneName) {
            partyAliasFieldsMultiMatchQuery.field(PARTY_ALIAS_LAST_NAME_FIELD, 2.0f);
            partyAliasFieldsMultiMatchQuery.field(PARTY_ALIAS_ORGANISATION_NAME_FIELD, 2.0f);
        }

        partyAliasFieldsMultiMatchQuery.boost(0.3F);

        return partyAliasFieldsMultiMatchQuery;
    }

    private List<QueryBuilder> getMustMatchQueryBuilderForPartiesList() {

        return getMustMatchQueryBuilderList(PARTY_FIRST_NAME_FIELD,
                PARTY_MIDDLE_NAME_FIELD,
                PARTY_LAST_NAME_FIELD
        );

    }

    private List<QueryBuilder> getMustMatchQueryBuilderForPartyAliasesList() {

        return getMustMatchQueryBuilderList(PARTY_ALIAS_FIRST_NAME_FIELD,
                PARTY_ALIAS_MIDDLE_NAME_FIELD,
                PARTY_ALIAS_LAST_NAME_FIELD
        );

    }

    private List<QueryBuilder> getMatchNgramQueryBuilderForPartiesList() {

        return getMatchNgramQueryBuilderList(PARTY_FIRST_NAME_NGRAM_FIELD,
                PARTY_LAST_NAME_NGRAM_FIELD
        );

    }


    private List<QueryBuilder> getMatchNgramQueryBuilderForPartyAliasesList() {

        return getMatchNgramQueryBuilderList(PARTY_ALIAS_FIRST_NAME_NGRAM_FIELD,
                PARTY_ALIAS_LAST_NAME_NGRAM_FIELD
        );
    }

    private List<QueryBuilder> getMustMatchQueryBuilderList(final String firstNameField, final String middleNameField, final String lastNameField) {

        if (!inSingleNameFieldMode) {
            return getMustMatchQueryBuilderListForSingleField(firstNameField, middleNameField, lastNameField);
        }

        final List<QueryBuilder> queryBuilderList = new ArrayList<>();

        if (hasOneName) {
            queryBuilderList.add(matchQueryBuilder(lastName, firstNameField));
            queryBuilderList.add(matchQueryBuilder(lastName, lastNameField));
        } else if (hasTwoNames) {
            queryBuilderList.add(matchQueryBuilder(firstName, firstNameField));
            queryBuilderList.add(matchQueryBuilder(lastName, lastNameField));
        } else {
            queryBuilderList.add(matchQueryBuilder(firstName, firstNameField));
            queryBuilderList.add(matchQueryBuilder(middleName, middleNameField));
            queryBuilderList.add(matchQueryBuilder(lastName, lastNameField));
        }

        return queryBuilderList;
    }

    private List<QueryBuilder> getMustMatchQueryBuilderListForSingleField(final String firstNameField, final String middleNameField, final String lastNameField) {

        final List<QueryBuilder> queryBuilderList = new ArrayList<>();

        if (!firstName.isEmpty()) {
            queryBuilderList.add(matchQueryBuilder(firstName, firstNameField));
        }

        if (!middleName.isEmpty()) {
            queryBuilderList.add(matchQueryBuilder(middleName, middleNameField));
        }

        if (!lastName.isEmpty()) {
            queryBuilderList.add(matchQueryBuilder(lastName, lastNameField));
        }

        return queryBuilderList;
    }

    private List<QueryBuilder> getMatchNgramQueryBuilderList(final String firstNameField, final String lastNameField) {

        if (!inSingleNameFieldMode) {
            return getMatchNgramQueryBuilderListForMultiFieldSearch(firstNameField, lastNameField);
        }

        final List<QueryBuilder> queryBuilderList = new ArrayList<>();

        if (hasOneName) {
            queryBuilderList.add(matchQueryBuilder(lastName, firstNameField, Optional.of(1.7f)));
            queryBuilderList.add(matchQueryBuilder(lastName, lastNameField, Optional.of(1.5f)));
        } else {
            queryBuilderList.add(matchQueryBuilder(firstName, firstNameField, Optional.of(1.7f)));
            queryBuilderList.add(matchQueryBuilder(lastName, lastNameField, Optional.of(1.5f)));
        }

        return queryBuilderList;
    }

    private List<QueryBuilder> getMatchNgramQueryBuilderListForMultiFieldSearch(final String firstNameField, final String lastNameField) {

        final List<QueryBuilder> queryBuilderList = new ArrayList<>();

        if (!lastName.isEmpty()) {
            queryBuilderList.add(matchQueryBuilder(lastName, lastNameField, Optional.of(1.7f)));
        }

        if (!firstName.isEmpty()) {
            queryBuilderList.add(matchQueryBuilder(firstName, firstNameField, Optional.of(1.5f)));
        }

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
