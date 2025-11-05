package uk.gov.moj.unifiedsearch.query.it.util.defendantCase;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.lang.Long.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.moj.unifiedsearch.query.it.util.defendantCase.AssertDefendantCase.assertDefendantCase;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.defendant.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.defendant.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class AssertDefendantCases {

    private AssertDefendantCases() {
    }

    public static void assertDefendantCases(final CaseSearchResponse defendantCaseSearchResponse,
                                            final List<CaseDocument> expectedCaseDocuments) {

        assertThat(defendantCaseSearchResponse, is(notNullValue()));

        assertThat(defendantCaseSearchResponse.getTotalResults(), is(valueOf(expectedCaseDocuments.size())));
        assertCases(defendantCaseSearchResponse.getCases(), expectedCaseDocuments);
    }

    private static void assertCases(final List<Case> actualCases, final List<CaseDocument> expectedCaseDocuments) {

        assertThat(actualCases, hasSize(expectedCaseDocuments.size()));

        for (final CaseDocument expectedCaseDocument : expectedCaseDocuments) {

            final Predicate<? super Case> sameCaseIds = aCase -> {
                final String caseId = expectedCaseDocument.getCaseId();
                final String prosecutionCaseId = aCase.getProsecutionCaseId().toString();
                return prosecutionCaseId.equals(caseId);
            };

            final Optional<Case> actualCaseOpt = actualCases.stream().filter(sameCaseIds).findFirst();

            if (!actualCaseOpt.isPresent()) {
                fail(String.format("No case with caseID %s was found", expectedCaseDocument.getCaseId()));
            }

            assertDefendantCase(actualCaseOpt.get(), expectedCaseDocument);
        }
    }
}
