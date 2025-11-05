package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.HearingDocumentMother.defaultHearingAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDocument;

import java.util.List;

public class IsBoxHearingTypeIngester {



    private static ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();


    public static void loadCaseDocuments(final List<CaseDocument> caseList)  throws Exception {
        elasticSearchIndexIngestorUtil.ingestCaseData(caseList);
    }


    public static List<CaseDocument> caseListForIsBoxHearingQuery() {

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


    private static List<HearingDocument.Builder> hearing(final Boolean isBoxHearing) {
        return singletonList(
                defaultHearingAsBuilder()
                        .withBoxHearing(isBoxHearing));
    }



}
