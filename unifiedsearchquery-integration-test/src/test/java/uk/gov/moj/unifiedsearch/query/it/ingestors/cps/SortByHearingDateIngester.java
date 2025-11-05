package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.HearingDocumentMother;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class SortByHearingDateIngester {

    private ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();
    private List<CaseDocument> indexedCaseList;

    public void loadCaseDocuments() throws Exception {
        indexedCaseList = createCaseDocuments();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    public List<CaseDocument> createCaseDocuments() {

        final List<CaseDocument.Builder> caseBuilderList = CaseDocumentMother.defaultCasesAsBuilderList(3);
        caseBuilderList.get(0)
                .withParties(emptyList())
                .withHearings(singletonList(HearingDocumentMother.defaultHearingAsBuilder()
                        .withHearingDateTime(ZonedDateTime.now().minusDays(2).format(ISO_INSTANT))
                        .build()));

        caseBuilderList.get(1)
                .withParties(emptyList())
                .withHearings(ImmutableList.of(HearingDocumentMother.defaultHearingAsBuilder()
                                .withHearingDateTime(ZonedDateTime.now().plusMinutes(10).format(ISO_INSTANT))
                                .build(),
                        HearingDocumentMother.defaultHearingAsBuilder()
                                .withHearingDateTime(ZonedDateTime.now().plusMonths(1).format(ISO_INSTANT))
                                .build()));

        caseBuilderList.get(2)
                .withParties(emptyList())
                .withHearings(Collections.singletonList(HearingDocumentMother.defaultHearingAsBuilder()
                        .withHearingDateTime(ZonedDateTime.now().plusDays(2).format(ISO_INSTANT))
                        .build()));


        return caseBuilderList
                .stream()
                .map(CaseDocument.Builder::build)
                .collect(
                        toList());

    }


    public List<CaseDocument> getIndexedCaseList() {
        return indexedCaseList;
    }

    public CaseDocument getExpectedTopHearingAsc() {
        return indexedCaseList.get(1);
    }

    public CaseDocument getExpectedBottomTopHearingAsc() {
        return indexedCaseList.get(0);
    }

    public CaseDocument getExpectedTopHearingDesc() {
        return indexedCaseList.get(2);
    }

    public CaseDocument getExpectedBottomHearingDec() {
        return indexedCaseList.get(0);
    }
}
