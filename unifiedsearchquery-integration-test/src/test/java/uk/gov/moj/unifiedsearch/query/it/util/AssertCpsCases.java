package uk.gov.moj.unifiedsearch.query.it.util;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.lang.Long.valueOf;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCpsCase.assertCase;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class AssertCpsCases {

    private AssertCpsCases() {
    }

    public static void assertCases(final CaseSearchResponse caseSearchResponse, final List<CaseDocument> expectedCaseDocuments) {

        assertThat(caseSearchResponse, is(notNullValue()));

        assertThat(caseSearchResponse.getTotalResults(), lessThanOrEqualTo(valueOf(expectedCaseDocuments.size())));
        if (isNotEmpty(caseSearchResponse.getCases())) {
            assertCases(caseSearchResponse.getCases(), expectedCaseDocuments);
        }
    }

    public static void assertCases(final List<Case> actualCases, final List<CaseDocument> expectedCaseDocuments) {

        assertThat(actualCases, hasSize(expectedCaseDocuments.size()));

        for (final CaseDocument expectedCaseDocument : expectedCaseDocuments) {

            final Predicate<? super Case> sameCaseIds = aCase -> aCase.getCaseId().equals(expectedCaseDocument.getCaseId());

            final Optional<Case> actualCaseOpt = actualCases.stream().filter(sameCaseIds).findFirst();

            if (!actualCaseOpt.isPresent()) {
                fail(String.format("No case with caseID %s was found", expectedCaseDocument.getCaseId()));
            }

            assertCase(actualCaseOpt.get(), expectedCaseDocument);
        }

    }

    public static boolean hasCases(final List<Case> caseList) {
        return !caseList.isEmpty();
    }

}
