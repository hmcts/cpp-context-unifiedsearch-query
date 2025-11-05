package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.HearingDayDocumentMother.hearingDays;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.HearingDocumentMother.defaultHearingAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDocument;

import java.util.List;

public class HearingTypeAndHearingDateIngester {

    public static final String SEARCHED_HEARING_TYPE_UUID = "111505b6-4a9e-4820-9ddc-f9a627914ef0";
    public static String SEARCHED_HEARING_DATE = "1958-07-13";

    private final ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();

    private List<CaseDocument> indexedCaseList;

    public void loadCases() throws Exception {
        indexedCaseList = caseListForPartyDateOfBirthAndNameSearch();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> caseListForPartyDateOfBirthAndNameSearch() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(3);

        caseBuilderList.get(0).withHearings(asList(createHearing(SEARCHED_HEARING_TYPE_UUID),
                createHearing("222505b6-4a9e-4820-9ddc-f9a627914ef0", SEARCHED_HEARING_DATE),
                createHearing("333505b6-4a9e-4820-9ddc-f9a627914ef0", "2018-01-01")));

        caseBuilderList.get(1).withHearings(singletonList(createHearing(SEARCHED_HEARING_TYPE_UUID, SEARCHED_HEARING_DATE)));

        caseBuilderList.get(2).withHearings(singletonList(createHearing("222505b6-4a9e-4820-9ddc-f9a627914ef0", "2019-01-01")));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    public List<CaseDocument> getExpectedCases() {
        return asList(indexedCaseList.get(1));
    }


    private static HearingDocument.Builder createHearing(final String hearingTypeUuid, final String... hearingDates) {
        final HearingDocument.Builder hearingBuilder = defaultHearingAsBuilder();
        hearingBuilder.withHearingTypeId(hearingTypeUuid);
        hearingBuilder.withHearingDays(hearingDays(hearingDates));
        return hearingBuilder;
    }
}
