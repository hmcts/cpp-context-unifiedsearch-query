package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTIES_DATE_OF_BIRTH_REFERENCE_PATH;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
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

        final Query actualQuery = dateOfBirthSearchQueryBuilder.getQueryBuilderBy(dateOfBirth);

        assertThat(actualQuery, is(notNullValue()));
        final TermQuery termQueryBuilder = actualQuery.term();
        assertThat(termQueryBuilder, notNullValue());

        assertThat(termQueryBuilder.field(), is(PARTIES_DATE_OF_BIRTH_REFERENCE_PATH));
        assertThat(termQueryBuilder.value().stringValue(), is(dateOfBirth));
    }
}