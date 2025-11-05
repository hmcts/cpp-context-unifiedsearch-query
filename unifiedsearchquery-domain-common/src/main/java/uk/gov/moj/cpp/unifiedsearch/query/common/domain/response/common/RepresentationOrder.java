package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common;

public class RepresentationOrder {

    private String applicationReference;
    private String effectiveFromDate;
    private String effectiveToDate;
    private String laaContractNumber;

    public String getApplicationReference() {
        return applicationReference;
    }

    public RepresentationOrder setApplicationReference(final String applicationReference) {
        this.applicationReference = applicationReference;
        return this;
    }

    public String getEffectiveFromDate() {
        return effectiveFromDate;
    }

    public RepresentationOrder setEffectiveFromDate(final String effectiveFromDate) {
        this.effectiveFromDate = effectiveFromDate;
        return this;
    }

    public String getEffectiveToDate() {
        return effectiveToDate;
    }

    public RepresentationOrder setEffectiveToDate(final String effectiveToDate) {
        this.effectiveToDate = effectiveToDate;
        return this;
    }

    public String getLaaContractNumber() {
        return laaContractNumber;
    }

    public RepresentationOrder setLaaContractNumber(final String laaContractNumber) {
        this.laaContractNumber = laaContractNumber;
        return this;
    }
}
