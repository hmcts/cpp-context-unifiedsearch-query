package uk.gov.moj.cpp.unifiedsearch.query.common.domain.laa;

public class Plea {

    private String originatingHearingId;
    private String pleaDate;
    private String pleaValue;

    public String getOriginatingHearingId() {
        return originatingHearingId;
    }

    public void setOriginatingHearingId(final String originatingHearingId) {
        this.originatingHearingId = originatingHearingId;
    }

    public String getPleaDate() {
        return pleaDate;
    }

    public void setPleaDate(final String pleaDate) {
        this.pleaDate = pleaDate;
    }

    public String getPleaValue() {
        return pleaValue;
    }

    public void setPleaValue(final String pleaValue) {
        this.pleaValue = pleaValue;
    }
}
