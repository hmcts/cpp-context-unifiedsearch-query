package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.URN;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;

public class UrnSearchQueryBuilderTest {

    private UrnSearchQueryBuilder urnSearchQueryBuilder;

    @BeforeEach
    public void setUp() {
        urnSearchQueryBuilder = new UrnSearchQueryBuilder();
    }

    @Test
    public void shouldReturnValidQueryBuilderForCourtQueryBuilder() {
        final String urn = "3fdcf368";

        final Query actualQuery = urnSearchQueryBuilder.getQueryBuilderBy(urn);

        assertThat(actualQuery, is(notNullValue()));
        assertThat(actualQuery.bool(), notNullValue());
        final BoolQuery actualBoolQueryBuilder = actualQuery.bool();

        assertThat(actualBoolQueryBuilder.must(), hasSize(0));

        final Query builder = actualBoolQueryBuilder.filter().get(0);
        assertThat(builder.term(), notNullValue());

        final TermQuery termQueryBuilder = builder.term();
        assertThat(termQueryBuilder.field(), is(URN));
        assertThat(termQueryBuilder.value().stringValue(), is(urn));
    }
}