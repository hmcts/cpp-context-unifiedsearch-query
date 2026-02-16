package uk.gov.moj.cpp.unifiedsearch.query.common.constant;


import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;

public class CpsCaseSearchConstants {

    public static final String CPS_CASE_INDEX_NAME = "cps_case_index";
    public static final String RESULT_HIT_NODE_NAME = "cases";
    public static final String TOTAL_RESULTS_NODE_NAME = "totalResults";
    public static final String ALIAS_PARTY_TYPE = "DEFENDANT_ALIAS";
    public static final String DEFENDANT_PARTY_TYPE = "DEFENDANT";

    //Search criteria
    public static final String PARTY_FIRST_NAME = "firstName";
    public static final String PARTY_LAST_NAME = "lastName";
    public static final String ORGANISATION = "organisation";
    public static final String URN = "urn";
    public static final String CASE_STATUS = "caseStatusCode";
    public static final String CASE_TYPE = "caseType";
    public static final String PROSECUTOR = "prosecutor";
    public static final String CPS_UNIT = "cpsUnitCode";
    public static final String CJS_AREA = "cjsAreaCodes";
    public static final String CPS_AREA = "cpsAreaCode";
    public static final String OPERATION_NAME = "operationName";
    public static final String PARALEGAL_OFFICER = "paralegalOfficer";
    public static final String CROWN_ADVOCATE = "crownAdvocate";
    public static final String PNC_ID = "pncId";
    public static final String OIC_SHOULDER_NUMBER = "oicShoulderNumber";
    public static final String HEARING_DATE_TIME = "hearingDateTime";
    public static final String HEARING_TYPE = "hearingType";
    public static final String COURT_ROOM = "courtRoom";
    public static final String COURT_HOUSE = "courtHouse";
    public static final String DATE_OF_BIRTH = "dateOfBirth";
    public static final String JURISDICTION = "jurisdiction";
    public static final String OFFENCE_CODE = "offenceCode";
    public static final String WITNESS_CARE_UNIT = "witnessCareUnitCode";
    public static final String ORDER_BY = "orderBy";
    public static final String ORDER = "order";
    public static final String LINKED_CASE_ID = "linkedCaseId";
    public static final String MANUALLY_LINKED_CASE = "manuallyLinked";
    public static final String PARTY_TYPES = "partyTypes";
    public static final String PARTY_NAMES = "partyNames";

    //Query
    public static final String OPERATION_NAME_PATH_NGRAMMED = "operationName.ngrammed";
    public static final String PARTY_NESTED_PATH = "parties";
    public static final String HEARINGS_NESTED_PATH = "hearings";
    public static final String HEARING_TYPE_PATH = "hearings.hearingType";
    public static final String COURT_ROOM_PATH = "hearings.courtRoom";
    public static final String COURT_HOUSE_PATH = "hearings.courtHouse";
    public static final String JURISDICTION_PATH = "hearings.jurisdiction";
    public static final String HEARING_DATE_TIME_PATH = "hearings.hearingDateTime";
    public static final String PARTIES_OIC_SHOULDER_NUMBER_PATH = "parties.oicShoulderNumber";
    public static final String PARTIES_FIRST_NAME_PATH = "parties.firstName";
    public static final String PARTIES_FIRST_NAME_PATH_NGRAMMED = "parties.firstName.ngrammed";
    public static final String PARTIES_LAST_NAME_PATH = "parties.lastName";
    public static final String PARTIES_LAST_NAME_PATH_NGRAMMED = "parties.lastName.ngrammed";
    public static final String OFFENCES_NESTED_PATH = "parties.offences";
    public static final String PARTIES_OFFENCE_CODE_PATH = "parties.offences.offenceCode";
    public static final String PARTIES_ORGANISATION_NAME_PATH = "parties.organisationName";
    public static final String PARTIES_ORGANISATION_NAME_PATH_NGRAMMED = "parties.organisationName.ngrammed";
    public static final String MANUALLY_LINKED_CASES_PATH = "linkedCases.manuallyLinked";
    public static final String LINKED_CASES_ID_PATH = "linkedCases.linkedCaseId";
    public static final String LINKED_CASES_NESTED_PATH = "linkedCases";
    public static final String PARTIES_ALIASES_PATH = "parties.aliases";
    public static final String PARTIES_ALIASES_FIRST_NAME_PATH = "parties.aliases.firstName";
    public static final String PARTIES_ALIASES_FIRST_NAME_PATH_NGRAMMED = "parties.aliases.firstName.ngrammed";
    public static final String PARTIES_ALIASES_LAST_NAME_PATH = "parties.aliases.lastName";
    public static final String PARTIES_ALIASES_LAST_NAME_PATH_NGRAMMED = "parties.aliases.lastName.ngrammed";
    public static final String PARTY_TYPE_PATH = "parties._party_type";

    //OrderBy
    public static final String CASE_STATUS_SORT_BY = "status";
    public static final String HEARING_DATE_SORT_BY = "hearingDate";
    public static final String OFFENCE_DESCRIPTION_SORT_BY = "offenceDescription";
    public static final String DEFENDANT_LASTNAME_SORT_BY = "defendantLastName";
    public static final String PARTY_LASTNAME_OR_ORGANISATIONNAME_SORT_BY = "partyLastOrOrganisationName";
    public static final Query HEARING_DATE_NESTED_FILTER =
            Query.of(q -> q.range(r -> r
                    .untyped(u -> u
                            .field(HEARING_DATE_TIME_PATH)
                            .gte(JsonData.of("now"))
                    )
            ));

    private CpsCaseSearchConstants() {
    }
}
