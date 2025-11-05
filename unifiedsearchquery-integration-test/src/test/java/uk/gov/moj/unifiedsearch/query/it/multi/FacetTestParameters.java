package uk.gov.moj.unifiedsearch.query.it.multi;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;

import java.util.List;
import java.util.Map;

public interface FacetTestParameters {

    void addSearchParameters(final Map<String, String> parameters);

    void addMatchData(final List<CaseDocument.Builder> caseBuilderList);

    void ensureCapacity(final List<CaseDocument.Builder> caseBuilderList);

    boolean hasExpectedHits(final List<Case> caseList);


    String getFailureMessage();



}
