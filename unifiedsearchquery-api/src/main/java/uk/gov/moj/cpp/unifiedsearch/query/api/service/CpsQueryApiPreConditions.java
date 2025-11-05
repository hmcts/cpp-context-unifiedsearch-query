package uk.gov.moj.cpp.unifiedsearch.query.api.service;

import static java.lang.String.format;
import static uk.gov.moj.cpp.unifiedsearch.query.api.builder.CpsQueryParametersValidationPredicateBuilder.isValidDateRange;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.utils.DateValidator.isDateFormatValid;

import uk.gov.justice.services.adapter.rest.exception.BadRequestException;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.CpsQueryParameters;

public class CpsQueryApiPreConditions {
    private static final String HEARING_DATE_FROM_IS_AFTER_HEARING_DATE_TO = "Hearing Date From: %s is after Hearing Date To: %s";
    private static final String HEARING_DATE_FROM_IS_IN_BAD_FORMAT = "Invalid Date Format for Hearing Date From: %s";
    private static final String HEARING_DATE_TO_IS_IN_BAD_FORMAT = "Invalid Date Format for Hearing Date To: %s";
    private static final String OFFENCE_DATE_FROM_IS_IN_BAD_FORMAT = "Invalid Date Format for Offence Date From: %s";
    private static final String OFFENCE_DATE_TO_IS_IN_BAD_FORMAT = "Invalid Date Format for Offence Date To: %s";
    private static final String PARTY_DATE_OF_BIRTH_IS_IN_BAD_FORMAT = "Invalid Date Format for Party Date Of Birth : %s";

    private CpsQueryApiPreConditions() {
    }

    public static void validateQueryParameters(final CpsQueryParameters queryParameters) {
        validateDateParameters(queryParameters);
    }


    private static void validateDateParameters(final CpsQueryParameters queryParameters) {
        if (!isDateFormatValid(queryParameters.getDateOfBirth())) {
            throw new BadRequestException(format(PARTY_DATE_OF_BIRTH_IS_IN_BAD_FORMAT, queryParameters.getDateOfBirth()));
        }

        if (!isDateFormatValid(queryParameters.getHearingDateFrom())) {
            throw new BadRequestException(format(HEARING_DATE_FROM_IS_IN_BAD_FORMAT, queryParameters.getHearingDateFrom()));
        }

        if (!isDateFormatValid(queryParameters.getHearingDateTo())) {
            throw new BadRequestException(format(HEARING_DATE_TO_IS_IN_BAD_FORMAT, queryParameters.getHearingDateTo()));
        }

        if (!isDateFormatValid(queryParameters.getOffenceDateFrom())) {
            throw new BadRequestException(format(OFFENCE_DATE_FROM_IS_IN_BAD_FORMAT, queryParameters.getOffenceDateFrom()));
        }

        if (!isDateFormatValid(queryParameters.getOffenceDateTo())) {
            throw new BadRequestException(format(OFFENCE_DATE_TO_IS_IN_BAD_FORMAT, queryParameters.getOffenceDateTo()));
        }

        if (!isValidDateRange().test(queryParameters)) {
            throw new BadRequestException(format(HEARING_DATE_FROM_IS_AFTER_HEARING_DATE_TO,
                    queryParameters.getHearingDateFrom(),
                    queryParameters.getHearingDateTo()));
        }
    }

}
