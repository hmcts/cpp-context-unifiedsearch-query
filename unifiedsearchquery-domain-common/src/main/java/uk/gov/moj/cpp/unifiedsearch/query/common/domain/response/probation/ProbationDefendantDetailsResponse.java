package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.probation;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

public class ProbationDefendantDetailsResponse {

    private long totalResults;
    private List<ProbationDefendantDetailsSummary> cases = new ArrayList<>();

    public long getTotalResults() {
        return totalResults;
    }

    public ProbationDefendantDetailsResponse setTotalResults(final long totalResults) {
        this.totalResults = totalResults;
        return this;
    }

    public List<ProbationDefendantDetailsSummary> getCases() {
        return unmodifiableList(cases);
    }

    public ProbationDefendantDetailsResponse setCases(final List<ProbationDefendantDetailsSummary> cases) {
        this.cases = unmodifiableList(cases);
        return this;
    }
}
