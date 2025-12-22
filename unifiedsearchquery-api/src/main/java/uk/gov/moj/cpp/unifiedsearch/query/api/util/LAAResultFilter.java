package uk.gov.moj.cpp.unifiedsearch.query.api.util;

import java.util.Map;
import java.util.function.Predicate;

import uk.gov.justice.services.messaging.JsonObjects;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class LAAResultFilter {

    private static final String DEFENDANT_LAST_NAME = "defendantLastName";
    private static final String ORGANISATION_NAME = "organisationName";
    private static final String CASES = "cases";
    private static final String DEFENDANT_SUMMARY = "defendantSummary";
    private static final String EMPTY_STRING = "";


    private Predicate<JsonObject> validDefendantPredicate = (def ->  {
        final boolean isValidPersonDefendant =  def.containsKey(DEFENDANT_LAST_NAME) &&
                !def.getString(DEFENDANT_LAST_NAME, EMPTY_STRING).isEmpty();
        final boolean isValidOrganisationDefendant = def.containsKey(ORGANISATION_NAME) &&
                !def.getString(ORGANISATION_NAME, EMPTY_STRING).isEmpty();
        return  isValidPersonDefendant || isValidOrganisationDefendant;

    });
    public JsonObject filter(final JsonObject input) {
        final JsonObjectBuilder outputBuilder = JsonObjects.createObjectBuilder();
        input.entrySet().stream()
                .filter(e->!e.getKey().equalsIgnoreCase(CASES))
                .forEach(e -> outputBuilder.add(e.getKey(),e.getValue()));

        final JsonArrayBuilder caseArrayBuilder = JsonObjects.createArrayBuilder();
        input.getJsonArray(CASES).stream()
                .map(e -> (JsonObject)e)
                .forEach(e-> {
                    final JsonObjectBuilder caseBuilder = JsonObjects.createObjectBuilder();
                    e.entrySet().stream()
                            .forEach(el -> {
                                if (isDefendantSummary(el)) {
                                    caseBuilder.add(el.getKey(),getValidDefendantSummaryArrayObject(e));
                                } else {
                                    caseBuilder.add(el.getKey(),el.getValue());
                                }
                            });
                    caseArrayBuilder.add(caseBuilder);
                });
        outputBuilder.add(CASES, caseArrayBuilder);
        return outputBuilder.build();
    }

    private JsonArrayBuilder getValidDefendantSummaryArrayObject(final JsonObject el) {

        final JsonArray defendantSummaries = el.getJsonArray(DEFENDANT_SUMMARY);
        final JsonArrayBuilder defendantSummariesBuilder = JsonObjects.createArrayBuilder();
        defendantSummaries.stream()
                .map(e -> (JsonObject)e)
                .filter(validDefendantPredicate)
                .forEach(defendantSummariesBuilder::add);
        return defendantSummariesBuilder;
    }

    private static boolean isDefendantSummary(final Map.Entry<String, JsonValue> el) {
        return el.getKey().equalsIgnoreCase(DEFENDANT_SUMMARY);
    }
}
