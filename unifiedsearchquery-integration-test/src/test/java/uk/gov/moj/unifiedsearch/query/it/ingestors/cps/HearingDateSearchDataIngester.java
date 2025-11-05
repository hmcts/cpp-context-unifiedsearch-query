package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.HearingDocumentMother.hearingDays;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;

import java.io.IOException;
import java.util.List;

public class HearingDateSearchDataIngester {

    private List<CaseDocument> indexedCaseList;

    public void loadHearingDateData() throws IOException {
        indexedCaseList = createCaseHearingDateDocument();
        new ElasticSearchIndexIngestorUtil().ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> createCaseHearingDateDocument() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(3);

        caseBuilderList.get(0).withHearings(hearingDays("2021-03-01", "2021-04-01"));
        caseBuilderList.get(1).withHearings(hearingDays("2021-04-20"));
        caseBuilderList.get(2).withHearings(hearingDays("2021-06-01", "2021-04-30"));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    public CaseDocument getIndexDocumentAt(int index) {
        return indexedCaseList.get(index);
    }

    public List<CaseDocument> getMultipleCaseDocument() {
        return asList(indexedCaseList.get(1), indexedCaseList.get(2));
    }

    public List<CaseDocument> getCaseDataWithRange() {
        return asList(indexedCaseList.get(0), indexedCaseList.get(1), indexedCaseList.get(2));
    }

}
