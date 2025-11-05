package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.HearingDocumentMother.defaultHearingAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDocument;

import java.util.Arrays;
import java.util.List;

public class IsVirtualBoxHearingTypeIngester {
    private static ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();
    private List<CaseDocument> indexedCaseList;

    public void loadCaseDocuments()  throws Exception {
        indexedCaseList = caseListForIsVirtualBoxHearingQuery();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> caseListForIsVirtualBoxHearingQuery() {

        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(9);

        caseBuilderList.get(0).withHearings(hearing(false));
        caseBuilderList.get(1).withHearings(hearing(true));
        caseBuilderList.get(2).withHearings(hearing(false));

        caseBuilderList.get(3).withHearings(hearing(false));
        caseBuilderList.get(4).withHearings(hearing(true));
        caseBuilderList.get(5).withHearings(hearing(false));

        caseBuilderList.get(6).withHearings(hearing(false));
        caseBuilderList.get(7).withHearings(hearing(true));
        caseBuilderList.get(8).withHearings(hearing(false));

        return caseBuilderList
                .stream()
                .map(CaseDocument.Builder::build)
                .collect(
                        toList());

    }

    public List<CaseDocument> getExpectedVirtualBoxHearingCases() {
        return Arrays.asList(indexedCaseList.get(1), indexedCaseList.get(4), indexedCaseList.get(7));
    }

    public List<CaseDocument> getExpectedNonVirtualBoxHearingCases() {
        return Arrays.asList(indexedCaseList.get(0), indexedCaseList.get(2), indexedCaseList.get(3),
                indexedCaseList.get(5), indexedCaseList.get(6), indexedCaseList.get(8));
    }


    private static List<HearingDocument.Builder> hearing(final Boolean isVirtualBoxHearing) {
        return singletonList(
                defaultHearingAsBuilder()
                        .withVirtualBoxHearing(isVirtualBoxHearing));
    }



}
