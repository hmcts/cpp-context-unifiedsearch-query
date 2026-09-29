package uk.gov.moj.cpp.unifiedsearch.query.api.util;

import static uk.gov.justice.services.messaging.JsonObjects.createArrayBuilder;
import static uk.gov.justice.services.messaging.JsonObjects.createObjectBuilder;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class DeletedApplicationFilter {

    public static final String DELETED_APPLICATION_STATUS = "DELETED";

    private static final String CASES = "cases";
    private static final String APPLICATIONS = "applications";
    private static final String APPLICATION_STATUS = "applicationStatus";

    public JsonObject filter(final JsonObject input) {

        if (notRequireFiltering(input)) {
            return input;
        }

        final JsonObjectBuilder outputBuilder = createObjectBuilder();

        input.entrySet().stream()
                .filter(e -> !e.getKey().equalsIgnoreCase(CASES))
                .forEach(e -> outputBuilder.add(e.getKey(), e.getValue()));

        final JsonArrayBuilder caseArrayBuilder = createArrayBuilder();
        input.getJsonArray(CASES).stream()
                .map(JsonObject.class::cast)
                .forEach(currentCase -> {
                    if (hasDeletedApplication(currentCase)) {
                        caseArrayBuilder.add(removeDeletedApplications(currentCase));
                    } else {
                        caseArrayBuilder.add(currentCase);
                    }
                });
        outputBuilder.add(CASES, caseArrayBuilder);
        return outputBuilder.build();
    }

    private static JsonObject removeDeletedApplications(final JsonObject currentCase) {
        final JsonObjectBuilder caseBuilder = createObjectBuilder();

        currentCase.entrySet().stream()
                .filter(e -> !e.getKey().equalsIgnoreCase(APPLICATIONS))
                .forEach(e -> caseBuilder.add(e.getKey(), e.getValue()));

        final JsonArrayBuilder applicationsBuilder = createArrayBuilder();
        currentCase.getJsonArray(APPLICATIONS).stream()
                .map(JsonObject.class::cast)
                .filter(application -> !isDeleted(application))
                .forEach(applicationsBuilder::add);
        caseBuilder.add(APPLICATIONS, applicationsBuilder);

        return caseBuilder.build();
    }

    private boolean notRequireFiltering(final JsonObject queryResult) {
        if (!queryResult.containsKey(CASES) || queryResult.isNull(CASES)) {
            return true;
        }
        return queryResult.getJsonArray(CASES).stream()
                .map(JsonObject.class::cast)
                .noneMatch(DeletedApplicationFilter::hasDeletedApplication);
    }

    private static boolean hasDeletedApplication(final JsonObject currentCase) {
        if (!currentCase.containsKey(APPLICATIONS) || currentCase.isNull(APPLICATIONS)) {
            return false;
        }
        return currentCase.getJsonArray(APPLICATIONS).stream()
                .map(JsonObject.class::cast)
                .anyMatch(DeletedApplicationFilter::isDeleted);
    }

    private static boolean isDeleted(final JsonObject application) {
        return DELETED_APPLICATION_STATUS.equals(application.getString(APPLICATION_STATUS, null));
    }
}
