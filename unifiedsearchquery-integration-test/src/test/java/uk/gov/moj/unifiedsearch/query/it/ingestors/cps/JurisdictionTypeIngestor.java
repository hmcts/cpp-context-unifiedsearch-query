package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.HearingDocumentMother.defaultHearingAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.constant.JurisdictionType;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.HearingDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother;

import java.util.List;

public class JurisdictionTypeIngestor {

    private static ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();

    private List<CaseDocument> indexedCaseList;

    public  void loadCaseDocuments() throws Exception {
        indexedCaseList = caseListForJurisdictionTypeQuery();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    public CaseDocument getIndexDocumentAt(int index) {
        return indexedCaseList.get(index);
    }

    public static List<CaseDocument> caseListForJurisdictionTypeQuery() {

        final List<CaseDocument.Builder> caseBuilderList = CaseDocumentMother.defaultCasesAsBuilderList(9);

        caseBuilderList.get(0).withHearings(hearing(JurisdictionType.CROWN.name()));
        caseBuilderList.get(1).withHearings(hearing(JurisdictionType.MAGISTRATES.name()));
        caseBuilderList.get(2).withHearings(hearing(JurisdictionType.COURT_OF_APPEAL.name()));

        caseBuilderList.get(3).withHearings(hearing(JurisdictionType.CROWN.name()));
        caseBuilderList.get(4).withHearings(hearing(JurisdictionType.MAGISTRATES.name()));
        caseBuilderList.get(5).withHearings(hearing(JurisdictionType.COURT_OF_APPEAL.name()));

        caseBuilderList.get(6).withHearings(hearing(JurisdictionType.CROWN.name()));
        caseBuilderList.get(7).withHearings(hearing(JurisdictionType.MAGISTRATES.name()));
        caseBuilderList.get(8).withHearings(hearing(JurisdictionType.COURT_OF_APPEAL.name()));

        return caseBuilderList
                .stream()
                .map(CaseDocument.Builder::build)
                .collect(
                        toList());

    }


    private static List<HearingDocument> hearing(final String jurisdictionType) {
        return singletonList(
                defaultHearingAsBuilder()
                        .withJurisdiction(jurisdictionType).build());
    }
}
