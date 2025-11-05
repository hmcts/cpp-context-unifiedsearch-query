package uk.gov.moj.unifiedsearch.query.it.multi;

import static java.lang.String.format;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.HearingDayDocumentMother.hearingDays;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.hasCases;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;

import java.util.List;
import java.util.Map;

public class CourtFacetTestParameters implements FacetTestParameters {

    private static final String COURT_ID_PARAM_NAME = "courtId";
    private static final String COURT_ID_TO_QUERY = randomUUID().toString();

    private static final int TEST_DOCUMENT_COUNT = 9;

    private String failureMessage;


    @Override
    public void addSearchParameters(final Map<String, String> parameters) {

        parameters.put(COURT_ID_PARAM_NAME, COURT_ID_TO_QUERY);

    }

    @Override
    public void addMatchData(final List<CaseDocument.Builder> caseBuilderList) {

        caseBuilderList.get(2).getHearingBuilders().get(0).withCourtId(COURT_ID_TO_QUERY)
                .withHearingDays(hearingDays(fromString(COURT_ID_TO_QUERY)));

        caseBuilderList.get(6).getHearingBuilders().get(0).withCourtId(COURT_ID_TO_QUERY)
                .withHearingDays(hearingDays(fromString(COURT_ID_TO_QUERY)));

        caseBuilderList.get(8).getHearingBuilders().get(0).withCourtId(COURT_ID_TO_QUERY)
                .withHearingDays(hearingDays(fromString(COURT_ID_TO_QUERY)));

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
                failureMessage = format("Unexpected CourtId in provided case list, was expecting all CaseDocuments to have CourtId of [%s]", COURT_ID_TO_QUERY);
                return false;
            }
        } else {
            failureMessage = "Empty case list provided";
            return false;
        }

        return true;

    }

    public String getCourtIdToQuery() {
        return COURT_ID_TO_QUERY;
    }

    private boolean allMatch(final List<Case> caseList) {
        return caseList.stream()
                .flatMap(c -> c.getHearings().stream())
                .allMatch(h -> h.getCourtId().equals(COURT_ID_TO_QUERY));
    }


    @Override
    public String getFailureMessage() {
        return failureMessage;
    }
}
