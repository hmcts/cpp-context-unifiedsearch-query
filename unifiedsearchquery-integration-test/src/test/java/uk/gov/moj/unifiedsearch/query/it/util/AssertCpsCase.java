package uk.gov.moj.unifiedsearch.query.it.util;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.justice.services.unifiedsearch.client.constant.CpsPartyType.DEFENDANT;
import static uk.gov.justice.services.unifiedsearch.client.constant.CpsPartyType.SUSPECT;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Hearing;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Party;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.HearingDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.PartyDocument;

import java.util.List;
import java.util.stream.Collectors;

public class AssertCpsCase {

    private AssertCpsCase() {
    }

    public static void assertCase(final Case actualCase, final CaseDocument expectedCaseDocument) {
        assertThat(actualCase.getCaseId(), is(expectedCaseDocument.getCaseId()));
        assertThat(actualCase.getCaseStatusCode(), is(expectedCaseDocument.getCaseStatusCode()));
        assertThat(actualCase.getCaseType(), is(expectedCaseDocument.getCaseType()));

        assertParties(actualCase.getParties(), expectedCaseDocument.getParties());

        assertHearings(actualCase.getHearings(), expectedCaseDocument.getHearings());
    }

    private static void assertParties(final List<Party> parties, final List<PartyDocument> partyDocuments) {
       final  List<PartyDocument> partiesExpected = ofNullable(partyDocuments)
               .map(documents -> documents.stream()
                       .filter(partyExpected -> partyExpected.get_party_type().contains(DEFENDANT.toString()) || partyExpected.get_party_type().contains(SUSPECT.toString()))
                       .collect(Collectors.toList()))
               .orElse(emptyList());
        assertThat(parties, hasSize(partiesExpected.size()));
        for (int i = 0; i < partiesExpected.size(); i++) {
            final Party actualParty = parties.get(i);
            final PartyDocument expectedPartyDocument = partiesExpected.get(i);

            assertParty(actualParty, expectedPartyDocument);
        }
    }

    private static void assertParty(final Party actualParty, final PartyDocument expectedPartyDocument) {
        assertThat(actualParty.getPartyId(), is(expectedPartyDocument.getPartyId()));
        assertThat(actualParty.getPartyType(), is(expectedPartyDocument.get_party_type()));
        assertThat(actualParty.getDateOfBirth(), is(expectedPartyDocument.getDateOfBirth()));
        assertThat(actualParty.getFirstName(), is(expectedPartyDocument.getFirstName()));
        assertThat(actualParty.getLastName(), is(expectedPartyDocument.getLastName()));
        assertThat(actualParty.getOrganisationName(), is(expectedPartyDocument.getOrganisationName()));
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

    }
}
