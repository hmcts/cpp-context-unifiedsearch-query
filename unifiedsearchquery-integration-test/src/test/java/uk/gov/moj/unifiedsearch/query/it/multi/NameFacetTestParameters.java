package uk.gov.moj.unifiedsearch.query.it.multi;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother.defaultPartyAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Party;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NameFacetTestParameters implements FacetTestParameters {

    private static final String PARTY_NAME_PARAM_NAME = "partyName";

    private static final String PARTY_NAME = "John Allen Smith";

    private static final int TEST_DOCUMENT_COUNT = 12;

    private final PartyDocument johnSmith = defaultPartyAsBuilder().withFirstName("John").withLastName("Smith").withMiddleName(null).withOrganisationName(null).build();
    private final PartyDocument johnAllenSmith = defaultPartyAsBuilder().withFirstName("John").withMiddleName("Allen").withOrganisationName(null).withLastName("Smith").build();
    private final PartyDocument johnLuisSmith = defaultPartyAsBuilder().withFirstName("John").withMiddleName("Luis").withOrganisationName(null).withLastName("Smith").build();
    private final PartyDocument markSmith = defaultPartyAsBuilder().withFirstName("Mark").withLastName("Smith").withMiddleName(null).withOrganisationName(null).build();
    private final PartyDocument markAllenSmith = defaultPartyAsBuilder().withFirstName("Mark").withMiddleName("Allen").withOrganisationName(null).withLastName("Smith").build();
    private final PartyDocument markLuisSmith = defaultPartyAsBuilder().withFirstName("Mark").withMiddleName("Luis").withOrganisationName(null).withLastName("Smith").build();
    private final PartyDocument jamesSmithson = defaultPartyAsBuilder().withFirstName("James").withLastName("Smithson").withMiddleName(null).withOrganisationName(null).build();
    private final PartyDocument johnCraik = defaultPartyAsBuilder().withFirstName("John").withLastName("Craik").withMiddleName(null).withOrganisationName(null).build();
    private final PartyDocument johnAndrewSmith = defaultPartyAsBuilder().withFirstName("John").withMiddleName("Andrew").withOrganisationName(null).withLastName("Smith").build();
    private final PartyDocument johnSmithAllen = defaultPartyAsBuilder().withFirstName("John").withMiddleName("Smith").withOrganisationName(null).withLastName("Allen").build();
    private final PartyDocument johnAllanSmith = defaultPartyAsBuilder().withFirstName("John").withMiddleName("Allan").withOrganisationName(null).withLastName("Smith").build();
    private final PartyDocument johnsAllenSmith = defaultPartyAsBuilder().withFirstName("Johns").withMiddleName("Allen").withOrganisationName(null).withLastName("Smith").build();

    private final List<PartyDocument> expectedOrderList = asList(
       johnAllenSmith,
       johnsAllenSmith,
       johnAllanSmith,
       johnSmithAllen,
       johnLuisSmith,
       johnSmith,
       johnAndrewSmith,
       johnCraik,
       markAllenSmith,
       markLuisSmith,
       markSmith,
       jamesSmithson);

    private String failureMessage;

    @Override
    public void addSearchParameters(final Map<String, String> parameters) {
        parameters.put(PARTY_NAME_PARAM_NAME, PARTY_NAME);
    }

    @Override
    public void addMatchData(final List<CaseDocument.Builder> caseBuilderList) {

        updateOrAddParty(johnAllenSmith, caseBuilderList.get(0).getPartyBuilders());
        updateOrAddParty(johnAllanSmith, caseBuilderList.get(1).getPartyBuilders());
        updateOrAddParty(johnsAllenSmith, caseBuilderList.get(2).getPartyBuilders());
        updateOrAddParty(johnSmithAllen, caseBuilderList.get(3).getPartyBuilders());
        updateOrAddParty(johnLuisSmith, caseBuilderList.get(4).getPartyBuilders());
        updateOrAddParty(johnSmith, caseBuilderList.get(5).getPartyBuilders());
        updateOrAddParty(johnAndrewSmith, caseBuilderList.get(6).getPartyBuilders());
        updateOrAddParty(markAllenSmith, caseBuilderList.get(7).getPartyBuilders());
        updateOrAddParty(markLuisSmith, caseBuilderList.get(8).getPartyBuilders());
        updateOrAddParty(markSmith, caseBuilderList.get(9).getPartyBuilders());
        updateOrAddParty(johnCraik, caseBuilderList.get(10).getPartyBuilders());
        updateOrAddParty(jamesSmithson, caseBuilderList.get(11).getPartyBuilders());

    }

    private void updateOrAddParty(final PartyDocument party, final List<PartyDocument.Builder> targetParties) {

        if (targetParties.size() == 0) {
            targetParties.add(PartyDocumentMother.defaultPartyAsBuilder());
        }

        final PartyDocument.Builder targetPartyBuilder = targetParties.get(0);

        targetPartyBuilder.withFirstName(party.getFirstName())
                .withMiddleName(party.getMiddleName())
                .withLastName(party.getLastName())
                .withOrganisationName(party.getOrganisationName());


        if (targetParties.size() > 1) {
            // Need to remove any further parties as having these will affect the
            // query score and therefore the ordering of the returned documents when
            // querying by name fields.
            final List<PartyDocument.Builder> subList = targetParties.subList(1, targetParties.size());
            targetParties.removeAll(subList);
        }

    }



    @Override
    public boolean hasExpectedHits(final List<Case> caseList) {

        // Other matchers may have precluded some of the above added data so we need
        // to test that however many cases are provided here, the parties they contain
        // are in the above order

        if (!inCorrectOrder(caseList)) {
            failureMessage = String.format("Provided cases are not in expected party name order [%s]", partyNamesInActualOrder(caseList));
            return false;
        }

        return true;

    }

    private String partyNamesInActualOrder(final List<Case> caseList) {

        return caseList.stream()
                .map(Case::getParties)
                .map(pl -> pl.get(0))
                .map(this::toPartyNameString)
                .collect(joining(", "));

    }

    private String toPartyNameString(final Party party) {
            return new StringBuilder().append(party.getFirstName())
                    .append(" ")
                    .append(party.getMiddleName())
                    .append(" ")
                    .append(party.getLastName())
                    .toString()
                    .replaceAll("  ", " ");
    }

    @Override
    public String getFailureMessage() {
        return failureMessage;
    }

    @Override
    public void ensureCapacity(final List<CaseDocument.Builder> caseBuilderList) {

        // Add any further CaseDocuments if there aren't enough in the provided list
        final int requiredDocsCount = TEST_DOCUMENT_COUNT - caseBuilderList.size();

        if (requiredDocsCount > 0) {
            caseBuilderList.addAll( defaultCasesAsBuilderList(requiredDocsCount));
        }

    }


    private boolean inCorrectOrder(final List<Case> caseList) {

        boolean returnCode = true;

        int lastPosition = 0;

        for (final Case aCase : caseList) {
            final Optional<Integer> position = positionInExpectedOrderList(aCase);
            if (position.isPresent() && position.get() < lastPosition) {
                returnCode = false;
                break;
            }

            lastPosition = position.get();
        }


        return returnCode;
    }



    private Optional<Integer> positionInExpectedOrderList(final Case aCase) {

        return expectedOrderList.stream()
                .filter(party -> isCasePartyByNames(party, aCase))
                .map(expectedOrderList::indexOf)
                .findFirst();

    }


    private boolean isCasePartyByNames(final PartyDocument party, final Case aCase) {

        final Party targetParty = aCase.getParties().get(0);

        boolean returnCode = false;

        if (party.getFirstName() != null && targetParty.getFirstName() != null) {
            returnCode = party.getFirstName().equals(targetParty.getFirstName());
        }
        else if (party.getFirstName() == null && targetParty.getFirstName() == null) {
            returnCode = true;
        }

        if (party.getMiddleName() != null && targetParty.getMiddleName() != null) {
            returnCode = returnCode && party.getMiddleName().equals(targetParty.getMiddleName());
        }
        else if (party.getMiddleName() == null && targetParty.getMiddleName() == null) {
            returnCode = returnCode && true;
        }
        else {
            returnCode = false;
        }

        if (party.getLastName() != null && targetParty.getLastName() != null) {
            returnCode = returnCode & party.getLastName().equals(targetParty.getLastName());
        }
        else if (party.getLastName() == null && targetParty.getLastName() == null) {
            returnCode = returnCode && true;
        }
        else {
            returnCode = false;
        }

        return returnCode;
    }

}
