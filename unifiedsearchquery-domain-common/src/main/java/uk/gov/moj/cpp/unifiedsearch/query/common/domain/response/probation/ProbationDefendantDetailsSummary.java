package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.probation;

import static java.util.Collections.unmodifiableList;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.HearingSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProbationDefendantDetailsSummary {

    private UUID prosecutionCaseId;
    private String prosecutionCaseReference;
    private String caseStatus;

    private List<DefendantSummary> defendantSummary = new ArrayList<>();
    private List<HearingSummary> hearingSummary = new ArrayList<>();

    public UUID getProsecutionCaseId() {
        return prosecutionCaseId;
    }

    public ProbationDefendantDetailsSummary setProsecutionCaseId(final UUID prosecutionCaseId) {
        this.prosecutionCaseId = prosecutionCaseId;
        return this;
    }

    public String getProsecutionCaseReference() {
        return prosecutionCaseReference;
    }

    public ProbationDefendantDetailsSummary setProsecutionCaseReference(final String prosecutionCaseReference) {
        this.prosecutionCaseReference = prosecutionCaseReference;
        return this;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public ProbationDefendantDetailsSummary setCaseStatus(final String caseStatus) {
        this.caseStatus = caseStatus;
        return this;
    }

    public List<DefendantSummary> getDefendantSummary() {
        return unmodifiableList(defendantSummary);
    }

    public ProbationDefendantDetailsSummary setDefendantSummary(final List<DefendantSummary> defendantSummary) {
        this.defendantSummary = unmodifiableList(defendantSummary);
        return this;
    }

    public List<HearingSummary> getHearingSummary() {
        return unmodifiableList(hearingSummary);
    }

    public ProbationDefendantDetailsSummary setHearingSummary(final List<HearingSummary> hearingSummary) {
        this.hearingSummary = unmodifiableList(hearingSummary);
        return this;
    }
}
