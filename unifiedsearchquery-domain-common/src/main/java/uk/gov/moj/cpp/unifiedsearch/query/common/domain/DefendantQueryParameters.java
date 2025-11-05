package uk.gov.moj.cpp.unifiedsearch.query.common.domain;


import uk.gov.moj.cpp.unifiedsearch.query.common.domain.builders.QueryParametersBuilder;

public class DefendantQueryParameters {

    private int pageSize = 10;

    private int startFrom;

    private String pncId;

    private String croNumber;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private String addressLine1;

    private Boolean proceedingsConcluded;

    private Boolean crownOrMagistrates;

    private String courtOrderValidityDate;

    public DefendantQueryParameters() {
    }

    public DefendantQueryParameters(final int pageSize,
                                    final int startFrom,
                                    final String pncId,
                                    final String croNumber,
                                    final String firstName,
                                    final String lastName,
                                    final String dateOfBirth,
                                    final String addressLine1,
                                    final boolean proceedingsConcluded,
                                    final Boolean crownOrMagistrates,
                                    final String courtOrderValidityDate
    ) {
        this.pageSize = pageSize;
        this.startFrom = startFrom;
        this.pncId = pncId;
        this.croNumber = croNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.addressLine1 = addressLine1;
        this.proceedingsConcluded = proceedingsConcluded;
        this.crownOrMagistrates = crownOrMagistrates;
        this.courtOrderValidityDate = courtOrderValidityDate;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getStartFrom() {
        return startFrom;
    }

    public String getPncId() {
        return pncId;
    }

    public String getCroNumber() {
        return croNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public boolean isProceedingsConcluded() {
        return proceedingsConcluded;
    }

    public boolean isCrownOrMagistrates() {
        return crownOrMagistrates;
    }

    public String getCourtOrderValidityDate() {
        return courtOrderValidityDate;
    }

    public QueryParameters toQueryParameters() {
        return new QueryParametersBuilder()
                .withPageSize(this.pageSize)
                .withStartFrom(this.startFrom)
                .withPncId(this.pncId)
                .withCroNumber(this.croNumber)
                .withPartyFirstName(this.firstName)
                .withPartyLastName(this.lastName)
                .withPartyDateOfBirth(this.dateOfBirth)
                .withPartyAddressLine1(this.addressLine1)
                .withProceedingsConcluded(this.proceedingsConcluded)
                .withCrownOrMagistrates(this.crownOrMagistrates)
                .withCourtOrderValidityDate(this.courtOrderValidityDate)
                .build();
    }

    @Override
    public String toString() {
        return "DefendantQueryParameters{" +
                "pageSize=" + pageSize +
                ", startFrom=" + startFrom +
                ", pncId='" + pncId + '\'' +
                ", croNumber='" + croNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", proceedingsConcluded=" + proceedingsConcluded +
                ", crownOrMagistrates=" + crownOrMagistrates +
                ", courtOrderValidityDate='" + courtOrderValidityDate + '\'' +
                '}';
    }
}
