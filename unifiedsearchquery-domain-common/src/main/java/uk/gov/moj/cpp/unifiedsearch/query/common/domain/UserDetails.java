package uk.gov.moj.cpp.unifiedsearch.query.common.domain;

import java.util.List;

public class UserDetails {

    private List<UsersAndGroupsUser> users;

    public UserDetails(final List<UsersAndGroupsUser> users) {
        this.users = users;
    }

    public List<UsersAndGroupsUser> getUsers() {
        return users;
    }
}
