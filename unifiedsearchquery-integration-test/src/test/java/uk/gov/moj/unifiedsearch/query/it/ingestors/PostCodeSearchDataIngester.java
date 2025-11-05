package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother.defaultPartyAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother;

import java.io.IOException;
import java.util.List;

public class PostCodeSearchDataIngester {

    public void loadPostCodeData() throws IOException {
        new ElasticSearchIndexIngestorUtil().ingestCaseData(createCaseAddressDocument());
    }

    private List<CaseDocument> createCaseAddressDocument() {

        final List<CaseDocument.Builder> caseBuilderList = CaseDocumentMother.defaultCasesAsBuilderList(20);

        final PartyDocument.Builder partyDocumentBuilder1 = defaultPartyAsBuilder()
                .withPostCode("W13 5TG").withLastName("TEST");

        final PartyDocument.Builder partyDocumentBuilder2 = defaultPartyAsBuilder()
                .withPostCode("TG14 4RT").withLastName("TEST");

        final PartyDocument.Builder partyDocumentBuilder3 = defaultPartyAsBuilder()
                .withPostCode("TG14 T67").withLastName("TEST");

        final PartyDocument.Builder partyDocumentBuilder4 = defaultPartyAsBuilder()
                .withPostCode("TG14 15T").withLastName("TEST");
        final PartyDocument.Builder partyDocumentBuilder5 = defaultPartyAsBuilder()
                .withPostCode("YU45 5TG").withLastName("TEST");
        final PartyDocument.Builder partyDocumentBuilder6 = defaultPartyAsBuilder()
                .withPostCode("DC4 14T").withLastName("TEST");
        final PartyDocument.Builder partyDocumentBuilder7 = defaultPartyAsBuilder()
                .withPostCode("WE5 6YU").withLastName("TEST");
        final PartyDocument.Builder partyDocumentBuilder8 = defaultPartyAsBuilder()
                .withPostCode("W13 7JU").withLastName("TEST");
        final PartyDocument.Builder partyDocumentBuilder9 = defaultPartyAsBuilder()
                .withPostCode("KT7 5TG").withLastName("TEST");

        caseBuilderList.get(0).withParties(asList(partyDocumentBuilder1, partyDocumentBuilder4));
        caseBuilderList.get(1).withParties(asList(partyDocumentBuilder2, partyDocumentBuilder9));
        caseBuilderList.get(2).withParties(asList(partyDocumentBuilder3, partyDocumentBuilder6));
        caseBuilderList.get(3).withParties(asList(partyDocumentBuilder4, partyDocumentBuilder8));
        caseBuilderList.get(4).withParties(asList(partyDocumentBuilder5, partyDocumentBuilder4));
        caseBuilderList.get(5).withParties(asList(partyDocumentBuilder6, partyDocumentBuilder1));
        caseBuilderList.get(6).withParties(asList(partyDocumentBuilder7, partyDocumentBuilder8));
        caseBuilderList.get(7).withParties(asList(partyDocumentBuilder8, partyDocumentBuilder9));
        caseBuilderList.get(8).withParties(asList(partyDocumentBuilder9, partyDocumentBuilder7));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());

    }
}
