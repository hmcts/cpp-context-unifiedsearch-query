package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps;


public class Hearing {

    private String hearingId;
    private String hearingDateTime;
    private String courtHouse;
    private String courtRoom;
    private String hearingType;
    private String jurisdiction;

    public String getHearingId() {
        return hearingId;
    }

    public void setHearingId(final String hearingId) {
        this.hearingId = hearingId;
    }

    public String getHearingDateTime() {
        return hearingDateTime;
    }

    public void setHearingDateTime(final String hearingDateTime) {
        this.hearingDateTime = hearingDateTime;
    }

    public String getCourtHouse() {
        return courtHouse;
    }

    public void setCourtHouse(final String courtHouse) {
        this.courtHouse = courtHouse;
    }

    public String getCourtRoom() {
        return courtRoom;
    }

    public void setCourtRoom(final String courtRoom) {
        this.courtRoom = courtRoom;
    }

    public String getHearingType() {
        return hearingType;
    }

    public void setHearingType(final String hearingType) {
        this.hearingType = hearingType;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(final String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

}
