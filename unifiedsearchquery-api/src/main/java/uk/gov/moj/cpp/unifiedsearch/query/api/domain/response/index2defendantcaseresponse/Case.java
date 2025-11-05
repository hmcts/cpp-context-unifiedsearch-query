package uk.gov.moj.cpp.unifiedsearch.query.api.domain.response.index2defendantcaseresponse;

import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.PartyType.DEFENDANT;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is solely used for converting the response from ElasticSearch into the API response It can
 * be used to map a response from ElasticSearch into POJO representation of the API response. The
 * only difference between this one and the one in the common module is that this one has to cater
 * for names like _is_sjp etc in the ES index. Once we implement SCUS-193, the two will just be the
 * same
 * <p>
 * Note that this one is for internal use only for converting ES response deep in the library
 */
public class Case {

    private UUID caseId;

    private String caseReference;

    private List<Party> parties;

    @JsonProperty("prosecutionCaseId")
    public UUID getCaseId() {
        return caseId;
    }

    @JsonProperty("caseId")
    public void setCaseId(final UUID caseId) {
        this.caseId = caseId;
    }

    public String getCaseReference() {
        return caseReference;
    }

    public void setCaseReference(final String caseReference) {
        this.caseReference = caseReference;
    }

    @JsonProperty("defendants")
    public List<Party> getParties() {
        return parties;
    }

    @JsonProperty("parties")
    public void setParties(final List<Party> parties) {
        this.parties = parties == null ? null : parties.stream().filter(party -> DEFENDANT.name().equalsIgnoreCase(party.get_party_type())).collect(toList());
    }
}
