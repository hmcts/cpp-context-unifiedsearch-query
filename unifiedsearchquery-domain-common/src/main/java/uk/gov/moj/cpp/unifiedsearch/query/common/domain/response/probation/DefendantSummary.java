package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.probation;

import static java.util.Collections.unmodifiableList;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common.Address;

import java.util.ArrayList;
import java.util.List;

public class DefendantSummary {

    private String defendantId;
    private String masterDefendantId;
    private String defendantASN;
    private String defendantFirstName;
    private String defendantMiddleName;
    private String defendantLastName;
    private String defendantDOB;
    private String defendantNINO;
    private String dateOfNextHearing;
    private boolean proceedingsConcluded;
    private List<OffenceSummary> offenceSummary = new ArrayList<>();
    private Address defendantAddress;


    public String getDefendantId() {
        return defendantId;
    }

    public DefendantSummary setDefendantId(final String defendantId) {
        this.defendantId = defendantId;
        return this;
    }

    public String getMasterDefendantId() {
        return masterDefendantId;
    }

    public void setMasterDefendantId(String masterDefendantId) {
        this.masterDefendantId = masterDefendantId;
    }

    public String getDefendantASN() {
        return defendantASN;
    }

    public DefendantSummary setDefendantASN(final String defendantASN) {
        this.defendantASN = defendantASN;
        return this;
    }

    public String getDefendantFirstName() {
        return defendantFirstName;
    }

    public void setDefendantFirstName(final String defendantFirstName) {
        this.defendantFirstName = defendantFirstName;
    }

    public String getDefendantMiddleName() {
        return defendantMiddleName;
    }

    public void setDefendantMiddleName(final String defendantMiddleName) {
        this.defendantMiddleName = defendantMiddleName;
    }

    public String getDefendantLastName() {
        return defendantLastName;
    }

    public void setDefendantLastName(final String defendantLastName) {
        this.defendantLastName = defendantLastName;
    }

    public String getDefendantDOB() {
        return defendantDOB;
    }

    public DefendantSummary setDefendantDOB(final String defendantDOB) {
        this.defendantDOB = defendantDOB;
        return this;
    }

    public String getDefendantNINO() {
        return defendantNINO;
    }

    public DefendantSummary setDefendantNINO(final String defendantNINO) {
        this.defendantNINO = defendantNINO;
        return this;
    }

    public String getDateOfNextHearing() {
        return dateOfNextHearing;
    }

    public DefendantSummary setDateOfNextHearing(final String dateOfNextHearing) {
        this.dateOfNextHearing = dateOfNextHearing;
        return this;
    }

    public boolean isProceedingsConcluded() {
        return proceedingsConcluded;
    }

    public DefendantSummary setProceedingsConcluded(final boolean proceedingsConcluded) {
        this.proceedingsConcluded = proceedingsConcluded;
        return this;
    }

    public List<OffenceSummary> getOffenceSummary() {
        return unmodifiableList(offenceSummary);
    }

    public DefendantSummary setOffenceSummary(final List<OffenceSummary> offenceSummary) {
        this.offenceSummary = unmodifiableList(offenceSummary);
        return this;
    }

    public Address getDefendantAddress() {
        return defendantAddress;
    }

    public void setDefendantAddress(final Address defendantAddress) {
        this.defendantAddress = defendantAddress;
    }
}
