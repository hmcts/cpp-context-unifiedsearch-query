package uk.gov.moj.cpp.unifiedsearch.query.common.domain.builders;


import uk.gov.moj.cpp.unifiedsearch.query.common.domain.QueryParameters;

public class QueryParametersBuilder {

    private int pageSize;
    private int startFrom;
    private String caseReference;
    private String partyName;
    private String partyTypes;
    private String partyDateOfBirth;
    private String partyAddress;
    private String partyPostCode;
    private String partyNINO;
    private String partyArrestSummonsNumber;
    private String partyFirstAndOrMiddleName;
    private String partyLastNameOrOrganisationName;
    private String prosecutingAuthority;
    private String hearingDateFrom;
    private String hearingDateTo;
    private String hearingTypeId;
    private Boolean sjp;
    private Boolean magistrateCourt;
    private Boolean crownCourt;
    private String sortBySjpNoticeServed;
    private String courtId;
    private String applicationType;
    private Boolean boxWorkHearing;
    private String jurisdictionType;
    private String pncId;
    private String croNumber;
    private String partyFirstName;
    private String partyLastName;
    private Boolean proceedingsConcluded;
    private String partyAddressLine1;
    private Boolean crownOrMagistrates;
    private Boolean hasOffence;
    private String courtOrderValidityDate;
    private Boolean boxWorkVirtualHearing;
    private String sortByAppointmentDate;
    private Boolean excludeCompletedApplications;
    private String hearingId;
    private String caseStatus;

