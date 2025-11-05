package uk.gov.moj.cpp.unifiedsearch.query.api.builder;

import static java.util.UUID.fromString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.utils.DateValidator.isDateRangeValid;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.QueryParameters;

import java.util.function.IntPredicate;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("squid:S1067")
public class QueryParametersValidationPredicateBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryParametersValidationPredicateBuilder.class);

    private QueryParametersValidationPredicateBuilder() {
    }

    public static Predicate<QueryParameters> hasAllParameterEqualsToEmpty() {
        return queryParameters -> isBlank(queryParameters.getCaseReference())
                && arePartyParametersEmpty(queryParameters)
                && areHearingParametersEmpty(queryParameters)
                && areDefendantSearchParametersEmpty(queryParameters)
                && isBlank(queryParameters.getProsecutingAuthority())
                && isBlank(queryParameters.getApplicationType())
                && isBlank(queryParameters.getSortBySjpNoticeServed())
                && isBlank(queryParameters.getSortByAppointmentDate())
                && isBlank(queryParameters.getCaseStatus())
                && (queryParameters.isSjp() == null)
                && (queryParameters.isMagistrateCourt() == null)
                && (queryParameters.isCrownCourt() == null)
                && (queryParameters.isBoxWorkHearing() == null)
                && (queryParameters.isBoxWorkVirtualHearing() == null);
    }

    public static Predicate<QueryParameters> hasOnlyPostcodeParameter() {
        return queryParameters -> isBlank(queryParameters.getCaseReference())
                && arePartyParametersEmptyExceptPostcode(queryParameters)
                && areHearingParametersEmpty(queryParameters)
                && isBlank(queryParameters.getProsecutingAuthority())
                && isBlank(queryParameters.getApplicationType())
                && isBlank(queryParameters.getCaseStatus())
                && (queryParameters.isSjp() == null)
                && (queryParameters.isMagistrateCourt() == null)
                && (queryParameters.isCrownCourt() == null)
                && (queryParameters.isBoxWorkHearing() == null)
                && (queryParameters.isBoxWorkVirtualHearing() == null);
    }

    public static Predicate<QueryParameters> hasMultipleSortParameters() {
        return queryParameters -> isNotBlank(queryParameters.getSortBySjpNoticeServed())
                && isNotBlank(queryParameters.getSortByAppointmentDate());
    }

    public static Predicate<QueryParameters> missingCombinationOfTwoParameter() {
        return queryParameters -> isNotBlank(queryParameters.getPartyName())
                && isBlank(queryParameters.getPartyDateOfBirth())
                && isBlank(queryParameters.getCaseReference())
                && isBlank(queryParameters.getPartyArrestSummonsNumber())
                && isBlank(queryParameters.getPartyNINO())
                && isBlank(queryParameters.getHearingDateFrom())
                ||
                isNotBlank(queryParameters.getPartyDateOfBirth())
                        && isBlank(queryParameters.getPartyName())
                        && isBlank(queryParameters.getCaseReference())
                        && isBlank(queryParameters.getPartyArrestSummonsNumber())
                        && isBlank(queryParameters.getPartyNINO())
                        && isBlank(queryParameters.getHearingDateFrom())
                ||
                isNotBlank(queryParameters.getHearingDateFrom())
                        && isBlank(queryParameters.getPartyDateOfBirth())
                        && isBlank(queryParameters.getPartyName())
                        && isBlank(queryParameters.getCaseReference())
                        && isBlank(queryParameters.getPartyArrestSummonsNumber())
                        && isBlank(queryParameters.getPartyNINO());

    }

    public static Predicate<String> isValidUUID() {
        return QueryParametersValidationPredicateBuilder::validateUUIDFromString;
    }

    public static IntPredicate isValidNumber() {
        return QueryParametersValidationPredicateBuilder::validateNumberParam;
    }

    public static Predicate<QueryParameters> isValidDateRange() {
        return queryParameters -> validateDateRange(queryParameters.getHearingDateFrom(), queryParameters.getHearingDateTo());
    }

    private static boolean areDefendantSearchParametersEmpty(final QueryParameters queryParameters) {
        return (queryParameters.isProceedingConcluded() == null)
                && (queryParameters.isCrownOrMagistrates() == null)
                && isBlank(queryParameters.getPartyLastName())
                && isBlank(queryParameters.getPartyFirstName())
                && isBlank(queryParameters.getPartyDateOfBirth())
                && isBlank(queryParameters.getPartyAddressLine1())
                && isBlank(queryParameters.getPncId())
                && isBlank(queryParameters.getCroNumber())
                && isBlank(queryParameters.getCourtOrderValidityDate());
    }

    private static boolean areHearingParametersEmpty(final QueryParameters queryParameters) {
        return isBlank(queryParameters.getHearingDateFrom())
                && isBlank(queryParameters.getHearingDateTo())
                && isBlank(queryParameters.getHearingTypeId())
                && isBlank(queryParameters.getCourtId())
                && isBlank(queryParameters.getJurisdictionType())
                && isBlank(queryParameters.getHearingId());
    }

    private static boolean arePartyParametersEmpty(final QueryParameters queryParameters) {
        return isBlank(queryParameters.getPartyName())
                && isBlank(queryParameters.getPartyFirstAndOrMiddleName())
                && isBlank(queryParameters.getPartyLastNameOrOrganisationName())
                && isBlank(queryParameters.getPartyTypes())
                && isBlank(queryParameters.getPartyDateOfBirth())
                && isBlank(queryParameters.getPartyAddress())
                && isBlank(queryParameters.getPartyPostcode())
                && isBlank(queryParameters.getPartyNINO())
                && isBlank(queryParameters.getPartyArrestSummonsNumber());
    }

    private static boolean arePartyParametersEmptyExceptPostcode(final QueryParameters queryParameters) {
        return isBlank(queryParameters.getPartyName())
                && isBlank(queryParameters.getPartyFirstAndOrMiddleName())
                && isBlank(queryParameters.getPartyLastNameOrOrganisationName())
                && isBlank(queryParameters.getPartyTypes())
                && isBlank(queryParameters.getPartyDateOfBirth())
                && isBlank(queryParameters.getPartyAddress())
                && isBlank(queryParameters.getPartyNINO())
                && isBlank(queryParameters.getPartyArrestSummonsNumber())
                && isNotBlank(queryParameters.getPartyPostcode());
    }

    private static boolean validateDateRange(final String fromDate, final String toDate) {
        return isDateRangeValid(fromDate, toDate);
    }

    private static boolean validateUUIDFromString(final String param) {
        try {
            if (isNotBlank(param)) {
                return fromString(param).toString().equals(param);
            }
        } catch (IllegalArgumentException ex) {
            LOGGER.warn("Invalid UUID supplied: {} and exception is {}", param, ex);
            return false;
        }
        return true;
    }

    private static boolean validateNumberParam(final int param) {
        return param >= 0;
    }
}
