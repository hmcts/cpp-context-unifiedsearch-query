package uk.gov.moj.cpp.unifiedsearch.query.common.constant;

public class DefendantQueryParameterNamesConstants {
    public static final String PNC_ID = "pncId";
    public static final String CRO_NUMBER = "croNumber";
    public static final String LAST_NAME = "lastName";
    public static final String FIRST_NAME = "firstName";
    public static final String DATE_OF_BIRTH = "dateOfBirth";
    public static final String ADDRESS_LINE_1 = "addressLine1";
    public static final String PROCEEDINGS_CONCLUDED = "proceedingsConcluded";
    public static final String CROWN_OR_MAGISTRATES = "crownOrMagistrates";

    public static final String PNC_ID_INDEX = "parties.pncId";
    public static final String CRO_NUMBER_INDEX = "parties.croNumber";
    public static final String FIRST_NAME_INDEX = "parties.firstName.exact";
    public static final String LAST_NAME_INDEX = "parties.lastName.exact";
    public static final String ADDRESS1_INDEX = "parties.defendantAddress.address1";
    public static final String PROCEEDINGS_CONCLUDED_INDEX = "parties.proceedingsConcluded";
    public static final String COURT_ORDER_VALIDITY_DATE = "courtOrderValidityDate";

    private DefendantQueryParameterNamesConstants() {

    }
}
