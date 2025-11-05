package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.LAST_NAME_INDEX;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
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

        final QueryBuilder actualQueryBuilder = lastNameSearchQueryBuilder.getQueryBuilderBy(lastName);

        assertThat(actualQueryBuilder, is(notNullValue()));

        assertThat(actualQueryBuilder, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder termQueryBuilder = (TermQueryBuilder) actualQueryBuilder;
        assertThat(termQueryBuilder.getName(), is("term"));
        assertThat(termQueryBuilder.fieldName(), is(LAST_NAME_INDEX));
        assertThat(termQueryBuilder.value(), is(lastName));
    }
}