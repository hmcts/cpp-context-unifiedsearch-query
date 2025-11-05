package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Party {

    private String partyId;
    @JsonProperty("_party_type")
    private List<String> partyType;
    private String firstName;
    private String lastName;
    private List<Alias> aliases;
    private String dateOfBirth;
    private String asn;
    private String pncId;
    private String organisationName;
    private String oicShoulderNumber;
    private String operation;
    private List<Offence> offences;

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(final String partyId) {
        this.partyId = partyId;
    }

    @JsonProperty("_party_type")
    public List<String> getPartyType() {
        return partyType;
    }

    @JsonProperty("_party_type")
    public void setPartyType(final List<String> partyType) {
        this.partyType = partyType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public List<Alias> getAliases() {
        return aliases;
    }

    public void setAliases(final List<Alias> aliases) {
        this.aliases = aliases;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(final String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAsn() {
        return asn;
    }

    public void setAsn(final String asn) {
        this.asn = asn;
    }

    public String getPncId() {
        return pncId;
    }

    public void setPncId(final String pncId) {
        this.pncId = pncId;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(final String organisationName) {
        this.organisationName = organisationName;
    }

    public String getOicShoulderNumber() {
        return oicShoulderNumber;
    }

    public void setOicShoulderNumber(final String oicShoulderNumber) {
        this.oicShoulderNumber = oicShoulderNumber;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(final String operation) {
        this.operation = operation;
    }

    public List<Offence> getOffences() {
        return offences;
    }

    public void setOffences(final List<Offence> offences) {
        this.offences = offences;
    }

}
