package uk.gov.moj.cpp.unifiedsearch.query.common.domain;


public class QueryParameters {

    private int pageSize = 10;

    private int startFrom;

    private String caseReference;

    private String partyFirstAndOrMiddleName;

    private String partyLastNameOrOrganisationName;

    private String partyName;

    private String partyTypes;

    private String partyDateOfBirth;

    private String partyAddress;

    private String partyPostcode;

    private String partyNINO;

    private String partyArrestSummonsNumber;

    private String prosecutingAuthority;

    private String hearingDateFrom;

    private String hearingDateTo;

    private String hearingTypeId;

    private Boolean sjp;

    private Boolean magistrateCourt;

    private Boolean crownCourt;

    private String sortBySjpNoticeServed;

    private String sortByAppointmentDate;

    private String courtId;

    private String applicationType;

    private Boolean boxWorkHearing;

    private String jurisdictionType;

    private String requestUserId;

    private String pncId;

    private String croNumber;

    private String partyFirstName;

    private String partyLastName;

    private Boolean proceedingConcluded;

    private String partyAddressLine1;

    private Boolean crownOrMagistrates;

    private Boolean hasOffence;

    private String courtOrderValidityDate;

    private Boolean boxWorkVirtualHearing;

    private Boolean excludeCompletedApplications;

    private String hearingId;

    private String caseStatus;

    public QueryParameters() {
    }

    @SuppressWarnings("squid:S00107")
    public QueryParameters(final int pageSize, final int startFrom, final String caseReference, final String partyName, final String partyTypes, final String partyDateOfBirth,
                           final String partyAddress, final String partyPostcode, final String prosecutingAuthority, final String hearingDateFrom, final String hearingDateTo, final String hearingTypeId,
                           final Boolean sjp, final Boolean magistrateCourt, final Boolean crownCourt, final String sortBySjpNoticeServed, final String courtId, final String applicationType,
                           final Boolean boxWorkHearing, final String jurisdictionType, final String partyNINO, final String partyArrestSummonsNumber,
                           final String partyFirstAndOrMiddleName, final String partyLastNameOrOrganisationName, final String pncId, final String croNumber,
                           final String partyFirstName, final String partyLastName, final Boolean proceedingConcluded, final String partyAddressLine1,
                           final Boolean crownOrMagistrates, final Boolean hasOffence, final String courtOrderValidityDate, final Boolean boxWorkVirtualHearing,
                           final String sortByAppointmentDate, final Boolean excludeCompletedApplications, final String hearingId, final String caseStatus) {
        this.pageSize = pageSize;
        this.startFrom = startFrom;
        this.caseReference = caseReference;
        this.partyName = partyName;
        this.partyFirstAndOrMiddleName = partyFirstAndOrMiddleName;
        this.partyLastNameOrOrganisationName = partyLastNameOrOrganisationName;
        this.partyTypes = partyTypes;
        this.partyDateOfBirth = partyDateOfBirth;
        this.partyAddress = partyAddress;
        this.partyPostcode = partyPostcode;
        this.partyNINO = partyNINO;
        this.partyArrestSummonsNumber = partyArrestSummonsNumber;
        this.prosecutingAuthority = prosecutingAuthority;
        this.hearingDateFrom = hearingDateFrom;
        this.hearingDateTo = hearingDateTo;
        this.hearingTypeId = hearingTypeId;
        this.sjp = sjp;
        this.magistrateCourt = magistrateCourt;
        this.crownCourt = crownCourt;
        this.sortBySjpNoticeServed = sortBySjpNoticeServed;
        this.courtId = courtId;
        this.applicationType = applicationType;
        this.boxWorkHearing = boxWorkHearing;
        this.jurisdictionType = jurisdictionType;
        this.pncId = pncId;
        this.croNumber = croNumber;
        this.partyFirstName = partyFirstName;
        this.partyLastName = partyLastName;
        this.proceedingConcluded = proceedingConcluded;
        this.partyAddressLine1 = partyAddressLine1;
        this.crownOrMagistrates = crownOrMagistrates;
        this.hasOffence = hasOffence;
        this.courtOrderValidityDate = courtOrderValidityDate;
        this.boxWorkVirtualHearing = boxWorkVirtualHearing;
        this.sortByAppointmentDate = sortByAppointmentDate;
        this.excludeCompletedApplications = excludeCompletedApplications;
        this.hearingId = hearingId;
        this.caseStatus = caseStatus;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getStartFrom() {
        return startFrom;
    }

    public String getCaseReference() {
        return caseReference;
    }

    public String getPartyName() {
        return partyName;
    }

    public String getPartyTypes() {
        return partyTypes;
    }

    public String getPartyDateOfBirth() {
        return partyDateOfBirth;
    }

    public String getPartyAddress() {
        return partyAddress;
    }

    public String getProsecutingAuthority() {
        return prosecutingAuthority;
    }

    public String getHearingDateFrom() {
        return hearingDateFrom;
    }

    public String getHearingDateTo() {
        return hearingDateTo;
    }

    public String getHearingTypeId() {
        return hearingTypeId;
    }

    public Boolean isSjp() {
        return sjp;
    }

    public Boolean isMagistrateCourt() {
        return magistrateCourt;
    }

    public Boolean isCrownCourt() {
        return crownCourt;
    }

    public String getSortBySjpNoticeServed() {
        return sortBySjpNoticeServed;
    }

    public String getSortByAppointmentDate() {
        return sortByAppointmentDate;
    }

    public String getCourtId() {
        return courtId;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public Boolean isBoxWorkHearing() {
        return boxWorkHearing;
    }

    public String getPartyPostcode() {
        return partyPostcode;
    }

    public String getJurisdictionType() {
        return jurisdictionType;
    }

    public String getRequestUserId() {
        return requestUserId;
    }

    public String getPartyFirstAndOrMiddleName() {
        return partyFirstAndOrMiddleName;
    }

    public QueryParameters setRequestUserId(final String requestUserId) {
        this.requestUserId = requestUserId;
        return this;
    }

    public QueryParameters setPartyTypes(final String partyTypes) {
        this.partyTypes = partyTypes;
        return this;
    }

    public String getPartyNINO() {
        return partyNINO;
    }

    public String getPartyArrestSummonsNumber() {
        return partyArrestSummonsNumber;
    }

    public String getPartyLastNameOrOrganisationName() {
        return partyLastNameOrOrganisationName;
    }

    public String getPncId() {
        return pncId;
    }

    public String getCroNumber() {
        return croNumber;
    }

    public String getPartyFirstName() {
        return partyFirstName;
    }

    public String getPartyLastName() {
        return partyLastName;
    }

    public Boolean isProceedingConcluded() {
        return proceedingConcluded;
    }

    public String getPartyAddressLine1() {
        return partyAddressLine1;
    }

    public Boolean isCrownOrMagistrates(){ return crownOrMagistrates;}

    public Boolean hasOffence() {
        return hasOffence;
    }

    public String getCourtOrderValidityDate() {
        return courtOrderValidityDate;
    }

    public Boolean isBoxWorkVirtualHearing() {
        return boxWorkVirtualHearing;
    }

    public Boolean excludeCompletedApplications() {
        return excludeCompletedApplications;
    }


    public String getHearingId() {
        return hearingId;
    }

    public String getCaseStatus() {
        return caseStatus;
    }
}
