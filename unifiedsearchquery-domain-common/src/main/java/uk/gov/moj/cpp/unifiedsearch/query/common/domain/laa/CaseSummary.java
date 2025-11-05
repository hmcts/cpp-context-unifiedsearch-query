package uk.gov.moj.cpp.unifiedsearch.query.common.domain.laa;

import static java.util.Collections.unmodifiableList;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.PartyType.DEFENDANT;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CaseSummary {

    private UUID caseId;
    private String caseReference;
    private String caseStatus;

    private List<PartySummary> parties = new ArrayList<>();
    private List<HearingSummary> hearings = new ArrayList<>();

    private List<Application> applications;

    @JsonProperty("prosecutionCaseId")
    public UUID getCaseId() {
        return caseId;
    }

    @JsonProperty("caseId")
    public CaseSummary setCaseId(final UUID caseId) {
        this.caseId = caseId;
        return this;
    }

    @JsonProperty("prosecutionCaseReference")
    public String getCaseReference() {
        return caseReference;
    }

    @JsonProperty("caseReference")
    public CaseSummary setCaseReference(final String caseReference) {
        this.caseReference = caseReference;
        return this;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public CaseSummary setCaseStatus(final String caseStatus) {
        this.caseStatus = caseStatus;
        return this;
    }

    @JsonProperty("defendantSummary")
    public List<PartySummary> getParties() {
        return unmodifiableList(parties);
    }

    @JsonProperty("parties")
    public CaseSummary setParties(final List<PartySummary> parties) {
        this.parties = parties == null ? null : parties.stream().filter(party -> DEFENDANT.toString().equalsIgnoreCase(party.get_party_type())).collect(Collectors.toList());
        return this;
    }

    @JsonProperty("hearingSummary")
    public List<HearingSummary> getHearings() {
        return unmodifiableList(hearings);
    }

    @JsonProperty("hearings")
    public CaseSummary setHearings(final List<HearingSummary> hearings) {
        this.hearings = unmodifiableList(hearings);
        return this;
    }

    @JsonProperty("applicationSummary")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<Application> getApplications() {
        return applications == null ? null : unmodifiableList(applications);
    }

    @JsonProperty("applications")
    public void setApplications(final List<Application> applications) {
        this.applications = applications;
    }
}
