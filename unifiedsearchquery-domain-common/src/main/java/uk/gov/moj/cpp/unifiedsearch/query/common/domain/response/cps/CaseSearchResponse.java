package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps;


import java.util.List;

public class CaseSearchResponse {
    private long totalResults;
    private List<Case> cases;

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
}
