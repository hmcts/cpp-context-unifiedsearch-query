package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static com.google.common.collect.ImmutableList.of;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother;

import java.util.List;

public class CjsAreaCodeDataIngester {

    private static ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();

    public static final String LONDON = "1";
    public static final String HERTFORDSHIRE = "41";
    public static final String CHESHIRE = "7";
    public static final String WILTSHIRE = "54";
    public static final String SUFFOLK = "37";
    public static final String ESSEX = "42";

    private List<CaseDocument> indexedCaseList;

    public void loadCaseDocuments() throws Exception {
        indexedCaseList = caseListForCjsAreaQuery();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> caseListForCjsAreaQuery() {

        final List<CaseDocument.Builder> caseBuilderList = CaseDocumentMother.defaultCasesAsBuilderList(9);
        caseBuilderList.get(0).withCjsAreaCode(of(HERTFORDSHIRE));
        caseBuilderList.get(1).withCjsAreaCode(of(CHESHIRE));
        caseBuilderList.get(2).withCjsAreaCode(of(LONDON));

        caseBuilderList.get(3).withCjsAreaCode(of(HERTFORDSHIRE));
        caseBuilderList.get(4).withCjsAreaCode(of(ESSEX));
        caseBuilderList.get(5).withCjsAreaCode(of(WILTSHIRE));

        caseBuilderList.get(6).withCjsAreaCode(of(CHESHIRE));
        caseBuilderList.get(7).withCjsAreaCode(of(SUFFOLK));
        caseBuilderList.get(8).withCjsAreaCode(of(HERTFORDSHIRE));

        return caseBuilderList
                .stream()
                .map(CaseDocument.Builder::build)
                .collect(
                        toList());
    }

    public List<CaseDocument> getCasesForCjsAreaHertfordshire() {
        return asList(indexedCaseList.get(0), indexedCaseList.get(3), indexedCaseList.get(8));
    }
    public List<CaseDocument> getCasesForCjsAreaEssex() {
        return asList(indexedCaseList.get(4));
    }

    public List<CaseDocument> getCases() {
        return indexedCaseList;
    }
}
