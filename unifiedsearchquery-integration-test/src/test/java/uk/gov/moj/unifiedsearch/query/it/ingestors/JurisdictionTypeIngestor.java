package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;

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

    private List<CaseDocument> caseListForJurisdictionTypeQuery() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(8);

        createCaseBuilder(true, false, false, "2019-03-12", caseBuilderList.get(0));
        createCaseBuilder(false, true, false, "2019-04-23", caseBuilderList.get(1));
        createCaseBuilder(false, false, true, "2019-01-07", caseBuilderList.get(2));
        createCaseBuilder(true, true, false, "2019-06-20", caseBuilderList.get(3));
        createCaseBuilder(true, false, true, "2019-05-11", caseBuilderList.get(4));
        createCaseBuilder(false, true, true, "2019-06-21", caseBuilderList.get(5));
        createCaseBuilder(true, true, true, "2019-02-24", caseBuilderList.get(6));
        createCaseBuilder(false, false, false, "2019-07-05", caseBuilderList.get(7));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());

    }

    private void createCaseBuilder(final boolean isSjp, final boolean isMagistrates, final boolean isCrown, final String sjpNoticeServed, final CaseDocument.Builder builder) {
        builder
                .with_is_sjp(isSjp)
                .with_is_magistrates(isMagistrates)
                .with_is_crown(isCrown)
                .withSjpNoticeServed(sjpNoticeServed);
    }
}
