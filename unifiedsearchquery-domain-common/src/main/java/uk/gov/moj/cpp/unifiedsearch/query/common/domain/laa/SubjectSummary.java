package uk.gov.moj.cpp.unifiedsearch.query.common.domain.laa;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.common.Address;

public class SubjectSummary {
    private String subjectId;
    private String masterDefendantId;
    private String organisationName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dateOfBirth;
    private Address address;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getMasterDefendantId() {
        return masterDefendantId;
    }

    public void setMasterDefendantId(String masterDefendantId) {
        this.masterDefendantId = masterDefendantId;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SubjectSummary{" +
                "subjectId='" + subjectId + '\'' +
                ", masterDefendantId='" + masterDefendantId + '\'' +
                ", organisationName='" + organisationName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", address=" + address +
                '}';
    }
}
