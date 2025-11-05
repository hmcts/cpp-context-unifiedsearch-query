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

public class AddressFacetTestParameters implements FacetTestParameters {

    private static final String PARTY_ADDRESS_PARAM_NAME = "partyAddress";

    private static final String PARTY_ADDRESS_TO_QUERY = "Liverpool";

    private static final int TEST_DOCUMENT_COUNT = 11;

    private String failureMessage;


    @Override
    public void addSearchParameters(final Map<String, String> parameters) {

        parameters.put(PARTY_ADDRESS_PARAM_NAME, PARTY_ADDRESS_TO_QUERY);

    }

    @Override
    public void addMatchData(final List<CaseDocument.Builder> caseBuilderList) {

        updateOrAddPartyWithAddress("83 Liverpool Boulevard Woking Idaho", caseBuilderList.get(0).getPartyBuilders());
        updateOrAddPartyWithAddress("74 Liverpool Street Portsmouth Connecticut", caseBuilderList.get(1).getPartyBuilders());
        updateOrAddPartyWithAddress("130 Liverpool road Brighton Rhode Island", caseBuilderList.get(2).getPartyBuilders());
        updateOrAddPartyWithAddress("70 Irving Avenue Hemel Hemstead Liverpoo", caseBuilderList.get(3).getPartyBuilders());
        updateOrAddPartyWithAddress("130 Liverpool close Brighton Rhode Island", caseBuilderList.get(4).getPartyBuilders());
        updateOrAddPartyWithAddress("59 Baughman Place Liverpool New Jersey", caseBuilderList.get(5).getPartyBuilders());
        updateOrAddPartyWithAddress("18 Furman Avenue Hertford Colorado", caseBuilderList.get(6).getPartyBuilders());
        updateOrAddPartyWithAddress("70 Albany Avenue Woking Utah", caseBuilderList.get(7).getPartyBuilders());
        updateOrAddPartyWithAddress("19 Hendrickson Street Hertford New York", caseBuilderList.get(8).getPartyBuilders());
        updateOrAddPartyWithAddress("48 Mitchley Avenue Riddlesdown Purley", caseBuilderList.get(9).getPartyBuilders());
        updateOrAddPartyWithAddress("44 Mitchley Avenue Riddlesdown Purley", caseBuilderList.get(10).getPartyBuilders());

        // Clear addresses in any further Cases
        if (caseBuilderList.size() > TEST_DOCUMENT_COUNT) {
            for (int i = TEST_DOCUMENT_COUNT; i < caseBuilderList.size(); i++) {
                caseBuilderList.get(i).getPartyBuilders().forEach(pb -> pb.withAddressLines(null));
            }
        }

    }

    private void updateOrAddPartyWithAddress(final String address, final List<PartyDocument.Builder> targetParties) {

        if (targetParties.size() == 0) {
            targetParties.add(PartyDocumentMother.defaultPartyAsBuilder());
        }

        final PartyDocument.Builder targetPartyBuilder = targetParties.get(0);

        targetPartyBuilder.withAddressLines(address);


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

        if (hasCases(caseList)) {

            if (!allMatch(caseList)) {
                failureMessage = format("Unexpected Party.addressLines in provided case list, was expecting all CaseDocuments to have Party.addressLines matching [%s]", PARTY_ADDRESS_TO_QUERY);
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
                .allMatch(p -> p.getAddressLines().contains(PARTY_ADDRESS_TO_QUERY));
    }



}
