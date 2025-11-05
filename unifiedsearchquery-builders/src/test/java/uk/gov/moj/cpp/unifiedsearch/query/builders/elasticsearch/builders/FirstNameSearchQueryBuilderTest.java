package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.FIRST_NAME_INDEX;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
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

        final QueryBuilder actualQueryBuilder = firstNameSearchQueryBuilder.getQueryBuilderBy(firstName);

        assertThat(actualQueryBuilder, is(notNullValue()));

        assertThat(actualQueryBuilder, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder termQueryBuilder = (TermQueryBuilder) actualQueryBuilder;
        assertThat(termQueryBuilder.getName(), is("term"));
        assertThat(termQueryBuilder.fieldName(), is(FIRST_NAME_INDEX));
        assertThat(termQueryBuilder.value(), is(firstName));
    }
}