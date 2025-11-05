package uk.gov.moj.unifiedsearch.query.it.multi;

import static java.lang.String.format;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.APPLICANT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.DEFENDANT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.RESPONDENT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.hasCases;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother;

import java.util.List;
import java.util.Map;

public class PartyTypeFacetTestParameters implements FacetTestParameters {

    private static final String PARTY_TYPE_PARAM_NAME = "partyTypes";

    private static final int TEST_DOCUMENT_COUNT = 12;

    private String failureMessage;


    @Override
    public void addSearchParameters(final Map<String, String> parameters) {
        parameters.put(PARTY_TYPE_PARAM_NAME, DEFENDANT.name());
    }

    @Override
    public void addMatchData(final List<CaseDocument.Builder> caseBuilderList) {

        updateOrAddParty(DEFENDANT.name(), caseBuilderList.get(0).getPartyBuilders());
        updateOrAddParty(APPLICANT.name(), caseBuilderList.get(1).getPartyBuilders());
        updateOrAddParty(DEFENDANT.name(), caseBuilderList.get(2).getPartyBuilders());
        updateOrAddParty(null, caseBuilderList.get(3).getPartyBuilders());
        updateOrAddParty(DEFENDANT.name(), caseBuilderList.get(4).getPartyBuilders());
        updateOrAddParty(APPLICANT.name(), caseBuilderList.get(5).getPartyBuilders());
        updateOrAddParty(DEFENDANT.name(), caseBuilderList.get(6).getPartyBuilders());
        updateOrAddParty(null, caseBuilderList.get(7).getPartyBuilders());
        updateOrAddParty(APPLICANT.name(), caseBuilderList.get(8).getPartyBuilders());
        updateOrAddParty(DEFENDANT.name(), caseBuilderList.get(9).getPartyBuilders());
        updateOrAddParty(RESPONDENT.name(), caseBuilderList.get(10).getPartyBuilders());
        updateOrAddParty(APPLICANT.name(), caseBuilderList.get(11).getPartyBuilders());

        // Set party type  in any further Cases
        if (caseBuilderList.size() > TEST_DOCUMENT_COUNT) {
            for (int i = TEST_DOCUMENT_COUNT; i < caseBuilderList.size(); i++) {
                caseBuilderList.get(i).getPartyBuilders().forEach(pb -> pb.withPartyType(null));
            }
        }
    }

    private void updateOrAddParty(final String partyType, final List<PartyDocument.Builder> targetParties) {

        if (targetParties.size() == 0) {
            targetParties.add(PartyDocumentMother.defaultPartyAsBuilder());
        }

        final PartyDocument.Builder targetPartyBuilder = targetParties.get(0);

        targetPartyBuilder.withPartyType(partyType);

    }

    @Override
    public void ensureCapacity(final List<CaseDocument.Builder> caseBuilderList) {

        final int requiredDocsCount = TEST_DOCUMENT_COUNT - caseBuilderList.size();

        if (requiredDocsCount > 0) {
            caseBuilderList.addAll( defaultCasesAsBuilderList(requiredDocsCount) );
        }
    }

    @Override
    public boolean hasExpectedHits(final List<Case> caseList) {

        // Other matchers may have precluded some of the above added data so we need
        // to test that however many cases are provided here, the Parties they contain
        // have expected PartyTypw values

        if (hasCases(caseList)) {

            if (!allMatch(caseList)) {
                failureMessage = format("Unexpected Party.partyType in provided case list, was expecting all CaseDocuments to have PartyType of [%s]", DEFENDANT);
                return false;
            }
        }
        else {
            failureMessage = "Empty case list provided";
            return false;
        }

        return true;

    }


    private boolean allMatch(final List<Case> caseList) {
        return caseList.stream()
                .map(c -> c.getParties().get(0))
                .allMatch(p -> DEFENDANT.name().equals(p.getPartyType()));
    }

    @Override
    public String getFailureMessage() {
        return failureMessage;
    }
}
