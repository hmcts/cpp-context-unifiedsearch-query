package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.time.ZonedDateTime.now;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.APPLICANT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.DEFENDANT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.RESPONDENT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCaseAsBuilder;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_ADDRESS_LINE_AC3;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_ADDRESS_LINE_AC4;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_CRO_NUMBER_123456_60g_AC4;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_CRO_NUMBER_1_987654_312_AC2;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_DOB_AC3;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_DOB_AC4;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_FIRST_NAME_AC3;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_FIRST_NAME_AC4;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_LAST_NAME_AC1;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_LAST_NAME_AC2;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_LAST_NAME_AC3;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_LAST_NAME_AC4;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_PNC_ID_1_2099_1234567L_AC1;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_PNC_ID_5555_8135792K_AC4;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.createDefendantParty;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.createPartyWithPartyType;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.ISO_8601_FORMATTER;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.util.ArrayList;
import java.util.List;

public class PartyIngester {

    private final ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();

    private List<CaseDocument> indexedCaseList;
    private List<CaseDocument> filteredIndexedList = new ArrayList<>();
    private List<CaseDocument> indexedCaseListWithOnlyDefendantParty = new ArrayList<>();

    public void loadCases() throws Exception {
        indexedCaseList = createDefaultCases();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    public List<CaseDocument> getExpectedCasesForPncIdAndLastNameForDefendant() {

        return asList(filteredIndexedList.get(0), indexedCaseListWithOnlyDefendantParty.get(0));
    }

    public List<CaseDocument> getExpectedCasesCroNumberAndLastName() {
        return asList(indexedCaseList.get(1), indexedCaseList.get(5));
    }

    public List<CaseDocument> getExpectedCasesForFNLNDOBADD() {
        return asList(indexedCaseList.get(5), indexedCaseList.get(6));
    }

    public List<CaseDocument> getExpectedCasesForFNLNDOBADDPNCIDCRO() {
        return asList(indexedCaseList.get(8), indexedCaseList.get(9));
    }


    private final List<CaseDocument> createDefaultCases() {
        final List<CaseDocument.Builder> caseBuilderList = new ArrayList<>();

        /*
        AC-1-PNCId & Defendant Last Name--case insensitive
            Given I have my CP Search Defendant matching query
            When I call the above query with pncid and defendant last name in my sample payload (PNCID=2099/1234567L, DEF LASTNAME=SMITH)
            Then I expect to see the results that match PNCId & Defendant Last Name (row 1 and 8 from test data)
         */
        //PNC & lastname(AC-1)
        final String courtProceedingsInitiated = now().format(ISO_8601_FORMATTER);
        final String masterPartyId = randomUUID().toString();
        final String partyId = randomUUID().toString();
        final PartyDocument.Builder party1 = createDefendantParty(partyId, EXPECTED_PARTY_PNC_ID_1_2099_1234567L_AC1, "123456/20L", "TEGUN", EXPECTED_PARTY_LAST_NAME_AC1, "1931-01-01", "195 Ber Stree't", courtProceedingsInitiated, masterPartyId, false);
        final PartyDocument.Builder party11 = createDefendantParty(randomUUID().toString(), "WRONG_PNC", "123456/20L", "TEGUN", "NOT SMITH", "1931-01-01", "195 Ber Stree't", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("b45d409a-4e72-403d-adb0-015f15bd89c6", "63GD6253620", false, true, false));
        caseBuilderList.get(0).withParties(asList(party1, party11));


        final List<CaseDocument.Builder> expectedCaseBuilderList = new ArrayList<>();
        expectedCaseBuilderList.add(defaultCaseAsBuilder("b45d409a-4e72-403d-adb0-015f15bd89c6", "63GD6253620", true));
        final PartyDocument.Builder partyExp = createDefendantParty(partyId, EXPECTED_PARTY_PNC_ID_1_2099_1234567L_AC1, "123456/20L", "TEGUN", EXPECTED_PARTY_LAST_NAME_AC1, "1931-01-01", "195 Ber Stree't", courtProceedingsInitiated, masterPartyId, false);
        expectedCaseBuilderList.get(0).withParties(asList(partyExp));
        filteredIndexedList = expectedCaseBuilderList.stream().map(CaseDocument.Builder::build).collect(toList());


        //CRO & lastname(AC-2)
        /*
        AC-2-CRONumber & Defendant Last Name--case insensitive
        Given I have my CP Search Defendant matching query
        When I call the above query with CRONumber and defendant the last name in my sample payload (CRONUMBER=987654/312, DEF LASTNAME=ORMSBY)
        Then I expect to see the results that match CRONumber & Defendant Last Name (row 2 and 6 from test data)
        */
        final PartyDocument.Builder party2 = createDefendantParty(randomUUID().toString(), "2034/1234567r", EXPECTED_PARTY_CRO_NUMBER_1_987654_312_AC2, "ROBERT", EXPECTED_PARTY_LAST_NAME_AC2, "1932-01-15", "20 Rashleigh Way", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("f678854b-2bc4-41b5-8853-32d5fbaf0b3c", "89GD6995420", false, true, false));
        caseBuilderList.get(1).withParties(asList(party2));

        final PartyDocument.Builder party3 = createDefendantParty(randomUUID().toString(), "1981/98765432", "123456/20t", "JACK", "SMITH", "1933-01-29", "15 Katherine Road", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("1559f788-e155-452c-94f0-08f4cc1b180e", "78GD1219320", false, true, false));
        caseBuilderList.get(2).withParties(asList(party3));

        final PartyDocument.Builder party4 = createDefendantParty(randomUUID().toString(), "3033/8135792k", "SF20/123456m", "ALAN", "GRAY", "1934-02-02", "21 Whitfield Road", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("b53725e0-4502-49d6-8a0a-60650c64aa7c", "89GD2721420", false, true, false));
        caseBuilderList.get(3).withParties(asList(party4));

        final PartyDocument.Builder party5 = createDefendantParty(randomUUID().toString(), "1800/5289451Y", "SF20/123456u", "GREAME", "SHORTER", "1935-03-04", "76 Altmore Avenue", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("25f106bc-e4f6-49d2-a22c-bca71e1a5994", "50GD4719920", false, true, false));
        caseBuilderList.get(4).withParties(asList(party5));

        /*
        AC-3-(Defendant First && Last Name)&&(Defendant DOB)&&(Defendant Address Line 1)--case insensitive
        Given I have my CP Search Defendant matching query
        When I call the above query with the defendant first name, last name, dob and address line1  in my sample payload (FRISTNAME=PETER,LASTNAME=ORMSBY,DOB=10/03/1936,ADDRESSLINE1=91 Shrewsbury Road)
        Then I expect to see the results that match defendant first name, last name, dob and address line1(row 6 and 7 from the test data)
         */
        //CRO & lastname(AC-2)
        //(Defendant First && Last Name)&&(Defendant DOB)&&(Defendant Address Line 1) - AC3
        final PartyDocument.Builder party6 = createDefendantParty(randomUUID().toString(), "4055/1234567L", EXPECTED_PARTY_CRO_NUMBER_1_987654_312_AC2, EXPECTED_PARTY_FIRST_NAME_AC3, EXPECTED_PARTY_LAST_NAME_AC3, EXPECTED_PARTY_DOB_AC3, EXPECTED_PARTY_ADDRESS_LINE_AC3, now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("06a7cda8-5adf-4bd4-aa40-1b71ff9460f6", "56GD8582920", false, true, false));
        caseBuilderList.get(5).withParties(asList(party6));

        //(Defendant First && Last Name)&&(Defendant DOB)&&(Defendant Address Line 1) - AC3
        final PartyDocument.Builder party7 = createDefendantParty(randomUUID().toString(), "2123/1234567y", "987654/45L", EXPECTED_PARTY_FIRST_NAME_AC3, EXPECTED_PARTY_LAST_NAME_AC3, EXPECTED_PARTY_DOB_AC3, EXPECTED_PARTY_ADDRESS_LINE_AC3, now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("af9ef369-1bfe-4b1a-a9f8-1e8c87049957", "40GD9236020", false, true, false));
        caseBuilderList.get(6).withParties(asList(party7));

        //PNC & lastname(AC-1)
        final PartyDocument.Builder party8 = createPartyWithPartyType(randomUUID().toString(), EXPECTED_PARTY_PNC_ID_1_2099_1234567L_AC1, "123456/123", "WILL", EXPECTED_PARTY_LAST_NAME_AC1, "1938-03-15", "77 Forest Road", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false, DEFENDANT);
        final PartyDocument.Builder respondent = createPartyWithPartyType(randomUUID().toString(), EXPECTED_PARTY_PNC_ID_1_2099_1234567L_AC1, "123456/123", "WILL", EXPECTED_PARTY_LAST_NAME_AC1, "1938-03-15", "77 Forest Road", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false, RESPONDENT);
        final PartyDocument.Builder applicant = createPartyWithPartyType(randomUUID().toString(), EXPECTED_PARTY_PNC_ID_1_2099_1234567L_AC1, "123456/123", "WILL", EXPECTED_PARTY_LAST_NAME_AC1, "1938-03-15", "77 Forest Road", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false, APPLICANT);

        caseBuilderList.add(defaultCaseAsBuilder("e7bc74f3-71bd-4fcf-b622-462e7af1c289", "27GD5057120", false, true, false));
        caseBuilderList.get(7).withParties(asList(party8, respondent, applicant));

        final List<CaseDocument.Builder> casesWithOnlyDefendantParties = new ArrayList<>();
        CaseDocument.Builder caseWithOnlyDefendantParty = defaultCaseAsBuilder("e7bc74f3-71bd-4fcf-b622-462e7af1c289", "27GD5057120", false, true, false);
        casesWithOnlyDefendantParties.add(caseWithOnlyDefendantParty.withParties(asList(party8)));
        indexedCaseListWithOnlyDefendantParty = casesWithOnlyDefendantParties.stream().map(CaseDocument.Builder::build).collect(toList());

        /*
        AC-4-matching PNCId, No Exact Last Name Match --case insensitive
        Given I have my CP Search Defendant matching query
        When I call the above query with pncid in my sample payload (PNCID=2099/1234567P)
        Then I expect to see the results that match PNCId(row 1 and 2 from the test data)
         */
        //(pncid)&&(CRO number)&&(Defendant First && Last Name)&&(Defendant DOB)&&(Defendant Address Line 1)-AC-4
        final PartyDocument.Builder party9 = createDefendantParty(randomUUID().toString(), EXPECTED_PARTY_PNC_ID_5555_8135792K_AC4, EXPECTED_PARTY_CRO_NUMBER_123456_60g_AC4, EXPECTED_PARTY_FIRST_NAME_AC4, EXPECTED_PARTY_LAST_NAME_AC4, EXPECTED_PARTY_DOB_AC4, EXPECTED_PARTY_ADDRESS_LINE_AC4, now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("8e52ad76-5003-4a01-80b8-ba22b2cdfedb", "76GD9443920", false, true, false));
        caseBuilderList.get(8).withParties(asList(party9));

        //(pncid)&&(CRO number)&&(Defendant First && Last Name)&&(Defendant DOB)&&(Defendant Address Line 1)-AC-4
        final PartyDocument.Builder party10 = createDefendantParty(randomUUID().toString(), EXPECTED_PARTY_PNC_ID_5555_8135792K_AC4, EXPECTED_PARTY_CRO_NUMBER_123456_60g_AC4, EXPECTED_PARTY_FIRST_NAME_AC4, EXPECTED_PARTY_LAST_NAME_AC4, EXPECTED_PARTY_DOB_AC4, EXPECTED_PARTY_ADDRESS_LINE_AC4, now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("d440d3d7-ccd9-42e2-9c8a-26d64663682c", "44GD7188020", false, true, false));
        caseBuilderList.get(9).withParties(asList(party10));

        //crownOrMagistrates -AC8 (isSjp,isMagistrates,isCrown)
        /*
        AC-8-Matching DOB, Matching Address Line 1,No Match on Last Name --case insensitive
        Given I have my CP Search Defendant matching query
        When I call the above query with dob and address line 1 my sample payload (DOB=19/03/1949,ADDRESSLINE1=02296 Ursula Pines)
        Then I expect to see the results that match dob and address line 1(row 9 and 10 from the test data)
         */
        final PartyDocument.Builder party81 = createDefendantParty(randomUUID().toString(), "123/121", "", "is_sjp_true,is_crown_true", "is_magistrates_false", "1975-12-31", "", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("28c7cd3c-a726-412e-bf0e-aeb5f0723092", "84GD7188021", true, false, true));
        caseBuilderList.get(10).withParties(asList(party81));

        final PartyDocument.Builder party82 = createDefendantParty(randomUUID().toString(), "123/121", "", "is_sjp_true,is_magistrates_true", "is_crown_false", "1975-12-31", "", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("75a8e334-49a7-4807-8eb2-d9041cb3b815", "84GD7188022", true, true, false));
        caseBuilderList.get(11).withParties(asList(party82));

        final PartyDocument.Builder party83 = createDefendantParty(randomUUID().toString(), "123/123", "", "is_sjp_true,is_magistrates_true,is_crown_true", "", "1975-12-31", "", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("b61a5287-960b-4dff-a865-325181a0ef7e", "84GD7188023", true, true, true));
        caseBuilderList.get(12).withParties(asList(party83));

        final PartyDocument.Builder party84 = createDefendantParty(randomUUID().toString(), "123/125", "", "is_sjp_true", "is_magistrates_false,is_crown_false", "1975-12-31", "", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("854d36e9-800c-4b68-b7cf-4b7759168438", "84GD7188025", true, false, false));
        caseBuilderList.get(13).withParties(asList(party84));

        final PartyDocument.Builder party85 = createDefendantParty(randomUUID().toString(), "123/124", "", "", "is_sjp_false,is_magistrates_false,is_crown_false", "1975-12-31", "", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("753c112c-9de9-4e9d-bba0-b778990673af", "84GD7188024", false, false, false));
        caseBuilderList.get(14).withParties(asList(party85));

        final PartyDocument.Builder party86 = createDefendantParty(randomUUID().toString(), "123/126", "", "is_magistrates_true", "is_sjp_false,is_crown_false", "1975-12-31", "", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("63f7b7f1-450e-4a70-9121-8814059bde40", "84GD7188026", false, true, false));
        caseBuilderList.get(15).withParties(asList(party86));

        final PartyDocument.Builder party87 = createDefendantParty(randomUUID().toString(), "123/127", "", "is_crown_true", "is_sjp_false,is_magistrates_false", "1975-12-31", "", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("7896adee-1b51-460f-ba76-b64f10ed98d8", "84GD7188027", false, false, true));
        caseBuilderList.get(16).withParties(asList(party87));

        final PartyDocument.Builder party88 = createDefendantParty(randomUUID().toString(), "123/128", "", "is_crown_true,is_magistrates_true", "is_sjp_false", "1975-12-31", "", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("71e21b71-6613-46e7-99e1-729acd4f69cb", "84GD7188028", false, true, true));
        caseBuilderList.get(17).withParties(asList(party88));

        final PartyDocument.Builder party89 = createPartyWithPartyType(randomUUID().toString(), EXPECTED_PARTY_PNC_ID_1_2099_1234567L_AC1, "123456/123", "WILL", EXPECTED_PARTY_LAST_NAME_AC1, "1938-03-15", "77 Forest Road", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false, RESPONDENT);
        caseBuilderList.add(defaultCaseAsBuilder(randomUUID().toString(), "27GD5057120", false, true, false));
        caseBuilderList.get(18).withParties(asList(party89));

        final PartyDocument.Builder party90 = createPartyWithPartyType(randomUUID().toString(), EXPECTED_PARTY_PNC_ID_1_2099_1234567L_AC1, "123456/123", "WILL", EXPECTED_PARTY_LAST_NAME_AC1, "1938-03-15", "77 Forest Road", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false, APPLICANT);
        caseBuilderList.add(defaultCaseAsBuilder(randomUUID().toString(), "27GD5057120", false, true, false));
        caseBuilderList.get(19).withParties(asList(party90));


        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

}
