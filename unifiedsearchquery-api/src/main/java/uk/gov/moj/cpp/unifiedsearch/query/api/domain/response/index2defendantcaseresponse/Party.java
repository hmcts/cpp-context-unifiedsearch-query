package uk.gov.moj.cpp.unifiedsearch.query.api.domain.response.index2defendantcaseresponse;


import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Party {

    private String _party_type;

    private UUID partyId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String dateOfBirth;

    private String postCode;

    private String croNumber;

    private String masterPartyId;

    private String courtProceedingsInitiated;

    private String pncId;

    private Address defendantAddress;

    public Party set_party_type(final String _party_type) {
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
    public UUID getPartyId() {
        return partyId;
    }

    @JsonProperty("partyId")
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(final String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPostCode() {
        return postCode;
    }
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCroNumber() {
        return croNumber;
    }

    public void setCroNumber(String croNumber) {
        this.croNumber = croNumber;
    }

    @JsonProperty("masterDefendantId")
    public String getMasterPartyId() {
        return masterPartyId;
    }

    @JsonProperty("masterPartyId")
    public void setMasterPartyId(String masterPartyId) {
        this.masterPartyId = masterPartyId;
    }

    public String getCourtProceedingsInitiated() {
        return courtProceedingsInitiated;
    }

    public void setCourtProceedingsInitiated(String courtProceedingsInitiated) {
        this.courtProceedingsInitiated = courtProceedingsInitiated;
    }

    public String getPncId() {
        return pncId;
    }

    public void setPncId(String pncId) {
        this.pncId = pncId;
    }

    @JsonProperty("address")
    public Address getDefendantAddress() {
        return defendantAddress;
    }

    @JsonProperty("defendantAddress")
    public void setAddress(final Address defendantAddress) {
        this.defendantAddress = defendantAddress;
    }
}
