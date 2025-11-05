package uk.gov.moj.cpp.unifiedsearch.query.common.domain.laa;

public class LaaReference {

    private String applicationReference;
    private String statusId;
    private String statusCode;
    private String statusDescription;

    public String getApplicationReference() {
        return applicationReference;
    }

    public LaaReference setApplicationReference(final String applicationReference) {
        this.applicationReference = applicationReference;
        return this;
    }

    public String getStatusId() {
        return statusId;
    }

    public LaaReference setStatusId(final String statusId) {
        this.statusId = statusId;
        return this;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public LaaReference setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public LaaReference setStatusDescription(final String statusDescription) {
        this.statusDescription = statusDescription;
        return this;
    }
}
