package uk.gov.moj.cpp.unifiedsearch.query.common.domain.laa;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.RepresentationOrder;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings({"squid:S00116","squid:S00100"})
public class PartySummary {

    private String _party_type;
    private String partyId;
    private String masterPartyId;
    private String arrestSummonsNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String organisationName;
    private String dateOfBirth;
    private String nationalInsuranceNumber;
    private boolean proceedingsConcluded;
    private RepresentationOrder representationOrder;
    private List<OffenceSummary> offences = new ArrayList<>();

    public PartySummary set_party_type(final String _party_type) {
        this._party_type = _party_type;
        return this;
    }

    /**
     * Made protected so that it does not come in the json response
     */
    protected String get_party_type() {
        return _party_type;
    }

    @JsonProperty("defendantId")
    public String getPartyId() {
        return partyId;
    }

    @JsonProperty("partyId")
    public PartySummary setPartyId(final String partyId) {
        this.partyId = partyId;
        return this;
    }

    @JsonProperty("masterDefendantId")
    public String getMasterPartyId() {
        return masterPartyId;
    }

    @JsonProperty("masterPartyId")
    public void setMasterPartyId(String masterPartyId) {
        this.masterPartyId = masterPartyId;
    }

    @JsonProperty("defendantASN")
    public String getArrestSummonsNumber() {
        return arrestSummonsNumber;
    }

    @JsonProperty("arrestSummonsNumber")
    public PartySummary setArrestSummonsNumber(final String arrestSummonsNumber) {
        this.arrestSummonsNumber = arrestSummonsNumber;
        return this;
    }

    @JsonProperty("firstName")
    public PartySummary setFirstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }
    @JsonProperty("middleName")
    public PartySummary setMiddleName(final String middleName) {
        this.middleName = middleName;
        return this;
    }

    @JsonProperty("lastName")
    public PartySummary setLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public PartySummary setOrganisationName(final String organisationName) {
        this.organisationName = organisationName;
        return this;
    }

    @JsonProperty("defendantFirstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("defendantMiddleName")
    public String getMiddleName() {
        return middleName;
    }

    @JsonProperty("defendantLastName")
    public String getLastName() {
        return lastName;
    }


    @JsonProperty("defendantDOB")
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @JsonProperty("dateOfBirth")
    public PartySummary setDateOfBirth(final String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    @JsonProperty("defendantNINO")
    public String getNationalInsuranceNumber() {
        return nationalInsuranceNumber;
    }

    @JsonProperty("nationalInsuranceNumber")
    public PartySummary setNationalInsuranceNumber(final String nationalInsuranceNumber) {
        this.nationalInsuranceNumber = nationalInsuranceNumber;
        return this;
    }

    public boolean isProceedingsConcluded() {
        return proceedingsConcluded;
    }

    public PartySummary setProceedingsConcluded(final boolean proceedingsConcluded) {
        this.proceedingsConcluded = proceedingsConcluded;
        return this;
    }

    public RepresentationOrder getRepresentationOrder() {
        return representationOrder;
    }

    public PartySummary setRepresentationOrder(final RepresentationOrder representationOrder) {
        this.representationOrder = representationOrder;
        return this;
    }

    @JsonProperty("offenceSummary")
    public List<OffenceSummary> getOffences() {
        return unmodifiableList(offences);
    }

    @JsonProperty("offences")
    public PartySummary setOffences(final List<OffenceSummary> offences) {
        this.offences = unmodifiableList(offences
                .stream()
                .filter(offence -> isNotBlank(offence.getOffenceCode()))
                .collect(toList()));
        return this;
    }
}
