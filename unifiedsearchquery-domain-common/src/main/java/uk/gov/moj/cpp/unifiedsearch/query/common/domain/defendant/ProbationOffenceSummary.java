package uk.gov.moj.cpp.unifiedsearch.query.common.domain.defendant;

public class ProbationOffenceSummary {

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

    public ProbationOffenceSummary setOffenceId(final String offenceId) {
        this.offenceId = offenceId;
        return this;
    }

    public String getOffenceCode() {
        return offenceCode;
    }

    public ProbationOffenceSummary setOffenceCode(final String offenceCode) {
        this.offenceCode = offenceCode;
        return this;
    }

    public String getOffenceTitle() {
        return offenceTitle;
    }

    public ProbationOffenceSummary setOffenceTitle(final String offenceTitle) {
        this.offenceTitle = offenceTitle;
        return this;
    }

    public String getOffenceLegislation() {
        return offenceLegislation;
    }

    public ProbationOffenceSummary setOffenceLegislation(final String offenceLegislation) {
        this.offenceLegislation = offenceLegislation;
        return this;
    }

    public boolean isProceedingsConcluded() {
        return proceedingsConcluded;
    }

    public ProbationOffenceSummary setProceedingsConcluded(final boolean proceedingsConcluded) {
        this.proceedingsConcluded = proceedingsConcluded;
        return this;
    }

    public String getArrestDate() {
        return arrestDate;
    }

    public ProbationOffenceSummary setArrestDate(final String arrestDate) {
        this.arrestDate = arrestDate;
        return this;
    }

    public String getDateOfInformation() {
        return dateOfInformation;
    }

    public ProbationOffenceSummary setDateOfInformation(final String dateOfInformation) {
        this.dateOfInformation = dateOfInformation;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public ProbationOffenceSummary setEndDate(final String endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public ProbationOffenceSummary setStartDate(final String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getChargeDate() {
        return chargeDate;
    }

    public ProbationOffenceSummary setChargeDate(final String chargeDate) {
        this.chargeDate = chargeDate;
        return this;
    }

    public String getModeOfTrial() {
        return modeOfTrial;
    }

    public ProbationOffenceSummary setModeOfTrial(final String modeOfTrial) {
        this.modeOfTrial = modeOfTrial;
        return this;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public ProbationOffenceSummary setOrderIndex(final int orderIndex) {
        this.orderIndex = orderIndex;
        return this;
    }

    public String getWording() {
        return wording;
    }

    public ProbationOffenceSummary setWording(final String wording) {
        this.wording = wording;
        return this;
    }
}
