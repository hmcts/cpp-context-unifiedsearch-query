package uk.gov.moj.cpp.unifiedsearch.query.api.util;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
@SuppressWarnings("squid:S2187")
@ExtendWith(MockitoExtension.class)
public class LAAResultFilterTest  {

    @InjectMocks
    private LAAResultFilter laaResultFilter;

    @Test
    public void shouldFilterResults() {

        final String CASE_ID_1 = UUID.randomUUID().toString();
        final String CASE_ID_2 = UUID.randomUUID().toString();
        final String CASE_ID_3 = UUID.randomUUID().toString();
        final String CASE_ID_4 = UUID.randomUUID().toString();

        final String CASE_REF_1 = "caseReference1";
        final String CASE_REF_2 = "caseReference2";
        final String CASE_REF_3 = "caseReference3";
        final String CASE_REF_4 = "caseReference4";

        final String DEFENDANT_ID_PERSON = UUID.randomUUID().toString();
        final String DEFENDANT_ID_ORGANISATION = UUID.randomUUID().toString();
        final String DEFENDANT_FIRST_NAME = "defendantFirstName";
        final String DEFENDANT_LAST_NAME = "defendantLastName";
        final String ORGANISATION_NAME = "organisationName";
        final String MASTER_DEF_ID = UUID.randomUUID().toString();

        final JsonObject partyValidPersonDefendant = Json.createObjectBuilder()
                .add("defendantId", DEFENDANT_ID_PERSON)
                .add("defendantFirstName", DEFENDANT_FIRST_NAME)
                .add("defendantLastName", DEFENDANT_LAST_NAME)
                .build();
        final JsonObject partyInvalidDefendant = Json.createObjectBuilder()
                .add("defendantId", UUID.randomUUID().toString())
                .build();
        final JsonObject partyValidOrganisationDefendant = Json.createObjectBuilder()
                .add("defendantId", DEFENDANT_ID_ORGANISATION)
                .add("organisationName", ORGANISATION_NAME)
                .add("masterDefendantId", MASTER_DEF_ID)
                .build();


        final JsonArray partiesDefendant = Json.createArrayBuilder()
                .add(partyValidPersonDefendant)
                .add(partyInvalidDefendant)
                .build();

        final JsonArray partiesOrganisation = Json.createArrayBuilder()
                .add(partyValidOrganisationDefendant)
                .add(partyInvalidDefendant)
                .build();

        final JsonArray partiesAll = Json.createArrayBuilder()
                .add(partyValidPersonDefendant)
                .add(partyInvalidDefendant)
                .add(partyValidOrganisationDefendant)
                .add(partyInvalidDefendant)
                .build();
        final JsonArray partiesNone = Json.createArrayBuilder()
                .build();

        final JsonObject case1 = Json.createObjectBuilder()
                .add("caseStatus", "ACTIVE")
                .add("prosecutionCaseId", CASE_ID_1)
                .add("prosecutionCaseReference", CASE_REF_1)
                .add("defendantSummary", partiesDefendant)

                .build();
        final JsonObject case2 = Json.createObjectBuilder()
                .add("caseStatus", "INACTIVE")
                .add("prosecutionCaseId", CASE_ID_2)
                .add("prosecutionCaseReference", CASE_REF_2)
                .add("defendantSummary", partiesOrganisation)
                .build();
        final JsonObject case3 = Json.createObjectBuilder()
                .add("caseStatus", "INACTIVE")
                .add("prosecutionCaseId", CASE_ID_3)
                .add("prosecutionCaseReference", CASE_REF_3)
                .add("defendantSummary", partiesAll)
                .build();
        final JsonObject case4 = Json.createObjectBuilder()
                .add("caseStatus", "INACTIVE")
                .add("prosecutionCaseId", CASE_ID_4)
                .add("prosecutionCaseReference", CASE_REF_4)
                .add("defendantSummary", partiesNone)
                .build();
        final JsonArray cases = Json.createArrayBuilder()
                .add(case1)
                .add(case2)
                .add(case3)
                .add(case4)
                .build();
        JsonObjectBuilder resultsBuilder = Json.createObjectBuilder();
        resultsBuilder.add("totalResults", "1");
        resultsBuilder.add("cases", cases);

        final JsonObject input = resultsBuilder.build();

        JsonObject output = laaResultFilter.filter(input);
        // assert JSON structure
        with(output.toString())
                .assertThat("$.totalResults",equalTo("1"))
                .assertThat("$.cases[0].defendantSummary",hasSize(1))
                .assertThat("$.cases[1].defendantSummary",hasSize(1))
                .assertThat("$.cases[2].defendantSummary",hasSize(2))
                .assertThat("$.cases[3].defendantSummary",hasSize(0));

        // assert defendant core attribute
        with(output.toString())
                .assertThat("$.cases[0].defendantSummary[0].defendantId",equalTo(DEFENDANT_ID_PERSON))
                .assertThat("$.cases[1].defendantSummary[0].defendantId",equalTo(DEFENDANT_ID_ORGANISATION))
                .assertThat("$.cases[2].defendantSummary[0].defendantId",equalTo(DEFENDANT_ID_PERSON))
                .assertThat("$.cases[2].defendantSummary[1].defendantId",equalTo(DEFENDANT_ID_ORGANISATION));

        // assert defendant additional attribute
        with(output.toString())
                .assertThat("$.cases[0].defendantSummary[0].defendantFirstName",equalTo(DEFENDANT_FIRST_NAME))
                .assertThat("$.cases[0].defendantSummary[0].defendantLastName",equalTo(DEFENDANT_LAST_NAME))
                .assertThat("$.cases[1].defendantSummary[0].organisationName",equalTo(ORGANISATION_NAME))
                .assertThat("$.cases[2].defendantSummary[1].masterDefendantId",equalTo(MASTER_DEF_ID));

        // assert case additional attribute
        with(output.toString())
                .assertThat("$.cases[0].prosecutionCaseId",equalTo(CASE_ID_1))
                .assertThat("$.cases[1].prosecutionCaseId",equalTo(CASE_ID_2))
                .assertThat("$.cases[2].prosecutionCaseId",equalTo(CASE_ID_3))
                .assertThat("$.cases[3].prosecutionCaseId",equalTo(CASE_ID_4));

        with(output.toString())
                .assertThat("$.cases[0].prosecutionCaseReference",equalTo(CASE_REF_1))
                .assertThat("$.cases[1].prosecutionCaseReference",equalTo(CASE_REF_2))
                .assertThat("$.cases[2].prosecutionCaseReference",equalTo(CASE_REF_3))
                .assertThat("$.cases[3].prosecutionCaseReference",equalTo(CASE_REF_4));






        // assertion will go here

    }
}