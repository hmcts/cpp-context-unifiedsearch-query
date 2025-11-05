package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.HearingDocumentMother.defaultHearingAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDocument;

import java.util.List;

public class HearingTypeAndIsBoxHearingIngester {

    public static final String SEARCHED_HEARING_TYPE_UUID = "111505b6-4a9e-4820-9ddc-f9a627914ef0";

    private final ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();

    private List<CaseDocument> indexedCaseList;

    public void loadCases() throws Exception {
        indexedCaseList = caseListForPartyDateOfBirthAndNameSearch();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> caseListForPartyDateOfBirthAndNameSearch() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(3);

        caseBuilderList.get(0).withHearings(asList(createHearing(SEARCHED_HEARING_TYPE_UUID, false),
                createHearing("222505b6-4a9e-4820-9ddc-f9a627914ef0", true),
                createHearing("333505b6-4a9e-4820-9ddc-f9a627914ef0", false)));

        caseBuilderList.get(1).withHearings(singletonList(createHearing(SEARCHED_HEARING_TYPE_UUID, true)));

        caseBuilderList.get(2).withHearings(singletonList(createHearing("222505b6-4a9e-4820-9ddc-f9a627914ef0", true)));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    public List<CaseDocument> getExpectedCases() {
        return asList(indexedCaseList.get(1));
    }

    public List<CaseDocument> getNonBoxHearingSearchedCases() {
        return asList(indexedCaseList.get(0));
    }


    private static HearingDocument.Builder createHearing(final String hearingTypeUuid, final boolean isBoxHearing) {
        final HearingDocument.Builder hearingBuilder = defaultHearingAsBuilder();
        hearingBuilder.withHearingTypeId(hearingTypeUuid);
        hearingBuilder.withBoxHearing(isBoxHearing);
        return hearingBuilder;
    }
}
