package uk.gov.moj.unifiedsearch.query.it.multi;

import static java.lang.String.format;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.hasCases;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother;

import java.util.List;
import java.util.Map;

public class DateOfBirthFacetTestParameters implements FacetTestParameters {

    private static final String DATE_OF_BIRTH_PARAM_NAME = "partyDateOfBirth";
    private static final String DATE_OF_BIRTH_TO_QUERY = "1972-11-23";

    private static final int TEST_DOCUMENT_COUNT = 8;

    private String failureMessage;


    @Override
    public void addSearchParameters(final Map<String, String> parameters) {

        parameters.put(DATE_OF_BIRTH_PARAM_NAME, DATE_OF_BIRTH_TO_QUERY);

    }

    @Override
    public void addMatchData(final List<CaseDocument.Builder> caseBuilderList) {

        updateOrAddParty("1954-04-29", caseBuilderList.get(0).getPartyBuilders());
        updateOrAddParty("1958-07-18", caseBuilderList.get(1).getPartyBuilders());
        updateOrAddParty("1972-11-23", caseBuilderList.get(2).getPartyBuilders());
        updateOrAddParty("1972-11-23", caseBuilderList.get(3).getPartyBuilders());
        updateOrAddParty("1975-08-20", caseBuilderList.get(4).getPartyBuilders());
        updateOrAddParty("1950-05-05", caseBuilderList.get(5).getPartyBuilders());
        updateOrAddParty("1972-11-23", caseBuilderList.get(6).getPartyBuilders());
        updateOrAddParty("1981-02-27", caseBuilderList.get(7).getPartyBuilders());

        // Clear DOB in any further Cases
        if (caseBuilderList.size() > TEST_DOCUMENT_COUNT) {
            for (int i = TEST_DOCUMENT_COUNT; i < caseBuilderList.size(); i++) {
                caseBuilderList.get(i).getPartyBuilders().forEach(pb -> pb.withDateOfBirth(null));
            }
        }

    }

    private void updateOrAddParty(final String dob, final List<PartyDocument.Builder> targetParties) {

        if (targetParties.size() == 0) {
            targetParties.add(PartyDocumentMother.defaultPartyAsBuilder());
        }

        final PartyDocument.Builder targetPartyBuilder = targetParties.get(0);

        targetPartyBuilder.withDateOfBirth(dob);


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
        // have expected DateOfBirth values
        if (hasCases(caseList)) {

            if (!allMatch(caseList)) {
                failureMessage = format("Unexpected Party.dateOfBirth in providedList case, was expecting all CaseDocuments to have PartyDateOfBirth of [%s]", DATE_OF_BIRTH_TO_QUERY);
                return false;
            }
        }
        else {
            failureMessage = "Empty case list provided";
            return false;
        }

        return true;

    }

    @Override
    public String getFailureMessage() {
        return failureMessage;
    }

    private boolean allMatch(final List<Case> caseList) {
        return caseList.stream()
                .map(c -> c.getParties().get(0))
                .allMatch(p -> p.getDateOfBirth().equals(DATE_OF_BIRTH_TO_QUERY));
    }



}
