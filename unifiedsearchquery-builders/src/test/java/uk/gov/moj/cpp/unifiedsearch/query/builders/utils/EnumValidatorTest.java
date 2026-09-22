package uk.gov.moj.cpp.unifiedsearch.query.builders.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import co.elastic.clients.elasticsearch._types.SortOrder;
import org.junit.jupiter.api.Test;

public class EnumValidatorTest {

    @Test
    public void shouldPassIfEnumIsValid() {
        final String key = "asc";
        final boolean isValidEnum = EnumValidator.isValidEnum(SortOrder.class, key);

        assertThat(isValidEnum, is(true));
    }

    @Test
    public void shouldFailIfEnumINotValid() {
        final String key = "unknown";
        final boolean isValidEnum = EnumValidator.isValidEnum(SortOrder.class, key);

        assertThat(isValidEnum, is(false));
    }

    @Test
    public void shouldPassIfEnumIsValidStringButPaddedWithSpace() {
        final String key = "asc ";
        final boolean isValidEnum = EnumValidator.isValidEnum(SortOrder.class, key);

        assertThat(isValidEnum, is(true));
    }

    @Test
    public void shouldPassIfEnumIsValidStringButUppercase() {
        final String key = "ASC";
        final boolean isValidEnum = EnumValidator.isValidEnum(SortOrder.class, key);

        assertThat(isValidEnum, is(true));
    }
}