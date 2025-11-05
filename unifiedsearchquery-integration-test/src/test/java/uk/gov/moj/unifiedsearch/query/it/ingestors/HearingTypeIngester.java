package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.HearingDocumentMother.defaultHearingAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDocument;

import java.util.List;

public class HearingTypeIngester {

    public static final String HEARING_TYPE_ID_1 = randomUUID().toString();
    public static final String HEARING_TYPE_ID_2 = randomUUID().toString();
    public static final String HEARING_TYPE_ID_3 = randomUUID().toString();


    private static ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();


    public static void loadCaseDocuments(final List<CaseDocument> caseList)  throws Exception {
        elasticSearchIndexIngestorUtil.ingestCaseData(caseList);
    }


    public static List<CaseDocument> caseListForHearingTypeQuery() {

        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(9);

        caseBuilderList.get(0).withHearings(hearing(HEARING_TYPE_ID_1));
        caseBuilderList.get(1).withHearings(hearing(HEARING_TYPE_ID_2));
        caseBuilderList.get(2).withHearings(hearing(HEARING_TYPE_ID_3));

        caseBuilderList.get(3).withHearings(hearing(HEARING_TYPE_ID_1));
        caseBuilderList.get(4).withHearings(hearing(HEARING_TYPE_ID_2));
        caseBuilderList.get(5).withHearings(hearing(HEARING_TYPE_ID_3));

        caseBuilderList.get(6).withHearings(hearing(HEARING_TYPE_ID_1));
        caseBuilderList.get(7).withHearings(hearing(HEARING_TYPE_ID_2));
        caseBuilderList.get(8).withHearings(hearing(HEARING_TYPE_ID_3));

        return caseBuilderList
                .stream()
                .map(CaseDocument.Builder::build)
                .collect(
                        toList());

    }


    private static List<HearingDocument.Builder> hearing(final String hearingType) {
        return singletonList(
                defaultHearingAsBuilder()
                        .withHearingTypeId(hearingType));
    }



}
