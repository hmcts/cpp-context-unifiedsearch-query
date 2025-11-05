package uk.gov.moj.cpp.unifiedsearch.query.api.domain.response.indexdoc2apiresponse;

public class Offence {

    private String offenceId;
    private String offenceCode;
    private String offenceTitle;
    private String offenceLegislation;
    private boolean proceedingsConcluded;
    private String arrestDate;
    private String dateOfInformation;
    private String endDate;
    private String startDate;
    private String chargeDate;
    private String modeOfTrial;
    private int orderIndex;
    private String wording;

    public String getOffenceId() {
        return offenceId;
    }

    public Offence setOffenceId(final String offenceId) {
        this.offenceId = offenceId;
        return this;
    }

    public String getOffenceCode() {
        return offenceCode;
    }

    public Offence setOffenceCode(final String offenceCode) {
        this.offenceCode = offenceCode;
        return this;
    }

    public String getOffenceTitle() {
        return offenceTitle;
    }

    public Offence setOffenceTitle(final String offenceTitle) {
        this.offenceTitle = offenceTitle;
        return this;
    }

    public String getOffenceLegislation() {
        return offenceLegislation;
    }

    public Offence setOffenceLegislation(final String offenceLegislation) {
        this.offenceLegislation = offenceLegislation;
        return this;
    }

    public boolean isProceedingsConcluded() {
        return proceedingsConcluded;
    }

    public Offence setProceedingsConcluded(final boolean proceedingsConcluded) {
        this.proceedingsConcluded = proceedingsConcluded;
        return this;
    }

    public String getArrestDate() {
        return arrestDate;
    }

    public Offence setArrestDate(final String arrestDate) {
        this.arrestDate = arrestDate;
        return this;
    }

    public String getDateOfInformation() {
        return dateOfInformation;
    }

    public Offence setDateOfInformation(final String dateOfInformation) {
        this.dateOfInformation = dateOfInformation;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public Offence setEndDate(final String endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public Offence setStartDate(final String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getChargeDate() {
        return chargeDate;
    }

    public Offence setChargeDate(final String chargeDate) {
        this.chargeDate = chargeDate;
        return this;
    }

    public String getModeOfTrial() {
        return modeOfTrial;
    }

    public Offence setModeOfTrial(final String modeOfTrial) {
        this.modeOfTrial = modeOfTrial;
        return this;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public Offence setOrderIndex(final int orderIndex) {
        this.orderIndex = orderIndex;
        return this;
    }

    public String getWording() {
        return wording;
    }

    public Offence setWording(final String wording) {
        this.wording = wording;
        return this;
    }
}
