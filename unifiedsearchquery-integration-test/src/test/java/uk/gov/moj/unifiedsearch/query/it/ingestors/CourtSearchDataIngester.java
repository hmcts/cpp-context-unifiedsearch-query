package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.HearingDayDocumentMother.defaultHearingDayDocumentAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.HearingDocumentMother.defaultHearingAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDocument;

import java.io.IOException;
import java.util.List;

public class CourtSearchDataIngester {
    private List<CaseDocument> indexedCaseList;

    public final String courtId1 = randomUUID().toString();
    public final String courtId2 = randomUUID().toString();
    public final String courtId3 = randomUUID().toString();

    public void loadCourtData() throws IOException {
        indexedCaseList = createCaseCourtDocument();
        new ElasticSearchIndexIngestorUtil().ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> createCaseCourtDocument() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(6);

        final HearingDocument.Builder hearingDocumentBuilder1 = defaultHearingAsBuilder()
                .withHearingDays(singletonList(defaultHearingDayDocumentAsBuilder().withCourtCentreId(courtId1).build()));

        final HearingDocument.Builder hearingDocumentBuilder2 = defaultHearingAsBuilder()
                .withHearingDays(singletonList(defaultHearingDayDocumentAsBuilder().withCourtCentreId(courtId2).build()));

        final HearingDocument.Builder hearingDocumentBuilder3 = defaultHearingAsBuilder()
                .withHearingDays(singletonList(defaultHearingDayDocumentAsBuilder().withCourtCentreId(courtId1).build()));

        final HearingDocument.Builder hearingDocumentBuilder4 = defaultHearingAsBuilder()
                .withHearingDays(singletonList(defaultHearingDayDocumentAsBuilder().withCourtCentreId(courtId2).build()));

        final HearingDocument.Builder hearingDocumentBuilder5 = defaultHearingAsBuilder()
                .withHearingDays(singletonList(defaultHearingDayDocumentAsBuilder().withCourtCentreId(courtId3).build()));

        final HearingDocument.Builder hearingDocumentBuilder6 = defaultHearingAsBuilder()
                .withHearingDays(asList(defaultHearingDayDocumentAsBuilder().withCourtCentreId(courtId2).build(), defaultHearingDayDocumentAsBuilder().withCourtCentreId(randomUUID().toString()).build()));

        caseBuilderList.get(0).withHearings(asList(hearingDocumentBuilder1, hearingDocumentBuilder4));
        caseBuilderList.get(1).withHearings(singletonList(hearingDocumentBuilder2));
        caseBuilderList.get(2).withHearings(asList(hearingDocumentBuilder3, hearingDocumentBuilder4));
        caseBuilderList.get(3).withHearings(singletonList(hearingDocumentBuilder1));
        caseBuilderList.get(4).withHearings(singletonList(hearingDocumentBuilder5));
        caseBuilderList.get(5).withHearings(asList(hearingDocumentBuilder2, hearingDocumentBuilder6));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    public CaseDocument getIndexDocumentAt(int index) {
        return indexedCaseList.get(index);
    }
}
