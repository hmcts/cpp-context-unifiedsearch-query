package uk.gov.moj.unifiedsearch.query.it.multi;

import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.ApplicationStatus.DRAFT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.ApplicationStatus.EJECTED;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.ApplicationStatus.IN_PROGRESS;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.hasCases;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;

import java.util.List;
import java.util.Map;

public class IsBoxHearingFacetTestParameters implements FacetTestParameters {

    private static final String BOX_HEARING_PARAM_NAME = "boxWorkHearing";

    private static final int TEST_DOCUMENT_COUNT = 9;

    private String failureMessage;


    @Override
    public void addSearchParameters(final Map<String, String> parameters) {

        parameters.put(BOX_HEARING_PARAM_NAME, "true");

    }

    @Override
    public void addMatchData(final List<CaseDocument.Builder> caseBuilderList) {

        caseBuilderList.get(0).getHearingBuilders().get(0).withBoxHearing(false);
        caseBuilderList.get(1).getHearingBuilders().get(0).withBoxHearing(true);
        caseBuilderList.get(1).getApplicationBuilders().get(0).withApplicationStatus(DRAFT.toString());
        caseBuilderList.get(2).getHearingBuilders().get(0).withBoxHearing(false);

        caseBuilderList.get(3).getHearingBuilders().get(0).withBoxHearing(false);
        caseBuilderList.get(4).getHearingBuilders().get(0).withBoxHearing(true);
        caseBuilderList.get(4).getApplicationBuilders().get(0).withApplicationStatus(IN_PROGRESS.toString());
        caseBuilderList.get(5).getHearingBuilders().get(0).withBoxHearing(false);

        caseBuilderList.get(6).getHearingBuilders().get(0).withBoxHearing(false);
        caseBuilderList.get(7).getHearingBuilders().get(0).withBoxHearing(true);
        caseBuilderList.get(7).getApplicationBuilders().get(0).withApplicationStatus(EJECTED.toString());
        caseBuilderList.get(8).getHearingBuilders().get(0).withBoxHearing(false);

        if (caseBuilderList.size() > TEST_DOCUMENT_COUNT) {
            for (int i = TEST_DOCUMENT_COUNT; i < caseBuilderList.size(); i++) {
                caseBuilderList.get(i).getHearingBuilders().forEach( hb -> hb.withBoxHearing(false));
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
                failureMessage = "Unexpected IsBoxHearing in provided case list, was expecting all CaseDocuments to have IsBoxHearing of 'true'";
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
                .allMatch(h -> h.isIsBoxHearing() == true);
    }



    @Override
    public String getFailureMessage() {
        return failureMessage;
    }
}
