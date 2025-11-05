package uk.gov.moj.cpp.unifiedsearch.query.common.domain.laa;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class Application {

    private UUID applicationId;

    private String applicationReference;

    private String applicationType;

    private String receivedDate;

    private SubjectSummary subjectSummary;

    public SubjectSummary getSubjectSummary() {
        return subjectSummary;
    }

    public void setSubjectSummary(final SubjectSummary subjectSummary) {
        this.subjectSummary = subjectSummary;
    }

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

    @JsonProperty("applicationTitle")
    public String getApplicationType() {
        return applicationType;
    }

    @JsonProperty("applicationType")
    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(final String receivedDate) {
        this.receivedDate = receivedDate;
    }

}
