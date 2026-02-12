package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PROCEEDINGS_CONCLUDED_INDEX;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.ExistsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import org.junit.jupiter.api.Test;

public class ProceedingsConcludedQueryBuilderTest {

    private ProceedingsConcludedQueryBuilder proceedingsConcludedQueryBuilder = new ProceedingsConcludedQueryBuilder();


    @Test
    public void shouldCreateQueryBuilderWhenTrue() {

        final Query query = proceedingsConcludedQueryBuilder.getQueryBuilderBy(true);

        assertThat(query, is(notNullValue()));

        final BoolQuery boolQueryBuilder =  query.bool();
        assertThat(boolQueryBuilder.should(), hasSize(0));
        assertThat(boolQueryBuilder.must(), hasSize(1));
        assertThat(boolQueryBuilder.mustNot(), hasSize(0));

        final Query firstMustClause = boolQueryBuilder.must().get(0);

        final TermQuery firstTerm = firstMustClause.term();
        assertThat(firstTerm.field(), is(PROCEEDINGS_CONCLUDED_INDEX));
        assertThat(firstTerm.value().booleanValue(), is(true));
    }


    @Test
    public void shouldCreateQueryBuilderWhenFalse() {

        final Query query = proceedingsConcludedQueryBuilder.getQueryBuilderBy(false);

        assertThat(query, is(notNullValue()));

        final BoolQuery actualBoolQueryBuilder = query.bool();
        assertThat(actualBoolQueryBuilder.should(), hasSize(2));
        assertThat(actualBoolQueryBuilder.must(), hasSize(0));
        assertThat(actualBoolQueryBuilder.mustNot(), hasSize(0));

        final Query firstShouldClause = actualBoolQueryBuilder.should().get(0);
        final Query secondShouldClause = actualBoolQueryBuilder.should().get(1);

        final TermQuery firstTerm = firstShouldClause.term();
        assertThat(firstTerm.field(), is(PROCEEDINGS_CONCLUDED_INDEX));
        assertThat(firstTerm.value().booleanValue(), is(false));

        final BoolQuery secondBoolQueryBuilder = secondShouldClause.bool();
        assertThat(secondBoolQueryBuilder.mustNot().size(), is(1));
        ExistsQuery existsQueryBuilder = secondBoolQueryBuilder.mustNot().get(0).exists();
        assertThat(existsQueryBuilder.field(), is(PROCEEDINGS_CONCLUDED_INDEX));
    }
}