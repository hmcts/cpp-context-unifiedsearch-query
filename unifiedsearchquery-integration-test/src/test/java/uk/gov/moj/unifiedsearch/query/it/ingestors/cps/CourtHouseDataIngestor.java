package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.HearingDocumentMother.defaultHearingAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.HearingDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother;

import java.util.List;

public class CourtHouseDataIngestor {

    private static ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();
    public static final String OXFORD = "C43OX00";
    public static final String GUILDFORD = "C45GU00";
    public static final String LEICESTER = "C33LC00";
    public static final String CROYDON = "C01CY00";
    private List<CaseDocument> indexedCaseList;

    public void loadCaseDocuments() throws Exception {
        indexedCaseList = caseListForCourtHouseQuery();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    public CaseDocument getIndexDocumentAt(int index) {
        return indexedCaseList.get(index);
    }

    public static List<CaseDocument> caseListForCourtHouseQuery() {

        final List<CaseDocument.Builder> caseBuilderList = CaseDocumentMother.defaultCasesAsBuilderList(9);

        caseBuilderList.get(0).withHearings(hearing(OXFORD));
        caseBuilderList.get(1).withHearings(hearing(LEICESTER));
        caseBuilderList.get(2).withHearings(hearing(GUILDFORD));

        caseBuilderList.get(3).withHearings(hearing(OXFORD));
        caseBuilderList.get(4).withHearings(hearing(CROYDON));
        caseBuilderList.get(5).withHearings(hearing(GUILDFORD));

        caseBuilderList.get(6).withHearings(hearing(OXFORD));
        caseBuilderList.get(7).withHearings(hearing(CROYDON));
        caseBuilderList.get(8).withHearings(hearing(GUILDFORD));

        return caseBuilderList
                .stream()
                .map(CaseDocument.Builder::build)
                .collect(
                        toList());

    }


    private static List<HearingDocument> hearing(final String courtHouse) {
        return singletonList(
                defaultHearingAsBuilder()
                        .withCourtHouse(courtHouse).build());
    }

    public List<CaseDocument> getCaseDatas() {
        return asList(indexedCaseList.get(2), indexedCaseList.get(5), indexedCaseList.get(8));
    }
}
