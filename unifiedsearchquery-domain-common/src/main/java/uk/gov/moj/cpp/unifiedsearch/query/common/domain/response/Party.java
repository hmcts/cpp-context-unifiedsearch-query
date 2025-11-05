package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response;

import java.util.List;
import java.util.UUID;

public class Party {

    private UUID partyId;

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

    public String getPartyType() {
        return partyType;
    }

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

    public void setAddressLines(final String addressLines) {
        this.addressLines = addressLines;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(final String postCode) {
        this.postCode = postCode;
    }
}
