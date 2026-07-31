package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.LAST_NAME_INDEX;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LastNameSearchQueryBuilderTest {
    private LastNameSearchQueryBuilder lastNameSearchQueryBuilder;

    @BeforeEach
    public void setUp() {
        lastNameSearchQueryBuilder = new LastNameSearchQueryBuilder();
    }

    @Test
    public void shouldCreateValidBuilderForExactLastName() {
        final String lastName = "lastName";

        final Query actualQuery = lastNameSearchQueryBuilder.getQueryBuilderBy(lastName);

        assertThat(actualQuery, is(notNullValue()));
        final TermQuery termQueryBuilder = actualQuery.term();

        assertThat(termQueryBuilder, notNullValue());

        assertThat(termQueryBuilder.field(), is(LAST_NAME_INDEX));
        assertThat(termQueryBuilder.value().stringValue(), is(lastName));
    }
}