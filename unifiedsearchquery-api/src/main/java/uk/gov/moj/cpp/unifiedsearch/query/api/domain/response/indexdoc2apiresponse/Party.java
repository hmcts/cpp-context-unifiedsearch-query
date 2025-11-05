package uk.gov.moj.cpp.unifiedsearch.query.api.domain.response.indexdoc2apiresponse;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Party {

    private UUID partyId;

    private UUID masterDefendantId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String partyType;

    private String organisationName;

    private String dateOfBirth;

    private String addressLines;

    private String postCode;

    private List<Offence> offences;

    public List<Offence> getOffences() {
        return offences;
    }
    
    public void setOffences(final List<Offence> offences) {
        this.offences = offences;
    }

    public UUID getPartyId() {
        return partyId;
    }

    public void setPartyId(final UUID partyId) {
        this.partyId = partyId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("partyType")
    public String getPartyType() {
        return partyType;
    }

    @JsonProperty("_party_type")
    public void setPartyType(final String partyType) {
        this.partyType = partyType;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(final String organisationName) {
        this.organisationName = organisationName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(final String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddressLines() {
        return addressLines;
    }

    public String getPostCode() {
        return postCode;
    }

    public UUID getMasterDefendantId() {
        return masterDefendantId;
    }

    public void setMasterDefendantId(UUID masterDefendantId) {
        this.masterDefendantId = masterDefendantId;
    }


    public void setAddressLines(String addressLines) {
        this.addressLines = addressLines;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

}
