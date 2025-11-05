package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch;

import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.ADDRESS;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATION_STATUS;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATION_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.ARREST_SUMMONS_NUMBER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.CASE_REFERENCE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.CASE_STATUS_PARAM;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.COURT_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.DATE_OF_BIRTH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_DATE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_TYPE_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_BOX_HEARING;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_VIRTUAL_BOX_HEARING;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.JURISDICTION_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.NINO;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.OFFENCES_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTY_FIRST_NAME_AND_OR_MIDDLE_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTY_LAST_NAME_OR_ORGANISATION_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTY_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.POST_CODE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PROSECUTING_AUTHORITY;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CASE_STATUS;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CASE_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CJS_AREA;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.COURT_HOUSE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.COURT_ROOM;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_AREA;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_UNIT;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CROWN_ADVOCATE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.HEARING_DATE_TIME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.JURISDICTION;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.LINKED_CASE_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OFFENCE_CODE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OIC_SHOULDER_NUMBER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OPERATION_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARALEGAL_OFFICER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_NAMES;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PROSECUTOR;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.URN;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.WITNESS_CARE_UNIT;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.ADDRESS_LINE_1;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.COURT_ORDER_VALIDITY_DATE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.CROWN_OR_MAGISTRATES;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.CRO_NUMBER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.FIRST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.LAST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PNC_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PROCEEDINGS_CONCLUDED;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.AddressQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.ApplicationStatusQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.ApplicationTypeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.ArrestSummonsNumberSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.BoxHearingQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.CourtOrderValidityDateQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.CourtSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.CroNumberSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.CrownOrMagistratesQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.DateOfBirthSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.DefendantAddressLine1QueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.FirstAndOrMiddleNameSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.FirstNameSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.HasOffenceQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.HearingDateSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.HearingIdQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.HearingTypeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.JurisdictionTypeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.LastNameSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.LastOrOrganisationNameSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.NameSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.NinoSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.PartyTypeSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.PncIdQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.PostCodeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.ProceedingsConcludedQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.ProsecutingAuthoritySearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.ReferenceSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.VirtualBoxHearingQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CaseStatusQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CaseTypeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CjsAreaQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CourtHouseQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CourtRoomQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CpsAreaQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CpsPartyNameAndTypeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CpsUnitQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CrownAdvocateQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.ExcludeAutomaticallyLinkedCasesQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.HearingDateTimeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.JurisdictionQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.OffenceCodeSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.OicShoulderNumberQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.OperationNameQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.ParalegalOfficerQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.ProsecutorSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.UrnSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.WitnessCareUnitQueryBuilder;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ElasticSearchQueryBuilderCache {

    @Inject
    private NameSearchQueryBuilder nameSearchQueryBuilder;

    @Inject
    private ReferenceSearchQueryBuilder referenceSearchQueryBuilder;

    @Inject
    private AddressQueryBuilder addressQueryBuilder;

    @Inject
    private DefendantAddressLine1QueryBuilder addressLine1QueryBuilder;

    @Inject
    private PostCodeQueryBuilder postCodeQueryBuilder;

    @Inject
    private HearingDateSearchQueryBuilder hearingDateSearchQueryBuilder;

    @Inject
    private ApplicationTypeQueryBuilder applicationTypeQueryBuilder;

    @Inject
    private ApplicationStatusQueryBuilder applicationStatusQueryBuilder;

    @Inject
    private HearingTypeQueryBuilder hearingTypeQueryBuilder;

    @Inject
    private HearingIdQueryBuilder hearingIdQueryBuilder;

    @Inject
    private PartyTypeSearchQueryBuilder partyTypeSearchQueryBuilder;

    @Inject
    private DateOfBirthSearchQueryBuilder dateOfBirthSearchQueryBuilder;

    @Inject
    private ProsecutingAuthoritySearchQueryBuilder prosecutingAuthoritySearchQueryBuilder;

    @Inject
    private CourtSearchQueryBuilder courtSearchQueryBuilder;

    @Inject
    private BoxHearingQueryBuilder boxHearingQueryBuilder;

    @Inject
    private JurisdictionTypeQueryBuilder jurisdictionTypeQueryBuilder;

    @Inject
    private NinoSearchQueryBuilder ninoSearchQueryBuilder;

    @Inject
    private ArrestSummonsNumberSearchQueryBuilder arrestSummonsNumberSearchQueryBuilder;

    @Inject
    private FirstAndOrMiddleNameSearchQueryBuilder firstAndOrMiddleNameSearchQueryBuilder;

    @Inject
    private LastOrOrganisationNameSearchQueryBuilder lastOrOrganisationNameSearchQueryBuilder;

    @Inject
    private FirstNameSearchQueryBuilder firstNameSearchQueryBuilder;

    @Inject
    private LastNameSearchQueryBuilder lastNameSearchQueryBuilder;

    @Inject
    private PncIdQueryBuilder pncIdQueryBuilder;

    @Inject
    private CroNumberSearchQueryBuilder croNumberSearchQueryBuilder;

    @Inject
    private ProceedingsConcludedQueryBuilder proceedingsConcludedQueryBuilder;

    @Inject
    private CrownOrMagistratesQueryBuilder crownOrMagistratesQueryBuilder;

    @Inject
    private HasOffenceQueryBuilder hasOffenceQueryBuilder;

    @Inject
    private CourtOrderValidityDateQueryBuilder courtOrderValidityDateQueryBuilder;

    @Inject
    private VirtualBoxHearingQueryBuilder virtualBoxHearingQueryBuilder;

    @Inject
    private JurisdictionQueryBuilder jurisdictionQueryBuilder;

    @Inject
    private CourtHouseQueryBuilder courtHouseQueryBuilder;

    @Inject
    private CourtRoomQueryBuilder courtRoomQueryBuilder;

    @Inject
    private ProsecutorSearchQueryBuilder prosecutorSearchQueryBuilder;

    @Inject
    private CaseStatusQueryBuilder caseStatusQueryBuilder;

    @Inject
    private CaseTypeQueryBuilder caseTypeQueryBuilder;

    @Inject
    private ParalegalOfficerQueryBuilder paralegalOfficerQueryBuilder;

    @Inject
    private CrownAdvocateQueryBuilder crownAdvocateQueryBuilder;

    @Inject
    private OperationNameQueryBuilder operationNameQueryBuilder;

    @Inject
    private CpsUnitQueryBuilder cpsUnitQueryBuilder;

    @Inject
    private CjsAreaQueryBuilder cjsAreaQueryBuilder;

    @Inject
    private UrnSearchQueryBuilder urnSearchQueryBuilder;

    @Inject
    private HearingDateTimeQueryBuilder hearingDateTimeQueryBuilder;

    @Inject
    private OicShoulderNumberQueryBuilder oicShoulderNumberQueryBuilder;

    @Inject
    private OffenceCodeSearchQueryBuilder offenceCodeSearchQueryBuilder;

    @Inject
    private WitnessCareUnitQueryBuilder witnessCareUnitQueryBuilder;


    @Inject
    private CpsAreaQueryBuilder cpsAreaQueryBuilder;

    @Inject
    private ExcludeAutomaticallyLinkedCasesQueryBuilder excludeAutomaticallyLinkedCasesQueryBuilder;

    @Inject
    private CpsPartyNameAndTypeQueryBuilder cpsPartyNameAndTypeQueryBuilder;

    @Inject
    private uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.CaseStatusQueryBuilder caseStatusParamQueryBuilder;


    private static final Map<String, ElasticSearchQueryBuilder> QUERY_BUILDER_CACHE = new HashMap<>();

    public ElasticSearchQueryBuilder getQueryBuilderFromCacheBy(final String queryParameterName) {
        return QUERY_BUILDER_CACHE.get(queryParameterName);
    }

    @PostConstruct
    protected void populateUnifiedSearchQueryBuilderMap() {
        QUERY_BUILDER_CACHE.put(NAME, nameSearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(CASE_REFERENCE, referenceSearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(ADDRESS, addressQueryBuilder);
        QUERY_BUILDER_CACHE.put(POST_CODE, postCodeQueryBuilder);
        QUERY_BUILDER_CACHE.put(HEARING_DATE, hearingDateSearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(APPLICATION_TYPE, applicationTypeQueryBuilder);
        QUERY_BUILDER_CACHE.put(APPLICATION_STATUS, applicationStatusQueryBuilder);
        QUERY_BUILDER_CACHE.put(HEARING_TYPE_ID, hearingTypeQueryBuilder);
        QUERY_BUILDER_CACHE.put(HEARING_ID, hearingIdQueryBuilder);
        QUERY_BUILDER_CACHE.put(PARTY_TYPE, partyTypeSearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(DATE_OF_BIRTH, dateOfBirthSearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(PROSECUTING_AUTHORITY, prosecutingAuthoritySearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(COURT_ID, courtSearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(IS_BOX_HEARING, boxHearingQueryBuilder);
        QUERY_BUILDER_CACHE.put(JURISDICTION_TYPE, jurisdictionTypeQueryBuilder);
        QUERY_BUILDER_CACHE.put(NINO, ninoSearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(ARREST_SUMMONS_NUMBER, arrestSummonsNumberSearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(PARTY_FIRST_NAME_AND_OR_MIDDLE_NAME, firstAndOrMiddleNameSearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(PARTY_LAST_NAME_OR_ORGANISATION_NAME, lastOrOrganisationNameSearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(FIRST_NAME, firstNameSearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(LAST_NAME, lastNameSearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(PNC_ID, pncIdQueryBuilder);
        QUERY_BUILDER_CACHE.put(CRO_NUMBER, croNumberSearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(ADDRESS_LINE_1, addressLine1QueryBuilder);
        QUERY_BUILDER_CACHE.put(PROCEEDINGS_CONCLUDED, proceedingsConcludedQueryBuilder);
        QUERY_BUILDER_CACHE.put(CROWN_OR_MAGISTRATES, crownOrMagistratesQueryBuilder);
        QUERY_BUILDER_CACHE.put(OFFENCES_NESTED_PATH, hasOffenceQueryBuilder);
        QUERY_BUILDER_CACHE.put(COURT_ORDER_VALIDITY_DATE, courtOrderValidityDateQueryBuilder);
        QUERY_BUILDER_CACHE.put(IS_VIRTUAL_BOX_HEARING, virtualBoxHearingQueryBuilder);
        QUERY_BUILDER_CACHE.put(CASE_STATUS_PARAM, caseStatusParamQueryBuilder);

        //CPS Search
        QUERY_BUILDER_CACHE.put(JURISDICTION, jurisdictionQueryBuilder);
        QUERY_BUILDER_CACHE.put(COURT_HOUSE, courtHouseQueryBuilder);
        QUERY_BUILDER_CACHE.put(COURT_ROOM, courtRoomQueryBuilder);
        QUERY_BUILDER_CACHE.put(PROSECUTOR, prosecutorSearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(CASE_STATUS, caseStatusQueryBuilder);
        QUERY_BUILDER_CACHE.put(CASE_TYPE, caseTypeQueryBuilder);
        QUERY_BUILDER_CACHE.put(PARALEGAL_OFFICER, paralegalOfficerQueryBuilder);
        QUERY_BUILDER_CACHE.put(OPERATION_NAME, operationNameQueryBuilder);
        QUERY_BUILDER_CACHE.put(CPS_UNIT, cpsUnitQueryBuilder);
        QUERY_BUILDER_CACHE.put(CJS_AREA, cjsAreaQueryBuilder);
        QUERY_BUILDER_CACHE.put(URN, urnSearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(HEARING_DATE_TIME, hearingDateTimeQueryBuilder);
        QUERY_BUILDER_CACHE.put(OIC_SHOULDER_NUMBER, oicShoulderNumberQueryBuilder);
        QUERY_BUILDER_CACHE.put(OFFENCE_CODE, offenceCodeSearchQueryBuilder);
        QUERY_BUILDER_CACHE.put(CROWN_ADVOCATE, crownAdvocateQueryBuilder);
        QUERY_BUILDER_CACHE.put(WITNESS_CARE_UNIT, witnessCareUnitQueryBuilder);
        QUERY_BUILDER_CACHE.put(CPS_AREA, cpsAreaQueryBuilder);
        QUERY_BUILDER_CACHE.put(LINKED_CASE_ID, excludeAutomaticallyLinkedCasesQueryBuilder);
        QUERY_BUILDER_CACHE.put(PARTY_NAMES, cpsPartyNameAndTypeQueryBuilder);
    }
}
