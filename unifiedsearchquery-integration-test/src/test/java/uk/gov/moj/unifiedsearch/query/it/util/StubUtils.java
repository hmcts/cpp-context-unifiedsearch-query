package uk.gov.moj.unifiedsearch.query.it.util;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.reset;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.apache.http.HttpStatus.SC_OK;
import static uk.gov.justice.service.wiremock.testutil.InternalEndpointMockUtils.stubPingFor;
import static uk.gov.justice.services.common.http.HeaderConstants.ID;
import static uk.gov.justice.services.messaging.JsonObjects.createObjectBuilder;
import static uk.gov.justice.services.test.utils.core.http.RequestParamsBuilder.requestParams;
import static uk.gov.justice.services.test.utils.core.matchers.ResponseStatusMatcher.status;
import static uk.gov.moj.cpp.platform.data.utils.testutils.FileUtil.getPayload;
import static uk.gov.moj.unifiedsearch.query.it.util.WiremockTestHelper.waitForStubToBeReady;

import uk.gov.justice.services.test.utils.core.http.RestPoller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.ws.rs.core.Response;

import com.github.tomakehurst.wiremock.client.WireMock;

public class StubUtils {
    private static final String HOST = System.getProperty("INTEGRATION_HOST_KEY", "localhost");
    private static final String USERS_GROUPS_URL = "/usersgroups-service/query/api/rest/usersgroups/users/%s/groups";
    private static final String SJP_CASE_SEARCH_URL = "/sjp-service/query/api/rest/sjp/search?q=%s";
    private static final String CONTENT_TYPE = "application/json";
    private static final int PORT = 8080;
    private static final String BASE_URL = "http://" + HOST + ":" + PORT;
    private static final AtomicBoolean atomicBoolean = new AtomicBoolean();
    public static final String CONTEXT_USER_ID = "822d5847-5825-4399-aca4-cb7810c820c2";
    public static final DateTimeFormatter ISO_8601_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final String DEFAULT_JSON_CONTENT_TYPE = "application/json";
    private static final String LOGGED_IN_USER_PERMISSIONS_URL = "/usersgroups-service/query/api/rest/usersgroups/users/logged-in-user/permissions";
    private static int i = 0;
    private static final String CONTENT_TYPE_QUERY_PERMISSION = "application/vnd.usersgroups.get-logged-in-user-permissions+json";

    public static void setupUsersGroupQueryStub() {

        final String stubUrl = format("/usersgroups-service/query/api/rest/usersgroups/users%s", ".*");

        stubPingFor("usersgroups-service");

        stubFor(get(urlMatching(stubUrl))
                .willReturn(aResponse().withStatus(SC_OK)
                        .withHeader("CPPID", randomUUID().toString())
                        .withHeader("Content-Type", DEFAULT_JSON_CONTENT_TYPE)
                        .withBody(getPayload("stub-data/usersgroups.get-users-by-user-ids.json"))));
    }

    public static void stubEnableAllCapabilities() {
        final String stubUrl = format("/authorisation-service-server/rest/capabilities/%s", ".*");
        final String responsePayload = createObjectBuilder().add("enabled", true).build().toString();

        stubPingFor("authorisation-service-server");

        stubFor(get(urlMatching(stubUrl))
                .willReturn(aResponse().withStatus(SC_OK)
                        .withHeader(ID, randomUUID().toString())
                        .withHeader("Content-Type", DEFAULT_JSON_CONTENT_TYPE)
                        .withBody(responsePayload)));

        waitForStubToBeReady(stubUrl, "application/vnd.authorisation.capability+json");
    }

    public static void stubUsersAndGroups(final String userId) throws IOException {
        stubPingFor("usersgroups-service");
        stubFor(get(urlPathEqualTo(format(USERS_GROUPS_URL, userId)))
                .willReturn(aResponse().withStatus(Response.Status.OK.getStatusCode())
                        .withHeader("CPPID", UUID.randomUUID().toString())
                        .withHeader("Content-Type", CONTENT_TYPE)
                        .withBody(getPayload("stub-data/get-groups-by-user.json"))));

        pollForResponseAfterUpdateHasTakenEffect(userId);

    }

    public static void initializeStubbing() throws IOException {

        if (!atomicBoolean.get()) {
            atomicBoolean.set(true);
            WireMock.configureFor(System.getProperty("INTEGRATION_HOST_KEY", "localhost"), 8080);
            reset();
            stubPingFor("unifiedsearchquery-api");
            StubUtils.stubUsersAndGroups(CONTEXT_USER_ID);
            stubUserWithCPSSearchPermission();
        }
    }

    public static void resetLock() {
        if (atomicBoolean.get()) {
            atomicBoolean.set(false);
        }
    }

    private static void pollForResponseAfterUpdateHasTakenEffect(final String userId) {

        RestPoller restPoller = RestPoller.poll(requestParams(BASE_URL + format(USERS_GROUPS_URL, userId), CONTENT_TYPE).build());

        restPoller.until(status().is(Response.Status.OK));
    }

    private static void pollSjpMockForResponseAfterUpdateHasTakenEffect(final String caseReference) {

        RestPoller restPoller = RestPoller.poll(requestParams(BASE_URL + format(SJP_CASE_SEARCH_URL, caseReference), CONTENT_TYPE).build());

        restPoller.until(status().is(Response.Status.OK));
    }


    public static void stubUserWithCPSSearchPermission() {
        stubPingFor("usersgroups-service");

        stubFor(get(urlPathEqualTo(LOGGED_IN_USER_PERMISSIONS_URL))
                .willReturn(aResponse().withStatus(SC_OK)
                        .withHeader(ID, CONTEXT_USER_ID)
                        .withHeader("Content-Type", CONTENT_TYPE)
                        .withBody(getPayload("stub-data/usersgroups.get-permission-with-view-cps-search.json"))));

        waitForStubToBeReady(LOGGED_IN_USER_PERMISSIONS_URL, CONTENT_TYPE_QUERY_PERMISSION);
    }

    public static void stubUserWithCPSRestrictedSearchPermission() {
        stubPingFor("usersgroups-service");

        stubFor(get(urlPathEqualTo(LOGGED_IN_USER_PERMISSIONS_URL))
                .willReturn(aResponse().withStatus(SC_OK)
                        .withHeader(ID, CONTEXT_USER_ID)
                        .withHeader("Content-Type", CONTENT_TYPE)
                        .withBody(getPayload("stub-data/usersgroups.get-permission-with-view-cps-restricted-search.json"))));

        waitForStubToBeReady(LOGGED_IN_USER_PERMISSIONS_URL, CONTENT_TYPE_QUERY_PERMISSION);
    }
}