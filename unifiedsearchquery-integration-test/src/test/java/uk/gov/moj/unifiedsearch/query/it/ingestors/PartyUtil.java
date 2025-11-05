package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.DEFENDANT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother.defaultPartyAsBuilder;

import uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.AddressDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

public class PartyUtil {
    public static final String EXPECTED_PARTY_PNC_ID_1_2099_1234567L_AC1 = "2099/1234567L";
    public static final String EXPECTED_PARTY_LAST_NAME_AC1 = "SMITH";

    public static final String EXPECTED_PARTY_CRO_NUMBER_1_987654_312_AC2 = "987654/312";
    public static final String EXPECTED_PARTY_LAST_NAME_AC2 = "ORMSBY";

    public static final String EXPECTED_PARTY_LAST_NAME_AC3 = "ORMSBY";
    public static final String EXPECTED_PARTY_FIRST_NAME_AC3 = "PETER";
    public static final String EXPECTED_PARTY_DOB_AC3 = "1936-03-10";
    public static final String EXPECTED_PARTY_ADDRESS_LINE_AC3 = "91 Shrewsbury Road";

    public static final String EXPECTED_PARTY_PNC_ID_5555_8135792K_AC4 = "5555/8135792k";
    public static final String EXPECTED_PARTY_CRO_NUMBER_123456_60g_AC4 = "123456/60g";
    public static final String EXPECTED_PARTY_FIRST_NAME_AC4 = "HARRY";
    public static final String EXPECTED_PARTY_LAST_NAME_AC4 = "PORTER";
    public static final String EXPECTED_PARTY_DOB_AC4 = "1939-03-19";
    public static final String EXPECTED_PARTY_ADDRESS_LINE_AC4 = "43 Longword Road";

    public static PartyDocument.Builder createDefendantParty(final String partyId,
                                                             final String pncId,
                                                             final String croNumber,
                                                             final String defFirstName,
                                                             final String defLastName,
                                                             final String defDob,
                                                             final String defAddressLine1,
                                                             final String courtProceedingsInitiated,
                                                             final String masterPartyId,
                                                             final boolean proceedingsConcluded) {

        return createPartyWithPartyType(partyId, pncId, croNumber, defFirstName, defLastName, defDob, defAddressLine1,
                                        courtProceedingsInitiated, masterPartyId, proceedingsConcluded, DEFENDANT);
    }

    public static PartyDocument.Builder createPartyWithPartyType(final String partyId,
                                                                 final String pncId,
                                                                 final String croNumber,
                                                                 final String defFirstName,
                                                                 final String defLastName,
                                                                 final String defDob,
                                                                 final String defAddressLine1,
                                                                 final String courtProceedingsInitiated,
                                                                 final String masterPartyId,
                                                                 final Boolean proceedingsConcluded,
                                                                 final PartyType partyType) {
        final PartyDocument.Builder partyBuilder = defaultPartyAsBuilder();
        partyBuilder.withPartyType(partyType.name());
        partyBuilder.withPartyId(partyId);
        partyBuilder.withPncId(pncId);
        partyBuilder.withProceedingsConcluded(proceedingsConcluded);
        partyBuilder.withCroNumber(croNumber);
        partyBuilder.withFirstName(defFirstName);
        partyBuilder.withMiddleName("middleName");
        partyBuilder.withLastName(defLastName);
        partyBuilder.withDateOfBirth(defDob);
        partyBuilder.withAddressLines(defAddressLine1);
        partyBuilder.withCourtProceedingsInitiated(courtProceedingsInitiated);
        partyBuilder.withMasterPartyId(masterPartyId);
        final AddressDocument defendantAddress = new AddressDocument(defAddressLine1, "addressLine2", "addressLine3", "addressLine4", "addressLine5", "postCode");
        partyBuilder.withDefendantAddress(defendantAddress);
        return partyBuilder;
    }

    public static PartyDocument.Builder createPartyNoProceedingsConcluded(final String partyId,
                                                                          final String pncId,
                                                                          final String croNumber,
                                                                          final String defFirstName,
                                                                          final String defLastName,
                                                                          final String defDob,
                                                                          final String defAddressLine1,
                                                                          final String courtProceedingsInitiated,
                                                                          final String masterPartyId) {
        return createPartyWithPartyType(partyId,
                pncId,
                croNumber,
                defFirstName,
                defLastName,
                defDob,
                defAddressLine1,
                courtProceedingsInitiated,
                masterPartyId, null, DEFENDANT);
    }

}
