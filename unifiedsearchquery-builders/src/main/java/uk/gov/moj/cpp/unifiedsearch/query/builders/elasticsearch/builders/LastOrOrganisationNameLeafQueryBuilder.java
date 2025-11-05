package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.google.common.collect.ImmutableList.of;
import static java.util.Arrays.asList;
import static org.elasticsearch.index.query.MultiMatchQueryBuilder.Type.CROSS_FIELDS;
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
    private final List<QueryBuilder> additionalPartyQueryBuilders;

    private String allNames;

    public LastOrOrganisationNameLeafQueryBuilder(final String allNames, final List<QueryBuilder> additionalPartyQueryBuilders) {

        this.additionalPartyQueryBuilders = additionalPartyQueryBuilders == null ? of() : additionalPartyQueryBuilders;
        this.allNames = allNames.trim();

    }

    public QueryBuilder nestedPartiesBuilder() {

        final BoolQueryBuilder booleanQueryBuilder = boolQuery();
        booleanQueryBuilder.should(getMustMatchQueryBuilderForParties());
        booleanQueryBuilder.should(getMustMatchWithFuzzinessQueryBuilderForParties());
        booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(getMultiMatchQueryBuilderForParties().operator(OR)));

        final List<QueryBuilder> prefixQueryBuilderList = getMatchNgramQueryBuilderForPartiesList();
        prefixQueryBuilderList.forEach(queryBuilder -> booleanQueryBuilder.should(wrapWithAdditionalPartyQueryBuilders(queryBuilder)));

        return nestedQuery(PARTY_NESTED_PATH, booleanQueryBuilder, ScoreMode.Avg);
    }

    public QueryBuilder nestedPartyAliasesBuilder() {

        final BoolQueryBuilder booleanQueryBuilder = boolQuery();

        booleanQueryBuilder.should(getMustMatchQueryBuilderForPartyAliases());
        booleanQueryBuilder.should(getMustMatchWithFuzzinessQueryBuilderForPartyAliases());
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
        final MultiMatchQueryBuilder allNameFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(allNames, allPartyNamesFields);
        allNameFieldsMultiMatchQuery.field(PARTY_LAST_NAME_FIELD, 2.0f);
        allNameFieldsMultiMatchQuery.field(PARTY_ORGANISATION_NAME_FIELD, 2.0f);
        allNameFieldsMultiMatchQuery.boost(0.3F);
        return allNameFieldsMultiMatchQuery;
    }

    private MultiMatchQueryBuilder getMultiMatchQueryBuilderForPartyAliases() {
        final MultiMatchQueryBuilder partyAliasFieldsMultiMatchQuery = crossFieldsMultiMatchQueryBuilder(allNames, allPartyAliasNamesFields);
        partyAliasFieldsMultiMatchQuery.field(PARTY_ALIAS_LAST_NAME_FIELD, 2.0f);
        partyAliasFieldsMultiMatchQuery.field(PARTY_ALIAS_ORGANISATION_NAME_FIELD, 2.0f);
        partyAliasFieldsMultiMatchQuery.boost(0.3F);
        return partyAliasFieldsMultiMatchQuery;
    }

    private List<QueryBuilder> getMustMatchQueryBuilderForPartiesList() {

        return getMustMatchQueryBuilderList(PARTY_LAST_NAME_FIELD, PARTY_ORGANISATION_NAME_FIELD);

    }

    private List<QueryBuilder> getMustMatchQueryBuilderForPartyAliasesList() {

        return getMustMatchQueryBuilderList(PARTY_ALIAS_LAST_NAME_FIELD, PARTY_ALIAS_ORGANISATION_NAME_FIELD);

    }

    private List<QueryBuilder> getMatchNgramQueryBuilderForPartiesList() {

        return getMatchNgramQueryBuilderList(PARTY_LAST_NAME_NGRAM_FIELD);
    }

    private List<QueryBuilder> getMatchNgramQueryBuilderForPartyAliasesList() {

        return getMatchNgramQueryBuilderList(PARTY_ALIAS_LAST_NAME_NGRAM_FIELD);
    }

    private List<QueryBuilder> getMustMatchQueryBuilderList(final String lastNameField, final String orgNameField) {

        final List<QueryBuilder> queryBuilderList = new ArrayList<>();
        queryBuilderList.add(matchQueryBuilder(allNames, lastNameField));
        queryBuilderList.add(matchQueryBuilder(allNames, orgNameField));
        return queryBuilderList;
    }

    private List<QueryBuilder> getMatchNgramQueryBuilderList(final String fieldName) {

        final List<QueryBuilder> queryBuilderList = new ArrayList<>();

        queryBuilderList.add(matchQueryBuilder(allNames, fieldName, Optional.of(1.7f)));

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
