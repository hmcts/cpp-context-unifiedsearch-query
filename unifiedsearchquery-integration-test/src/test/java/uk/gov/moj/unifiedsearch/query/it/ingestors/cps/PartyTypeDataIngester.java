package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.justice.services.unifiedsearch.client.constant.CpsPartyType.DEFENDANT;
import static uk.gov.justice.services.unifiedsearch.client.constant.CpsPartyType.VICTIM;
import static uk.gov.justice.services.unifiedsearch.client.constant.CpsPartyType.WITNESS;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.PartyDocumentMother.defaultPartyAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.PartyDocument;

import java.util.List;


public class PartyTypeDataIngester {

    private final ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();

    private List<CaseDocument> indexedCaseList;

    public void loadCases() throws Exception {
        indexedCaseList = caseListForPartyTypeSearch();
        loadCaseDocuments();
    }

    private void loadCaseDocuments() throws Exception {
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }


    private List<CaseDocument> caseListForPartyTypeSearch() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(4);

        caseBuilderList.get(0).withParties(asList(createParty(asList(VICTIM.name()), "Joe"), createParty(asList(WITNESS.name()), "MOJ", "Joe", "Sam", "Doe")));

        caseBuilderList.get(1).withParties(singletonList(createParty(asList(DEFENDANT.name()), "MOJ", "Joe", "Sam", "Doe")));

        caseBuilderList.get(2).withParties(singletonList(createParty(asList(DEFENDANT.name()), "Joe")));

        caseBuilderList.get(3).withParties(singletonList(createParty(asList(VICTIM.name(), WITNESS.name()), "Joe")));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    public List<CaseDocument> getCasesWithDefendant() {
        return asList(indexedCaseList.get(1), indexedCaseList.get(2));
    }

    public List<CaseDocument> getCasesWithWitness() {
        return asList(indexedCaseList.get(0), indexedCaseList.get(3));
    }

    public List<CaseDocument> getCasesWithVictim() {
        return asList(indexedCaseList.get(0), indexedCaseList.get(3));
    }

    public List<CaseDocument> getAllCases() {
        return indexedCaseList;
    }

    private static PartyDocument createParty(final List<String> partyTypes, final String firstName) {
        return createParty(partyTypes, null, firstName, null, null);
    }


    private static PartyDocument createParty(final List<String> partyTypes, final String orgName, final String firstName, final String middleName, final String lastName) {
        final PartyDocument.Builder partyBuilder = defaultPartyAsBuilder();
        partyBuilder.with_party_type(partyTypes).withFirstName(firstName)
                .withLastName(lastName);
        return partyBuilder.build();
    }
}