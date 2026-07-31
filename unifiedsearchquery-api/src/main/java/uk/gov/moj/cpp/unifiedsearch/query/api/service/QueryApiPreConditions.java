package uk.gov.moj.cpp.unifiedsearch.query.api.service;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static uk.gov.moj.cpp.unifiedsearch.query.api.builder.QueryParametersValidationPredicateBuilder.hasAllParameterEqualsToEmpty;
import static uk.gov.moj.cpp.unifiedsearch.query.api.builder.QueryParametersValidationPredicateBuilder.hasMultipleSortParameters;
import static uk.gov.moj.cpp.unifiedsearch.query.api.builder.QueryParametersValidationPredicateBuilder.hasOnlyPostcodeParameter;
import static uk.gov.moj.cpp.unifiedsearch.query.api.builder.QueryParametersValidationPredicateBuilder.isValidDateRange;
import static uk.gov.moj.cpp.unifiedsearch.query.api.builder.QueryParametersValidationPredicateBuilder.isValidNumber;
import static uk.gov.moj.cpp.unifiedsearch.query.api.builder.QueryParametersValidationPredicateBuilder.isValidUUID;
import static uk.gov.moj.cpp.unifiedsearch.query.api.builder.QueryParametersValidationPredicateBuilder.missingCombinationOfTwoParameter;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.utils.DateValidator.isDateFormatValid;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.utils.EnumValidator.isValidEnum;

import uk.gov.justice.services.adapter.rest.exception.BadRequestException;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.QueryParameters;

import java.util.List;

import co.elastic.clients.elasticsearch._types.SortOrder;


public class QueryApiPreConditions {
    private static final String HEARING_DATE_FROM_IS_AFTER_HEARING_DATE_TO = "Hearing Date From: %s is after Hearing Date To: %s";
    private static final String HEARING_DATE_FROM_IS_IN_BAD_FORMAT = "Invalid Date Format for Hearing Date From: %s";
    private static final String HEARING_DATE_TO_IS_IN_BAD_FORMAT = "Invalid Date Format for Hearing Date To: %s";
    private static final String PARTY_DATE_OF_BIRTH_IS_IN_BAD_FORMAT = "Invalid Date Format for Party Date Of Birth : %s";
    private static final String COURT_ORDER_VALIDITY_DATE_IS_IN_BAD_FORMAT = "Invalid Date Format for Court Order Validity Date : %s";
    private static final String INVALID_ENUM = "Invalid %s: %s";

    private QueryApiPreConditions() {
    }

    public static void validateQueryParameters(final QueryParameters queryParameters) {
        if (hasAllParameterEqualsToEmpty().test(queryParameters)) {
            throw new BadRequestException("You must provide at least one parameter");
        }

        if (hasOnlyPostcodeParameter().test(queryParameters)) {
            throw new BadRequestException("You cannot search by postcode alone, at least one additional search parameter must be provided");
        }

        if (hasMultipleSortParameters().test(queryParameters)) {
            throw new BadRequestException("You cannot sort by more than one field");
        }

        validateUUIDParameters(queryParameters);

        validateNumberParameters(queryParameters);

        validateDateParameters(queryParameters);

        validateEnumParameters(queryParameters);
    }

    public static void validateQueryParameterForLAACaseQuery(final QueryParameters queryParameters) {

        validateQueryParameters(queryParameters);

        if (missingCombinationOfTwoParameter().test(queryParameters)) {
            throw new BadRequestException("You cannot search by defendantName,defendantDOB and dateOfNextHearing alone, please choose combination of any two among these three parameters");
        }
    }

    private static void validateUUIDParameters(final QueryParameters queryParameters) {
        if (!isValidUUID().test(queryParameters.getHearingTypeId())) {
            throw new BadRequestException(format("UUID provided for Hearing Type: %s is not valid", queryParameters.getHearingTypeId()));
        }

        if (!isValidUUID().test(queryParameters.getCourtId())) {
            throw new BadRequestException(format("UUID provided for Court Id: %s is not valid", queryParameters.getCourtId()));
        }

        if (!isValidUUID().test(queryParameters.getJurisdictionType())) {
            throw new BadRequestException(format("UUID provided for Jurisdiction Type: %s is not valid", queryParameters.getJurisdictionType()));
        }

        if (!isValidUUID().test(queryParameters.getHearingId())) {
            throw new BadRequestException(format("UUID provided for Hearing Id: %s is not valid", queryParameters.getHearingId()));
        }
    }

    private static void validateNumberParameters(final QueryParameters queryParameters) {
        if (!isValidNumber().test(queryParameters.getPageSize())) {
            throw new BadRequestException(format("Invalid page size: %s", queryParameters.getPageSize()));
        }

        if (!isValidNumber().test(queryParameters.getStartFrom())) {
            throw new BadRequestException(format("Invalid start from: %s", queryParameters.getStartFrom()));
        }
    }

    private static void validateDateParameters(final QueryParameters queryParameters) {
        if (!isDateFormatValid(queryParameters.getPartyDateOfBirth())) {
            throw new BadRequestException(format(PARTY_DATE_OF_BIRTH_IS_IN_BAD_FORMAT, queryParameters.getPartyDateOfBirth()));
        }

        if (!isDateFormatValid(queryParameters.getHearingDateFrom())) {
            throw new BadRequestException(format(HEARING_DATE_FROM_IS_IN_BAD_FORMAT, queryParameters.getHearingDateFrom()));
        }

        if (!isDateFormatValid(queryParameters.getHearingDateTo())) {
            throw new BadRequestException(format(HEARING_DATE_TO_IS_IN_BAD_FORMAT, queryParameters.getHearingDateTo()));
        }

        if (!isDateFormatValid(queryParameters.getCourtOrderValidityDate())) {
            throw new BadRequestException(format(COURT_ORDER_VALIDITY_DATE_IS_IN_BAD_FORMAT, queryParameters.getCourtOrderValidityDate()));
        }

        if (!isValidDateRange().test(queryParameters)) {
            throw new BadRequestException(format(HEARING_DATE_FROM_IS_AFTER_HEARING_DATE_TO,
                    queryParameters.getHearingDateFrom(),
                    queryParameters.getHearingDateTo()));
        }
    }

    private static void validateEnumParameters(final QueryParameters queryParameters) {
        final List<String> enumParameters = asList(queryParameters.getSortBySjpNoticeServed(), queryParameters.getSortByAppointmentDate());
        for (final String param : enumParameters) {
            if (isNotBlank(param) && !isValidEnum(SortOrder.class, param)) {
                throw new BadRequestException(format(INVALID_ENUM, SortOrder.class.getSimpleName(), param));
            }
        }
    }
}
