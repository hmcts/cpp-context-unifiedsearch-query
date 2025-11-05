package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother.defaultPartyAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.io.IOException;
import java.util.List;

public class DateOfBirthSearchDataIngester {

    private List<CaseDocument> indexedCaseList;

    public void loadDateOfBirthData() throws IOException {
        indexedCaseList = createCaseHearingDateDocument();
        new ElasticSearchIndexIngestorUtil().ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> createCaseHearingDateDocument() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(8);

        final PartyDocument.Builder partyDocumentBuilder1 = createPartyBuilder("1954-04-29");

        final PartyDocument.Builder partyDocumentBuilder2 = createPartyBuilder("1958-07-18");

        final PartyDocument.Builder partyDocumentBuilder3 = createPartyBuilder("1970-06-19");

        final PartyDocument.Builder partyDocumentBuilder4 = createPartyBuilder("1972-11-23");

        final PartyDocument.Builder partyDocumentBuilder5 = createPartyBuilder("1975-08-20");

        final PartyDocument.Builder partyDocumentBuilder6 = createPartyBuilder("1950-05-05");

        final PartyDocument.Builder partyDocumentBuilder7 = createPartyBuilder("1977-10-22");

        final PartyDocument.Builder partyDocumentBuilder8 = createPartyBuilder("1981-02-27");

        caseBuilderList.get(0).withParties(asList(partyDocumentBuilder1, partyDocumentBuilder4));
        caseBuilderList.get(1).withParties(singletonList(partyDocumentBuilder3));
        caseBuilderList.get(2).withParties(asList(partyDocumentBuilder2, partyDocumentBuilder7, partyDocumentBuilder5));
        caseBuilderList.get(3).withParties(singletonList(partyDocumentBuilder8));
        caseBuilderList.get(4).withParties(asList(partyDocumentBuilder6, partyDocumentBuilder3));
        caseBuilderList.get(5).withParties(singletonList(partyDocumentBuilder1));
        caseBuilderList.get(6).withParties(singletonList(partyDocumentBuilder4));
        caseBuilderList.get(7).withParties(asList(partyDocumentBuilder2, partyDocumentBuilder7));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    private PartyDocument.Builder createPartyBuilder(final String s) {
        return defaultPartyAsBuilder()
                .withDateOfBirth(s);
    }

    public CaseDocument getIndexDocumentAt(int index) {
        return indexedCaseList.get(index);
    }
}
