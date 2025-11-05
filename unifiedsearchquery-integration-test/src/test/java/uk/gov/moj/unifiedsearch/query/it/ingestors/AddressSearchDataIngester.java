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

public class AddressSearchDataIngester {

    public void loadCasesAddressData() throws IOException {
        new ElasticSearchIndexIngestorUtil().ingestCaseData(createCaseAddressDocument());
    }

    private List<CaseDocument> createCaseAddressDocument() {

        final List<CaseDocument.Builder> caseBuilderList = CaseDocumentMother.defaultCasesAsBuilderList(20);

        final PartyDocument.Builder partyDocumentBuilder1 = defaultPartyAsBuilder()
                .withAddressLines("83 Liverpool Boulevard Woking Idaho")
                .withPostCode("W13 5TG");
        final PartyDocument.Builder partyDocumentBuilder2 = defaultPartyAsBuilder()
                .withAddressLines("130 Liverpool road Brighton Rhode Island")
                .withPostCode("TG14 4RT");
        final PartyDocument.Builder partyDocumentBuilder3 = defaultPartyAsBuilder()
                .withAddressLines("74 Liverpool Street Portsmouth Connecticut")
                .withPostCode("TG14 T67");
        final PartyDocument.Builder partyDocumentBuilder4 = defaultPartyAsBuilder()
                .withAddressLines("70 Irving Avenue Hemel Hemstead Liverpool")
                .withPostCode("TG14 15T");
        final PartyDocument.Builder partyDocumentBuilder5 = defaultPartyAsBuilder()
                .withAddressLines("130 Liverpool close Brighton Rhode Island")
                .withPostCode("YU45 5TG");
        final PartyDocument.Builder partyDocumentBuilder6 = defaultPartyAsBuilder()
                .withAddressLines("59 Baughman Place Liverpool New Jersey")
                .withPostCode("DC4 14T");
        final PartyDocument.Builder partyDocumentBuilder7 = defaultPartyAsBuilder()
                .withAddressLines("18 Furman Avenue Hertford Colorado")
                .withPostCode("WE5 6YU");
        final PartyDocument.Builder partyDocumentBuilder8 = defaultPartyAsBuilder()
                .withAddressLines("70 Albany Avenue Woking Utah")
                .withPostCode("W13 7JU");
        final PartyDocument.Builder partyDocumentBuilder9 = defaultPartyAsBuilder()
                .withAddressLines("19 Hendrickson Street Hertford New York")
                .withPostCode("KT7 5TG");
        final PartyDocument.Builder partyDocumentBuilder10 = defaultPartyAsBuilder()
                .withAddressLines("48 Mitchley Avenue Riddlesdown Purley")
                .withPostCode("CR8 1GH");
        final PartyDocument.Builder partyDocumentBuilder11 = defaultPartyAsBuilder()
                .withAddressLines("44 Mitchley Avenue Riddlesdown Purley")
                .withPostCode("CR2 1GH");

        caseBuilderList.get(0).withParties(asList(partyDocumentBuilder1, partyDocumentBuilder4));
        caseBuilderList.get(1).withParties(asList(partyDocumentBuilder2, partyDocumentBuilder9));
        caseBuilderList.get(2).withParties(asList(partyDocumentBuilder3, partyDocumentBuilder6));
        caseBuilderList.get(3).withParties(asList(partyDocumentBuilder4, partyDocumentBuilder8));
        caseBuilderList.get(4).withParties(asList(partyDocumentBuilder5, partyDocumentBuilder4));
        caseBuilderList.get(5).withParties(asList(partyDocumentBuilder6, partyDocumentBuilder1));
        caseBuilderList.get(6).withParties(asList(partyDocumentBuilder7, partyDocumentBuilder8));
        caseBuilderList.get(7).withParties(asList(partyDocumentBuilder8, partyDocumentBuilder9));
        caseBuilderList.get(8).withParties(asList(partyDocumentBuilder9, partyDocumentBuilder7));
        caseBuilderList.get(9).withParties(asList(partyDocumentBuilder9, partyDocumentBuilder10));
        caseBuilderList.get(10).withParties(asList(partyDocumentBuilder9, partyDocumentBuilder11));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());

    }

}
