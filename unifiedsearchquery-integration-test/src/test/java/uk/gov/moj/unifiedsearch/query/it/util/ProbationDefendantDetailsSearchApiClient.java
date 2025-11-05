package uk.gov.moj.unifiedsearch.query.it.util;

import static com.google.common.collect.ImmutableMap.of;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jboss.resteasy.util.HttpResponseCodes.SC_OK;
import static uk.gov.justice.services.test.utils.common.host.TestHostProvider.getHost;

import uk.gov.justice.services.common.http.HeaderConstants;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.probation.ProbationDefendantDetailsResponse;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProbationDefendantDetailsSearchApiClient {

    private static final String PROBATION_DEFENDANT_DETAILS_CASE_SEARCH_URL = format("http://%s:8080//unifiedsearchquery-query-api/query/api/rest/unifiedsearchquery/probation-defendant-details", getHost());
    private static final int TIMEOUT = 5000;
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final Logger LOG = LoggerFactory.getLogger(ProbationDefendantDetailsSearchApiClient.class);
    private final ResteasyClient client = new ResteasyClientBuilderImpl()
            .connectionPoolSize(1) // A single connection is more than enough if properly released
            .connectTimeout(TIMEOUT, MILLISECONDS)
            .connectionCheckoutTimeout(TIMEOUT, MILLISECONDS)
            .readTimeout(TIMEOUT, MILLISECONDS)
            .build();
    private static final String CONTEXT_USER_ID = "822d5847-5825-4399-aca4-cb7810c820c2";

    private static final ProbationDefendantDetailsSearchApiClient searchApiClient = new ProbationDefendantDetailsSearchApiClient();

    public static ProbationDefendantDetailsSearchApiClient getInstance(){
        return searchApiClient;
    }

    public final ProbationDefendantDetailsResponse searchCases(final Map<String, String> parameters) throws IOException {

        return searchCasesAndValidateStatusCode(parameters, SC_OK);
    }

    public final ProbationDefendantDetailsResponse searchCasesAndValidateStatusCode(final Map<String, String> parameters, int statusCode) throws IOException {
        return searchCasesAndValidateStatusCode(parameters, of(HeaderConstants.USER_ID, CONTEXT_USER_ID), statusCode);
    }

    public final ProbationDefendantDetailsResponse searchCasesAndValidateStatusCode(final Map<String, String> parameters, final Map<String, String> additionalHeaders, int statusCode) throws IOException {

        final ImmutableMap<String, String> headers = ImmutableMap.<String, String> builder()
                .putAll(additionalHeaders)
                .put("Accept", "application/vnd.unifiedsearch.query.probation.defendant.details+json")
                .build();

        final Response response = get(parameters, headers, statusCode);

        final String responseAsString = response.readEntity(String.class);

        return mapper.readValue(responseAsString, ProbationDefendantDetailsResponse.class);
    }


    private Response get(final Map<String, String> parameters, final Map<String, String> headers, final int statusCode) {
        Response response = null;
        try {
            response = client
                    .target(PROBATION_DEFENDANT_DETAILS_CASE_SEARCH_URL)
                    .queryParams(new MultivaluedHashMap<>(parameters))
                    .request()
                    .headers(new MultivaluedHashMap<>(headers))
                    .get();

            assertThat(response.getStatus(), is(statusCode));

            return response;

        } catch (Throwable throwable) {
            releaseConnection(response);
            throw throwable;
        }
    }

    /**
     * response Connection is released by default unless there is exception in which case we need to
     * close it by closing the response
     *
     * @param response to use to release connection
     */
    private void releaseConnection(final Response response) {
        if (response != null) {
            try {
                /* release connection.
                 See https://github.com/resteasy/Resteasy/blob/master/resteasy-client/src/main/java/org/jboss/resteasy/client/jaxrs/internal/ClientResponse.java#L138
                Note that for this to work, we need to be using connection pooling
                */
                response.close();
            } catch (Exception e) {
                LOG.warn("Failed to release connection", e);
            }
        }
    }
}
