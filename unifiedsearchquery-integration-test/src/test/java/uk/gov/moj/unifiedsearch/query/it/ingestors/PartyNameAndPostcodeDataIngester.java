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

public class PartyNameAndPostcodeDataIngester {

    public static final String PARTY_POSTCODE_CR0_2AB = "CR0 2AB";
    private final ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();

    private List<CaseDocument> indexedCaseList;

    public void loadCases() throws Exception {
        indexedCaseList = createCases();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> createCases() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(3);

        caseBuilderList.get(0).withParties(asList(createParty(PARTY_POSTCODE_CR0_2AB), createParty("RH16 2BW"), createParty("SL12 3AZ", "MOJ", "Joe", "Sam", "Doe")));

        caseBuilderList.get(1).withParties(singletonList(createParty(PARTY_POSTCODE_CR0_2AB, "MOJ", "Joe", "Sam", "Doe")));

        caseBuilderList.get(2).withParties(singletonList(createParty("SL11 3AZ")));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    public List<CaseDocument> getExpectedCases() {
        return asList(indexedCaseList.get(1));
    }


    private static PartyDocument.Builder createParty(final String postcode) {
        return createParty(postcode, null, null, null, null);
    }


    private static PartyDocument.Builder createParty(final String postcode, final String orgName, final String firstName, final String middleName, final String lastName) {
        final PartyDocument.Builder partyBuilder = defaultPartyAsBuilder();
        partyBuilder.withPostCode(postcode);
        partyBuilder.withOrganisationName(orgName)
                .withFirstName(firstName)
                .withMiddleName(middleName)
                .withLastName(lastName);
        return partyBuilder;
    }
}
