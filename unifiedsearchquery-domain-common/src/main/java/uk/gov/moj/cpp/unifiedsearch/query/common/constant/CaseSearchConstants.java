package uk.gov.moj.cpp.unifiedsearch.query.common.constant;

import com.google.common.collect.ImmutableList;

public class CaseSearchConstants {

    public static final String CRIME_CASE_INDEX_NAME = "crime_case_index";
    public static final String RESULT_HIT_NODE_NAME = "cases";
    public static final String TOTAL_RESULTS_NODE_NAME = "totalResults";

    //Search criteria
    public static final String NAME = "partyName";
    public static final String PARTY_FIRST_NAME_AND_OR_MIDDLE_NAME = "partyFirstAndOrMiddleName";
    public static final String PARTY_LAST_NAME_OR_ORGANISATION_NAME = "partyLastNameOrOrganisationName";

    public static final String ADDRESS = "partyAddress";
    public static final String POST_CODE = "partyPostCode";
    public static final String POSTCODE = "parties.postCode";
    public static final String APPLICATION_REFERENCE_PATH = "applications.applicationReference";

    public static final String PARTY_TYPE_NESTED_PATH = "parties._party_type";

    public static final String CASE_REFERENCE = "caseReference";
    public static final String PROSECUTING_AUTHORITY = "prosecutingAuthority";
    public static final String COURT_ID = "courtId";

    public static final String PARTY_TYPE = "_party_type";

    public static final String HEARING_DATE = "hearingDate";
    public static final String HEARING_TYPE_ID = "hearingTypeId";
    public static final String HEARING_ID = "hearingId";
    public static final String IS_BOX_HEARING = "isBoxHearing";
    public static final String IS_VIRTUAL_BOX_HEARING = "isVirtualBoxHearing";
    public static final String APPLICATION_TYPE = "applicationType";
    public static final String DATE_OF_BIRTH = "dateOfBirth";
    public static final String JURISDICTION_TYPE = "jurisdictionType";
    public static final String IS_SJP= "_is_sjp";
    public static final String IS_MAGISTRATE_COURT= "_is_magistrates";
    public static final String IS_CROWN_COURT= "_is_crown";
    public static final String NINO = "nationalInsuranceNumber";
    public static final String ARREST_SUMMONS_NUMBER = "arrestSummonsNumber";
    public static final String OFFENCES_NESTED_PATH = "parties.offences";
    public static final String EXCLUDE_COMPLETED_APPLICATIONS = "excludeCompletedApplications";
    public static final String CASE_STATUS_PARAM = "caseStatus";
    public static final String INACTIVE = "INACTIVE";
    public static final String ACTIVE = "ACTIVE";

    //Query
    public static final String PARTY_NESTED_PATH = "parties";
    public static final String MATCH_PERCENTAGE = "100%";
    public static final String APPLICATIONS_NESTED_PATH = "applications";
    public static final String APPLICATIONS_STATUS_PATH = "applications.applicationStatus";
    public static final String APPLICATION_TYPE_PATH = "applications.applicationType";
    public static final String APPLICATION_STATUS = "applicationStatus";
    public static final String HEARING_NESTED_PATH = "hearings";
    public static final String HEARING_DAY_REFERENCE_PATH = "hearings.hearingDays.sittingDay";
    public static final String COURT_ORDER_START_DATE_PATH = "parties.offences.courtOrders.startDate";
    public static final String COURT_ORDER_END_DATE_PATH = "parties.offences.courtOrders.endDate";
    public static final String HEARING_DAY_COURTCENTRE_REFERENCE_PATH = "hearings.hearingDays.courtCentreId";
    public static final String HEARING_TYPE_ID_PATH = "hearings.hearingTypeId";
    public static final String IS_BOX_HEARING_PATH = "hearings.isBoxHearing";
    public static final String IS_VIRTUAL_BOX_HEARING_PATH = "hearings.isVirtualBoxHearing";
    public static final String JURISDICTION_TYPE_PATH = "hearings.jurisdictionType";
    public static final String HEARING_ID_PATH = "hearings.hearingId";

    public static final String COURT_ORDERS_NESTED_PATH = "parties.offences.courtOrders";
    public static final String PARTIES_DATE_OF_BIRTH_REFERENCE_PATH = "parties.dateOfBirth";
    public static final String PARTIES_NINO_REFERENCE_PATH = "parties.nationalInsuranceNumber";
    public static final String PARTIES_ASN_REFERENCE_PATH = "parties.arrestSummonsNumber";
    public static final ImmutableList<String> APPLICATION_STATUS_LIST = ImmutableList.of("IN_PROGRESS", "DRAFT", "UN_ALLOCATED");

    private CaseSearchConstants() {
    }
}
