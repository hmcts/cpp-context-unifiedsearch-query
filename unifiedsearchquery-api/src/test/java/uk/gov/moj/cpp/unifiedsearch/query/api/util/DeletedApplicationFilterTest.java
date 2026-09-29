package uk.gov.moj.cpp.unifiedsearch.query.api.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static uk.gov.justice.services.messaging.JsonObjects.createReader;

import java.io.StringReader;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.jupiter.api.Test;

public class DeletedApplicationFilterTest {

    private final DeletedApplicationFilter deletedApplicationFilter = new DeletedApplicationFilter();

    @Test
    public void shouldRemoveDeletedApplicationsFromCases() {

        final JsonObject input = jsonObject("{" +
                "\"totalResults\": 1," +
                "\"cases\": [{" +
                "  \"caseId\": \"case-1\"," +
                "  \"applications\": [" +
                "    {\"applicationId\": \"app-1\", \"applicationStatus\": \"LISTED\"}," +
                "    {\"applicationId\": \"app-2\", \"applicationStatus\": \"DELETED\"}" +
                "  ]" +
                "}]}");

        final JsonObject result = deletedApplicationFilter.filter(input);

        assertThat(result.getInt("totalResults"), is(1));

        final JsonObject filteredCase = result.getJsonArray("cases").getJsonObject(0);
        assertThat(filteredCase.getString("caseId"), is("case-1"));

        final JsonArray applications = filteredCase.getJsonArray("applications");
        assertThat(applications.size(), is(1));
        assertThat(applications.getJsonObject(0).getString("applicationId"), is("app-1"));
    }

    @Test
    public void shouldNotChangeCasesWithoutDeletedApplications() {

        final JsonObject input = jsonObject("{" +
                "\"cases\": [{" +
                "  \"caseId\": \"case-1\"," +
                "  \"applications\": [{\"applicationId\": \"app-1\", \"applicationStatus\": \"FINALISED\"}]" +
                "}]}");

        assertSame(input, deletedApplicationFilter.filter(input));
    }

    @Test
    public void shouldNotChangeCasesWithApplicationsWithoutStatus() {

        final JsonObject input = jsonObject("{" +
                "\"cases\": [{" +
                "  \"caseId\": \"case-1\"," +
                "  \"applications\": [{\"applicationId\": \"app-1\"}]" +
                "}]}");

        assertSame(input, deletedApplicationFilter.filter(input));
    }

    @Test
    public void shouldNotChangeResultWithoutCases() {

        final JsonObject input = jsonObject("{\"totalResults\": 0}");

        assertSame(input, deletedApplicationFilter.filter(input));
    }

    @Test
    public void shouldLeaveOtherCasesUntouchedWhenRemovingDeletedApplications() {

        final JsonObject input = jsonObject("{" +
                "\"cases\": [" +
                "  {\"caseId\": \"case-1\"}," +
                "  {\"caseId\": \"case-2\", \"applications\": [" +
                "    {\"applicationId\": \"app-1\", \"applicationStatus\": \"DELETED\"}" +
                "  ]}" +
                "]}");

        final JsonObject result = deletedApplicationFilter.filter(input);

        final JsonArray cases = result.getJsonArray("cases");
        assertThat(cases.size(), is(2));
        assertThat(cases.getJsonObject(0), is(input.getJsonArray("cases").getJsonObject(0)));
        assertThat(cases.getJsonObject(1).getString("caseId"), is("case-2"));
        assertThat(cases.getJsonObject(1).getJsonArray("applications").isEmpty(), is(true));
    }

    private JsonObject jsonObject(final String json) {
        try (final JsonReader reader = createReader(new StringReader(json))) {
            return reader.readObject();
        }
    }
}
