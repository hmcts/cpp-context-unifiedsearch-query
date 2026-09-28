package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.junit.jupiter.api.Test;

public class PostCodeQueryBuilderTest {

    @Test
    public void shouldReturnValidPostCodeQueryBuilder() {

        final PostCodeQueryBuilder postCodeQueryBuilder = new PostCodeQueryBuilder();

        final String postCode = "W13 7TB";
        final Query actualQuery = postCodeQueryBuilder.getQueryBuilderBy(postCode);

        assertThat(actualQuery, is(notNullValue()));
        final MatchQuery matchQueryBuilder = actualQuery.match();
        assertThat(matchQueryBuilder, notNullValue());

        assertThat(matchQueryBuilder.field(), is("parties.postCode"));
        assertThat(matchQueryBuilder.query().stringValue(), is(postCode));
    }

}