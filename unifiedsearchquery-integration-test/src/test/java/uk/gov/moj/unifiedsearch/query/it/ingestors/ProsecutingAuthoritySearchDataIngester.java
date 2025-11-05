package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static com.google.common.collect.ImmutableList.of;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.ProsecutingAuthority.TFL;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.ProsecutingAuthority.TVL;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;

import java.util.List;

public class ProsecutingAuthoritySearchDataIngester {

    private final ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();

    private List<CaseDocument> indexedCaseList;

    public void loadCases() throws Exception {
        indexedCaseList = caseListForReferenceSearch();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> caseListForReferenceSearch() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(3);

        caseBuilderList.get(0).withProsecutingAuthority(TFL.name());
        caseBuilderList.get(1).withProsecutingAuthority(TVL.name());
        caseBuilderList.get(2).withProsecutingAuthority(TVL.name());


        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    public List<CaseDocument> getTflCases() {
        return of(indexedCaseList.get(0));
    }

    public List<CaseDocument> getTvlCases() {
        return of(indexedCaseList.get(1), indexedCaseList.get(2));
    }
}
