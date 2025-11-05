package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.jupiter.api.Test;

public class AddressQueryBuilderTest {

    @Test
    public void shouldReturnValidAddressQueryBuilder() {

        final AddressQueryBuilder addressQueryBuilder = new AddressQueryBuilder();

        final String address = "156 Long street Liverpool";

        final QueryBuilder actualQueryBuilder = addressQueryBuilder.getQueryBuilderBy(address);

        assertThat(actualQueryBuilder, is(notNullValue()));
        final MatchQueryBuilder matchQueryBuilder = (MatchQueryBuilder) actualQueryBuilder;
        assertThat(matchQueryBuilder.getName(), is("match"));
        assertThat(matchQueryBuilder.fieldName(), is("parties.addressLines"));
        assertThat(matchQueryBuilder.value(), is(address));
    }



}