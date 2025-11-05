package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.OffenceDocumentMother.defaultOffenceDocument;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother.defaultPartyAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.RepresentationOrderDocumentMother.defaultRepresentationOrder;

import uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.AddressDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.io.IOException;
import java.util.List;

public class AsnSearchDataIngester {

    private List<CaseDocument> indexedCaseList;

    public void loadAsnData() throws IOException {
        indexedCaseList = createCaseAsnDocuments();
        new ElasticSearchIndexIngestorUtil().ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> createCaseAsnDocuments() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(5);

        final PartyDocument.Builder partyDocumentBuilder1 = createPartyBuilder("ASN001");

        final PartyDocument.Builder partyDocumentBuilder2 = createPartyBuilder("ASN002");


        final PartyDocument.Builder partyDocumentBuilder3 = createPartyBuilder("ASN003");

        final PartyDocument.Builder partyDocumentBuilder4 = createPartyBuilder("TFL");
        final PartyDocument.Builder partyDocumentBuilder5 = createPartyBuilderWithAddress("ASN004");



        caseBuilderList.get(0).withParties(asList(partyDocumentBuilder1, partyDocumentBuilder4));
        caseBuilderList.get(1).withParties(singletonList(partyDocumentBuilder2));
        caseBuilderList.get(2).withParties(asList(partyDocumentBuilder1, partyDocumentBuilder2, partyDocumentBuilder4));
        caseBuilderList.get(3).withParties(singletonList(partyDocumentBuilder3));
        caseBuilderList.get(4).withParties(asList(partyDocumentBuilder5));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    private PartyDocument.Builder createPartyBuilder(final String asn) {
        return defaultPartyAsBuilder()
                .withArrestSummonsNumber(asn)
                .withPartyType(PartyType.DEFENDANT.toString())
                .withOffences(asList(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder());
    }

    private PartyDocument.Builder createPartyBuilderWithAddress(final String asn) {
        return defaultPartyAsBuilder()
                .withArrestSummonsNumber(asn)
                .withPartyType(PartyType.DEFENDANT.toString())
                .withOffences(asList(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder())
                .withDefendantAddress(new AddressDocument("83 Liverpool", "Boulevard Woking Idaho",null, null, null,"W13 5TG"));

    }

    public CaseDocument getIndexDocumentAt(int index) {
        return indexedCaseList.get(index);
    }

    public CaseDocument getCaseForAsn003() {
        return indexedCaseList.get(3);
    }

    public CaseDocument getCaseForAsn004() {
        return indexedCaseList.get(4);
    }

    public List<CaseDocument> getCasesForTflAsn() {
        return asList(indexedCaseList.get(0), indexedCaseList.get(2));
    }
}
