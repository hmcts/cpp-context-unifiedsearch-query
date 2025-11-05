package uk.gov.moj.unifiedsearch.query.it.multi;

import static java.lang.String.format;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPEAL_AGAINST_CONVICTION;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPEAL_AGAINST_DECISION_THAT_AN_OFFENCE_HAS_A_TERRORIST_CONNECTION;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPEAL_AGAINST_DECISION_TO_MAKE_A_HOSPITAL_ORDER_OR_GUARDIANSHIP_ORDER_WITHOUT_CONVICTING_THE_DEFENDANT;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPEAL_AGAINST_GRANT_OF_BAIL;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPEAL_AGAINST_REFUSAL_TO_EXTEND_A_CUSTODY_TIME_LIMIT;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPLICATION_FOR_A_DOMESTIC_VIOLENCE_PROTECTION_ORDER;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.ApplicationTypeSearchDataIngester.APPLICATION_TO_PREFER_INDICTMENT_OUT_OF_TIME;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.hasCases;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;

import java.util.List;
import java.util.Map;

public class ApplicationTypeFacetTestParameters implements FacetTestParameters {

    private static final String APPLICATION_TYPE__PARAM_NAME = "applicationType";

    private static final int TEST_DOCUMENT_COUNT = 10;

    private String failureMessage;


    @Override
    public void addSearchParameters(final Map<String, String> parameters) {

        parameters.put(APPLICATION_TYPE__PARAM_NAME, APPEAL_AGAINST_CONVICTION);

    }

    @Override
    public void addMatchData(final List<CaseDocument.Builder> caseBuilderList) {

        caseBuilderList.get(0).getApplicationBuilders().get(0).withApplicationType(APPLICATION_FOR_A_DOMESTIC_VIOLENCE_PROTECTION_ORDER);
        caseBuilderList.get(1).getApplicationBuilders().get(0).withApplicationType(APPEAL_AGAINST_DECISION_TO_MAKE_A_HOSPITAL_ORDER_OR_GUARDIANSHIP_ORDER_WITHOUT_CONVICTING_THE_DEFENDANT);
        caseBuilderList.get(2).getApplicationBuilders().get(0).withApplicationType(APPEAL_AGAINST_CONVICTION);
        caseBuilderList.get(3).getApplicationBuilders().get(0).withApplicationType(APPEAL_AGAINST_DECISION_THAT_AN_OFFENCE_HAS_A_TERRORIST_CONNECTION);
        caseBuilderList.get(4).getApplicationBuilders().get(0).withApplicationType(APPEAL_AGAINST_CONVICTION);
        caseBuilderList.get(5).getApplicationBuilders().get(0).withApplicationType(APPEAL_AGAINST_REFUSAL_TO_EXTEND_A_CUSTODY_TIME_LIMIT);
        caseBuilderList.get(6).getApplicationBuilders().get(0).withApplicationType(APPEAL_AGAINST_CONVICTION);
        caseBuilderList.get(7).getApplicationBuilders().get(0).withApplicationType(APPEAL_AGAINST_CONVICTION);
        caseBuilderList.get(8).getApplicationBuilders().get(0).withApplicationType(APPEAL_AGAINST_GRANT_OF_BAIL);
        caseBuilderList.get(9).getApplicationBuilders().get(0).withApplicationType(APPLICATION_TO_PREFER_INDICTMENT_OUT_OF_TIME);

        if (caseBuilderList.size() > TEST_DOCUMENT_COUNT) {
            // Set all further App Types to something we're not testing for
            for (int i = TEST_DOCUMENT_COUNT; i < caseBuilderList.size(); i++ ) {
                caseBuilderList.get(i).getApplicationBuilders().forEach(ab ->ab.withApplicationType(APPEAL_AGAINST_GRANT_OF_BAIL));
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
        // to test that however many cases are provided here, the Applications they contain
        // have expected ApplicationTypes
        if (hasCases(caseList)) {

            if (!allMatch(caseList)) {
                failureMessage = format("Unexpected ApplicationType in provided case list, was expecting all CaseDocuments to have ApplicationType of [%s]", APPEAL_AGAINST_CONVICTION);
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
                .flatMap(c -> c.getApplications().stream())
                .allMatch(h -> h.getApplicationType().equals(APPEAL_AGAINST_CONVICTION));
    }



    @Override
    public String getFailureMessage() {
        return failureMessage;
    }
}
