package uk.gov.moj.cpp.unifiedsearch.query.common.domain.laa;

public class Verdict {

    private String originatingHearingId;
    private String verdictDate;
    private VerdictType verdictType;

    public String getOriginatingHearingId() {
        return originatingHearingId;
    }

    public Verdict setOriginatingHearingId(final String originatingHearingId) {
        this.originatingHearingId = originatingHearingId;
        return this;
    }

    public String getVerdictDate() {
        return verdictDate;
    }

    public Verdict setVerdictDate(final String verdictDate) {
        this.verdictDate = verdictDate;
        return this;
    }

    public VerdictType getVerdictType() {
        return verdictType;
    }

    public Verdict setVerdictType(final VerdictType verdictType) {
        this.verdictType = verdictType;
        return this;
    }
}
