package uk.gov.moj.cpp.unifiedsearch.query.common.domain;

public class UsersAndGroupsUser {

    private String userId;
    private String firstName;
    private String lastName;

    public UsersAndGroupsUser(final String userId, final String firstName, final String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
