package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.URN;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UrnSearchQueryBuilderTest {

    private UrnSearchQueryBuilder urnSearchQueryBuilder;

    @BeforeEach
    public void setUp() {
        urnSearchQueryBuilder = new UrnSearchQueryBuilder();
    }

    @Test
    public void shouldReturnValidQueryBuilderForCourtQueryBuilder() {
        final String urn = "3fdcf368";

        final QueryBuilder actualQueryBuilder = urnSearchQueryBuilder.getQueryBuilderBy(urn);

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf((BoolQueryBuilder.class)));
        final BoolQueryBuilder actualBoolQueryBuilder = (BoolQueryBuilder) actualQueryBuilder;
        assertThat(actualBoolQueryBuilder.getName(), is("bool"));

        assertThat(actualBoolQueryBuilder.must(), hasSize(0));

        final QueryBuilder builder = actualBoolQueryBuilder.filter().get(0);
        assertThat(builder, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder termQueryBuilder = (TermQueryBuilder) builder;
        assertThat(termQueryBuilder.getName(), is("term"));
        assertThat(termQueryBuilder.fieldName(), is(URN));
        assertThat(termQueryBuilder.value(), is(urn));
    }
}