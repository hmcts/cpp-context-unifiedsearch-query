package uk.gov.moj.unifiedsearch.query.it.multi;

import static java.lang.String.format;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ReferenceSearchDataIngester.TFL_CASE_REFERENCE;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ReferenceSearchDataIngester.TVL_CASE_REFERENCE;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.hasCases;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;

import java.util.List;
import java.util.Map;

public class ReferenceFacetTestParameters implements FacetTestParameters {

    private static final String CASE_REFERENCE_PARAM_NAME = "caseReference";

    private static final int TEST_DOCUMENT_COUNT = 9;

    private String failureMessage;


    @Override
    public void addSearchParameters(final Map<String, String> parameters) {
        parameters.put(CASE_REFERENCE_PARAM_NAME, TVL_CASE_REFERENCE);
    }

    @Override
    public void addMatchData(final List<CaseDocument.Builder> caseBuilderList) {

        caseBuilderList.get(0).withCaseReference(TVL_CASE_REFERENCE);
        caseBuilderList.get(1).withCaseReference(TFL_CASE_REFERENCE);
        caseBuilderList.get(2).withCaseReference(TVL_CASE_REFERENCE);
        caseBuilderList.get(3).withCaseReference(TFL_CASE_REFERENCE);
        caseBuilderList.get(4).withCaseReference(TVL_CASE_REFERENCE);
        caseBuilderList.get(6).withCaseReference(TVL_CASE_REFERENCE);
        caseBuilderList.get(8).withCaseReference(TVL_CASE_REFERENCE);

        if (caseBuilderList.size() > TEST_DOCUMENT_COUNT) {
            for (int i = TEST_DOCUMENT_COUNT; i < caseBuilderList.size(); i++) {
                caseBuilderList.get(i).withCaseReference("DUMMY");
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
        // to test that however many cases are provided here, they all contain
        // expected CaseReference

        if (hasCases(caseList)) {

            if (!allMatch(caseList)) {
                failureMessage = format("Unexpected CaseReference in provided case list, was expecting all CaseDocuments to have Reference of [%s]", TVL_CASE_REFERENCE);
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
                .allMatch(c -> c.getCaseReference().equals(TVL_CASE_REFERENCE));
    }


    @Override
    public String getFailureMessage() {
        return failureMessage;
    }
}
