package uk.gov.moj.unifiedsearch.query.it.ingestors;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;

import java.io.IOException;
import java.util.List;

public class CaseDocumentIngester {

    private static ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();


    public static void loadCaseDocuments(final List<CaseDocument> caseList) throws IOException {
        elasticSearchIndexIngestorUtil.ingestCaseData(caseList);
    }

}
