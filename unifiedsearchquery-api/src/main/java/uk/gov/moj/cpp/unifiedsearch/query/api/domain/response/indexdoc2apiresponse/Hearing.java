package uk.gov.moj.cpp.unifiedsearch.query.api.domain.response.indexdoc2apiresponse;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.HearingDay;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.User;

import java.util.List;

public class Hearing {
    private String hearingId;
    private String courtId;
    private String courtCentreName;
    private String hearingTypeId;
    private String hearingTypeLabel;
    private List<String> hearingDates;
    private String jurisdictionType;
    private List<String> judiciaryTypes;
    private User assignedTo;
    private boolean isBoxHearing;
    private List<HearingDay> hearingDays;
    private boolean isVirtualBoxHearing;

    public String getHearingId() {
        return hearingId;
    }

    public void setHearingId(final String hearingId) {
        this.hearingId = hearingId;
    }

    public String getCourtId() {
        return courtId;
    }

    public void setCourtId(final String courtId) {
        this.courtId = courtId;
    }

    public String getCourtCentreName() {
        return courtCentreName;
    }

    public void setCourtCentreName(final String courtCentreName) {
        this.courtCentreName = courtCentreName;
    }

    public String getHearingTypeId() {
        return hearingTypeId;
    }

    public void setHearingTypeId(final String hearingTypeId) {
        this.hearingTypeId = hearingTypeId;
    }

    public String getHearingTypeLabel() {
        return hearingTypeLabel;
    }

    public void setHearingTypeLabel(String hearingTypeLabel) {
        this.hearingTypeLabel = hearingTypeLabel;
    }

    public List<String> getHearingDates() {
        return hearingDates;
    }

    public void setHearingDates(final List<String> hearingDates) {
        this.hearingDates = hearingDates;
    }

    public String getJurisdictionType() {
        return jurisdictionType;
    }

    public void setJurisdictionType(final String jurisdictionType) {
        this.jurisdictionType = jurisdictionType;
    }

    public List<String> getJudiciaryTypes() {
        return judiciaryTypes;
    }

    public void setJudiciaryTypes(final List<String> judiciaryTypes) {
        this.judiciaryTypes = judiciaryTypes;
    }

    public boolean isIsBoxHearing() {
        return isBoxHearing;
    }

    public void setIsIsBoxHearing(final boolean boxHearing) {
        isBoxHearing = boxHearing;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(final User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public List<HearingDay> getHearingDays() {
        return hearingDays;
    }

    public void setHearingDays(final List<HearingDay> hearingDays) {
        this.hearingDays = hearingDays;
    }

    public boolean isIsVirtualBoxHearing() {
        return isVirtualBoxHearing;
    }

    public void setIsIsVirtualBoxHearing(final boolean virtualBoxHearing) {
        isVirtualBoxHearing = virtualBoxHearing;
    }
}
