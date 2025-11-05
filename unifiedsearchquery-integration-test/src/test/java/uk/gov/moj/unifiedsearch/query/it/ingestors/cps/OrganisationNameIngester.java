package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.PartyDocumentMother.defaultPartyAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;

import java.io.IOException;
import java.util.List;

public class OrganisationNameIngester {

    private List<CaseDocument> indexedCasesList;

    public void loadCases() throws IOException {
        indexedCasesList = createCaseDocuments();
        new ElasticSearchIndexIngestorUtil().ingestCaseData(indexedCasesList);
    }

    private List<CaseDocument> createCaseDocuments() {
        List<CaseDocument.Builder> caseDocumentBuilders = defaultCasesAsBuilderList(3);

        caseDocumentBuilders.get(0).withParties(asList(defaultPartyAsBuilder().withOrganisationName("DRS Solutions").build()));
        caseDocumentBuilders.get(1).withParties(asList(defaultPartyAsBuilder().withOrganisationName("MKS Associates").build()));
        caseDocumentBuilders.get(2).withParties(asList(defaultPartyAsBuilder().withOrganisationName("Kites").build()));

        return caseDocumentBuilders.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }


    public List<CaseDocument> getDRSSolutionsCaseDocument() {
        return asList(indexedCasesList.get(0));
    }

    public List<CaseDocument> getMKSAssociatesCaseDocument() {
        return asList(indexedCasesList.get(1));
    }

    public List<CaseDocument> getKitesCaseDocument() {
        return asList(indexedCasesList.get(2));
    }
}
