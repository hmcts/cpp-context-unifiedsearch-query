package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.defendant;


import java.util.UUID;

public class Party {

    private UUID defendantId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String dateOfBirth;

    private String postCode;

    private String croNumber;

    private String masterDefendantId;

    private String courtProceedingsInitiated;

    private String pncId;

    private Address address;

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

    public void setCroNumber(final String croNumber) {
        this.croNumber = croNumber;
    }


    public String getCourtProceedingsInitiated() {
        return courtProceedingsInitiated;
    }

    public void setCourtProceedingsInitiated(final String courtProceedingsInitiated) {
        this.courtProceedingsInitiated = courtProceedingsInitiated;
    }

    public String getPncId() {
        return pncId;
    }

    public void setPncId(final String pncId) {
        this.pncId = pncId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public UUID getDefendantId() {
        return defendantId;
    }

    public void setDefendantId(final UUID defendantId) {
        this.defendantId = defendantId;
    }

    public String getMasterDefendantId() {
        return masterDefendantId;
    }

    public void setMasterDefendantId(final String masterDefendantId) {
        this.masterDefendantId = masterDefendantId;
    }
}
