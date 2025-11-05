package uk.gov.moj.unifiedsearch.query.it.util;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.util.Optional.ofNullable;
import static java.util.UUID.fromString;
import static java.util.stream.IntStream.range;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.laa.DefenceCounsel;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.Address;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.CourtCentre;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.HearingDay;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.HearingSummary;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.HearingType;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.RepresentationOrder;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.CaseSummary;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.DefendantSummary;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.LAAReference;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.OffenceSummary;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.Plea;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.Verdict;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.VerdictType;
import uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.AddressDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.DefenceCounselDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDayDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.LaaReferenceDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.OffenceDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PleaDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.RepresentationOrderDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.VerdictDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.VerdictTypeDocument;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AssertLaaCase {

    private AssertLaaCase() {
    }

    public static void assertCase(final CaseSummary actualCase, final CaseDocument expectedCaseDocument) {
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
        assertRepresentationOrder(actualDefendant.getRepresentationOrder(), expectedPartyDocument.getRepresentationOrder());
    }

    private static void assertOffences(final List<OffenceSummary> actualOffences, final List<OffenceDocument> expectedOffences) {

        final int expectedSize = expectedOffences.size();
        assertThat(actualOffences, hasSize(expectedSize));

        range(0, expectedSize)
                .forEach(index ->
                        assertOffence(actualOffences.get(index), expectedOffences.get(index)));

    }

    public static void assertOffence(final OffenceSummary actualOffence, final OffenceDocument expectedOffence) {
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
        assertLaaReference(actualOffence.getLaaApplnReference(), expectedOffence.getLaaReference());
        assertVerdict(actualOffence.getVerdict(), expectedOffence.getVerdict());
        assertPleas(actualOffence.getPlea(), expectedOffence.getPleas());
    }

    private static void assertLaaReference(final LAAReference actualLaaReference, final LaaReferenceDocument expectedLaaReference) {
        assertThat(actualLaaReference.getApplicationReference(), is(expectedLaaReference.getApplicationReference()));
        assertThat(actualLaaReference.getStatusId(), is(expectedLaaReference.getStatusId()));
        assertThat(actualLaaReference.getStatusCode(), is(expectedLaaReference.getStatusCode()));
        assertThat(actualLaaReference.getStatusDescription(), is(expectedLaaReference.getStatusDescription()));
    }

    private static void assertVerdict(final Verdict actualverdict, final VerdictDocument expectedVerdict) {
        assertThat(actualverdict.getOriginatingHearingId(), is(expectedVerdict.getOriginatingHearingId()));
        assertThat(actualverdict.getVerdictDate(), is(expectedVerdict.getVerdictDate()));
        assertVerdictType(actualverdict.getVerdictType(), expectedVerdict.getVerdictType());
    }

    private static void assertVerdictType(final VerdictType actualverdictType, final VerdictTypeDocument expectedVerdictType) {
        assertThat(actualverdictType.getCategory(), is(expectedVerdictType.getCategory()));
        assertThat(actualverdictType.getCategoryType(), is(expectedVerdictType.getCategoryType()));
    }

    private static void assertPleas(final List<Plea> actualPleas, final List<PleaDocument> expectedPleas) {
        final int expectedSize = expectedPleas.size();
        assertThat(actualPleas, hasSize(expectedSize));
        range(0, expectedSize)
                .forEach(index ->
                        assertPlea(actualPleas.get(index), expectedPleas.get(index)));

    }

    private static void assertPlea(final Plea actualPlea, final PleaDocument expectedPlea) {
        assertThat(actualPlea.getOriginatingHearingId(), is(expectedPlea.getOriginatingHearingId()));
        assertThat(actualPlea.getPleaDate(), is(expectedPlea.getPleaDate()));
        assertThat(actualPlea.getPleaValue(), is(expectedPlea.getPleaValue()));
    }

    private static void assertRepresentationOrder(final RepresentationOrder actualRepresentationOrder, final RepresentationOrderDocument expectedRepresentationOrder) {
        assertThat(actualRepresentationOrder.getApplicationReference(), is(expectedRepresentationOrder.getApplicationReference()));
        assertThat(actualRepresentationOrder.getEffectiveFromDate(), is(expectedRepresentationOrder.getEffectiveFromDate()));
        assertThat(actualRepresentationOrder.getEffectiveToDate(), is(expectedRepresentationOrder.getEffectiveToDate()));
        assertThat(actualRepresentationOrder.getLaaContractNumber(), is(expectedRepresentationOrder.getLaaContractNumber()));
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

        assertDefenceCounsels(actualHearing.getDefenceCounsel(), expectedHearingDocument.getDefenceCounsels());
    }

    private static void assertDefenceCounsels(final List<DefenceCounsel> actualdefenceCounsels, final List<DefenceCounselDocument> expectedDefenceCounsels) {
        final int expectedSize = expectedDefenceCounsels.size();
        assertThat(actualdefenceCounsels, hasSize(expectedSize));
        range(0, expectedSize)
                .forEach(index ->
                        assertDefenceCounsel(actualdefenceCounsels.get(index), expectedDefenceCounsels.get(index)));

    }

    private static void assertDefenceCounsel(final DefenceCounsel actualDefenceCounsel, final DefenceCounselDocument expectedDefenceCounsel) {
        assertThat(actualDefenceCounsel.getId(), is(expectedDefenceCounsel.getId()));
        assertThat(actualDefenceCounsel.getFirstName(), is(expectedDefenceCounsel.getFirstName()));
        assertThat(actualDefenceCounsel.getMiddleName(), is(expectedDefenceCounsel.getMiddleName()));
        assertThat(actualDefenceCounsel.getLastName(), is(expectedDefenceCounsel.getLastName()));
        assertThat(actualDefenceCounsel.getTitle(), is(expectedDefenceCounsel.getTitle()));
        assertThat(actualDefenceCounsel.getStatus(), is(expectedDefenceCounsel.getStatus()));
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


    private static String concatNameFields(final PartyDocument party) {
        final StringBuilder sb = new StringBuilder();

        final Optional<String> firstName = ofNullable(party.getFirstName());
        final Optional<String> middleName = ofNullable(party.getMiddleName());
        final Optional<String> lastName = ofNullable(party.getLastName());

        firstName.map(String::toString).ifPresent(sb::append);
        middleName.map(String::toString).ifPresent(n -> {
            sb.append(" ").append(n);
        });
        lastName.map(String::toString).ifPresent(n -> {
            sb.append(" ").append(n);
        });

        return sb.toString().trim();
    }
}
