package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.google.common.collect.ImmutableList.of;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.withoutJsonPath;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;


import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NameSearchQueryBuilderTest {

    private NameSearchQueryBuilder builder = new NameSearchQueryBuilder();

    private Query partyTypesQuery;
    private Query dateOfBirthQuery;

    @BeforeEach
    public void setUp() {
        partyTypesQuery = new PartyTypeSearchQueryBuilder().getQueryBuilderBy("DEFENDANT,APPLICANT");
        dateOfBirthQuery = new DateOfBirthSearchQueryBuilder().getQueryBuilderBy("2018-01-01");
    }

    @Test
    public void shouldReturnValidQueryBuilderForThreeNameComponents() {

        final String nameQueryString = "John Allen Smith";
        final Query query = builder.getQueryBuilderBy(nameQueryString, emptyList());
        final String queryBuilderJson = query.toString().substring(7);

        validateResponseForNameQueryString(nameQueryString, "John", "Allen", "Smith", queryBuilderJson, 0);

    }

    @Test
    public void shouldReturnValidQueryBuilderForThreeNameComponentsAndPartyTypes() {
        final String nameQueryString = "John Allen Smith";
        final Query query = builder.getQueryBuilderBy(nameQueryString, of(partyTypesQuery, dateOfBirthQuery));
        final String queryBuilderJson = query.toString().substring(7);

        validateResponseForNameQueryString(nameQueryString, "John", "Allen", "Smith", queryBuilderJson, 2);

    }

    @Test
    public void shouldReturnValidQueryBuilderForTwoNameComponents() {

        final String nameQueryString = "John Smith";
        final Query query = builder.getQueryBuilderBy(nameQueryString, emptyList());
        final String queryBuilderJson = query.toString().substring(7);

        validateResponseForNameQueryString(nameQueryString, "John", null, "Smith", queryBuilderJson, 0);
    }

    @Test
    public void shouldReturnValidQueryBuilderForTwoNameComponentsAndPartyTypes() {

        final String nameQueryString = "John Smith";
        final Query query = builder.getQueryBuilderBy(nameQueryString, of(partyTypesQuery, dateOfBirthQuery));
        final String queryBuilderJson = query.toString().substring(7);

        validateResponseForNameQueryString(nameQueryString, "John", null, "Smith", queryBuilderJson, 2);
    }

    @Test
    public void shouldReturnValidQueryBuilderForOneNameComponents() {

        final String nameQueryString = "CapGemini";
        final Query query = builder.getQueryBuilderBy(nameQueryString, emptyList());
        final String queryBuilderJson = query.toString().substring(7);

        validateResponseForNameQueryString(nameQueryString, "CapGemini", null, null, queryBuilderJson, 0);

    }

    @Test
    public void shouldReturnValidQueryBuilderForOneNameComponentsAndPartyTypes() {

        final String nameQueryString = "CapGemini";
        final Query query = builder.getQueryBuilderBy(nameQueryString, of(partyTypesQuery, dateOfBirthQuery));
        final String queryBuilderJson = query.toString().substring(7);

        validateResponseForNameQueryString(nameQueryString, "CapGemini", null, null, queryBuilderJson, 2);

    }

    @Test
    public void shouldReturnValidQueryBuilderForMultiWordOrgNameComponents() {

        final String nameQueryString = "Marks and Spencers";
        final Query query = builder.getQueryBuilderBy(nameQueryString, emptyList());
        final String queryBuilderJson = query.toString().substring(7);

        validateResponseForNameQueryString(nameQueryString, "Marks", "and", "Spencers", queryBuilderJson, 0);
    }

    @Test
    public void shouldReturnValidQueryBuilderForFiveNameComponents() {

        final String nameQueryString = "John Allen De La Smith";
        final Query query = builder.getQueryBuilderBy(nameQueryString, emptyList());
        final String queryBuilderJson = query.toString().substring(7);

        validateResponseForNameQueryString(nameQueryString, "John", "Allen", "De", queryBuilderJson, 0);

    }

    @Test
    public void shouldReturnValidQueryBuilderForFiveNameComponentsAndPartyTypes() {

        final String nameQueryString = "John Allen De La Smith";
        final Query query = builder.getQueryBuilderBy(nameQueryString, of(partyTypesQuery));
        final String queryBuilderJson = query.toString().substring(7);

        validateResponseForNameQueryString(nameQueryString, "John", "Allen", "De", queryBuilderJson, 1);

    }

    @Test
    public void shouldReturnValidQueryBuilderForTwoNameWithoutAlias() {

        final String nameQueryString = "John Smith";
        final Query query = builder.getQueryBuilderBy(nameQueryString, emptyList());
        final String queryBuilderJson = query.toString().substring(7);

        validateResponseForNameQueryString(nameQueryString, "John", null, "Smith", queryBuilderJson, 0);

    }

    @Test
    public void shouldReturnValidQueryBuilderForTwoNameWithoutAliasAndPartyTypes() {

        final String nameQueryString = "John Smith";
        final Query query = builder.getQueryBuilderBy(nameQueryString, of(partyTypesQuery));
        final String queryBuilderJson = query.toString().substring(7);

        validateResponseForNameQueryString(nameQueryString, "John", null, "Smith", queryBuilderJson, 1);

    }

    @Test
    public void shouldReturnValidQueryBuilderForTwoNameWithoutAliasAndDateOfBirth() {

        final String nameQueryString = "John Smith";
        final Query query = builder.getQueryBuilderBy(nameQueryString, of(partyTypesQuery, dateOfBirthQuery));
        final String queryBuilderJson = query.toString().substring(7);

        validateResponseForNameQueryString(nameQueryString, "John", null, "Smith", queryBuilderJson, 2);

    }

    private void validateResponseForNameQueryString(final String fullName, final String firstName, final String middleName, final String lastName, final String queryBuilderJson, final int additionalPartyFilterSize) {

        assertThat(queryBuilderJson, isJson());

        final int lastNameIndex;
        final int lastNameShouldIndex = 5;
        final int orgNameShouldIndex = 6;
        if (middleName != null) {
            lastNameIndex = 2;
        } else {
            lastNameIndex = 1;
        }

        assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[*]", hasSize(2)));

        if (firstName != null) {
            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[0].bool.must[" + additionalPartyFilterSize + "].match['parties.firstName'].query", equalTo(firstName)));
            assertThat(queryBuilderJson, isJson(withoutJsonPath("$.dis_max.queries[0].nested.query.bool.should[0].bool.must[" + additionalPartyFilterSize + "].match['parties.firstName'].fuzziness")));

            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[1].bool.must[" + additionalPartyFilterSize + "].match['parties.firstName'].query", equalTo(firstName)));
            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[1].bool.must[" + additionalPartyFilterSize + "].match['parties.firstName'].fuzziness", equalTo("AUTO")));

            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[4].bool.must[" + additionalPartyFilterSize + "].match['parties.firstName.ngrammed'].query", equalTo(firstName)));

            if (middleName == null && lastName == null) {
                assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[4].bool.must[" + additionalPartyFilterSize + "].match['parties.firstName.ngrammed'].boost", is(closeTo(1.7f, 0.0001f))));


                    assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[4].bool.must[" + additionalPartyFilterSize + "].match['parties.aliases.firstName.ngrammed'].boost", is(closeTo(1.7f, 0.0001f))));;

            } else {
                assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[4].bool.must[" + additionalPartyFilterSize + "].match['parties.firstName.ngrammed'].boost", is(closeTo(1.7f, 0.0001f))));

                    assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[4].bool.must[" + additionalPartyFilterSize + "].match['parties.aliases.firstName.ngrammed'].boost", is(closeTo(1.7f, 0.0001f))));

            }

                assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[0].bool.must[" + additionalPartyFilterSize + "].match['parties.aliases.firstName'].query", equalTo(firstName)));
                assertThat(queryBuilderJson, isJson(withoutJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[0].bool.must[" + additionalPartyFilterSize + "].match['parties.aliases.firstName'].fuzziness")));

                assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[1].bool.must[" + additionalPartyFilterSize + "].match['parties.aliases.firstName'].query", equalTo(firstName)));
                assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[1].bool.must[" + additionalPartyFilterSize + "].match['parties.aliases.firstName'].fuzziness", equalTo("AUTO")));

                assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[4].bool.must[" + additionalPartyFilterSize + "].match['parties.aliases.firstName.ngrammed'].query", equalTo(firstName)));

        }

        if (middleName != null) {
            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[0].bool.must[" + (additionalPartyFilterSize + 1) + "].match['parties.middleName'].query", equalTo(middleName)));
            assertThat(queryBuilderJson, isJson(withoutJsonPath("$.dis_max.queries[0].nested.query.bool.should[0].bool.must[" + (additionalPartyFilterSize + 1) + "].match['parties.middleName'].fuzziness")));

            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[1].bool.must[" + (additionalPartyFilterSize + 1) + "].match['parties.middleName'].query", equalTo(middleName)));
            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[1].bool.must[" + (additionalPartyFilterSize + 1) + "].match['parties.middleName'].fuzziness", equalTo("AUTO")));


                assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[0].bool.must[" + (additionalPartyFilterSize + 1) + "].match['parties.aliases.middleName'].query", equalTo(middleName)));
                assertThat(queryBuilderJson, isJson(withoutJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[0].bool.must[" + (additionalPartyFilterSize + 1) + "].match['parties.aliases.middleName'].fuzziness")));

                assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[1].bool.must[" + (additionalPartyFilterSize + 1) + "].match['parties.aliases.middleName'].query", equalTo(middleName)));
                assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[1].bool.must[" + (additionalPartyFilterSize + 1) + "].match['parties.aliases.middleName'].fuzziness", equalTo("AUTO")));


        }

        if (lastName != null) {
            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[0].bool.must[" + (lastNameIndex + additionalPartyFilterSize) + "].match['parties.lastName'].query", equalTo(lastName)));
            assertThat(queryBuilderJson, isJson(withoutJsonPath("$.dis_max.queries[0].nested.query.bool.should[0].bool.must[" + (lastNameIndex + additionalPartyFilterSize) + "].match['parties.lastName'].fuzziness")));

            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[1].bool.must[" + (lastNameIndex + additionalPartyFilterSize) + "].match['parties.lastName'].query", equalTo(lastName)));
            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[1].bool.must[" + (lastNameIndex + additionalPartyFilterSize) + "].match['parties.lastName'].fuzziness", equalTo("AUTO")));

            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[" + lastNameShouldIndex + "].bool.must[" + additionalPartyFilterSize + "].match['parties.lastName.ngrammed'].query", equalTo(lastName)));
            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[" + lastNameShouldIndex + "].bool.must[" + additionalPartyFilterSize + "].match['parties.lastName.ngrammed'].boost", equalTo(1.5)));

                assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[0].bool.must[" + (lastNameIndex + additionalPartyFilterSize) + "].match['parties.aliases.lastName'].query", equalTo(lastName)));
                assertThat(queryBuilderJson, isJson(withoutJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[0].bool.must[" + (lastNameIndex + additionalPartyFilterSize) + "].match['parties.aliases.lastName'].fuzziness")));

                assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[1].bool.must[" + (lastNameIndex + additionalPartyFilterSize) + "].match['parties.aliases.lastName'].query", equalTo(lastName)));
                assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[1].bool.must[" + (lastNameIndex + additionalPartyFilterSize) + "].match['parties.aliases.lastName'].fuzziness", equalTo("AUTO")));

                assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[" + lastNameShouldIndex + "].bool.must[" + additionalPartyFilterSize + "].match['parties.aliases.lastName.ngrammed'].query", equalTo(lastName)));
                assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[" + lastNameShouldIndex + "].bool.must[" + additionalPartyFilterSize + "].match['parties.aliases.lastName.ngrammed'].boost", equalTo(1.5)));

        }


        assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[0].bool.boost", equalTo(3.0)));
        assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[1].bool.boost", equalTo(2.0)));

        assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[2].bool.must[" + additionalPartyFilterSize + "].multi_match.query", equalTo(fullName)));
        assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[2].bool.must[" + additionalPartyFilterSize + "].multi_match.operator", equalTo("and")));
        assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[2].bool.must[" + additionalPartyFilterSize + "].multi_match.type", equalTo("cross_fields")));
        assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[2].bool.must[" + additionalPartyFilterSize + "].multi_match.fields", hasSize(4)));

        if (middleName == null && lastName == null) {
            assertThat(queryBuilderJson,
                    hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[2].bool.must[" + additionalPartyFilterSize + "].multi_match.fields",
                            containsInAnyOrder("parties.firstName", "parties.lastName^2.0f", "parties.middleName", "parties.organisationName^2.0f")));
        } else {
            assertThat(queryBuilderJson,
                    hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[2].bool.must[" + additionalPartyFilterSize + "].multi_match.fields",
                            containsInAnyOrder("parties.firstName", "parties.lastName", "parties.middleName", "parties.organisationName")));
        }

        assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[3].bool.must[" + additionalPartyFilterSize + "].multi_match.query", equalTo(fullName)));
        assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[3].bool.must[" + additionalPartyFilterSize + "].multi_match.operator", equalTo("or")));
        assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[3].bool.must[" + additionalPartyFilterSize + "].multi_match.type", equalTo("cross_fields")));
        assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[3].bool.must[" + additionalPartyFilterSize + "].multi_match.fields", hasSize(4)));

        if (middleName == null && lastName == null) {
            assertThat(queryBuilderJson,
                    hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[3].bool.must[" + additionalPartyFilterSize + "].multi_match.fields",
                            containsInAnyOrder("parties.firstName", "parties.lastName^2.0f", "parties.middleName", "parties.organisationName^2.0f")));
        } else {
            assertThat(queryBuilderJson,
                    hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[3].bool.must[" + additionalPartyFilterSize + "].multi_match.fields",
                            containsInAnyOrder("parties.firstName", "parties.lastName", "parties.middleName", "parties.organisationName")));
        }


        assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[" + orgNameShouldIndex + "].bool.must[" + additionalPartyFilterSize + "].match['parties.organisationName'].query", equalTo(fullName)));
        assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[" + orgNameShouldIndex + "].bool.must[" + additionalPartyFilterSize + "].match['parties.organisationName'].fuzziness", equalTo("AUTO")));
        assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[0].nested.query.bool.should[" + orgNameShouldIndex + "].bool.must[" + additionalPartyFilterSize + "].match['parties.organisationName'].boost", equalTo(2.0)));

            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[0].bool.boost", equalTo(3.0)));
            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[1].bool.boost", equalTo(2.0)));


            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[2].bool.must[" + additionalPartyFilterSize + "].multi_match.query", equalTo(fullName)));
            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[2].bool.must[" + additionalPartyFilterSize + "].multi_match.operator", equalTo("and")));
            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[2].bool.must[" + additionalPartyFilterSize + "].multi_match.type", equalTo("cross_fields")));
            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[2].bool.must[" + additionalPartyFilterSize + "].multi_match.fields", hasSize(4)));

            if (middleName == null && lastName == null) {
                assertThat(queryBuilderJson,
                        hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[2].bool.must[" + additionalPartyFilterSize + "].multi_match.fields",
                                containsInAnyOrder("parties.aliases.firstName", "parties.aliases.lastName^2.0f", "parties.aliases.middleName", "parties.aliases.organisationName^2.0f")));
            } else {
                assertThat(queryBuilderJson,
                        hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[2].bool.must[" + additionalPartyFilterSize + "].multi_match.fields",
                                containsInAnyOrder("parties.aliases.firstName", "parties.aliases.lastName", "parties.aliases.middleName", "parties.aliases.organisationName")));
            }

            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[3].bool.must[" + additionalPartyFilterSize + "].multi_match.query", equalTo(fullName)));
            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[3].bool.must[" + additionalPartyFilterSize + "].multi_match.operator", equalTo("or")));
            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[3].bool.must[" + additionalPartyFilterSize + "].multi_match.type", equalTo("cross_fields")));
            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[3].bool.must[" + additionalPartyFilterSize + "].multi_match.fields", hasSize(4)));

            if (middleName == null && lastName == null) {
                assertThat(queryBuilderJson,
                        hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[3].bool.must[" + additionalPartyFilterSize + "].multi_match.fields",
                                containsInAnyOrder("parties.aliases.firstName", "parties.aliases.lastName^2.0f", "parties.aliases.middleName", "parties.aliases.organisationName^2.0f")));
            } else {
                assertThat(queryBuilderJson,
                        hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[3].bool.must[" + additionalPartyFilterSize + "].multi_match.fields",
                                containsInAnyOrder("parties.aliases.firstName", "parties.aliases.lastName", "parties.aliases.middleName", "parties.aliases.organisationName")));
            }


            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[" + orgNameShouldIndex + "].bool.must[" + additionalPartyFilterSize + "].match['parties.aliases.organisationName'].query", equalTo(fullName)));
            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[" + orgNameShouldIndex + "].bool.must[" + additionalPartyFilterSize + "].match['parties.aliases.organisationName'].fuzziness", equalTo("AUTO")));
            assertThat(queryBuilderJson, hasJsonPath("$.dis_max.queries[1].nested.query.nested.query.bool.should[" + orgNameShouldIndex + "].bool.must[" + additionalPartyFilterSize + "].match['parties.aliases.organisationName'].boost", equalTo(2.0)));

        }

}
