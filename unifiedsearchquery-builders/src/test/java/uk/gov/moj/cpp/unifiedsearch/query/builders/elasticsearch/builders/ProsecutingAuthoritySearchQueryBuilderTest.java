package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProsecutingAuthoritySearchQueryBuilderTest {

    private ProsecutingAuthoritySearchQueryBuilder prosecutingAuthoritySearchQueryBuilder;

    @BeforeEach
    public void setUp() {
        prosecutingAuthoritySearchQueryBuilder = new ProsecutingAuthoritySearchQueryBuilder();
    }

    @Test
    public void shouldReturnValidQueryBuilderForProsecutingAuthority() {
        final String prosecutingAuthority = "tfl";
        final Query actualQuery = prosecutingAuthoritySearchQueryBuilder.getQueryBuilderBy(prosecutingAuthority);

        assertThat(actualQuery, is(notNullValue()));

        final BoolQuery actualBoolQueryBuilder = actualQuery.bool();
        assertThat(actualBoolQueryBuilder.must(), hasSize(0));
        assertThat(actualBoolQueryBuilder.should(), hasSize(0));

        assertThat(actualBoolQueryBuilder.filter(), hasSize(1));

        final Query firstFilter = actualBoolQueryBuilder.filter().get(0);

        final TermQuery firstTerm = firstFilter.term();
        assertThat(firstTerm.field(), is("prosecutingAuthority"));
        assertThat(firstTerm.value().stringValue(), is(prosecutingAuthority));
    }
}