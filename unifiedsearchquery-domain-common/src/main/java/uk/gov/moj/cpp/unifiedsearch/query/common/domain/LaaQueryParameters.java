package uk.gov.moj.cpp.unifiedsearch.query.common.domain;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.builders.QueryParametersBuilder;

public class LaaQueryParameters {

    private static final int LAA_DEFAULT_RESPONSE_PAGE_SIZE = 100;

    private String prosecutionCaseReference;
    private String defendantName;
    private String defendantASN;
    private String defendantNINO;
    private String defendantDOB;
    private String dateOfNextHearing;
    private String hearingId;
    private int pageSize = 100;

    public LaaQueryParameters() {
    }

    public LaaQueryParameters(final String prosecutionCaseReference, final String defendantName, final String defendantNINO,
                              final String defendantDOB, final String dateOfNextHearing, final String defendantASN,
                              final String hearingId, final int pageSize) {
        this.prosecutionCaseReference = prosecutionCaseReference;
        this.defendantName = defendantName;
        this.defendantASN = defendantASN;
        this.defendantNINO = defendantNINO;
        this.defendantDOB = defendantDOB;
        this.dateOfNextHearing = dateOfNextHearing;
        this.hearingId = hearingId;
        this.pageSize = pageSize > 0 ? pageSize : LAA_DEFAULT_RESPONSE_PAGE_SIZE;
    }


    public String getProsecutionCaseReference() {
        return prosecutionCaseReference;
    }

    public String getDefendantName() {
        return defendantName;
    }

    public String getDefendantNINO() {
        return defendantNINO;
    }

    public String getDefendantDOB() {
        return defendantDOB;
    }

    public String getDateOfNextHearing() {
        return dateOfNextHearing;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getDefendantASN() {
        return defendantASN;
    }

    public String getHearingId() {
        return hearingId;
    }

    public QueryParameters toQueryParameters() {
        return new QueryParametersBuilder()
                .withPartyName(this.defendantName)
                .withCaseReference(this.prosecutionCaseReference)
                .withPartyArrestSummonsNumber(this.defendantASN)
                .withPartyNINO(this.defendantNINO)
                .withPartyDateOfBirth(this.defendantDOB)
                .withHearingDateFrom(this.dateOfNextHearing)
                .withHearingId(hearingId)
                .withPageSize(this.pageSize)
                .withHasOffence(true)
                .build();
    }


    @Override
    public String toString() {
        return "LaaQueryParameters{" +
                "prosecutionCaseReference='" + prosecutionCaseReference + '\'' +
                ", defendantName='" + defendantName + '\'' +
                ", defendantASN='" + defendantASN + '\'' +
                ", defendantNINO='" + defendantNINO + '\'' +
                ", defendantDOB='" + defendantDOB + '\'' +
                ", dateOfNextHearing='" + dateOfNextHearing + '\'' +
                ", hearingId='" + hearingId + '\'' +
                ", pageSize='" + pageSize + '\'' +
                '}';
    }
}
