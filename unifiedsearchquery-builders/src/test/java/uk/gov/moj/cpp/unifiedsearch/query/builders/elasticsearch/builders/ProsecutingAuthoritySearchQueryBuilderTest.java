package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
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
        final QueryBuilder actualQueryBuilder = prosecutingAuthoritySearchQueryBuilder.getQueryBuilderBy(prosecutingAuthority);

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf((BoolQueryBuilder.class)));

        final BoolQueryBuilder actualBoolQueryBuilder = (BoolQueryBuilder) actualQueryBuilder;
        assertThat(actualBoolQueryBuilder.getName(), is("bool"));
        assertThat(actualBoolQueryBuilder.must(), hasSize(0));
        assertThat(actualBoolQueryBuilder.should(), hasSize(0));

        assertThat(actualBoolQueryBuilder.filter(), hasSize(1));

        final QueryBuilder firstFilter = actualBoolQueryBuilder.filter().get(0);

        assertThat(firstFilter, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder firstTerm = (TermQueryBuilder) firstFilter;
        assertThat(firstTerm.getName(), is("term"));
        assertThat(firstTerm.fieldName(), is("prosecutingAuthority"));
        assertThat(firstTerm.value(), is(prosecutingAuthority));
    }
}