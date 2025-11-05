package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother.defaultPartyAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.util.List;

public class MultiFieldNameSearchExactMatchFirstIngester {

    private final ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();

    private List<CaseDocument> indexedCaseList;

    public void loadCases() throws Exception {
        indexedCaseList = caseListForPartyNameSearch();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> caseListForPartyNameSearch() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(3);


        caseBuilderList.get(1).withParties(singletonList(createParty("Joe", "Smith")));

        caseBuilderList.get(2).withParties(singletonList(createParty("Tamara", "Smith")));

        caseBuilderList.get(0).withParties(asList(createParty("Eric", "Smith")));


        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    public List<CaseDocument> getCasesWithEricSmith() {
        return asList(indexedCaseList.get(0));
    }

    public List<CaseDocument> getCasesWithJoeSmith() {
        return asList(indexedCaseList.get(1));
    }

    public List<CaseDocument> getCasesWithTamaraSmith() {
        return asList(indexedCaseList.get(2));
    }

    public List<CaseDocument> getCasesWithSmiths() {
        return indexedCaseList;
    }

    private static PartyDocument.Builder createParty(final String firstName, String lastName) {
        return createParty(null, null, firstName, null, lastName);
    }


    private static PartyDocument.Builder createParty(final String partyDateOfBirth, final String orgName, final String firstName, final String middleName, final String lastName) {
        final PartyDocument.Builder partyBuilder = defaultPartyAsBuilder();
        partyBuilder.withDateOfBirth(partyDateOfBirth);
        partyBuilder.withOrganisationName(orgName)
                .withFirstName(firstName)
                .withMiddleName(middleName)
                .withLastName(lastName);
        return partyBuilder;
    }
}
