package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.defendant;

import java.util.List;

/**
 * This is an exact representation of the final JSON response returned by the query API It can be
 * used to map a JSON response from the API to a POJO
 */
public class CaseSearchResponse {
    private long totalResults;
    private List<Case> cases;
    private List<Party> parties;

    public long getTotalResults() {
        return totalResults;
    }

    public CaseSearchResponse setTotalResults(final long totalResults) {
        this.totalResults = totalResults;
        return this;
    }

    public List<Case> getCases() {
        return cases;
    }

    public CaseSearchResponse setCases(final List<Case> cases) {
        this.cases = cases;
        return this;
    }

    public List<Party> getParties() {
        return parties;
    }

    public CaseSearchResponse setParties(final List<Party> parties) {
        this.parties = parties;
        return this;
    }
}
