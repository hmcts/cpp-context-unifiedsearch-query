package uk.gov.moj.cpp.unifiedsearch.query.api.domain.response.indexdoc2apiresponse;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is solely used for converting the response from ElasticSearch into the API response It can
 * be used to map a response from ElasticSearch into POJO representation of the API response. The
 * only difference between this one and the one in the common module is that this one has to cater
 * for names like _is_sjp etc in the ES index. Once we implement SCUS-193, the two will just be the
 * same
 *
 * Note that this one is for internal use only for converting ES response deep in the library
 */
public class Case {
    private UUID caseId;

    private String prosecutingAuthority;

    private String caseReference;

    private boolean sjp;

    private boolean magistrateCourt;

    private boolean crownCourt;

    private String sjpNoticeServed;

    private List<Party> parties;

    private List<Hearing> hearings;

    private List<Application> applications;

    private String caseStatus;

    private String caseType;

    private String sourceSystemReference;

    public UUID getCaseId() {
        return caseId;
    }

    public void setCaseId(final UUID caseId) {
        this.caseId = caseId;
    }

    public String getProsecutingAuthority() {
        return prosecutingAuthority;
    }

    public void setProsecutingAuthority(final String prosecutingAuthority) {
        this.prosecutingAuthority = prosecutingAuthority;
    }

    public String getCaseReference() {
        return caseReference;
    }

    public void setCaseReference(final String caseReference) {
        this.caseReference = caseReference;
    }

    @JsonProperty("sjp")
    public boolean isSjp() {
        return sjp;
    }

    @JsonProperty("_is_sjp")
    public void setSjp(final boolean sjp) {
        this.sjp = sjp;
    }

    @JsonProperty("magistrateCourt")
    public boolean isMagistrateCourt() {
        return magistrateCourt;
    }

    @JsonProperty("_is_magistrates")
    public void setMagistrateCourt(final boolean magistrateCourt) {
        this.magistrateCourt = magistrateCourt;
    }

    @JsonProperty("crownCourt")
    public boolean isCrownCourt() {
        return crownCourt;
    }

    @JsonProperty("_is_crown")
    public void setCrownCourt(final boolean crownCourt) {
        this.crownCourt = crownCourt;
    }

    public String getSjpNoticeServed() {
        return sjpNoticeServed;
    }

    public void setSjpNoticeServed(final String sjpNoticeServed) {
        this.sjpNoticeServed = sjpNoticeServed;
    }

    public List<Party> getParties() {
        return parties;
    }

    public void setParties(final List<Party> parties) {
        this.parties = parties;
    }

    public List<Hearing> getHearings() {
        return hearings;
    }

    public void setHearings(final List<Hearing> hearings) {
        this.hearings = hearings;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(final List<Application> applications) {
        this.applications = applications;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public Case setCaseStatus(final String caseStatus) {
        this.caseStatus = caseStatus;
        return this;
    }

    @JsonProperty("caseType")
    public String getCaseType() {
        return caseType;
    }

    @JsonProperty("_case_type")
    public Case setCaseType(final String caseType) {
        this.caseType = caseType;
        return this;
    }

    @JsonProperty("sourceSystemReference")
    public String getSourceSystemReference() {
        return sourceSystemReference;
    }

    public void setSourceSystemReference(final String sourceSystemReference) {
        this.sourceSystemReference = sourceSystemReference;
    }
}
