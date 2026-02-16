package uk.gov.moj.cpp.unifiedsearch.query.common.domain;


import uk.gov.moj.cpp.unifiedsearch.query.common.constant.SortOrder;

public class CpsQueryParameters {

    private int pageSize = 10;
    private int startFrom;
    private String urn;
    private String firstName;
    private String lastName;
    private String organisation;
    private String dateOfBirth;
    private String caseStatusCode;
    private String partyTypes;
    private String jurisdictionTypes;
    private String cjsAreaCode;
    private String cpsUnitCode;
    private String cpsAreaCode;
    private String unitGroup;
    private String unit;
    private String hearingType;
    private String hearingDateFrom;
    private String hearingDateTo;
    private String offence;
    private String offenceDateFrom;
    private String offenceDateTo;
    private String caseType;
    private String courtHouse;
    private String courtRoom;
    private String prosecutor;
    private String paralegalOfficer;
    private String crownAdvocate;
    private String witnessCareUnitCode;
    private String witnessCareOfficer;
    private Boolean custodyTimeLimit;
    private String pncId;
    private String oicShoulderNumber;
    private String operationName;
    private String requestUserId;
    private String orderBy;
    private SortOrder order;
    private String excludeAutomaticallyLinkedCasesTo;

    public CpsQueryParameters() {
    }

