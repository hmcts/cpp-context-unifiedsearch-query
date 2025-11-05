package uk.gov.moj.unifiedsearch.query.it.multi;

import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.hasCases;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;

import java.util.List;
import java.util.Map;

public class IsVirtualBoxHearingFacetTestParameters implements FacetTestParameters {

    private static final String VIRTUAL_BOX_HEARING_PARAM_NAME = "boxWorkVirtualHearing";

    private static final int TEST_DOCUMENT_COUNT = 9;

    private String failureMessage;


    @Override
    public void addSearchParameters(final Map<String, String> parameters) {

        parameters.put(VIRTUAL_BOX_HEARING_PARAM_NAME, "true");

    }

    @Override
    public void addMatchData(final List<CaseDocument.Builder> caseBuilderList) {

        caseBuilderList.get(0).getHearingBuilders().get(0).withVirtualBoxHearing(false);
        caseBuilderList.get(1).getHearingBuilders().get(0).withVirtualBoxHearing(true);
        caseBuilderList.get(2).getHearingBuilders().get(0).withVirtualBoxHearing(false);

        caseBuilderList.get(3).getHearingBuilders().get(0).withVirtualBoxHearing(false);
        caseBuilderList.get(4).getHearingBuilders().get(0).withVirtualBoxHearing(true);
        caseBuilderList.get(5).getHearingBuilders().get(0).withVirtualBoxHearing(false);

        caseBuilderList.get(6).getHearingBuilders().get(0).withVirtualBoxHearing(false);
        caseBuilderList.get(7).getHearingBuilders().get(0).withVirtualBoxHearing(true);
        caseBuilderList.get(8).getHearingBuilders().get(0).withVirtualBoxHearing(false);

        if (caseBuilderList.size() > TEST_DOCUMENT_COUNT) {
            for (int i = TEST_DOCUMENT_COUNT; i < caseBuilderList.size(); i++) {
                caseBuilderList.get(i).getHearingBuilders().forEach( hb -> hb.withVirtualBoxHearing(false));
            }
        }

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
        // to test that however many cases are provided here, the hearings they contain
        // have expected HearingTypes
        if (hasCases(caseList)) {

            if (!allMatch(caseList)) {
                failureMessage = "Unexpected IsVirtualBoxHearing in provided case list, was expecting all CaseDocuments to have IsVirtualBoxHearing of 'true'";
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
                .flatMap(c -> c.getHearings().stream())
                .allMatch(h -> h.isIsVirtualBoxHearing() == true);
    }



    @Override
    public String getFailureMessage() {
        return failureMessage;
    }
}
