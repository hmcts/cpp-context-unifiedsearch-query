package uk.gov.moj.unifiedsearch.query.it.multi;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.hasCases;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother;

import java.util.List;
import java.util.Map;

public class PostcodeFacetTestParameters implements FacetTestParameters {

    private static final String PARTY_POSTCODE_PARAM_NAME = "partyPostcode";

    private static final String PARTIAL_POSTCODE = "TG14";

    private static final int TEST_DOCUMENT_COUNT = 10;

    private String failureMessage;


    private static final List<String> TEST_POSTCODE_LIST =
            asList("W13 5TG",
                   "TG14 4RT",
                   "TG14 T67",
                   "TG14 15T",
                   "YU45 5TG",
                   "DC4 14T",
                   "TG14 55T",
                   "W13 7JU",
                   "TG14 15T",
                   "EX1 3PB");


    @Override
    public void addSearchParameters(final Map<String, String> parameters) {
        parameters.put(PARTY_POSTCODE_PARAM_NAME, PARTIAL_POSTCODE);
    }



    @Override
    public void addMatchData(final List<CaseDocument.Builder> caseBuilderList) {


        updateOrAddPartyPostcode(TEST_POSTCODE_LIST.get(0), caseBuilderList.get(0).getPartyBuilders());
        updateOrAddPartyPostcode(TEST_POSTCODE_LIST.get(1), caseBuilderList.get(1).getPartyBuilders());
        updateOrAddPartyPostcode(TEST_POSTCODE_LIST.get(2), caseBuilderList.get(2).getPartyBuilders());
        updateOrAddPartyPostcode(TEST_POSTCODE_LIST.get(3), caseBuilderList.get(3).getPartyBuilders());
        updateOrAddPartyPostcode(TEST_POSTCODE_LIST.get(4), caseBuilderList.get(4).getPartyBuilders());
        updateOrAddPartyPostcode(TEST_POSTCODE_LIST.get(5), caseBuilderList.get(5).getPartyBuilders());
        updateOrAddPartyPostcode(TEST_POSTCODE_LIST.get(6), caseBuilderList.get(6).getPartyBuilders());
        updateOrAddPartyPostcode(TEST_POSTCODE_LIST.get(7), caseBuilderList.get(7).getPartyBuilders());
        updateOrAddPartyPostcode(TEST_POSTCODE_LIST.get(8), caseBuilderList.get(8).getPartyBuilders());
        updateOrAddPartyPostcode(TEST_POSTCODE_LIST.get(9), caseBuilderList.get(9).getPartyBuilders());

        // Set postcodes in any further Cases
        if (caseBuilderList.size() > TEST_DOCUMENT_COUNT) {
            for (int i = TEST_DOCUMENT_COUNT; i < caseBuilderList.size(); i++) {
                caseBuilderList.get(i).getPartyBuilders().forEach(pb -> pb.withPostCode("W13 7JU"));
            }
        }
    }

    private void updateOrAddPartyPostcode(final String postCode, final List<PartyDocument.Builder> targetParties) {

        if (targetParties.size() == 0) {
            targetParties.add(PartyDocumentMother.defaultPartyAsBuilder());
        }

        final PartyDocument.Builder targetPartyBuilder = targetParties.get(0);

        targetPartyBuilder.withPostCode(postCode);

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
        // have expected Postcode values

        if (hasCases(caseList)) {

            if (!allMatch(caseList)) {
                failureMessage = format("Unexpected Party.postcode in provided case list, was expecting all CaseDocuments to have Postcode starting with '%s'", PARTIAL_POSTCODE);
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
                .allMatch(p -> p.getPostCode() != null && p.getPostCode().startsWith(PARTIAL_POSTCODE));
    }


    @Override
    public String getFailureMessage() {
        return failureMessage;
    }


}
