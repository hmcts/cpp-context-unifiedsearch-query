package uk.gov.moj.unifiedsearch.query.it.util;

import static java.lang.Boolean.valueOf;
import static java.util.UUID.fromString;
import static java.util.stream.IntStream.range;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import uk.gov.moj.cpp.unifiedsearch.query.common.constant.PartyType;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.HearingDay;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Application;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Hearing;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Offence;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Party;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.OffenceSummary;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.ApplicationDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDayDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.OffenceDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.util.List;

public class AssertCase {

    private AssertCase() {
    }

    public static void assertCase(final Case actualCase, final CaseDocument expectedCaseDocument) {
        assertThat(actualCase.getCaseReference(), is(expectedCaseDocument.getCaseReference()));
        assertThat(actualCase.getCaseId(), is(fromString(expectedCaseDocument.getCaseId())));
        assertThat(actualCase.getProsecutingAuthority(), is(expectedCaseDocument.getProsecutingAuthority()));
        assertThat(actualCase.isSjp(), is(expectedCaseDocument.is_is_sjp()));
        assertThat(actualCase.isCrownCourt(), is(expectedCaseDocument.is_is_crown()));
        assertThat(actualCase.isMagistrateCourt(), is(expectedCaseDocument.is_is_magistrates()));
        assertThat(actualCase.getCaseStatus(), is(expectedCaseDocument.getCaseStatus()));
        assertThat(actualCase.getCaseType(), is(expectedCaseDocument.get_case_type()));

        final List<Application> applications = actualCase.getApplications();
        final List<ApplicationDocument> applicationDocuments = expectedCaseDocument.getApplications();

        assertApplications(applications, applicationDocuments);

        assertParties(actualCase.getParties(), expectedCaseDocument.getParties());

        assertHearings(actualCase.getHearings(), expectedCaseDocument.getHearings());
        assertThat(actualCase.getSourceSystemReference(), is(expectedCaseDocument.getSourceSystemReference()));
    }

    private static void assertParties(final List<Party> parties, final List<PartyDocument> partyDocuments) {

        final int expectedSize = partyDocuments.size();
        assertThat(parties, hasSize(expectedSize));
        for (int i = 0; i < expectedSize; i++) {
            final Party actualParty = parties.get(i);
            final PartyDocument expectedPartyDocument = partyDocuments.get(i);

            assertParty(actualParty, expectedPartyDocument);
        }
    }

    private static void assertParty(final Party actualParty, final PartyDocument expectedPartyDocument) {
        assertThat(actualParty.getPartyId(), is(fromString(expectedPartyDocument.getPartyId())));
        assertThat(actualParty.getPartyType(), is(expectedPartyDocument.get_party_type()));
        assertThat(actualParty.getOrganisationName(), is(expectedPartyDocument.getOrganisationName()));
        assertThat(actualParty.getMiddleName(), is(expectedPartyDocument.getMiddleName()));
        assertThat(actualParty.getFirstName(), is(expectedPartyDocument.getFirstName()));
        assertThat(actualParty.getLastName(), is(expectedPartyDocument.getLastName()));
        assertThat(actualParty.getDateOfBirth(), is(expectedPartyDocument.getDateOfBirth()));
        if (actualParty.getPartyType().equals(PartyType.DEFENDANT)) {
            assertOffences(actualParty.getOffences(), expectedPartyDocument.getOffences());
        }
    }

    private static void assertOffences(final List<Offence> actualOffences, final List<OffenceDocument> expectedOffences) {

        final int expectedSize = expectedOffences.size();
        assertThat(actualOffences, hasSize(expectedSize));

        range(0, expectedSize)
                .forEach(index ->
                        assertOffence(actualOffences.get(index), expectedOffences.get(index)));

    }

    private static void assertOffence(final Offence actualOffence, final OffenceDocument expectedOffence) {
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

    private static void assertApplications(final List<Application> applications, final List<ApplicationDocument> applicationDocuments) {

        final int expectedSize = applicationDocuments.size();

        assertThat(applicationDocuments, hasSize(expectedSize));

        for (int i = 0; i < expectedSize; i++) {
            final Application actualApplication = applications.get(i);
            final ApplicationDocument expectedApplicationDocument = applicationDocuments.get(i);

            assertApplication(actualApplication, expectedApplicationDocument);
        }
    }


    private static void assertApplication(final Application actualApplication, final ApplicationDocument expectedApplicationDocument) {

        assertThat(actualApplication.getApplicationId(), is(fromString(expectedApplicationDocument.getApplicationId())));
        assertThat(actualApplication.getReceivedDate(), is(expectedApplicationDocument.getReceivedDate()));
        assertThat(actualApplication.getDecisionDate(), is(expectedApplicationDocument.getDecisionDate()));
        assertThat(actualApplication.getDueDate(), is(expectedApplicationDocument.getDueDate()));
        assertThat(actualApplication.getApplicationReference(), is(expectedApplicationDocument.getApplicationReference()));
        assertThat(actualApplication.getApplicationType(), is(expectedApplicationDocument.getApplicationType()));
        assertThat(actualApplication.getApplicationStatus(), is(expectedApplicationDocument.getApplicationStatus()));
        assertThat(actualApplication.getApplicationExternalCreatorType(), is(expectedApplicationDocument.getApplicationExternalCreatorType()));
    }


    private static void assertHearings(final List<Hearing> hearings, final List<HearingDocument> hearingDocuments) {

        final int expectedSize = hearingDocuments.size();

        assertThat(hearingDocuments, hasSize(expectedSize));

        for (int i = 0; i < expectedSize; i++) {
            final Hearing actualHearing = hearings.get(i);
            final HearingDocument expectedHearingDocument = hearingDocuments.get(i);

            assertHearing(actualHearing, expectedHearingDocument);
        }
    }

    private static void assertHearing(final Hearing actualHearing, final HearingDocument expectedHearingDocument) {
        assertThat(actualHearing.getHearingId(), is(expectedHearingDocument.getHearingId()));
        assertThat(actualHearing.getHearingDates(), is(expectedHearingDocument.getHearingDates()));
        assertThat(actualHearing.getCourtCentreName(), is(expectedHearingDocument.getCourtCentreName()));
        assertThat(actualHearing.getCourtId(), is(expectedHearingDocument.getCourtId()));
        assertThat(actualHearing.getHearingTypeId(), is(expectedHearingDocument.getHearingTypeId()));
        assertThat(actualHearing.getHearingTypeLabel(), is(expectedHearingDocument.getHearingTypeLabel()));
        assertThat(actualHearing.getJudiciaryTypes(), is(expectedHearingDocument.getJudiciaryTypes()));
        assertThat(actualHearing.getJurisdictionType(), is(expectedHearingDocument.getJurisdictionType()));
        assertThat(actualHearing.getCourtCentreCode(), is(nullValue()));
        assertThat(actualHearing.isIsVirtualBoxHearing(), is(assertBoolean(expectedHearingDocument.isIsVirtualBoxHearing())));
        assertThat(actualHearing.isIsBoxHearing(), is(assertBoolean(expectedHearingDocument.isIsBoxHearing())));
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

    private static boolean assertBoolean(final Boolean booleanValue) {
        return booleanValue == null ? false : valueOf(booleanValue);
    }

}
