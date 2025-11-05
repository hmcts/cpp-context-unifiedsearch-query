package uk.gov.moj.cpp.unifiedsearch.query.common.domain.laa;

import static java.util.Collections.unmodifiableList;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.common.Address;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.common.CourtCentre;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.common.HearingDay;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.common.HearingType;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class HearingSummary {

    private String hearingId;
    private String jurisdictionType;

    private String hearingTypeId;
    private String hearingTypeLabel;
    private String hearingTypeCode;

    private String courtId;
    private String courtCentreName;
    private String courtCentreRoomId;
    private String courtCentreRoomName;
    private String courtCentreWelshName;
    private String courtCentreRoomWelshName;
    private Address courtCentreAddress;
    private String courtCentreCode;
    private List<String> defendantIds = new ArrayList<>();
    private List<HearingDay> hearingDays = new ArrayList<>();
    private List<DefenceCounsel> defenceCounsels = new ArrayList<>();
    private String estimatedDuration;

    public String getHearingId() {
        return hearingId;
    }

    public HearingSummary setHearingId(final String hearingId) {
        this.hearingId = hearingId;
        return this;
    }

    public String getJurisdictionType() {
        return jurisdictionType;
    }

    public HearingSummary setJurisdictionType(final String jurisdictionType) {
        this.jurisdictionType = jurisdictionType;
        return this;
    }

    public HearingSummary setHearingTypeId(final String hearingTypeId) {
        this.hearingTypeId = hearingTypeId;
        return this;
    }

    public HearingSummary setHearingTypeLabel(final String hearingTypeLabel) {
        this.hearingTypeLabel = hearingTypeLabel;
        return this;
    }

    public HearingSummary setHearingTypeCode(final String hearingTypeCode) {
        this.hearingTypeCode = hearingTypeCode;
        return this;
    }

    public HearingType getHearingType() {
        final HearingType hearingType = new HearingType();
        hearingType.setId(hearingTypeId);
        hearingType.setCode(hearingTypeCode);
        hearingType.setDescription(hearingTypeLabel);
        return hearingType;

    }

    public CourtCentre getCourtCentre() {
        final CourtCentre courtCentre = new CourtCentre();
        courtCentre.setAddress(this.courtCentreAddress);
        courtCentre.setId(courtId);
        courtCentre.setName(courtCentreName);
        courtCentre.setRoomId(courtCentreRoomId);
        courtCentre.setRoomName(courtCentreRoomName);
        courtCentre.setWelshName(courtCentreWelshName);
        courtCentre.setWelshRoomName(courtCentreRoomWelshName);
        courtCentre.setCode(courtCentreCode);
        return courtCentre;
    }

    public HearingSummary setCourtId(final String courtId) {
        this.courtId = courtId;
        return this;
    }

    public HearingSummary setCourtCentreName(final String courtCentreName) {
        this.courtCentreName = courtCentreName;
        return this;
    }

    public HearingSummary setCourtCentreRoomId(final String courtCentreRoomId) {
        this.courtCentreRoomId = courtCentreRoomId;
        return this;
    }

    public HearingSummary setCourtCentreRoomName(final String courtCentreRoomName) {
        this.courtCentreRoomName = courtCentreRoomName;
        return this;
    }

    public HearingSummary setCourtCentreWelshName(final String courtCentreWelshName) {
        this.courtCentreWelshName = courtCentreWelshName;
        return this;
    }

    public HearingSummary setCourtCentreRoomWelshName(final String courtCentreRoomWelshName) {
        this.courtCentreRoomWelshName = courtCentreRoomWelshName;
        return this;
    }

    public HearingSummary setCourtCentreAddress(final Address courtCentreAddress) {
        this.courtCentreAddress = courtCentreAddress;
        return this;
    }

    public HearingSummary setCourtCentreCode(final String courtCentreCode) {
        this.courtCentreCode = courtCentreCode;
        return this;
    }

    public List<HearingDay> getHearingDays() {
        return unmodifiableList(hearingDays);
    }

    public HearingSummary setHearingDays(final List<HearingDay> hearingDays) {
        this.hearingDays = unmodifiableList(hearingDays);
        return this;
    }


    public List<String> getDefendantIds() {
        return unmodifiableList(defendantIds);
    }

    public HearingSummary setDefendantIds(final List<String> defendantIds) {
        this.defendantIds = unmodifiableList(defendantIds);
        return this;
    }

    public String getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(final String estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    @JsonProperty("defenceCounsel")
    public List<DefenceCounsel> getDefenceCounsels() {
        return unmodifiableList(defenceCounsels);
    }

    @JsonProperty("defenceCounsels")
    public HearingSummary setDefenceCounsels(final List<DefenceCounsel> defenceCounsels) {
        this.defenceCounsels = unmodifiableList(defenceCounsels);
        return this;
    }
}
