package uk.gov.moj.cpp.unifiedsearch.query.api.builder;

import static uk.gov.moj.cpp.unifiedsearch.query.builders.utils.DateValidator.isDateRangeValid;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.CpsQueryParameters;

import java.util.function.Predicate;

@SuppressWarnings("squid:S1067")
public class CpsQueryParametersValidationPredicateBuilder {

    private CpsQueryParametersValidationPredicateBuilder() {
    }

    public static Predicate<CpsQueryParameters> isValidDateRange(){
        return queryParameters -> validateDateRange(queryParameters.getHearingDateFrom(), queryParameters.getHearingDateTo());
    }

    private static boolean validateDateRange(final String fromDate, final String toDate) {
        return isDateRangeValid(fromDate, toDate);
    }

}
