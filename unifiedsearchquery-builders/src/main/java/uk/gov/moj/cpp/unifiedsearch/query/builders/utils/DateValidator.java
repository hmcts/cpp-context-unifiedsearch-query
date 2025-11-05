package uk.gov.moj.cpp.unifiedsearch.query.builders.utils;

import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static uk.gov.justice.services.common.converter.LocalDates.from;

import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DateValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateValidator.class);

    private DateValidator() {
    }

    public static boolean isDateRangeValid(final String fromDate, final String toDate) {

        if(areDateRangeParamsNotBlank(fromDate, toDate)){
            return from(fromDate).equals(from(toDate))
            || from(toDate).isAfter(from(fromDate));
        }
        return true;
    }

    public static boolean isDateFormatValid(final String date) {
        try {
            if(isNotBlank(date)){
                parse(date, ISO_LOCAL_DATE);
                return true;
            }

        } catch (final DateTimeParseException e) {
            LOGGER.warn("Invalid Date supplied: {} and exception is {}", date, e);
            return false;
        }
        return true;
    }

    private static boolean areDateRangeParamsNotBlank(final String fromDate, final String toDate){
        return isNotBlank(fromDate) && isNotBlank(toDate);
    }
}
