package uk.gov.moj.cpp.unifiedsearch.query.api.util;

import static java.util.Objects.isNull;
import static uk.gov.justice.services.messaging.JsonObjects.createObjectBuilder;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import uk.gov.justice.services.messaging.Metadata;
import uk.gov.moj.cpp.unifiedsearch.query.api.service.UsersGroupsService;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.Permission;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import uk.gov.justice.services.messaging.JsonObjects;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ApplicationTypeFilter {

    private static final String CASES = "cases";
    public static final String APPLICATIONS = "applications";
    public static final String DECISION_DATE = "decisionDate";
    public static final String APPLICATION_TYPE = "applicationType";
    public static final String APPLICATION_REFERENCE = "applicationReference";
    public static final String PARTIES = "parties";
    public static final String MAGISTRATE_COURT = "magistrateCourt";
    public static final String CROWN_COURT = "crownCourt";
    public static final String CASE_TYPE = "caseType";
    public static final String HEARINGS = "hearings";
    public static final String FIRST_NAME = "firstName";
    public static final String ORGANISATION_NAME = "organisationName";
    public static final String PARTY_TYPE = "partyType";
    public static final String APPLICANT = "APPLICANT";

    @Inject
    private UsersGroupsService usersGroupsService;


    public JsonObject filter(final Metadata queryMetadata, final JsonObject input) {

        if (notRequireFiltering(input)) {
            return input;
        }

        final List<Permission> permissions = usersGroupsService.getUserPermissionForApplicationTypes(queryMetadata);

        //If no permission found, don't apply any filtering. (ex: systemUsers)
        if (isEmpty(permissions)) {
            return input;
        }

        final JsonObjectBuilder outputBuilder = createObjectBuilder();

        //keep everything same in the payload as it is other than cases element
        input.entrySet().stream()
                .filter(e -> !e.getKey().equalsIgnoreCase(CASES))
                .forEach(e -> outputBuilder.add(e.getKey(), e.getValue()));

        final JsonArrayBuilder caseArrayBuilder = JsonObjects.createArrayBuilder();
        input.getJsonArray(CASES).stream()
                .map(JsonObject.class::cast)
                .forEach(currentCase -> {
                    if (isApplication(currentCase)) {
                        caseArrayBuilder.add(buildApplicationPayloadBasedOnPermissions(currentCase, permissions));
                    } else {
                        caseArrayBuilder.add(currentCase);
                    }

                });
        outputBuilder.add(CASES, caseArrayBuilder);
        return outputBuilder.build();
    }

    private JsonObject buildApplicationPayloadBasedOnPermissions(final JsonObject currentCase, final List<Permission> permissions) {
        if (isUserHasPermissionForTheApplication(currentCase, permissions)) {
            return currentCase;
        } else {
            return buildMaskedApplicationPayload(currentCase);
        }

    }

    private static JsonObject buildMaskedApplicationPayload(final JsonObject currentCase) {
        final JsonObjectBuilder objectBuilder = createObjectBuilder();
        if (currentCase.containsKey(PARTIES)) {
            objectBuilder.add(PARTIES, buildMaskedParties(currentCase.getJsonArray(PARTIES)));
        }
        objectBuilder.add(HEARINGS, buildMaskedHearings(currentCase));
        if (currentCase.containsKey(APPLICATIONS)) {
            objectBuilder.add(APPLICATIONS, buildMaskedApplications(currentCase.getJsonArray(APPLICATIONS)));
        }
        objectBuilder.add(MAGISTRATE_COURT, currentCase.getBoolean(MAGISTRATE_COURT));
        objectBuilder.add(CROWN_COURT, currentCase.getBoolean(CROWN_COURT));
        objectBuilder.add(CASE_TYPE, currentCase.getString(CASE_TYPE));

        return objectBuilder.build();
    }

    private static JsonArray buildMaskedHearings(final JsonObject currentCase) {
        if(!currentCase.containsKey(HEARINGS)){
            return JsonObjects.createArrayBuilder().build();
        }
        final JsonArrayBuilder arrayBuilder = JsonObjects.createArrayBuilder();

        currentCase.getJsonArray(HEARINGS).stream()
                .map(JsonObject.class::cast)
                .map( hearing -> createObjectBuilder()
                            .add("courtCentreName", hearing.getString("courtCentreName", EMPTY))
                            .add("courtId", hearing.getString("courtId", EMPTY))
                            .build()).forEach(arrayBuilder::add);
        return arrayBuilder.build();
    }

    private static JsonArray buildMaskedApplications(final JsonArray applications) {
        final JsonArrayBuilder arrayBuilder = JsonObjects.createArrayBuilder();

        applications.stream()
                .map(JsonObject.class::cast)
                .map(application -> {
                    final JsonObjectBuilder objectBuilder = createObjectBuilder();

                    objectBuilder.add(APPLICATION_REFERENCE, application.getString(APPLICATION_REFERENCE, EMPTY))
                            .add(APPLICATION_TYPE, application.getString(APPLICATION_TYPE, EMPTY));
                    if (application.containsKey(DECISION_DATE)) {
                        objectBuilder.add(DECISION_DATE, application.getString(DECISION_DATE));
                    }

                    return objectBuilder.build();
                }).forEach(arrayBuilder::add);

        return arrayBuilder.build();
    }

    private static JsonArray buildMaskedParties(final JsonArray parties) {
        final JsonArrayBuilder arrayBuilder = JsonObjects.createArrayBuilder();
        parties.stream()
                .map(JsonObject.class::cast)
                .map(party -> {
                    final JsonObjectBuilder partiesObjectBuilder = createObjectBuilder();
                    final String partyType = party.getString(PARTY_TYPE, EMPTY);
                    if(APPLICANT.equalsIgnoreCase(partyType)){
                        partiesObjectBuilder.add(ORGANISATION_NAME, party.getString(ORGANISATION_NAME, EMPTY));
                    }
                    partiesObjectBuilder.add(PARTY_TYPE, partyType);
                    return partiesObjectBuilder.build();
                })
                .forEach(arrayBuilder::add);

        return arrayBuilder.build();
    }

    private boolean isUserHasPermissionForTheApplication(final JsonObject currentCase, final List<Permission> permissions) {
        if (isNull(currentCase) || currentCase.isNull(APPLICATIONS) || currentCase.getJsonArray(APPLICATIONS).isEmpty()) {
            return true;
        }

        //Map<applicationTypeCode, hasPermission>
        final Map<String, Boolean> permissionMap = permissions.stream()
                .collect(Collectors.toMap(
                        Permission::getObject,
                        Permission::isHasPermission,
                        (p1, p2) -> p1
                ));

        return currentCase.getJsonArray(APPLICATIONS).stream()
                .map(JsonObject.class::cast)
                .allMatch(currentApplication -> {
                    final String appType = currentApplication.getString("applicationTypeCode", null);
                    return permissionMap.getOrDefault(appType, true);
                });
    }


    private boolean notRequireFiltering(final JsonObject queryResult) {
        if (!queryResult.containsKey(CASES) || queryResult.isNull(CASES)) {
            return false;
        }
        return queryResult.getJsonArray(CASES).stream()
                .map(JsonObject.class::cast)
                .noneMatch(ApplicationTypeFilter::isApplication);
    }

    private static boolean isApplication(final JsonObject currentCase) {
        return currentCase.containsKey(APPLICATIONS) &&
                !currentCase.isNull(APPLICATIONS) &&
                !currentCase.getJsonArray(APPLICATIONS).isEmpty() &&
                isAnyApplicationHasTypeCode(currentCase.getJsonArray(APPLICATIONS));
    }

    private static boolean isAnyApplicationHasTypeCode(final JsonArray applicationsArray) {
        return applicationsArray.stream()
                .map(JsonObject.class::cast)
                .anyMatch(currentApplication -> currentApplication.containsKey("applicationTypeCode"));
    }

}
