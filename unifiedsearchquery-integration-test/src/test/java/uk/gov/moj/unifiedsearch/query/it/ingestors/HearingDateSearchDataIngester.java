package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.HearingDayDocumentMother.hearingDays;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.HearingDocumentMother.defaultHearingAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother.defaultPartyAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.io.IOException;
import java.util.List;

public class HearingDateSearchDataIngester {

    private List<CaseDocument> indexedCaseList;
    public static final String PARTY_POSTCODE_CR0_2AB = "CR0 2AB";
    public static final String DATE_OF_NEXT_HEARING_2019_04_19 = "2019-04-19";

    public void loadHearingDateData() throws IOException {
        indexedCaseList = createCaseHearingDateDocument();
        new ElasticSearchIndexIngestorUtil().ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> createCaseHearingDateDocument() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(5);


        final HearingDocument.Builder hearingDocumentBuilder1 = defaultHearingAsBuilder()
                .withHearingDays(hearingDays(DATE_OF_NEXT_HEARING_2019_04_19, "2019-03-01"));

        final HearingDocument.Builder hearingDocumentBuilder2 = defaultHearingAsBuilder()
                .withHearingDays(hearingDays("2019-05-12", "2019-04-23"));

        final HearingDocument.Builder hearingDocumentBuilder3 = defaultHearingAsBuilder()
                .withHearingDays(hearingDays("2019-05-17"));

        final HearingDocument.Builder hearingDocumentBuilder4 = defaultHearingAsBuilder()
                .withHearingDays(hearingDays("2019-04-24", DATE_OF_NEXT_HEARING_2019_04_19));

        final HearingDocument.Builder hearingDocumentBuilder5 = defaultHearingAsBuilder()
                .withHearingDays(hearingDays("2019-03-09", "2019-01-12", "2019-02-27"));


        caseBuilderList.get(0).withHearings(asList(hearingDocumentBuilder1, hearingDocumentBuilder4));
        caseBuilderList.get(0).withParties(asList(
                createParty(PARTY_POSTCODE_CR0_2AB, "", "", "", "last1"),
                createParty("RH16 2BW", "", "", "", "last2"),
                createParty("SL12 3AZ", "MOJ", "Joe", "Sam", "Doe")));
        caseBuilderList.get(1).withHearings(singletonList(hearingDocumentBuilder2));
        caseBuilderList.get(2).withHearings(singletonList(hearingDocumentBuilder5));
        caseBuilderList.get(3).withHearings(asList(hearingDocumentBuilder3, hearingDocumentBuilder1));
        caseBuilderList.get(3).withParties(asList(
                createParty(PARTY_POSTCODE_CR0_2AB,"", "", "", "last3"),
                createParty("RH16 2BW","org1", "", "", ""),
                createParty("SL12 3AZ", "MOJ", "Joe", "Sam", "Doe")));

        caseBuilderList.get(4).withHearings(asList(hearingDocumentBuilder5, hearingDocumentBuilder2));


        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
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

    public CaseDocument getIndexDocumentAt(int index) {
        return indexedCaseList.get(index);
    }

}
