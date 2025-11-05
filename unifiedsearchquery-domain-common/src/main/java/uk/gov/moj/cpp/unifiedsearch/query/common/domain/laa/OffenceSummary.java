package uk.gov.moj.cpp.unifiedsearch.query.common.domain.laa;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OffenceSummary {

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
    private LaaReference laaReference;
    private Verdict verdict;
    private List<Plea> pleas = new ArrayList<>();

    public String getOffenceId() {
        return offenceId;
    }

    public OffenceSummary setOffenceId(final String offenceId) {
        this.offenceId = offenceId;
        return this;
    }

    public String getOffenceCode() {
        return offenceCode;
    }

    public OffenceSummary setOffenceCode(final String offenceCode) {
        this.offenceCode = offenceCode;
        return this;
    }

    public String getOffenceTitle() {
        return offenceTitle;
    }

    public OffenceSummary setOffenceTitle(final String offenceTitle) {
        this.offenceTitle = offenceTitle;
        return this;
    }

    public String getOffenceLegislation() {
        return offenceLegislation;
    }

    public OffenceSummary setOffenceLegislation(final String offenceLegislation) {
        this.offenceLegislation = offenceLegislation;
        return this;
    }

    public boolean isProceedingsConcluded() {
        return proceedingsConcluded;
    }

    public OffenceSummary setProceedingsConcluded(final boolean proceedingsConcluded) {
        this.proceedingsConcluded = proceedingsConcluded;
        return this;
    }

    public String getArrestDate() {
        return arrestDate;
    }

    public OffenceSummary setArrestDate(final String arrestDate) {
        this.arrestDate = arrestDate;
        return this;
    }

    public String getDateOfInformation() {
        return dateOfInformation;
    }

    public OffenceSummary setDateOfInformation(final String dateOfInformation) {
        this.dateOfInformation = dateOfInformation;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public OffenceSummary setEndDate(final String endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public OffenceSummary setStartDate(final String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getChargeDate() {
        return chargeDate;
    }

    public OffenceSummary setChargeDate(final String chargeDate) {
        this.chargeDate = chargeDate;
        return this;
    }

    public String getModeOfTrial() {
        return modeOfTrial;
    }

    public OffenceSummary setModeOfTrial(final String modeOfTrial) {
        this.modeOfTrial = modeOfTrial;
        return this;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public OffenceSummary setOrderIndex(final int orderIndex) {
        this.orderIndex = orderIndex;
        return this;
    }

    public String getWording() {
        return wording;
    }

    public OffenceSummary setWording(final String wording) {
        this.wording = wording;
        return this;
    }

    @JsonProperty("laaApplnReference")
    public LaaReference getLaaReference() {
        return laaReference;
    }

    @JsonProperty("laaReference")
    public OffenceSummary setLaaReference(final LaaReference laaReference) {
        this.laaReference = laaReference;
        return this;
    }

    public Verdict getVerdict() {
        return verdict;
    }

    public OffenceSummary setVerdict(final Verdict verdict) {
        this.verdict = verdict;
        return this;
    }

    @JsonProperty("plea")
    public List<Plea> getPleas() {
        return unmodifiableList(pleas);
    }

    @JsonProperty("pleas")
    public OffenceSummary setPleas(final List<Plea> pleas) {
        this.pleas = unmodifiableList(pleas);
        return this;
    }
}
