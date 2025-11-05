package uk.gov.moj.unifiedsearch.query.it.util.defendantCase;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.util.UUID.fromString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.defendant.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.defendant.Party;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.util.List;

public class AssertDefendantCase {

    private AssertDefendantCase() {
    }

    public static void assertDefendantCase(final Case actualDefendantCase, final CaseDocument expectedCaseDocument) {
        assertThat(actualDefendantCase.getCaseReference(), is(expectedCaseDocument.getCaseReference()));
        assertThat(actualDefendantCase.getProsecutionCaseId(), is(fromString(expectedCaseDocument.getCaseId())));
        assertParties(actualDefendantCase.getDefendants(), expectedCaseDocument.getParties());
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
        assertThat(actualParty.getPncId(), is(expectedPartyDocument.getPncId()));
        assertThat(actualParty.getCroNumber(), is(expectedPartyDocument.getCroNumber()));
        assertThat(actualParty.getMiddleName(), is(expectedPartyDocument.getMiddleName()));
        assertThat(actualParty.getFirstName(), is(expectedPartyDocument.getFirstName()));
        assertThat(actualParty.getLastName(), is(expectedPartyDocument.getLastName()));
        assertThat(actualParty.getDateOfBirth(), is(expectedPartyDocument.getDateOfBirth()));
        assertThat(actualParty.getMasterDefendantId(), is(expectedPartyDocument.getMasterPartyId()));
        assertThat(actualParty.getCourtProceedingsInitiated(), is(expectedPartyDocument.getCourtProceedingsInitiated()));
        assertThat(actualParty.getAddress().getAddress1(), is(expectedPartyDocument.getDefendantAddress().getAddress1()));
        assertThat(actualParty.getAddress().getAddress2(), is(expectedPartyDocument.getDefendantAddress().getAddress2()));
        assertThat(actualParty.getAddress().getAddress3(), is(expectedPartyDocument.getDefendantAddress().getAddress3()));
        assertThat(actualParty.getAddress().getAddress4(), is(expectedPartyDocument.getDefendantAddress().getAddress4()));
        assertThat(actualParty.getAddress().getAddress5(), is(expectedPartyDocument.getDefendantAddress().getAddress5()));
        assertThat(actualParty.getAddress().getPostCode(), is(expectedPartyDocument.getDefendantAddress().getPostCode()));
        assertThat(actualParty.getCourtProceedingsInitiated(), is(expectedPartyDocument.getCourtProceedingsInitiated()));
        assertThat(actualParty.getDefendantId().toString(), is(expectedPartyDocument.getPartyId()));
        assertThat(actualParty.getMasterDefendantId().toString(), is(expectedPartyDocument.getMasterPartyId()));

    }
}