    @SuppressWarnings("squid:S00107")
    public CpsQueryParameters(int pageSize, int startFrom, String urn, String firstName,
                              String lastName, String organisation, String dateOfBirth,
                              String caseStatusCode, String partyTypes, String jurisdictionTypes,
                              String cjsAreaCode, String cpsUnitCode, String cpsAreaCode, String unitGroup, String unit,
                              String hearingType, String hearingDateFrom,
                              String hearingDateTo, String offence, String offenceDateFrom,
                              String offenceDateTo, String caseType, String courtHouse,
                              String courtRoom, String prosecutor, String paralegalOfficer,
                              String crownAdvocate, String witnessCareUnitCode,
                              String witnessCareOfficer, Boolean custodyTimeLimit,
                              String pncId, String oicShoulderNumber,
                              String operationName, String orderBy, SortOrder order,
                              String excludeAutomaticallyLinkedCasesTo, String requestUserId) {
        this.pageSize = pageSize;
        this.startFrom = startFrom;
        this.urn = urn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organisation = organisation;
        this.dateOfBirth = dateOfBirth;
        this.caseStatusCode = caseStatusCode;
        this.partyTypes = partyTypes;
        this.jurisdictionTypes = jurisdictionTypes;
        this.cjsAreaCode = cjsAreaCode;
        this.cpsUnitCode = cpsUnitCode;
        this.cpsAreaCode = cpsAreaCode;
        this.unitGroup = unitGroup;
        this.unit = unit;
        this.hearingType = hearingType;
        this.hearingDateFrom = hearingDateFrom;
        this.hearingDateTo = hearingDateTo;
        this.offence = offence;
        this.offenceDateFrom = offenceDateFrom;
        this.offenceDateTo = offenceDateTo;
        this.caseType = caseType;
        this.courtHouse = courtHouse;
        this.courtRoom = courtRoom;
        this.prosecutor = prosecutor;
        this.paralegalOfficer = paralegalOfficer;
        this.crownAdvocate = crownAdvocate;
        this.witnessCareUnitCode = witnessCareUnitCode;
        this.witnessCareOfficer = witnessCareOfficer;
        this.custodyTimeLimit = custodyTimeLimit;
        this.pncId = pncId;
        this.oicShoulderNumber = oicShoulderNumber;
        this.operationName = operationName;
        this.orderBy = orderBy;
        this.order = order;
        this.excludeAutomaticallyLinkedCasesTo = excludeAutomaticallyLinkedCasesTo;
        this.requestUserId = requestUserId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getStartFrom() {
        return startFrom;
    }

    public String getUrn() {
        return urn;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getCaseStatusCode() {
        return caseStatusCode;
    }

    public String getPartyTypes() {
        return partyTypes;
    }

    public String getJurisdictionTypes() {
        return jurisdictionTypes;
    }

    public String getCjsAreaCode() {
        return cjsAreaCode;
    }

    public String getCpsUnitCode() {
        return cpsUnitCode;
    }

    public String getCpsAreaCode() {
        return cpsAreaCode;
    }

    public String getUnitGroup() {
        return unitGroup;
    }

    public String getUnit() {
        return unit;
    }

    public String getHearingType() {
        return hearingType;
    }

    public String getHearingDateFrom() {
        return hearingDateFrom;
    }

    public String getHearingDateTo() {
        return hearingDateTo;
    }

    public String getOffence() {
        return offence;
    }

    public String getOffenceDateFrom() {
        return offenceDateFrom;
    }

    public String getOffenceDateTo() {
        return offenceDateTo;
    }

    public String getCaseType() {
        return caseType;
    }

    public String getCourtHouse() {
        return courtHouse;
    }

    public String getCourtRoom() {
        return courtRoom;
    }

    public String getProsecutor() {
        return prosecutor;
    }

    public String getParalegalOfficer() {
        return paralegalOfficer;
    }

    public String getCrownAdvocate() {
        return crownAdvocate;
    }

    public String getWitnessCareUnitCode() {
        return witnessCareUnitCode;
    }

    public String getWitnessCareOfficer() {
        return witnessCareOfficer;
    }

    public Boolean getCustodyTimeLimit() {
        return custodyTimeLimit;
    }

    public String getPncId() {
        return pncId;
    }

    public String getOicShoulderNumber() {
        return oicShoulderNumber;
    }

    public String getOperationName() {
        return operationName;
    }

    public String getRequestUserId() {
        return requestUserId;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public SortOrder getOrder() {
        return order;
    }

    public String getExcludeAutomaticallyLinkedCasesTo() {
        return excludeAutomaticallyLinkedCasesTo;
    }

    public static CpsQueryParametersBuilder builder() {
        return new CpsQueryParametersBuilder();
    }

    public static final class CpsQueryParametersBuilder {

        private int pageSize = 10;
        private int startFrom;
        private String urn;
        private String firstName;
        private String lastName;
        private String organisation;
        private String dateOfBirth;
        private String caseStatusCode;
        private String partyTypes;
        private String jurisdictionTypes;
        private String cjsAreaCode;
        private String cpsUnitCode;
        private String cpsAreaCode;
        private String unitGroup;
        private String unit;
        private String hearingType;
        private String hearingDateFrom;
        private String hearingDateTo;
        private String offence;
        private String offenceDateFrom;
        private String offenceDateTo;
        private String caseType;
        private String courtHouse;
        private String courtRoom;
        private String prosecutor;
        private String paralegalOfficer;
        private String crownAdvocate;
        private String witnessCareUnitCode;
        private String witnessCareOfficer;
        private Boolean custodyTimeLimit;
        private String pncId;
        private String oicShoulderNumber;
        private String operationName;
        private String requestUserId;
        private String orderBy;
        private SortOrder order;
        private String excludeAutomaticallyLinkedCasesTo;

        public CpsQueryParametersBuilder withPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public CpsQueryParametersBuilder withStartFrom(int startFrom) {
            this.startFrom = startFrom;
            return this;
        }

        public CpsQueryParametersBuilder withUrn(String urn) {
            this.urn = urn;
            return this;
        }

        public CpsQueryParametersBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CpsQueryParametersBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CpsQueryParametersBuilder withOrganisation(String organisation) {
            this.organisation = organisation;
            return this;
        }

        public CpsQueryParametersBuilder withDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public CpsQueryParametersBuilder withCaseStatusCode(String caseStatusCode) {
            this.caseStatusCode = caseStatusCode;
            return this;
        }

        public CpsQueryParametersBuilder withPartyTypes(String partyTypes) {
            this.partyTypes = partyTypes;
            return this;
        }

        public CpsQueryParametersBuilder withJurisdictionTypes(String jurisdictionTypes) {
            this.jurisdictionTypes = jurisdictionTypes;
            return this;
        }

        public CpsQueryParametersBuilder withCjsAreaCode(String cjsAreaCode) {
            this.cjsAreaCode = cjsAreaCode;
            return this;
        }

        public CpsQueryParametersBuilder withCpsUnitCode(String cpsUnitCode) {
            this.cpsUnitCode = cpsUnitCode;
            return this;
        }

        public CpsQueryParametersBuilder withCpsAreaCode(String cpsAreaCode) {
            this.cpsAreaCode = cpsAreaCode;
            return this;
        }

        public CpsQueryParametersBuilder withUnitGroup(String unitGroup) {
            this.unitGroup = unitGroup;
            return this;
        }

        public CpsQueryParametersBuilder withUnit(String unit) {
            this.unit = unit;
            return this;
        }

        public CpsQueryParametersBuilder withHearingType(String hearingType) {
            this.hearingType = hearingType;
            return this;
        }

        public CpsQueryParametersBuilder withHearingDateFrom(String hearingDateFrom) {
            this.hearingDateFrom = hearingDateFrom;
            return this;
        }

        public CpsQueryParametersBuilder withHearingDateTo(String hearingDateTo) {
            this.hearingDateTo = hearingDateTo;
            return this;
        }

        public CpsQueryParametersBuilder withOffence(String offence) {
            this.offence = offence;
            return this;
        }

        public CpsQueryParametersBuilder withOffenceDateFrom(String offenceDateFrom) {
            this.offenceDateFrom = offenceDateFrom;
            return this;
        }

        public CpsQueryParametersBuilder withOffenceDateTo(String offenceDateTo) {
            this.offenceDateTo = offenceDateTo;
            return this;
        }

        public CpsQueryParametersBuilder withCaseType(String caseType) {
            this.caseType = caseType;
            return this;
        }

        public CpsQueryParametersBuilder withCourtHouse(String courtHouse) {
            this.courtHouse = courtHouse;
            return this;
        }

        public CpsQueryParametersBuilder withCourtRoom(String courtRoom) {
            this.courtRoom = courtRoom;
            return this;
        }

        public CpsQueryParametersBuilder withProsecutor(String prosecutor) {
            this.prosecutor = prosecutor;
            return this;
        }

        public CpsQueryParametersBuilder withParalegalOfficer(String paralegalOfficer) {
            this.paralegalOfficer = paralegalOfficer;
            return this;
        }

        public CpsQueryParametersBuilder withCrownAdvocate(String crownAdvocate) {
            this.crownAdvocate = crownAdvocate;
            return this;
        }

        public CpsQueryParametersBuilder withWitnessCareUnitCode(String witnessCareUnitCode) {
            this.witnessCareUnitCode = witnessCareUnitCode;
            return this;
        }

        public CpsQueryParametersBuilder withWitnessCareOfficer(String witnessCareOfficer) {
            this.witnessCareOfficer = witnessCareOfficer;
            return this;
        }

        public CpsQueryParametersBuilder withCustodyTimeLimit(Boolean custodyTimeLimit) {
            this.custodyTimeLimit = custodyTimeLimit;
            return this;
        }

        public CpsQueryParametersBuilder withPncId(String pncId) {
            this.pncId = pncId;
            return this;
        }

        public CpsQueryParametersBuilder withOicShoulderNumber(String oicShoulderNumber) {
            this.oicShoulderNumber = oicShoulderNumber;
            return this;
        }

        public CpsQueryParametersBuilder withOperationName(String operationName) {
            this.operationName = operationName;
            return this;
        }

        public CpsQueryParametersBuilder withOrderBy(String orderBy) {
            this.orderBy = orderBy;
            return this;
        }

        public CpsQueryParametersBuilder withOrderBy(SortOrder order) {
            this.order = order;
            return this;
        }

        public CpsQueryParametersBuilder withExcludeAutomaticallyLinkedCasesTo(String excludeAutomaticallyLinkedCasesTo) {
            this.excludeAutomaticallyLinkedCasesTo = excludeAutomaticallyLinkedCasesTo;
            return this;
        }

        public CpsQueryParametersBuilder withRequestUserId(final String requestUserId) {
            this.requestUserId = requestUserId;
            return this;
        }

        public CpsQueryParametersBuilder withValuesFrom(final CpsQueryParameters cpsQueryParameters) {
            this.pageSize = cpsQueryParameters.getPageSize();
            this.startFrom = cpsQueryParameters.getStartFrom();
            this.urn = cpsQueryParameters.getUrn();
            this.firstName = cpsQueryParameters.getFirstName();
            this.lastName = cpsQueryParameters.getLastName();
            this.organisation = cpsQueryParameters.getOrganisation();
            this.dateOfBirth = cpsQueryParameters.getDateOfBirth();
            this.caseStatusCode = cpsQueryParameters.getCaseStatusCode();
            this.partyTypes = cpsQueryParameters.getPartyTypes();
            this.jurisdictionTypes = cpsQueryParameters.getJurisdictionTypes();
            this.cjsAreaCode = cpsQueryParameters.getCjsAreaCode();
            this.cpsUnitCode = cpsQueryParameters.getCpsUnitCode();
            this.cpsAreaCode = cpsQueryParameters.getCpsAreaCode();
            this.unitGroup = cpsQueryParameters.getUnitGroup();
            this.unit = cpsQueryParameters.getUnit();
            this.hearingType = cpsQueryParameters.getHearingType();
            this.hearingDateFrom = cpsQueryParameters.getHearingDateFrom();
            this.hearingDateTo = cpsQueryParameters.getHearingDateTo();
            this.offence = cpsQueryParameters.getOffence();
            this.offenceDateFrom = cpsQueryParameters.getOffenceDateFrom();
            this.offenceDateTo = cpsQueryParameters.getOffenceDateTo();
            this.caseType = cpsQueryParameters.getCaseType();
            this.courtHouse = cpsQueryParameters.getCourtHouse();
            this.courtRoom = cpsQueryParameters.getCourtRoom();
            this.prosecutor = cpsQueryParameters.getProsecutor();
            this.paralegalOfficer = cpsQueryParameters.getParalegalOfficer();
            this.crownAdvocate = cpsQueryParameters.getCrownAdvocate();
            this.witnessCareUnitCode = cpsQueryParameters.getWitnessCareUnitCode();
            this.witnessCareOfficer = cpsQueryParameters.getWitnessCareOfficer();
            this.custodyTimeLimit = cpsQueryParameters.getCustodyTimeLimit();
            this.pncId = cpsQueryParameters.getPncId();
            this.oicShoulderNumber = cpsQueryParameters.getOicShoulderNumber();
            this.operationName = cpsQueryParameters.getOperationName();
            this.requestUserId = cpsQueryParameters.getRequestUserId();
            this.orderBy = cpsQueryParameters.getOrderBy();
            this.order = cpsQueryParameters.getOrder();
            this.excludeAutomaticallyLinkedCasesTo = cpsQueryParameters.getExcludeAutomaticallyLinkedCasesTo();
            return this;
        }

        public CpsQueryParameters build() {
            return new CpsQueryParameters(pageSize, startFrom, urn, firstName, lastName,
                    organisation, dateOfBirth, caseStatusCode, partyTypes, jurisdictionTypes, cjsAreaCode,
                    cpsUnitCode, cpsAreaCode, unitGroup, unit, hearingType, hearingDateFrom, hearingDateTo, offence,
                    offenceDateFrom, offenceDateTo, caseType, courtHouse, courtRoom, prosecutor,
                    paralegalOfficer, crownAdvocate, witnessCareUnitCode, witnessCareOfficer,
                    custodyTimeLimit, pncId, oicShoulderNumber, operationName, orderBy, order,
                    excludeAutomaticallyLinkedCasesTo, requestUserId);
        }
    }
}
