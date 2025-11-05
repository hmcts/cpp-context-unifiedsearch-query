package uk.gov.moj.cpp.unifiedsearch.query.builders.utils;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.utils.DateValidator.isDateFormatValid;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.utils.DateValidator.isDateRangeValid;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.QueryParameters;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.builders.QueryParametersBuilder;

import org.junit.jupiter.api.Test;

public class DateValidatorTest {

    @Test
    public void shouldCheckIfDateToIsAfterDateFrom() {
        final QueryParameters queryParameters = new QueryParametersBuilder().withHearingDateFrom("2012-05-05")
                .withHearingDateTo("2016-05-05")
                .build();

        final boolean isValid = isDateRangeValid(queryParameters.getHearingDateFrom(), queryParameters.getHearingDateTo());

        assertTrue(isValid);
    }

    @Test
    public void shouldCheckIfDateToIsBeforeDateFrom() {
        final QueryParameters queryParameters = new QueryParametersBuilder().withHearingDateFrom("2120-05-05")
                .withHearingDateTo("2016-05-05")
                .build();

        final boolean isValid = isDateRangeValid(queryParameters.getHearingDateFrom(), queryParameters.getHearingDateTo());

        assertFalse(isValid);
    }

    @Test
    public void shouldCheckIfDateEqualsBeforeDateFrom() {
        final QueryParameters queryParameters = new QueryParametersBuilder().withHearingDateFrom("2016-05-05")
                .withHearingDateTo("2016-05-05")
                .build();

        final boolean isValid = isDateRangeValid(queryParameters.getHearingDateFrom(), queryParameters.getHearingDateTo());

        assertTrue(isValid);
    }


    @Test
    public void shouldReturnFalseIfInValidDateFormat() {
        final boolean valid = isDateFormatValid("201205-05");

        assertFalse(valid);
    }

    @Test
    public void shouldReturnFalseIfValidDateFormat() {
        final boolean valid = isDateFormatValid("2012-05-05");

        assertTrue(valid);
    }
}
