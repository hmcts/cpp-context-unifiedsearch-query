package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa;

import static java.util.Collections.unmodifiableList;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.laa.Application;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.HearingSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CaseSummary {

    private UUID prosecutionCaseId;
    private String prosecutionCaseReference;
    private String caseStatus;

    private List<DefendantSummary> defendantSummary = new ArrayList<>();
    private List<HearingSummary> hearingSummary = new ArrayList<>();

    private List<Application> applicationSummary;

    public UUID getProsecutionCaseId() {
        return prosecutionCaseId;
    }

    public CaseSummary setProsecutionCaseId(final UUID prosecutionCaseId) {
        this.prosecutionCaseId = prosecutionCaseId;
        return this;
    }

    public String getProsecutionCaseReference() {
        return prosecutionCaseReference;
    }

    public CaseSummary setProsecutionCaseReference(final String prosecutionCaseReference) {
        this.prosecutionCaseReference = prosecutionCaseReference;
        return this;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public CaseSummary setCaseStatus(final String caseStatus) {
        this.caseStatus = caseStatus;
        return this;
    }

    public List<DefendantSummary> getDefendantSummary() {
        return unmodifiableList(defendantSummary);
    }

    public CaseSummary setDefendantSummary(final List<DefendantSummary> defendantSummary) {
        this.defendantSummary = unmodifiableList(defendantSummary);
        return this;
    }

    public List<HearingSummary> getHearingSummary() {
        return unmodifiableList(hearingSummary);
    }

    public CaseSummary setHearingSummary(final List<HearingSummary> hearingSummary) {
        this.hearingSummary = unmodifiableList(hearingSummary);
        return this;
    }

    public List<Application> getApplicationSummary() {
        return applicationSummary == null ? null : unmodifiableList(applicationSummary);
    }

    public CaseSummary setApplicationSummary(final List<Application> applicationSummary) {
        this.applicationSummary = unmodifiableList(applicationSummary);
        return this;
    }
}
