package uk.gov.moj.unifiedsearch.query.it.util;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.util.UUID.fromString;
import static java.util.stream.IntStream.range;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.Address;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.CourtCentre;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.HearingDay;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.HearingSummary;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.HearingType;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.probation.DefendantSummary;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.probation.OffenceSummary;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.probation.ProbationDefendantDetailsSummary;
import uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.AddressDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDayDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.OffenceDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AssertProbationDefendantDetails {

    private AssertProbationDefendantDetails() {
    }

    public static void assertCase(final ProbationDefendantDetailsSummary actualCase, final CaseDocument expectedCaseDocument) {
        assertThat(actualCase.getProsecutionCaseReference(), is(expectedCaseDocument.getCaseReference()));
        assertThat(actualCase.getProsecutionCaseId(), is(fromString(expectedCaseDocument.getCaseId())));
        assertThat(actualCase.getCaseStatus(), is(expectedCaseDocument.getCaseStatus()));

        final List<PartyDocument> expectedDefendants = expectedCaseDocument.getParties().stream().filter(p -> PartyType.DEFENDANT.name().equalsIgnoreCase(p.get_party_type())).collect(Collectors.toList());
        assertParties(actualCase.getDefendantSummary(), expectedDefendants);

        assertHearings(actualCase.getHearingSummary(), expectedCaseDocument.getHearings());
    }

    private static void assertParties(final List<DefendantSummary> parties, final List<PartyDocument> partyDocuments) {

        final int expectedSize = partyDocuments.size();
        assertThat(parties, hasSize(expectedSize));

        range(0, expectedSize)
                .forEach(index ->
                        assertParty(parties.get(index), partyDocuments.get(index)));

    }

    private static void assertParty(final DefendantSummary actualDefendant, final PartyDocument expectedPartyDocument) {
        assertThat(actualDefendant.getDefendantId(), is(expectedPartyDocument.getPartyId()));
        assertThat(actualDefendant.getDefendantFirstName(), is(expectedPartyDocument.getFirstName()));
        assertThat(actualDefendant.getDefendantMiddleName(), is(expectedPartyDocument.getMiddleName()));
        assertThat(actualDefendant.getDefendantLastName(), is(expectedPartyDocument.getLastName()));
        assertThat(actualDefendant.getDefendantDOB(), is(expectedPartyDocument.getDateOfBirth()));
        assertThat(actualDefendant.getDefendantNINO(), is(expectedPartyDocument.getNationalInsuranceNumber()));
        assertThat(actualDefendant.getDefendantASN(), is(expectedPartyDocument.getArrestSummonsNumber()));
        assertThat(actualDefendant.getMasterDefendantId(), is(expectedPartyDocument.getMasterPartyId()));
        assertOffences(actualDefendant.getOffenceSummary(), expectedPartyDocument.getOffences());
        if (Objects.nonNull(actualDefendant.getDefendantAddress()) && Objects.nonNull(expectedPartyDocument.getDefendantAddress())) {
            assertThat(actualDefendant.getDefendantAddress().getAddress1(), is(expectedPartyDocument.getDefendantAddress().getAddress1()));
            assertThat(actualDefendant.getDefendantAddress().getAddress2(), is(expectedPartyDocument.getDefendantAddress().getAddress2()));
            assertThat(actualDefendant.getDefendantAddress().getAddress3(), is(expectedPartyDocument.getDefendantAddress().getAddress3()));
            assertThat(actualDefendant.getDefendantAddress().getAddress4(), is(expectedPartyDocument.getDefendantAddress().getAddress4()));
            assertThat(actualDefendant.getDefendantAddress().getAddress5(), is(expectedPartyDocument.getDefendantAddress().getAddress5()));
            assertThat(actualDefendant.getDefendantAddress().getPostCode(), is(expectedPartyDocument.getDefendantAddress().getPostCode()));
        }
    }

    private static void assertOffences(final List<uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.probation.OffenceSummary> actualOffences, final List<OffenceDocument> expectedOffences) {

        final int expectedSize = expectedOffences.size();
        assertThat(actualOffences, hasSize(expectedSize));

        range(0, expectedSize)
                .forEach(index ->
                        assertOffence(actualOffences.get(index), expectedOffences.get(index)));

    }

    private static void assertOffence(final OffenceSummary actualOffence, final OffenceDocument expectedOffence) {
        assertThat(actualOffence.getOffenceId(), is(expectedOffence.getOffenceId()));
        assertThat(actualOffence.getOffenceCode(), is(expectedOffence.getOffenceCode()));
        assertThat(actualOffence.getOffenceLegislation(), is(expectedOffence.getOffenceLegislation()));
        assertThat(actualOffence.isProceedingsConcluded(), is(expectedOffence.getProceedingsConcluded()));
        assertThat(actualOffence.getArrestDate(), is(expectedOffence.getArrestDate()));
        assertThat(actualOffence.getDateOfInformation(), is(expectedOffence.getDateOfInformation()));
        assertThat(actualOffence.getEndDate(), is(expectedOffence.getEndDate()));
        assertThat(actualOffence.getStartDate(), is(expectedOffence.getStartDate()));
        assertThat(actualOffence.getChargeDate(), is(expectedOffence.getChargeDate()));
        assertThat(actualOffence.getModeOfTrial(), is(expectedOffence.getModeOfTrial()));
        assertThat(actualOffence.getOrderIndex(), is(expectedOffence.getOrderIndex()));
        assertThat(actualOffence.getWording(), is(expectedOffence.getWording()));
        assertThat(actualOffence.getOffenceId(), is(expectedOffence.getOffenceId()));
    }

    private static void assertHearings(final List<HearingSummary> hearings, final List<HearingDocument> hearingDocuments) {

        final int expectedSize = hearingDocuments.size();
        assertThat(hearingDocuments, hasSize(expectedSize));

        range(0, expectedSize)
                .forEach(index ->
                        assertHearing(hearings.get(index), hearingDocuments.get(index)));
    }

    private static void assertHearing(final HearingSummary actualHearing, final HearingDocument expectedHearingDocument) {
        assertThat(actualHearing.getHearingId(), is(expectedHearingDocument.getHearingId()));
        assertThat(actualHearing.getJurisdictionType(), is(expectedHearingDocument.getJurisdictionType()));

        final HearingType hearingType = actualHearing.getHearingType();
        assertThat(hearingType, is(notNullValue()));
        assertThat(hearingType.getId(), is(expectedHearingDocument.getHearingTypeId()));
        assertThat(hearingType.getDescription(), is(expectedHearingDocument.getHearingTypeLabel()));
        assertThat(hearingType.getCode(), is(expectedHearingDocument.getHearingTypeCode()));

        final CourtCentre courtCentre = actualHearing.getCourtCentre();
        assertThat(courtCentre, is(notNullValue()));
        assertThat(courtCentre.getId(), is(expectedHearingDocument.getCourtId()));
        assertThat(courtCentre.getName(), is(expectedHearingDocument.getCourtCentreName()));
        assertThat(courtCentre.getRoomId(), is(expectedHearingDocument.getCourtCentreRoomId()));
        assertThat(courtCentre.getRoomName(), is(expectedHearingDocument.getCourtCentreRoomName()));
        assertThat(courtCentre.getWelshName(), is(expectedHearingDocument.getCourtCentreWelshName()));
        assertThat(courtCentre.getWelshRoomName(), is(expectedHearingDocument.getCourtCentreRoomWelshName()));
        assertThat(courtCentre.getCode(), is(expectedHearingDocument.getCourtCentreCode()));

        final Address laaAddress = courtCentre.getAddress();
        final AddressDocument unifiedSearchddress = expectedHearingDocument.getCourtCentreAddress();
        assertThat(laaAddress, is(notNullValue()));
        assertThat(laaAddress.getAddress1(), is(unifiedSearchddress.getAddress1()));
        assertThat(laaAddress.getAddress2(), is(unifiedSearchddress.getAddress2()));
        assertThat(laaAddress.getAddress3(), is(unifiedSearchddress.getAddress3()));
        assertThat(laaAddress.getAddress4(), is(unifiedSearchddress.getAddress4()));
        assertThat(laaAddress.getAddress5(), is(unifiedSearchddress.getAddress5()));
        assertThat(laaAddress.getPostCode(), is(unifiedSearchddress.getPostCode()));

        assertHearingDays(actualHearing.getHearingDays(), expectedHearingDocument.getHearingDays());
    }

    private static void assertHearingDays(final List<HearingDay> actualHearingDays, final List<HearingDayDocument> expectedHearingDocuments) {

        final int expectedSize = expectedHearingDocuments.size();
        assertThat(actualHearingDays, hasSize(expectedSize));

        range(0, expectedSize)
                .forEach(index ->
                        assertHearingDay(actualHearingDays.get(index), expectedHearingDocuments.get(index)));

    }

    private static void assertHearingDay(final HearingDay actualHearingDay, final HearingDayDocument expectedHearingDocument) {
        assertThat(actualHearingDay.getListedDurationMinutes(), is(expectedHearingDocument.getListedDurationMinutes()));
        assertThat(actualHearingDay.getListingSequence(), is(expectedHearingDocument.getListingSequence()));
        assertThat(actualHearingDay.getSittingDay(), is(expectedHearingDocument.getSittingDay()));
        assertThat(actualHearingDay.getHasSharedResults(), is(expectedHearingDocument.getHasSharedResults()));
    }

}
