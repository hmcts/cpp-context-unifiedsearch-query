package uk.gov.moj.cpp.unifiedsearch.query.common.domain.laa;

import static java.util.Collections.unmodifiableList;

import java.util.List;

public class DefenceCounsel {
    private String id;
    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String status;
    private List<String> defendants;
    private List<String> attendanceDays;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public List<String> getDefendants() {
        return unmodifiableList(defendants);
    }

    public DefenceCounsel setDefendants(final List<String> defendants) {
        this.defendants = unmodifiableList(defendants);
        return this;
    }

    public List<String> getAttendanceDays() {
        return unmodifiableList(attendanceDays);
    }

    public DefenceCounsel setAttendanceDays(final List<String> attendanceDays) {
        this.attendanceDays = unmodifiableList(attendanceDays);
        return this;
    }


}
