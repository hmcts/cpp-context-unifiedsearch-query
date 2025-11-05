package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.LinkedCaseDocumentMother.defaultLinkedCaseAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.LinkedCaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother;

import java.util.List;

public class ExcludeAutomaticallyLinkedCasesDataIngester {

    private static ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();

    private List<CaseDocument> indexedCaseList;
    public static String linkedCaseId1 = "10e49f41-3b70-433a-addf-758f5f27d65d";
    private static String linkedCaseId2 = "9bf67b61-3d40-4154-b79d-3704f2db69d4";
    private static String linkedCaseId3 = "218a2065-a93d-4401-9565-0fa24202c6d7";
    private static String linkedCaseId4 = "42a6d703-f6fe-41f0-a4c6-b35441944f62";

    public void loadCaseDocuments() throws Exception {
        indexedCaseList = caseListForExcludingAutomaticallyLinkedQuery();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> caseListForExcludingAutomaticallyLinkedQuery() {

        final LinkedCaseDocument linkedCaseDocument1 = defaultLinkedCaseAsBuilder().withLinkedCaseId(linkedCaseId1).withManuallyLinked(true).build();
        final LinkedCaseDocument linkedCaseDocument2 = defaultLinkedCaseAsBuilder().withLinkedCaseId(linkedCaseId1).withManuallyLinked(false).build();
        final LinkedCaseDocument linkedCaseDocument3 = defaultLinkedCaseAsBuilder().withLinkedCaseId(linkedCaseId2).withManuallyLinked(true).build();
        final LinkedCaseDocument linkedCaseDocument4 = defaultLinkedCaseAsBuilder().withLinkedCaseId(linkedCaseId2).withManuallyLinked(false).build();
        final LinkedCaseDocument linkedCaseDocument5 = defaultLinkedCaseAsBuilder().withLinkedCaseId(linkedCaseId3).withManuallyLinked(true).build();
        final LinkedCaseDocument linkedCaseDocument6 = defaultLinkedCaseAsBuilder().withLinkedCaseId(linkedCaseId3).withManuallyLinked(false).build();
        final LinkedCaseDocument linkedCaseDocument7 = defaultLinkedCaseAsBuilder().withLinkedCaseId(linkedCaseId4).withManuallyLinked(true).build();
        final LinkedCaseDocument linkedCaseDocument8 = defaultLinkedCaseAsBuilder().withLinkedCaseId(linkedCaseId4).withManuallyLinked(false).build();

        final List<CaseDocument.Builder> caseBuilderList = CaseDocumentMother.defaultCasesAsBuilderList(4);
        caseBuilderList.get(0).withCaseId("e9db3b9f-9ecb-4904-afd3-c91e61b50be4").withLinkedCases(asList(linkedCaseDocument1,linkedCaseDocument3));
        caseBuilderList.get(1).withCaseId("bcb24ef8-efcd-447b-8075-11165211356f").withLinkedCases(asList(linkedCaseDocument2,linkedCaseDocument4));
        caseBuilderList.get(2).withCaseId("6fa0101f-c6d7-496c-8d20-3c69b4a06764").withLinkedCases(asList(linkedCaseDocument5,linkedCaseDocument8));
        caseBuilderList.get(3).withCaseId("5fec618e-8890-436f-b4d4-efe2928d7591");

        return caseBuilderList
                .stream()
                .map(CaseDocument.Builder::build)
                .collect(
                        toList());
    }

    public List<CaseDocument> getCasesWithLinkedIdWithManuallyLinkedFalse() {
        return asList( indexedCaseList.get(0), indexedCaseList.get(2), indexedCaseList.get(3));
    }

    public List<CaseDocument> getCases() {
        return asList( indexedCaseList.get(0), indexedCaseList.get(2), indexedCaseList.get(3), indexedCaseList.get(1));
    }
}
