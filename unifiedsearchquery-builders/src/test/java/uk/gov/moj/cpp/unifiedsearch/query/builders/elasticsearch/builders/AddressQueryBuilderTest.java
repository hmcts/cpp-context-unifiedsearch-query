package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class AddressQueryBuilderTest {

    @Test
    public void shouldReturnValidAddressQueryBuilder() {

        final AddressQueryBuilder addressQueryBuilder = new AddressQueryBuilder();

        final String address = "156 Long street Liverpool";

        final Query actualQuery = addressQueryBuilder.getQueryBuilderBy(address);

        assertThat(actualQuery, is(notNullValue()));
        final MatchQuery matchQueryBuilder = actualQuery.match();
        assertThat(matchQueryBuilder.field(), is("parties.addressLines"));
        assertThat(matchQueryBuilder.query().stringValue(), is(address));
    }



}