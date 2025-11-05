package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static uk.gov.justice.services.unifiedsearch.client.constant.CpsPartyType.DEFENDANT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.PartyDocumentMother.defaultPartyAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.AliasDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;

import java.io.IOException;
import java.util.List;

public class PartyAliasOrNameIngester {

    private List<CaseDocument> indexedCaseList;

    public void loadCases() throws IOException {
        indexedCaseList = createCases();
        new ElasticSearchIndexIngestorUtil().ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> createCases() {
        List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(2);

        caseBuilderList.get(0).withParties(asList(defaultPartyAsBuilder()
                .with_party_type(asList(DEFENDANT.name()))
                .withFirstName("Peter")
                .withLastName("Wick")
                .withAliases(asList(new AliasDocument("Bob", "Marley"))).build()));
        caseBuilderList.get(1).withParties(asList(defaultPartyAsBuilder()
                .with_party_type(asList(DEFENDANT.name()))
                .withFirstName("Johnie")
                .withLastName("Smith")
                .withAliases(asList(new AliasDocument("Roger", "Smith"))).build()));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    public List<CaseDocument> getIngestedCaseDocuments() {
        return indexedCaseList;
    }

    public List<CaseDocument> getBobCaseDocuments() {
        return asList(indexedCaseList.get(0));
    }

    public List<CaseDocument> getJohnieCaseDocuments() {
        return asList(indexedCaseList.get(1));
    }

    public List<CaseDocument> getRogerCaseDocuments() {
        return asList(indexedCaseList.get(1));
    }
}
