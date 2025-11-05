package uk.gov.moj.unifiedsearch.query.it.multi;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.hasCases;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;

import java.util.List;
import java.util.Map;

public class HearingTypeFacetTestParameters implements FacetTestParameters {

    private static final String HEARING_TYPE_ID_PARAM_NAME = "hearingTypeId";
    private static final String HEARING_TYPE_ID_TO_QUERY = randomUUID().toString();

    private static final int TEST_DOCUMENT_COUNT = 9;

    private String failureMessage;


    @Override
    public void addSearchParameters(final Map<String, String> parameters) {

        parameters.put(HEARING_TYPE_ID_PARAM_NAME, HEARING_TYPE_ID_TO_QUERY);

    }

    @Override
    public void addMatchData(final List<CaseDocument.Builder> caseBuilderList) {

        caseBuilderList.get(2).getHearingBuilders().get(0).withHearingTypeId(HEARING_TYPE_ID_TO_QUERY);
        caseBuilderList.get(6).getHearingBuilders().get(0).withHearingTypeId(HEARING_TYPE_ID_TO_QUERY);
        caseBuilderList.get(8).getHearingBuilders().get(0).withHearingTypeId(HEARING_TYPE_ID_TO_QUERY);

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
        // to test that however many cases are provided here, the heatings they contain
        // have expected HearingTypes
        if (hasCases(caseList)) {

            if (!allMatch(caseList)) {
                failureMessage = format("Unexpected HearingType in provided case list, was expecting all CaseDocuments to have HearingType of [%s]", HEARING_TYPE_ID_TO_QUERY);
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
                .allMatch(h -> h.getHearingTypeId().equals(HEARING_TYPE_ID_TO_QUERY));
    }



    @Override
    public String getFailureMessage() {
        return failureMessage;
    }
}
