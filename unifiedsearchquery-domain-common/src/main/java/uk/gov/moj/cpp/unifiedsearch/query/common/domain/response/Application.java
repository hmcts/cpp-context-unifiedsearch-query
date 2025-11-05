package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response;

import java.util.UUID;

public class Application {

    private UUID applicationId;

    private String applicationReference;

    private String applicationType;

    private String applicationStatus;

    private String applicationExternalCreatorType;

    private String receivedDate;

    private String decisionDate;

    private String dueDate;

    public UUID getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(final UUID applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationReference() {
        return applicationReference;
    }

    public void setApplicationReference(final String applicationReference) {
        this.applicationReference = applicationReference;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(final String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getApplicationExternalCreatorType() {
        return applicationExternalCreatorType;
    }

    public void setApplicationExternalCreatorType(final String applicationExternalCreatorType) {
        this.applicationExternalCreatorType = applicationExternalCreatorType;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(final String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(final String decisionDate) {
        this.decisionDate = decisionDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(final String dueDate) {
        this.dueDate = dueDate;
    }
}
