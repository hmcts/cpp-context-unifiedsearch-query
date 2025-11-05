package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response;

import java.util.List;
import java.util.UUID;

/**
 * This is an exact representation of the final JSON response returned by the query API It can be
 * used to map a JSON response from the API to a POJO
 */
public class Case {

    private UUID caseId;

    private String prosecutingAuthority;

    private String caseReference;

    private boolean sjp;

    private boolean magistrateCourt;

    private boolean crownCourt;

    private String sjpNoticeServed;

    private String caseStatus;

    private String caseType;

    private List<Party> parties;

    private List<Hearing> hearings;

    private List<Application> applications;

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

    public boolean isSjp() {
        return sjp;
    }

    public void setSjp(final boolean sjp) {
        this.sjp = sjp;
    }

    public boolean isMagistrateCourt() {
        return magistrateCourt;
    }

    public void setMagistrateCourt(final boolean magistrateCourt) {
        this.magistrateCourt = magistrateCourt;
    }

    public boolean isCrownCourt() {
        return crownCourt;
    }

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

    public void setCaseStatus(final String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(final String caseType) {
        this.caseType = caseType;
    }

    public String getSourceSystemReference() {
        return sourceSystemReference;
    }

    public void setSourceSystemReference(final String sourceSystemReference) {
        this.sourceSystemReference = sourceSystemReference;
    }
}
