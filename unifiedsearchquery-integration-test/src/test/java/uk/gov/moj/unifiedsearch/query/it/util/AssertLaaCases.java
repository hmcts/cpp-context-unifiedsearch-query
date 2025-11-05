package uk.gov.moj.unifiedsearch.query.it.util;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.lang.Long.valueOf;
import static java.util.UUID.fromString;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertLaaCase.assertCase;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa.CaseSummary;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class AssertLaaCases {

    private AssertLaaCases() {
    }

    public static void assertCases(final CaseSearchResponse caseSearchResponse, final List<CaseDocument> expectedCaseDocuments) {

        assertThat(caseSearchResponse, is(notNullValue()));
        assertThat(caseSearchResponse.getTotalResults(), lessThanOrEqualTo(valueOf(expectedCaseDocuments.size())));
        if (isNotEmpty(caseSearchResponse.getCases())) {
            assertCases(caseSearchResponse.getCases(), expectedCaseDocuments);
        }
    }

    public static void assertCases(final List<CaseSummary> actualCases, final List<CaseDocument> expectedCaseDocuments) {

        assertThat(actualCases, hasSize(expectedCaseDocuments.size()));

        for (final CaseDocument expectedCaseDocument : expectedCaseDocuments) {

            final Predicate<? super CaseSummary> sameCaseIds = aCase -> aCase.getProsecutionCaseId().equals(fromString(expectedCaseDocument.getCaseId()));

            final Optional<CaseSummary> actualCaseOpt = actualCases.stream().filter(sameCaseIds).findFirst();

            if (!actualCaseOpt.isPresent()) {
                fail(String.format("No case with caseID %s was found", expectedCaseDocument.getCaseId()));
            }

            assertCase(actualCaseOpt.get(), expectedCaseDocument);
        }

    }

    public static boolean hasCases(final List<CaseSummary> caseList) {
        return !caseList.isEmpty();
    }

}
