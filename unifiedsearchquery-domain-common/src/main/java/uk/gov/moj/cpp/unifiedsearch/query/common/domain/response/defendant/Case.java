package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.defendant;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Case {

    private UUID prosecutionCaseId;

    private String caseReference;

    private List<Party> defendants;

    public UUID getProsecutionCaseId() {
        return prosecutionCaseId;
    }

    public void setProsecutionCaseId(final UUID prosecutionCaseId) {
        this.prosecutionCaseId = prosecutionCaseId;
    }

    public String getCaseReference() {
        return caseReference;
    }

    public void setCaseReference(final String caseReference) {
        this.caseReference = caseReference;
    }

    @JsonProperty("defendants")
    public List<Party> getDefendants() {
        return defendants;
    }

    public Case setDefendants(final List<Party> defendants) {
        this.defendants = defendants;
        return this;
    }
}
