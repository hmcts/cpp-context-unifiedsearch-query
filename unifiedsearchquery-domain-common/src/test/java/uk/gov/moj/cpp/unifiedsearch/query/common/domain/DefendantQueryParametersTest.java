package uk.gov.moj.cpp.unifiedsearch.query.common.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class DefendantQueryParametersTest {
    @Test
    public void shouldCreateQueryParametersFromDefendantQueryParameters() {

        final DefendantQueryParameters defendantQueryParameters = new DefendantQueryParameters(
                10, 1, "PNC_ID", "CRO_NUMBER", "FIRST_NAME", "LAST_NAME", "DFENDANT DOB",
                "ADDRESS_LINE1", true, true, "COURT_ORDER_DATE");

        final QueryParameters  queryParameters = defendantQueryParameters.toQueryParameters();

        assertThat(queryParameters.getPageSize(), is(defendantQueryParameters.getPageSize()));
        assertThat(queryParameters.getStartFrom(), is(defendantQueryParameters.getStartFrom()));
        assertThat(queryParameters.getPncId(), is(defendantQueryParameters.getPncId()));
        assertThat(queryParameters.getCroNumber(), is(defendantQueryParameters.getCroNumber()));
        assertThat(queryParameters.getPartyFirstName(), is(defendantQueryParameters.getFirstName()));
        assertThat(queryParameters.getPartyLastName(), is(defendantQueryParameters.getLastName()));
        assertThat(queryParameters.getPartyDateOfBirth(), is(defendantQueryParameters.getDateOfBirth()));
        assertThat(queryParameters.getPartyAddressLine1(), is(defendantQueryParameters.getAddressLine1()));
        assertThat(queryParameters.isProceedingConcluded(), is(defendantQueryParameters.isProceedingsConcluded()));
        assertThat(queryParameters.isCrownOrMagistrates(), is(defendantQueryParameters.isCrownOrMagistrates()));

    }
}