package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother;

import java.util.List;

public class WitnessCareUnitDataIngester {

    private ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();
    private List<CaseDocument> indexedCaseList;

    public void loadCaseDocuments() throws Exception {
        indexedCaseList = createCaseWitnessCareUnitDocuments();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    public List<CaseDocument> createCaseWitnessCareUnitDocuments() {

        final List<CaseDocument.Builder> caseBuilderList = CaseDocumentMother.defaultCasesAsBuilderList(9);
        caseBuilderList.get(0).withWitnessCareUnitCode("London");
        caseBuilderList.get(1).withWitnessCareUnitCode("Brimingham");
        caseBuilderList.get(2).withWitnessCareUnitCode("Derby");

        caseBuilderList.get(3).withWitnessCareUnitCode("Brighton");
        caseBuilderList.get(4).withWitnessCareUnitCode("London");
        caseBuilderList.get(5).withWitnessCareUnitCode("Oxford");

        caseBuilderList.get(6).withWitnessCareUnitCode("Manchester");
        caseBuilderList.get(7).withWitnessCareUnitCode("Norfolk");
        caseBuilderList.get(8).withWitnessCareUnitCode("Cambridge");

        return caseBuilderList
                .stream()
                .map(CaseDocument.Builder::build)
                .collect(
                        toList());

    }

    public CaseDocument getIndexDocumentAt(int index) {
        return indexedCaseList.get(index);
    }

    public CaseDocument getCaseForWitnessCareUnit002() {
        return indexedCaseList.get(2);
    }

    public List<CaseDocument> getCasesForWitnessCareUnitUnitLondon() {
        return asList(indexedCaseList.get(0), indexedCaseList.get(4));
    }
}