    public QueryParametersBuilder withPageSize(final int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public QueryParametersBuilder withStartFrom(final int startFrom) {
        this.startFrom = startFrom;
        return this;
    }

    public QueryParametersBuilder withCaseReference(final String caseReference) {
        this.caseReference = caseReference;
        return this;
    }

    public QueryParametersBuilder withPartyName(final String partyName) {
        this.partyName = partyName;
        return this;
    }

    public QueryParametersBuilder withPartyTypes(final String partyTypes) {
        this.partyTypes = partyTypes;
        return this;
    }

    public QueryParametersBuilder withPartyDateOfBirth(final String partyDateOfBirth) {
        this.partyDateOfBirth = partyDateOfBirth;
        return this;
    }

    public QueryParametersBuilder withPartyAddress(final String partyAddress) {
        this.partyAddress = partyAddress;
        return this;
    }

    public QueryParametersBuilder withPartyPostCode(final String partyPostCode) {
        this.partyPostCode = partyPostCode;
        return this;
    }

    public QueryParametersBuilder withProsecutingAuthority(final String prosecutingAuthority) {
        this.prosecutingAuthority = prosecutingAuthority;
        return this;
    }

    public QueryParametersBuilder withHearingDateFrom(final String hearingDateFrom) {
        this.hearingDateFrom = hearingDateFrom;
        return this;
    }

    public QueryParametersBuilder withHearingDateTo(final String hearingDateTo) {
        this.hearingDateTo = hearingDateTo;
        return this;
    }

    public QueryParametersBuilder withHearingTypeId(final String hearingTypeId) {
        this.hearingTypeId = hearingTypeId;
        return this;
    }

    public QueryParametersBuilder withSjp(final Boolean sjp) {
        this.sjp = sjp;
        return this;
    }

    public QueryParametersBuilder withMagistrateCourt(final Boolean magistrateCourt) {
        this.magistrateCourt = magistrateCourt;
        return this;
    }

    public QueryParametersBuilder withCrownCourt(final Boolean crownCourt) {
        this.crownCourt = crownCourt;
        return this;
    }

    public QueryParametersBuilder withSortBySjpNoticeServed(final String sortBySjpNoticeServed) {
        this.sortBySjpNoticeServed = sortBySjpNoticeServed;
        return this;
    }

    public QueryParametersBuilder withSortByAppointmentDate(final String sortByAppointmentDate) {
        this.sortByAppointmentDate = sortByAppointmentDate;
        return this;
    }

    public QueryParametersBuilder withCourtId(final String courtId) {
        this.courtId = courtId;
        return this;
    }

    public QueryParametersBuilder withApplicationType(final String applicationType) {
        this.applicationType = applicationType;
        return this;
    }

    public QueryParametersBuilder withBoxWorkHearing(final Boolean boxWorkHearing) {
        this.boxWorkHearing = boxWorkHearing;
        return this;
    }

    public QueryParametersBuilder withJurisdictionType(final String jurisdictionType) {
        this.jurisdictionType = jurisdictionType;
        return this;
    }

    public QueryParametersBuilder withPartyNINO(final String partyNINO) {
        this.partyNINO = partyNINO;
        return this;
    }

    public QueryParametersBuilder withPartyArrestSummonsNumber(final String partyArrestSummonsNumber) {
        this.partyArrestSummonsNumber = partyArrestSummonsNumber;
        return this;
    }

    public QueryParametersBuilder withPartyFirstAndOrMiddleName(final String partyFirstAndOrMiddleName) {
        this.partyFirstAndOrMiddleName = partyFirstAndOrMiddleName;
        return this;
    }

    public QueryParametersBuilder withPartyLastNameOrOrganisationName(final String partyLastNameOrOrganisationName) {
        this.partyLastNameOrOrganisationName = partyLastNameOrOrganisationName;
        return this;
    }


    public QueryParametersBuilder withPncId(final String pncId) {
        this.pncId = pncId;
        return this;
    }

    public QueryParametersBuilder withCroNumber(final String croNumber) {
        this.croNumber = croNumber;
        return this;
    }


    public QueryParametersBuilder withProceedingsConcluded(final Boolean proceedingsConcluded) {
        this.proceedingsConcluded = proceedingsConcluded;
        return this;
    }

    public QueryParametersBuilder withPartyFirstName(final String firstName) {
        this.partyFirstName = firstName;
        return this;
    }

    public QueryParametersBuilder withPartyLastName(final String lastName) {
        this.partyLastName = lastName;
        return this;
    }

    public QueryParametersBuilder withPartyAddressLine1(final String lastName) {
        this.partyAddressLine1 = lastName;
        return this;
    }

    public QueryParametersBuilder withCrownOrMagistrates(final Boolean crownOrMagistrates) {
        this.crownOrMagistrates = crownOrMagistrates;
        return this;
    }

    public QueryParametersBuilder withHasOffence(final boolean hasOffence) {
        this.hasOffence = hasOffence;
        return this;
    }

    public QueryParametersBuilder withCourtOrderValidityDate(final String courtOrderValidityDate) {
        this.courtOrderValidityDate = courtOrderValidityDate;
        return this;
    }

    public QueryParametersBuilder withVirtualBoxWorkHearing(final Boolean virtualBoxWorkHearing) {
        this.boxWorkVirtualHearing = virtualBoxWorkHearing;
        return this;
    }

    public QueryParametersBuilder withExcludeCompletedApplications(final Boolean excludeCompletedApplications) {
        this.excludeCompletedApplications = excludeCompletedApplications;
        return this;
    }

    public QueryParametersBuilder withHearingId(final String hearingId) {
        this.hearingId = hearingId;
        return this;
    }

    public QueryParametersBuilder withCaseStatus(final String caseStatus) {
        this.caseStatus = caseStatus;
        return this;
    }

    public QueryParameters build() {
        return new QueryParameters(pageSize, startFrom, caseReference, partyName, partyTypes, partyDateOfBirth, partyAddress, partyPostCode, prosecutingAuthority,
                hearingDateFrom, hearingDateTo, hearingTypeId, sjp, magistrateCourt, crownCourt, sortBySjpNoticeServed, courtId, applicationType,
                boxWorkHearing, jurisdictionType, partyNINO, partyArrestSummonsNumber, partyFirstAndOrMiddleName, partyLastNameOrOrganisationName,
                pncId, croNumber, partyFirstName, partyLastName, proceedingsConcluded, partyAddressLine1, crownOrMagistrates, hasOffence, courtOrderValidityDate,
                boxWorkVirtualHearing, sortByAppointmentDate, excludeCompletedApplications,hearingId,caseStatus);
    }
}
