package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common;

import static java.util.Collections.unmodifiableList;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.laa.DefenceCounsel;

import java.util.ArrayList;
import java.util.List;

public class HearingSummary {

    private String hearingId;
    private String jurisdictionType;

    private CourtCentre courtCentre;
    private HearingType hearingType;
    private List<HearingDay> hearingDays = new ArrayList<>();
    private List<String> defendantIds = new ArrayList<>();
    private String estimatedDuration;
    private List<DefenceCounsel> defenceCounsel = new ArrayList<>();

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

    public HearingType getHearingType() {
        return hearingType;
    }

    public HearingSummary setHearingType(final HearingType hearingType) {
        this.hearingType = hearingType;
        return this;
    }


    public List<HearingDay> getHearingDays() {
        return unmodifiableList(hearingDays);
    }

    public HearingSummary setHearingDays(final List<HearingDay> hearingDays) {
        this.hearingDays = unmodifiableList(hearingDays);
        return this;
    }

    public CourtCentre getCourtCentre() {
        return courtCentre;
    }

    public HearingSummary setCourtCentre(final CourtCentre courtCentre) {
        this.courtCentre = courtCentre;
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

    public List<DefenceCounsel> getDefenceCounsel() {
        return unmodifiableList(defenceCounsel);
    }

    public HearingSummary setDefenceCounsel(final List<DefenceCounsel> defenceCounsel) {
        this.defenceCounsel = unmodifiableList(defenceCounsel);
        return this;
    }
}
