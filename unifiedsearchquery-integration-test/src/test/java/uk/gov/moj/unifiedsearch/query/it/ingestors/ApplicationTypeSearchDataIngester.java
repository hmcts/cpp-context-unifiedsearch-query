package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.ApplicationDocumentMother.defaultApplicationAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.ApplicationDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;

import java.util.List;

public class ApplicationTypeSearchDataIngester {


    private static ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();


    public static void loadCaseDocuments(final List<CaseDocument> caseList)  throws Exception {
        elasticSearchIndexIngestorUtil.ingestCaseData(caseList);
    }


    public static final String APPLICATION_FOR_A_DOMESTIC_VIOLENCE_PROTECTION_ORDER = "Application for a domestic violence protection order";
    public static final String APPEAL_AGAINST_CONVICTION = "Appeal against conviction";
    public static final String APPEAL_AGAINST_SENTENCE = "Appeal against Sentence";
    public static final String APPEAL_AGAINST_DECISION_TO_MAKE_A_HOSPITAL_ORDER_OR_GUARDIANSHIP_ORDER_WITHOUT_CONVICTING_THE_DEFENDANT =
                                         "Appeal against decision to make a hospital order or guardianship order without convicting the defendant";
    public static final String APPEAL_AGAINST_DECISION_TO_REVOKE_A_COMMUNITY_ORDER_AND_DEAL_WITH_THE_DEFENDANT_ANOTHER_WAY =
                                         "Appeal against decision to revoke a community order and deal with the defendant another way";
    public static final String APPEAL_AGAINST_DECISION_THAT_AN_OFFENCE_HAS_A_TERRORIST_CONNECTION = "Appeal against decision that an offence has a terrorist connection";
    public static final String APPEAL_AGAINST_GRANT_OF_BAIL = "Appeal against grant of bail";
    public static final String APPEAL_AGAINST_REFUSAL_TO_EXTEND_A_CUSTODY_TIME_LIMIT = "Appeal against refusal to extend a custody time limit";
    public static final String APPLICATION_TO_PREFER_INDICTMENT_OUT_OF_TIME = "Application to prefer indictment out of time";
    public static final String APPLICATION_TO_AMEND_INDICTMENT_BY_ADDING_OR_REMOVING_A_COURT_OR_COUNTS =
                                         "Application to amend indictment by adding or removing a court or counts";




    public static List<CaseDocument> caseListForApplicationTypeQuery() {

        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(30);

        caseBuilderList.get(0).withApplications(applicationDocumentBuilder(APPLICATION_FOR_A_DOMESTIC_VIOLENCE_PROTECTION_ORDER));
        caseBuilderList.get(1).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_CONVICTION));
        caseBuilderList.get(2).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_SENTENCE));
        caseBuilderList.get(3).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_DECISION_TO_MAKE_A_HOSPITAL_ORDER_OR_GUARDIANSHIP_ORDER_WITHOUT_CONVICTING_THE_DEFENDANT));
        caseBuilderList.get(4).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_DECISION_TO_REVOKE_A_COMMUNITY_ORDER_AND_DEAL_WITH_THE_DEFENDANT_ANOTHER_WAY));
        caseBuilderList.get(5).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_DECISION_THAT_AN_OFFENCE_HAS_A_TERRORIST_CONNECTION));
        caseBuilderList.get(6).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_GRANT_OF_BAIL));
        caseBuilderList.get(7).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_REFUSAL_TO_EXTEND_A_CUSTODY_TIME_LIMIT));
        caseBuilderList.get(8).withApplications(applicationDocumentBuilder(APPLICATION_TO_PREFER_INDICTMENT_OUT_OF_TIME));
        caseBuilderList.get(9).withApplications(applicationDocumentBuilder(APPLICATION_TO_AMEND_INDICTMENT_BY_ADDING_OR_REMOVING_A_COURT_OR_COUNTS));

        caseBuilderList.get(10).withApplications(applicationDocumentBuilder(APPLICATION_FOR_A_DOMESTIC_VIOLENCE_PROTECTION_ORDER));
        caseBuilderList.get(11).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_CONVICTION));
        caseBuilderList.get(12).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_SENTENCE));
        caseBuilderList.get(13).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_DECISION_TO_MAKE_A_HOSPITAL_ORDER_OR_GUARDIANSHIP_ORDER_WITHOUT_CONVICTING_THE_DEFENDANT));
        caseBuilderList.get(14).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_DECISION_TO_REVOKE_A_COMMUNITY_ORDER_AND_DEAL_WITH_THE_DEFENDANT_ANOTHER_WAY));
        caseBuilderList.get(15).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_DECISION_THAT_AN_OFFENCE_HAS_A_TERRORIST_CONNECTION));
        caseBuilderList.get(16).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_GRANT_OF_BAIL));
        caseBuilderList.get(17).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_REFUSAL_TO_EXTEND_A_CUSTODY_TIME_LIMIT));
        caseBuilderList.get(18).withApplications(applicationDocumentBuilder(APPLICATION_TO_PREFER_INDICTMENT_OUT_OF_TIME));
        caseBuilderList.get(19).withApplications(applicationDocumentBuilder(APPLICATION_TO_AMEND_INDICTMENT_BY_ADDING_OR_REMOVING_A_COURT_OR_COUNTS));

        caseBuilderList.get(20).withApplications(applicationDocumentBuilder(APPLICATION_FOR_A_DOMESTIC_VIOLENCE_PROTECTION_ORDER));
        caseBuilderList.get(21).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_CONVICTION));
        caseBuilderList.get(22).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_SENTENCE));
        caseBuilderList.get(23).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_DECISION_TO_MAKE_A_HOSPITAL_ORDER_OR_GUARDIANSHIP_ORDER_WITHOUT_CONVICTING_THE_DEFENDANT));
        caseBuilderList.get(24).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_DECISION_TO_REVOKE_A_COMMUNITY_ORDER_AND_DEAL_WITH_THE_DEFENDANT_ANOTHER_WAY));
        caseBuilderList.get(25).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_DECISION_THAT_AN_OFFENCE_HAS_A_TERRORIST_CONNECTION));
        caseBuilderList.get(26).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_GRANT_OF_BAIL));
        caseBuilderList.get(27).withApplications(applicationDocumentBuilder(APPEAL_AGAINST_REFUSAL_TO_EXTEND_A_CUSTODY_TIME_LIMIT));
        caseBuilderList.get(28).withApplications(applicationDocumentBuilder(APPLICATION_TO_PREFER_INDICTMENT_OUT_OF_TIME));
        caseBuilderList.get(29).withApplications(applicationDocumentBuilder(APPLICATION_TO_AMEND_INDICTMENT_BY_ADDING_OR_REMOVING_A_COURT_OR_COUNTS));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());

    }


    private static List<ApplicationDocument.Builder> applicationDocumentBuilder(final String applicationType) {
        return singletonList(
                defaultApplicationAsBuilder()
                        .withApplicationType(applicationType)
        );
    }


}
