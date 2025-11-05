package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother;

import java.util.List;

public class OperationNameDataIngester {

    private ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();
    private List<CaseDocument> indexedCaseList;

    public void loadCaseDocuments() throws Exception {
        indexedCaseList = createCaseDocuments();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    public List<CaseDocument> createCaseDocuments() {

        final List<CaseDocument.Builder> caseBuilderList = CaseDocumentMother.defaultCasesAsBuilderList(7);
        caseBuilderList.get(0).withOperationName("Whistle");
        caseBuilderList.get(1).withOperationName("Chase");
        caseBuilderList.get(2).withOperationName("Chase");

        caseBuilderList.get(3).withOperationName("Care");
        caseBuilderList.get(4).withOperationName("Country");
        caseBuilderList.get(5).withOperationName("Shield Service");
        caseBuilderList.get(6).withOperationName("Chase Connect");
        return caseBuilderList
                .stream()
                .map(CaseDocument.Builder::build)
                .collect(
                        toList());

    }

    public List<CaseDocument> getIndexDocumentAt(int index) {
        return asList(indexedCaseList.get(index));
    }

    public List<CaseDocument> getMultipleCases() {
        return asList(indexedCaseList.get(1), indexedCaseList.get(2), indexedCaseList.get(6));
    }

}
