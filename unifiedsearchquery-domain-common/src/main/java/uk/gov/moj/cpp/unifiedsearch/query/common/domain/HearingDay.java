package uk.gov.moj.cpp.unifiedsearch.query.common.domain;

public class HearingDay {
    private String sittingDay;
    private int listingSequence;
    private int listedDurationMinutes;
    private Boolean hasSharedResults;

    public Boolean getHasSharedResults() {
        return hasSharedResults;
    }

    public void setHasSharedResults(final Boolean hasSharedResults) {
        this.hasSharedResults = hasSharedResults;
    }

    public String getSittingDay() {
        return sittingDay;
    }

    public HearingDay setSittingDay(final String sittingDay) {
        this.sittingDay = sittingDay;
        return this;
    }

    public int getListingSequence() {
        return listingSequence;
    }

    public HearingDay setListingSequence(final int listingSequence) {
        this.listingSequence = listingSequence;
        return this;
    }

    public int getListedDurationMinutes() {
        return listedDurationMinutes;
    }

    public HearingDay setListedDurationMinutes(final int listedDurationMinutes) {
        this.listedDurationMinutes = listedDurationMinutes;
        return this;
    }
}
