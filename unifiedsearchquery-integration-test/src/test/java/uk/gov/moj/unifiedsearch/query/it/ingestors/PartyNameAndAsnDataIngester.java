package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother.defaultPartyAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.util.List;

public class PartyNameAndAsnDataIngester {

    public static final String ASN_1 = "Asn-1";
    public static final String ASN_2 = "Asn-2";
    public static final String FIRSTNAME_1 = "Joe";
    public static final String LASTNAME_1 = "Smith";
    public static final String FIRSTNAME_2 = "John";
    public static final String LASTNAME_2 = "Clarke";

    private final ElasticSearchIndexIngestorUtil
                            elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();
    private List<CaseDocument> indexedCaseList;

    public void loadCases() throws Exception {
        indexedCaseList = createCases();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> createCases() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(2);
        caseBuilderList.get(0).withParties(singletonList(createParty(FIRSTNAME_1, LASTNAME_1, ASN_1)));
        caseBuilderList.get(1).withParties(singletonList(createParty(FIRSTNAME_2, LASTNAME_2, ASN_2)));

        return caseBuilderList.stream().
                               map(CaseDocument.Builder::build).
                               collect(toList());
    }

    public CaseDocument getExpectedCases(int index) {
        return indexedCaseList.get(index);
    }

    private static PartyDocument.Builder createParty(String firstName, String lastName, String asn) {
        final PartyDocument.Builder partyBuilder = defaultPartyAsBuilder();
        partyBuilder.withFirstName(firstName).
                     withLastName(lastName).
                     withArrestSummonsNumber(asn);
        return partyBuilder;
    }
}
