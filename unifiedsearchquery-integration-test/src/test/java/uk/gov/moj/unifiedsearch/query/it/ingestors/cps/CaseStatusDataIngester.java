package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class CaseStatusDataIngester {

    private ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();
    private List<CaseDocument> indexedCaseList;

    public void loadCaseDocuments() throws IOException {
        indexedCaseList = createCaseDocuments();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    public List<CaseDocument> createCaseDocuments() {

        final List<CaseDocument.Builder> caseBuilderList = CaseDocumentMother.defaultCasesAsBuilderList(9);

        caseBuilderList.get(0).withCaseStatusCode("LIV");
        caseBuilderList.get(1).withCaseStatusCode("APP");
        caseBuilderList.get(2).withCaseStatusCode("FIN");

        caseBuilderList.get(3).withCaseStatusCode("MON").withParties(null);
        caseBuilderList.get(4).withCaseStatusCode("APP").withParties(null);
        caseBuilderList.get(5).withCaseStatusCode("LIV").withParties(null);

        caseBuilderList.get(6).withCaseStatusCode("MON");
        caseBuilderList.get(7).withCaseStatusCode("FIN");
        caseBuilderList.get(8).withCaseStatusCode("LIV");

        return caseBuilderList
                .stream()
                .map(CaseDocument.Builder::build)
                .collect(
                        toList());

    }

    public CaseDocument getIndexDocumentAt(int index) {
        return indexedCaseList.get(index);
    }

    public List<CaseDocument> getCasesWithCaseStatusLIV() {
        return asList(indexedCaseList.get(0), indexedCaseList.get(5), indexedCaseList.get(8));
    }

    public List<CaseDocument> getCasesWithCaseStatusAPP() {
        return asList(indexedCaseList.get(1), indexedCaseList.get(4));
    }

    public List<CaseDocument> getCasesWithCaseStatusMON() {
        return asList(indexedCaseList.get(3), indexedCaseList.get(6));
    }

    public List<CaseDocument> getCasesWithCaseStatusFIN() {
        return asList(indexedCaseList.get(2), indexedCaseList.get(7));
    }

    public List<CaseDocument> getAllCasesSortedAscByCaseStatus() {
        return indexedCaseList.stream()
                .sorted(Comparator.comparing(CaseDocument::getCaseStatusCode))
                .collect(toList());
    }

    public List<CaseDocument> getAllCasesSortedDescByCaseStatus() {
        return indexedCaseList.stream()
                .sorted(Comparator.comparing(CaseDocument::getCaseStatusCode).reversed())
                .collect(toList());
    }
}
