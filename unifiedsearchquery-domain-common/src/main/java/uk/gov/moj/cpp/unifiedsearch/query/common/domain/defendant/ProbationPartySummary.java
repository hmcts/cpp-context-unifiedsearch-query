package uk.gov.moj.cpp.unifiedsearch.query.common.domain.defendant;

import static java.util.Collections.unmodifiableList;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.common.Address;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
@SuppressWarnings({"squid:S00116", "squid:S00100"})
public class ProbationPartySummary {

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
    private List<ProbationOffenceSummary> offences = new ArrayList<>();
    private Address defendantAddress;

    public ProbationPartySummary set_party_type(final String _party_type) {
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
    public ProbationPartySummary setPartyId(final String partyId) {
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
    public ProbationPartySummary setArrestSummonsNumber(final String arrestSummonsNumber) {
        this.arrestSummonsNumber = arrestSummonsNumber;
        return this;
    }

    @JsonProperty("firstName")
    public ProbationPartySummary setFirstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }
    @JsonProperty("middleName")
    public ProbationPartySummary setMiddleName(final String middleName) {
        this.middleName = middleName;
        return this;
    }

    @JsonProperty("lastName")
    public ProbationPartySummary setLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public ProbationPartySummary setOrganisationName(final String organisationName) {
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
    public ProbationPartySummary setDateOfBirth(final String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    @JsonProperty("defendantNINO")
    public String getNationalInsuranceNumber() {
        return nationalInsuranceNumber;
    }

    @JsonProperty("nationalInsuranceNumber")
    public ProbationPartySummary setNationalInsuranceNumber(final String nationalInsuranceNumber) {
        this.nationalInsuranceNumber = nationalInsuranceNumber;
        return this;
    }

    public boolean isProceedingsConcluded() {
        return proceedingsConcluded;
    }

    public ProbationPartySummary setProceedingsConcluded(final boolean proceedingsConcluded) {
        this.proceedingsConcluded = proceedingsConcluded;
        return this;
    }

    @JsonProperty("offenceSummary")
    public List<ProbationOffenceSummary> getOffences() {
        return unmodifiableList(offences);
    }

    @JsonProperty("offences")
    public ProbationPartySummary setOffences(final List<ProbationOffenceSummary> offences) {
        this.offences = unmodifiableList(offences);
        return this;
    }

    @JsonProperty("defendantAddress")
    public Address getDefendantAddress() {
        return defendantAddress;
    }

    @JsonProperty("address")
    public void setAddress(final Address defendantAddress) {
        this.defendantAddress = defendantAddress;
    }
}
