package uk.gov.moj.cpp.unifiedsearch.query.builders.service;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.ChildScoreMode;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public abstract class AbstractQueryBuilderServiceTest {

    protected void checkNestedFilter(final Query resultQuery) {

        final BoolQuery resultQueryBuilderAsBool = resultQuery.bool();
        assertThat(resultQueryBuilderAsBool.filter(), hasSize(0));
        assertThat(resultQueryBuilderAsBool.should(), hasSize(0));
        assertThat(resultQueryBuilderAsBool.mustNot(), hasSize(0));
        final List<Query> must = resultQueryBuilderAsBool.must();
        assertThat(must, hasSize(1));

        final Query firstMust = resultQueryBuilderAsBool.must().get(0);
        assertThat(firstMust.nested(), notNullValue());
        final NestedQuery nestedQueryBuilder = firstMust.nested();
        assertThat(nestedQueryBuilder.scoreMode(), is(ChildScoreMode.Avg));
        final Query innerQueryBuilder = nestedQueryBuilder.query();
        assertThat(innerQueryBuilder.bool(), notNullValue());
        final BoolQuery innerBoolQueryBuilder = innerQueryBuilder.bool();

        assertThat(innerBoolQueryBuilder.should(), hasSize(0));
        assertThat(innerBoolQueryBuilder.filter(), hasSize(0));
        assertThat(innerBoolQueryBuilder.mustNot(), hasSize(0));
        assertThat(innerBoolQueryBuilder.must(), hasSize(1));
    }

    protected void checkExactMatchesNestedFilter(final Query resultQuery,
                                                 final int mustSize, final int innerMust) {
        final BoolQuery resultQueryBuilderAsBool = resultQuery.bool();
        assertThat(resultQueryBuilderAsBool, notNullValue());

        assertThat(resultQueryBuilderAsBool.filter(), hasSize(0));
        assertThat(resultQueryBuilderAsBool.should(), hasSize(0));
        assertThat(resultQueryBuilderAsBool.mustNot(), hasSize(0));
        final List<Query> must = resultQueryBuilderAsBool.must();
        assertThat(must, hasSize(mustSize));

        if (mustSize > 0) {
            final Query firstMust = resultQueryBuilderAsBool.must().get(0);
            assertThat(firstMust, notNullValue());
            final NestedQuery nestedQueryBuilder = firstMust.nested();
            assertThat(nestedQueryBuilder.scoreMode(), is(ChildScoreMode.Avg));
            final Query innerQueryBuilder = nestedQueryBuilder.query();
            assertThat(innerQueryBuilder.bool(), notNullValue());
            final BoolQuery innerBoolQueryBuilder = innerQueryBuilder.bool();

            assertThat(innerBoolQueryBuilder.should(), hasSize(0));
            assertThat(innerBoolQueryBuilder.filter(), hasSize(0));
            assertThat(innerBoolQueryBuilder.mustNot(), hasSize(0));
            assertThat(innerBoolQueryBuilder.must(), hasSize(innerMust));
        }
    }

}
