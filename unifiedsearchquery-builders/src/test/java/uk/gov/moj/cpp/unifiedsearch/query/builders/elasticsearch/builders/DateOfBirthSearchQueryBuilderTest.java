package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTIES_DATE_OF_BIRTH_REFERENCE_PATH;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DateOfBirthSearchQueryBuilderTest {

    private DateOfBirthSearchQueryBuilder dateOfBirthSearchQueryBuilder;

    @BeforeEach
    public void setUp() {
        dateOfBirthSearchQueryBuilder = new DateOfBirthSearchQueryBuilder();
    }

    @Test
    public void shouldReturnValidQueryBuilderForHearingDateFrom() {
        final String dateOfBirth = "1954-04-29";

        final QueryBuilder actualQueryBuilder = dateOfBirthSearchQueryBuilder.getQueryBuilderBy(dateOfBirth);

        assertThat(actualQueryBuilder, is(notNullValue()));

        assertThat(actualQueryBuilder, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder termQueryBuilder = (TermQueryBuilder) actualQueryBuilder;
        assertThat(termQueryBuilder.getName(), is("term"));
        assertThat(termQueryBuilder.fieldName(), is(PARTIES_DATE_OF_BIRTH_REFERENCE_PATH));
        assertThat(termQueryBuilder.value(), is(dateOfBirth));
    }
}