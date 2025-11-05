package uk.gov.moj.cpp.unifiedsearch.query.common.domain;

import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

import org.junit.jupiter.api.Test;

public class LaaQueryParametersTest {

    @Test
    public void shouldCreateQueryParametersFromLaaQueryParameters() {

        final LaaQueryParameters laaQueryParameters = new LaaQueryParameters("CASE_REF", "DEFENDANT NAME", "DEFENDANT NINO", "DFENDANT DOB", "DATE OF NEXT HEARING", "ASN", randomUUID().toString(), 1000);

        final QueryParameters  queryParameters = laaQueryParameters.toQueryParameters();

        assertThat(queryParameters.getCaseReference(), is(laaQueryParameters.getProsecutionCaseReference()));
        assertThat(queryParameters.getPartyName(), is(laaQueryParameters.getDefendantName()));
        assertThat(queryParameters.getPartyNINO(), is(laaQueryParameters.getDefendantNINO()));
        assertThat(queryParameters.getPartyDateOfBirth(), is(laaQueryParameters.getDefendantDOB()));
        assertThat(queryParameters.getHearingDateFrom(), is(laaQueryParameters.getDateOfNextHearing()));
        assertThat(queryParameters.getPartyArrestSummonsNumber(), is(laaQueryParameters.getDefendantASN()));
        assertThat(queryParameters.getHearingId(), is(laaQueryParameters.getHearingId()));
        assertThat(queryParameters.getPageSize(), is(laaQueryParameters.getPageSize()));
        assertThat(queryParameters.hasOffence(), is(true));

        assertRestOfTheAttributesAsEitherDefaultOrNullValues(queryParameters);
    }

    private void assertRestOfTheAttributesAsEitherDefaultOrNullValues(final QueryParameters queryParameters) {
        assertThat(queryParameters.getStartFrom(), is(0));
        assertThat(queryParameters.getPartyFirstAndOrMiddleName(), is(nullValue()));
        assertThat(queryParameters.getPartyLastNameOrOrganisationName(), is(nullValue()));
        assertThat(queryParameters.getPartyTypes(), is(nullValue()));
        assertThat(queryParameters.getPartyAddress(), is(nullValue()));
        assertThat(queryParameters.getPartyPostcode(), is(nullValue()));
        assertThat(queryParameters.getProsecutingAuthority(), is(nullValue()));
        assertThat(queryParameters.getHearingDateTo(), is(nullValue()));
        assertThat(queryParameters.getHearingTypeId(), is(nullValue()));
        assertThat(queryParameters.isSjp(), is(nullValue()));
        assertThat(queryParameters.isMagistrateCourt(), is(nullValue()));
        assertThat(queryParameters.isCrownCourt(), is(nullValue()));
        assertThat(queryParameters.getSortBySjpNoticeServed(), is(nullValue()));
        assertThat(queryParameters.getCourtId(), is(nullValue()));
        assertThat(queryParameters.getApplicationType(), is(nullValue()));
        assertThat(queryParameters.isBoxWorkHearing(), is(nullValue()));
        assertThat(queryParameters.getJurisdictionType(), is(nullValue()));
        assertThat(queryParameters.getRequestUserId(), is(nullValue()));
        assertThat(queryParameters.getPncId(), is(nullValue()));
        assertThat(queryParameters.getCroNumber(), is(nullValue()));
        assertThat(queryParameters.getPartyFirstName(), is(nullValue()));
        assertThat(queryParameters.getPartyLastName(), is(nullValue()));
        assertThat(queryParameters.isProceedingConcluded(), is(nullValue()));
        assertThat(queryParameters.getPartyAddressLine1(), is(nullValue()));
        assertThat(queryParameters.isCrownOrMagistrates(), is(nullValue()));
        assertThat(queryParameters.isBoxWorkVirtualHearing(), is(nullValue()));
    }

}