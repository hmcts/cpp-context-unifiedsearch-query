package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.APPLICANT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.DEFENDANT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.RESPONDENT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother.defaultPartyAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.util.List;

public class PartyTypeSearchDataIngester {

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
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(3);

        caseBuilderList.get(0).withParties(asList(createParty(DEFENDANT), createParty(RESPONDENT), createParty(APPLICANT, "MOJ", "Joe", "Sam", "Doe")));

        caseBuilderList.get(1).withParties(singletonList(createParty(DEFENDANT, "MOJ", "Joe", "Sam", "Doe")));

        caseBuilderList.get(2).withParties(singletonList(createParty(APPLICANT)));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    public List<CaseDocument> getCasesWithDefendant() {
        return asList(indexedCaseList.get(0), indexedCaseList.get(1));
    }

    public List<CaseDocument> getCasesWithDefendantJoe() {
        return asList(indexedCaseList.get(1));
    }

    public List<CaseDocument> getCasesWithApplicant() {
        return asList(indexedCaseList.get(0), indexedCaseList.get(2));
    }

    public List<CaseDocument> getCasesWithRespondent() {
        return asList(indexedCaseList.get(0));
    }

    public List<CaseDocument> getAllCases() {
        return indexedCaseList;
    }

    private static PartyDocument.Builder createParty(final PartyType partyType) {
        return createParty(partyType, null, null, null, null);
    }


    private static PartyDocument.Builder createParty(final PartyType partyType, final String orgName, final String firstName, final String middleName, final String lastName) {
        final PartyDocument.Builder partyBuilder = defaultPartyAsBuilder();
        partyBuilder.withPartyType(partyType.name());
        partyBuilder.withPartyType(partyType.name())
                .withOrganisationName(orgName)
                .withFirstName(firstName)
                .withMiddleName(middleName)
                .withLastName(lastName);
        return partyBuilder;
    }
}
