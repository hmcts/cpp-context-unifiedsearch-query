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

public class PartyNameAndDateOfBirthSearchDataIngester {

    public static final String PARTY_DATE_OF_BIRTH_1958 = "1958-07-13";
    private final ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();

    private List<CaseDocument> indexedCaseList;

    public void loadCases() throws Exception {
        indexedCaseList = caseListForPartyDateOfBirthAndNameSearch();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> caseListForPartyDateOfBirthAndNameSearch() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(3);

        caseBuilderList.get(0).withParties(asList(createParty(PARTY_DATE_OF_BIRTH_1958), createParty("2006-01-18"), createParty("2008-06-26", "MOJ", "Joe", "Sam", "Doe")));

        caseBuilderList.get(1).withParties(singletonList(createParty(PARTY_DATE_OF_BIRTH_1958, "MOJ", "Joe", "Sam", "Doe")));

        caseBuilderList.get(2).withParties(singletonList(createParty("2008-06-26")));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    public List<CaseDocument> getCasesWithJoeAndDateOfBirth58() {
        return asList(indexedCaseList.get(1));
    }


    private static PartyDocument.Builder createParty(final String dateOfBirth) {
        return createParty(dateOfBirth, null, null, null, null);
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
