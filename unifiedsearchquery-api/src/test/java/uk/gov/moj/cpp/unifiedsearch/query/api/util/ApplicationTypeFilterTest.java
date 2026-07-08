package uk.gov.moj.cpp.unifiedsearch.query.api.util;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.messaging.JsonObjects.createReader;

import uk.gov.justice.services.messaging.Metadata;
import uk.gov.moj.cpp.unifiedsearch.query.api.service.UsersGroupsService;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.Permission;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;

import javax.json.JsonObject;
import javax.json.JsonReader;

import com.google.common.io.Resources;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("squid:S2187")
@ExtendWith(MockitoExtension.class)
public class ApplicationTypeFilterTest {

    public static final String PERMITTED_TYPE_CODE1 = "appTypeCode1";
    public static final String PERMITTED_TYPE_CODE2 = "appTypeCode2";
    public static final String RANDOM_TYPE_CODE = "appTypeCode3";
    public static final String UN_PERMITTED_TYPE_CODE = "appTypeCode4";
    @InjectMocks
    private ApplicationTypeFilter applicationTypeFilter;

    @Mock
    private UsersGroupsService usersGroupsService;

    @Mock
    private Metadata queryMetadata;

    final List<Permission> permissions = asList(
            new Permission(randomUUID(), PERMITTED_TYPE_CODE1, "Access to Standalone Application", true),
            new Permission(randomUUID(), UN_PERMITTED_TYPE_CODE, "Access to Standalone Application", false),
            new Permission(randomUUID(), PERMITTED_TYPE_CODE2, "Access to Standalone Application", true)
    );

    @Test
    public void doNotApplyFilteringWhenUserHasPermission() throws IOException {

        when(usersGroupsService.getUserPermissionForApplicationTypes(queryMetadata)).thenReturn(permissions);

        final JsonObject inputJsonObject = getJsonPayload("elastic-search-result.json", PERMITTED_TYPE_CODE1);

        final JsonObject result = applicationTypeFilter.filter(queryMetadata, inputJsonObject);

        assertEquals(inputJsonObject, result);
    }

    @Test
    public void doNotApplyFilteringWhenUserHasPermissionForMultipleApplications() throws IOException {

        when(usersGroupsService.getUserPermissionForApplicationTypes(queryMetadata)).thenReturn(permissions);

        final JsonObject inputJsonObject = getJsonPayloadForMultipleApplications("elastic-search-result-with-multiple-case-multiple-parties.json", PERMITTED_TYPE_CODE1, PERMITTED_TYPE_CODE2);

        final JsonObject result = applicationTypeFilter.filter(queryMetadata, inputJsonObject);

        assertEquals(inputJsonObject, result);
    }

    @Test
    public void applyFilteringWhenUserDoNotHavePermissionForMultipleApplications() throws IOException {

        when(usersGroupsService.getUserPermissionForApplicationTypes(queryMetadata)).thenReturn(permissions);

        final JsonObject inputJsonObject = getJsonPayloadForMultipleApplications("elastic-search-result-with-multiple-case-multiple-parties.json", PERMITTED_TYPE_CODE1, UN_PERMITTED_TYPE_CODE);

        final JsonObject result = applicationTypeFilter.filter(queryMetadata, inputJsonObject);

        assertThat(result, is(getJsonPayload("expected-elastic-search-result-for-multiple-applications-after-filter.json", "")));
    }


    @Test
    public void doNotApplyFilteringWhenApplicationDoesNotHaveApplicationTypeCodeAndNoInteractionWithUsersGroups() throws IOException {

        final JsonObject inputJsonObject = getJsonPayload("elastic-search-result-without-application-type-code.json", PERMITTED_TYPE_CODE1);

        final JsonObject result = applicationTypeFilter.filter(queryMetadata, inputJsonObject);
        verifyNoMoreInteractions(usersGroupsService);

        assertEquals(inputJsonObject, result);
    }


    @Test
    public void doNotApplyFilteringWhenApplicationTypeDoesNotAppearInPermissionList() throws IOException {

        when(usersGroupsService.getUserPermissionForApplicationTypes(queryMetadata)).thenReturn(permissions);

        final JsonObject inputJsonObject = getJsonPayload("elastic-search-result.json", RANDOM_TYPE_CODE);


        final JsonObject result = applicationTypeFilter.filter(queryMetadata, inputJsonObject);

        assertEquals(inputJsonObject, result);
    }

    @Test
    public void doNotApplyFilteringWhenPermissionListReturnsEmpty() throws IOException {

        when(usersGroupsService.getUserPermissionForApplicationTypes(queryMetadata)).thenReturn(emptyList());

        final JsonObject inputJsonObject = getJsonPayload("elastic-search-result.json", RANDOM_TYPE_CODE);


        final JsonObject result = applicationTypeFilter.filter(queryMetadata, inputJsonObject);

        assertEquals(inputJsonObject, result);
    }

    @Test
    public void doNotApplyFilteringWhenApplicationsArrayDoesNotAppearInPayloadAndNoInteractionWithUsersGroups() throws IOException {

        final JsonObject inputJsonObject = getJsonPayload("elastic-search-result-for-case.json", RANDOM_TYPE_CODE);

        final JsonObject result = applicationTypeFilter.filter(queryMetadata, inputJsonObject);
        verifyNoMoreInteractions(usersGroupsService);

        assertEquals(inputJsonObject, result);
    }

    @Test
    public void applyFilteringWhenUserDoNotHavePermission() throws IOException {

        when(usersGroupsService.getUserPermissionForApplicationTypes(queryMetadata)).thenReturn(permissions);

        final JsonObject inputJsonObject = getJsonPayload("elastic-search-result.json", UN_PERMITTED_TYPE_CODE);

        final JsonObject result = applicationTypeFilter.filter(queryMetadata, inputJsonObject);

        assertThat(result, is(getJsonPayload("expected-elastic-search-result-after-filter.json", "")));
    }

    @Test
    public void applyFilteringWhenUserDoNotHavePermissionForApplicationsWithOrganisationApplicant() throws IOException {

        when(usersGroupsService.getUserPermissionForApplicationTypes(queryMetadata)).thenReturn(permissions);

        final JsonObject inputJsonObject = getJsonPayload("elastic-search-result-with-case-parties-organisation-applicant.json", UN_PERMITTED_TYPE_CODE);

        final JsonObject result = applicationTypeFilter.filter(queryMetadata, inputJsonObject);

        assertThat(result, is(getJsonPayload("expected-elastic-search-result-after-filter-organisation-applicant.json", "")));
    }

    private JsonObject getJsonPayload(final String fileName, final String applicationTypeCode) throws IOException {
        final String jsonString = Resources.toString(Resources.getResource(fileName), Charset.defaultCharset()).replaceAll("%APPLICATION_TYPE_CODE%", applicationTypeCode);

        try (JsonReader jsonReader = createReader(new StringReader(jsonString))) {
            return jsonReader.readObject();
        }
    }

    private JsonObject getJsonPayloadForMultipleApplications(final String fileName, final String applicationTypeCode1, final String applicationTypeCode2) throws IOException {
        final String jsonString = Resources.toString(Resources.getResource(fileName), Charset.defaultCharset())
                .replaceAll("%APPLICATION_TYPE_CODE1%", applicationTypeCode1)
                .replaceAll("%APPLICATION_TYPE_CODE2%", applicationTypeCode2);
        ;

        try (JsonReader jsonReader = createReader(new StringReader(jsonString))) {
            return jsonReader.readObject();
        }
    }

}