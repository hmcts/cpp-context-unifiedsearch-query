package uk.gov.moj.cpp.unifiedsearch.query.common.domain;

import java.util.List;

public class CaseStatus {

    private List<CaseResults> results;

    public List<CaseResults> getResults() {
        return results;
    }

    public void setResults(List<CaseResults> results) {
        this.results = results;
    }
}
