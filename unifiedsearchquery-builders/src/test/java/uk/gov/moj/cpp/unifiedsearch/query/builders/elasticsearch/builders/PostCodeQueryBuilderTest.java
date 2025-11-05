package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.jupiter.api.Test;

public class PostCodeQueryBuilderTest {

    @Test
    public void shouldReturnValidPostCodeQueryBuilder() {

        final PostCodeQueryBuilder postCodeQueryBuilder = new PostCodeQueryBuilder();

        final String postCode = "W13 7TB";
        final QueryBuilder actualQueryBuilder = postCodeQueryBuilder.getQueryBuilderBy(postCode);

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf(MatchQueryBuilder.class));

        final MatchQueryBuilder matchQueryBuilder = (MatchQueryBuilder) actualQueryBuilder;
        assertThat(matchQueryBuilder.getName(), is("match"));
        assertThat(matchQueryBuilder.fieldName(), is("parties.postCode"));
        assertThat(matchQueryBuilder.value(), is(postCode));
    }

}