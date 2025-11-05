package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother;

import java.util.List;

public class CpsAreaCodeDataIngester {

    private static ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();

    public static final String LONDON = "1";
    public static final String HERTFORDSHIRE = "41";
    public static final String CHESHIRE = "7";
    public static final String WILTSHIRE = "54";
    public static final String SUFFOLK = "37";
    public static final String ESSEX = "42";

    private List<CaseDocument> indexedCaseList;

    public void loadCaseDocuments() throws Exception {
        indexedCaseList = caseListForCpsAreaQuery();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> caseListForCpsAreaQuery() {

        final List<CaseDocument.Builder> caseBuilderList = CaseDocumentMother.defaultCasesAsBuilderList(9);
        caseBuilderList.get(0).withCpsAreaCode(HERTFORDSHIRE);
        caseBuilderList.get(1).withCpsAreaCode(CHESHIRE);
        caseBuilderList.get(2).withCpsAreaCode(LONDON);

        caseBuilderList.get(3).withCpsAreaCode(HERTFORDSHIRE);
        caseBuilderList.get(4).withCpsAreaCode(ESSEX);
        caseBuilderList.get(5).withCpsAreaCode(WILTSHIRE);

        caseBuilderList.get(6).withCpsAreaCode(CHESHIRE);
        caseBuilderList.get(7).withCpsAreaCode(SUFFOLK);
        caseBuilderList.get(8).withCpsAreaCode(HERTFORDSHIRE);

        return caseBuilderList
                .stream()
                .map(CaseDocument.Builder::build)
                .collect(
                        toList());
    }

    public List<CaseDocument> getCasesForCpsAreaHertfordshire() {
        return asList(indexedCaseList.get(0), indexedCaseList.get(3), indexedCaseList.get(8));
    }
    public List<CaseDocument> getCasesForCpsAreaEssex() {
        return asList(indexedCaseList.get(4));
    }
}
