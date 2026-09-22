package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.FIRST_NAME_INDEX;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FirstNameSearchQueryBuilderTest {
    private FirstNameSearchQueryBuilder firstNameSearchQueryBuilder;

    @BeforeEach
    public void setUp() {
        firstNameSearchQueryBuilder = new FirstNameSearchQueryBuilder();
    }

    @Test
    public void shouldCreateValidBuilderForExactFirstName() {
        final String firstName = "firstName";

        final Query actualQuery = firstNameSearchQueryBuilder.getQueryBuilderBy(firstName);

        assertThat(actualQuery, is(notNullValue()));
        final TermQuery termQueryBuilder = actualQuery.term();
        assertThat(termQueryBuilder, notNullValue());

        assertThat(termQueryBuilder.field(), is(FIRST_NAME_INDEX));
        assertThat(termQueryBuilder.value().stringValue(), is(firstName));
    }
}