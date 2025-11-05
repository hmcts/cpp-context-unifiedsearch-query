package uk.gov.moj.cpp.unifiedsearch.query.common.domain.defendant;

import static java.util.Collections.unmodifiableList;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.PartyType.DEFENDANT;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProbationDefendantDetails {

    private UUID caseId;
    private String caseReference;
    private String caseStatus;

    private List<ProbationPartySummary> parties = new ArrayList<>();
    private List<HearingSummary> hearings  = new ArrayList<>();

    @JsonProperty("prosecutionCaseId")
    public UUID getCaseId() {
        return caseId;
    }

    @JsonProperty("caseId")
    public ProbationDefendantDetails setCaseId(final UUID caseId) {
        this.caseId = caseId;
        return this;
    }

    @JsonProperty("prosecutionCaseReference")
    public String getCaseReference() {
        return caseReference;
    }

    @JsonProperty("caseReference")
    public ProbationDefendantDetails setCaseReference(final String caseReference) {
        this.caseReference = caseReference;
        return this;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public ProbationDefendantDetails setCaseStatus(final String caseStatus) {
        this.caseStatus = caseStatus;
        return this;
    }

    @JsonProperty("defendantSummary")
    public List<ProbationPartySummary> getParties() {
        return unmodifiableList(parties);
    }

    @JsonProperty("parties")
    public ProbationDefendantDetails setParties(final List<ProbationPartySummary> parties) {
        this.parties = parties == null ? null : parties.stream().filter(party -> DEFENDANT.toString().equalsIgnoreCase(party.get_party_type())).collect(Collectors.toList());
        return this;
    }

    @JsonProperty("hearingSummary")
    public List<HearingSummary> getHearings() {
        return unmodifiableList(hearings);
    }

    @JsonProperty("hearings")
    public ProbationDefendantDetails setHearings(final List<HearingSummary> hearings) {
        this.hearings = unmodifiableList(hearings);
        return this;
    }
}
