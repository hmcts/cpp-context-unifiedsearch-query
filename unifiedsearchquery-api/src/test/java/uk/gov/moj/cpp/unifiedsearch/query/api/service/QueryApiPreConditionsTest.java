package uk.gov.moj.cpp.unifiedsearch.query.api.service;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.moj.cpp.unifiedsearch.query.api.service.QueryApiPreConditions.validateQueryParameterForLAACaseQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.api.service.QueryApiPreConditions.validateQueryParameters;

import uk.gov.justice.services.adapter.rest.exception.BadRequestException;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.QueryParameters;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.builders.QueryParametersBuilder;

import org.junit.jupiter.api.Test;

public class QueryApiPreConditionsTest {

    @Test
    public void shouldFailValidationForQueryParametersWithNoMandatoryAttributesSet() {
        final QueryParameters queryParameters = new QueryParameters();

        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> validateQueryParameters(queryParameters));
        assertThat(badRequestException.getMessage(), is("You must provide at least one parameter"));

    }

    @Test
    public void shouldPassValidationForQueryParametersWithCorrectUUID() {
        final QueryParameters queryParameters = new QueryParametersBuilder().withCourtId("ab746921-d839-4867-bcf9-b41db8ebc852").build();

        validateQueryParameters(queryParameters);
    }

    @Test
    public void shouldFailValidationForQueryParametersWithIncorrectUUID() {
        final QueryParameters queryParameters = new QueryParametersBuilder().withCourtId("ab746921-d839-4867-bcf9-b41db8ebc85").build();

        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> validateQueryParameters(queryParameters));

        assertThat(badRequestException.getMessage(), startsWith("UUID provided for Court Id:"));
        assertThat(badRequestException.getMessage(), endsWith("is not valid"));
    }

    @Test
    public void shouldFailValidationForQueryParametersWithInvalidStringAsUUID() {
        final QueryParameters queryParameters = new QueryParametersBuilder().withCourtId("rubbish").build();

        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> validateQueryParameters(queryParameters));
        assertThat(badRequestException.getMessage(), startsWith("UUID provided for Court Id:"));
        assertThat(badRequestException.getMessage(), endsWith("is not valid"));

    }

    @Test
    public void shouldPassValidationForQueryParametersWithCorrectPageSize() {
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withCourtId("ab746921-d839-4867-bcf9-b41db8ebc852").
                        withPageSize(10).build();

        validateQueryParameters(queryParameters);
    }

    @Test
    public void shouldFailValidationForQueryParametersWithIncorrectPageSize() {
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withCourtId("ab746921-d839-4867-bcf9-b41db8ebc852").
                        withPageSize(-1).build();


        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> validateQueryParameters(queryParameters));
        assertThat(badRequestException.getMessage(), is("Invalid page size: -1"));
    }

    @Test
    public void shouldPassValidationForQueryParametersWithCorrectDateFormat() {
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withPartyName("John")
                .build();

        validateQueryParameters(queryParameters);
    }

    @Test
    public void shouldFailValidationForQueryParametersWithIncorrectDateFormat() {
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withPartyDateOfBirth("201205-05")
                .build();
        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> validateQueryParameters(queryParameters));
        assertThat(badRequestException.getMessage(), startsWith("Invalid Date Format for Party Date Of Birth"));
    }

    @Test
    public void shouldFailValidationForQueryParametersWithIncorrectCourtOrderValidityDateFormat() {
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withCourtOrderValidityDate("202005-05")
                .build();

        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> validateQueryParameters(queryParameters));
        assertThat(badRequestException.getMessage(), startsWith("Invalid Date Format for Court Order Validity Date"));
    }

    @Test
    public void shouldPassValidationForQueryParametersWithCorrectDateRange() {
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withHearingDateFrom("2019-04-05")
                .withHearingDateTo("2019-06-17")
                .build();

        validateQueryParameters(queryParameters);
    }

    @Test
    public void shouldFailValidationForQueryParametersWithIncorrectDateRange() {
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withHearingDateFrom("2019-04-05")
                .withHearingDateTo("2019-02-17")
                .build();

        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> validateQueryParameters(queryParameters));
        assertThat(badRequestException.getMessage(), startsWith("Hearing Date From: 2019-04-05"));
        assertThat(badRequestException.getMessage(), endsWith("is after Hearing Date To: 2019-02-17"));
    }

    @Test
    public void shouldPassValidationForQueryParametersWithDateToEqualsDateFrom() {
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withHearingDateFrom("2019-04-05")
                .withHearingDateTo("2019-04-05")
                .build();

        validateQueryParameters(queryParameters);
    }

    @Test
    public void shouldPassValidationForQueryParametersWithSortBySjpNoticeServed() {
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withSortBySjpNoticeServed("asc")
                .build();

        validateQueryParameters(queryParameters);
    }

    @Test
    public void shouldPassValidationForQueryParametersWithSortByAppointmentDate() {
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withSortByAppointmentDate("asc")
                .build();

        validateQueryParameters(queryParameters);
    }

    @Test
    public void shouldFailValidationForQueryParametersWithSortSjpByNoticeNotServedSetIncorrectly(){
        final QueryParameters queryParameters =new QueryParametersBuilder()
                .withSortBySjpNoticeServed("unknown")
                .build();


        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> validateQueryParameters(queryParameters));
        assertThat(badRequestException.getMessage(), is("Invalid SortOrder: unknown"));
    }

    @Test
    public void shouldFailValidationForQueryParametersWithMultipleSortParameters(){
        final QueryParameters queryParameters =new QueryParametersBuilder()
                .withSortBySjpNoticeServed("asc")
                .withSortByAppointmentDate("asc")
                .build();


        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> validateQueryParameters(queryParameters));
        assertThat(badRequestException.getMessage(), is("You cannot sort by more than one field"));
    }

    @Test
    public void shouldFailValidationForQueryParametersWithSortByAppointmentDateSetIncorrectly(){
        final QueryParameters queryParameters =new QueryParametersBuilder()
                .withSortByAppointmentDate("unknown")
                .build();

        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> validateQueryParameters(queryParameters));
        assertThat(badRequestException.getMessage(), is("Invalid SortOrder: unknown"));
    }


    @Test
    public void shouldFailValidationForQueryParametersContainingPostcodeOnly() {
        final QueryParameters queryParameters = new QueryParametersBuilder().withPartyPostCode("CR0 1XN").build();

        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> validateQueryParameters(queryParameters));
        assertThat(badRequestException.getMessage(), is("You cannot search by postcode alone, at least one additional search parameter must be provided"));
    }

    @Test
    public void shouldPassValidationForQueryParametersContainingPostcodeAndAnotherPartyParam() {
        final QueryParameters queryParameters = new QueryParametersBuilder().withPartyPostCode("CR0 1XN").withPartyName("John").build();

        validateQueryParameters(queryParameters);
    }

    @Test
    public void shouldPassValidationForQueryParametersContainingPostcodeAndAnotherParam() {
        final QueryParameters queryParameters = new QueryParametersBuilder().withPartyPostCode("CR0 1XN").withProsecutingAuthority("CPS").build();

        validateQueryParameters(queryParameters);
    }

    @Test
    public void shouldFailValidationForQueryParametersContainingDefendantNameOnly() {
        final QueryParameters queryParameters = new QueryParametersBuilder().withPartyName("John").build();

        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> validateQueryParameterForLAACaseQuery(queryParameters));
        assertThat(badRequestException.getMessage(), is("You cannot search by defendantName,defendantDOB and dateOfNextHearing alone, please choose combination of any two among these three parameters"));
    }

    @Test
    public void shouldFailValidationForQueryParametersContainingDefendantDOBOnly() {
        final QueryParameters queryParameters = new QueryParametersBuilder().withPartyDateOfBirth("2012-05-05").build();


        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> validateQueryParameterForLAACaseQuery(queryParameters));
        assertThat(badRequestException.getMessage(), is("You cannot search by defendantName,defendantDOB and dateOfNextHearing alone, please choose combination of any two among these three parameters"));
    }

    @Test
    public void shouldFailValidationForQueryParametersContainingDateOfNextHearingOnly() {
        final QueryParameters queryParameters = new QueryParametersBuilder().withHearingDateFrom("2019-04-05").build();


        final BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> validateQueryParameterForLAACaseQuery(queryParameters));
        assertThat(badRequestException.getMessage(), is("You cannot search by defendantName,defendantDOB and dateOfNextHearing alone, please choose combination of any two among these three parameters"));
    }

    @Test
    public void shouldPassValidationForQueryParametersContainingDefendantNmaeAndAnotherParamFromDefendantDOBOrDateOfNextHearing() {
        final QueryParameters queryParameters = new QueryParametersBuilder().withPartyName("John").withPartyDateOfBirth("2012-05-05").build();

        validateQueryParameters(queryParameters);
    }
}