package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa;

public class LAAReference {

    private String applicationReference;
    private String statusId;
    private String statusCode;
    private String statusDescription;

    public String getApplicationReference() {
        return applicationReference;
    }

    public LAAReference setApplicationReference(final String applicationReference) {
        this.applicationReference = applicationReference;
        return this;
    }

    public String getStatusId() {
        return statusId;
    }

    public LAAReference setStatusId(final String statusId) {
        this.statusId = statusId;
        return this;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public LAAReference setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public LAAReference setStatusDescription(final String statusDescription) {
        this.statusDescription = statusDescription;
        return this;
    }
}
