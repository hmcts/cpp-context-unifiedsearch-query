package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother;

import java.util.List;

public class UrnDataIngester {

    private ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();
    private List<CaseDocument> indexedCaseList;

    public void loadCaseDocuments() throws Exception {
        indexedCaseList = createCaseDocuments();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    public List<CaseDocument> createCaseDocuments() {

        final List<CaseDocument.Builder> caseBuilderList = CaseDocumentMother.defaultCasesAsBuilderList(14);
        caseBuilderList.get(0).withUrn("URN001");
        caseBuilderList.get(1).withUrn("URN002");
        caseBuilderList.get(2).withUrn("URN003");

        caseBuilderList.get(3).withUrn("URN004");
        caseBuilderList.get(4).withUrn("URN004");
        caseBuilderList.get(5).withUrn("URN006");

        caseBuilderList.get(6).withUrn("URN007");
        caseBuilderList.get(7).withUrn("URN008");
        caseBuilderList.get(8).withUrn("URN009");
        caseBuilderList.get(9).withUrn("URN009");
        caseBuilderList.get(10).withUrn("URN009");
        caseBuilderList.get(11).withUrn("URN009");
        caseBuilderList.get(12).withUrn("URN009");
        caseBuilderList.get(13).withUrn("URN009");

        return caseBuilderList
                .stream()
                .map(CaseDocument.Builder::build)
                .collect(
                        toList());

    }

    public CaseDocument getIndexDocumentAt(int index) {
        return indexedCaseList.get(index);
    }

    public CaseDocument getCaseFor002() {
        return indexedCaseList.get(2);
    }

    public List<CaseDocument> getMultipleCases() {
        return asList(indexedCaseList.get(3), indexedCaseList.get(4));
    }

}
