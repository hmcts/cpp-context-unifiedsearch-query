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

public class NinoSearchDataIngester {

    private List<CaseDocument> indexedCaseList;

    public void loadNinoData() throws IOException {
        indexedCaseList = createCaseNINODocuments();
        new ElasticSearchIndexIngestorUtil().ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> createCaseNINODocuments() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(8);

        final PartyDocument.Builder partyDocumentBuilder1 = createPartyBuilder("AB123456Z");

        final PartyDocument.Builder partyDocumentBuilder2 = createPartyBuilder("BC123456Y");

        final PartyDocument.Builder partyDocumentBuilder3 = createPartyBuilder("HJ123456S");

        final PartyDocument.Builder partyDocumentBuilder4 = createPartyBuilder("DE123456W");

        final PartyDocument.Builder partyDocumentBuilder5 = createPartyBuilderWithAddress("HJ123456F");



        caseBuilderList.get(0).withParties(asList(partyDocumentBuilder1, partyDocumentBuilder4));
        caseBuilderList.get(1).withParties(singletonList(partyDocumentBuilder2));
        caseBuilderList.get(2).withParties(asList(partyDocumentBuilder1, partyDocumentBuilder2, partyDocumentBuilder4));
        caseBuilderList.get(3).withParties(singletonList(partyDocumentBuilder3));
        caseBuilderList.get(4).withParties(singletonList(partyDocumentBuilder5));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    private PartyDocument.Builder createPartyBuilder(final String s) {
        return defaultPartyAsBuilder()
                .withNationalInsuranceNumber(s)
                .withPartyType(PartyType.DEFENDANT.toString())
                .withOffences(asList(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder());
    }

    private PartyDocument.Builder createPartyBuilderWithAddress(final String s) {
        return defaultPartyAsBuilder()
                .withNationalInsuranceNumber(s)
                .withPartyType(PartyType.DEFENDANT.toString())
                .withOffences(asList(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder())
                .withDefendantAddress(new AddressDocument("83 Liverpool", "Boulevard Woking Idaho",null, null, null,"W13 5TG"));

    }

    public CaseDocument getIndexDocumentAt(int index) {
        return indexedCaseList.get(index);
    }
}
