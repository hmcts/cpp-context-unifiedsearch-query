package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.PartyDocumentMother.defaultPartyAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;

import java.io.IOException;
import java.util.List;

public class PartyNameIngester {

    private List<CaseDocument> indexedCaseList;

    public void loadCases() throws IOException {
        indexedCaseList = createCases();
        new ElasticSearchIndexIngestorUtil().ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> createCases() {
        List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(3);

        caseBuilderList.get(0).withParties(asList(defaultPartyAsBuilder().withFirstName("John").withLastName("Wick").build()));
        caseBuilderList.get(1).withParties(asList(defaultPartyAsBuilder().withFirstName("Johnie").withLastName("Smith").build()));
        caseBuilderList.get(2).withParties(asList(defaultPartyAsBuilder().withFirstName("Debbie").withLastName("Williams").build()));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    public List<CaseDocument> getIngestedCaseDocuments() {
        return indexedCaseList;
    }

    public List<CaseDocument> getDebbieWilliamsCaseDocuments() {
        return asList(indexedCaseList.get(2));
    }

    public List<CaseDocument> getJohnieSmithCaseDocuments() {
        return asList(indexedCaseList.get(1));
    }

    public List<CaseDocument> getJohnWickCaseDocuments() {
        return asList(indexedCaseList.get(0));
    }
}
