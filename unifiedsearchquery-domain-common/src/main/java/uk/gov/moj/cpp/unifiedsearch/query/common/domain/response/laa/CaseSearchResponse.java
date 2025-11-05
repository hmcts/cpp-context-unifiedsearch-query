package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa;

import java.util.List;

public class CaseSearchResponse {

    private long totalResults;
    private List<CaseSummary> cases;

    public long getTotalResults() {
        return totalResults;
    }

    public CaseSearchResponse setTotalResults(final long totalResults) {
        this.totalResults = totalResults;
        return this;
    }

    public List<CaseSummary> getCases() {
        return cases;
    }

    public CaseSearchResponse setCases(final List<CaseSummary> cases) {
        this.cases = cases;
        return this;
    }
}
