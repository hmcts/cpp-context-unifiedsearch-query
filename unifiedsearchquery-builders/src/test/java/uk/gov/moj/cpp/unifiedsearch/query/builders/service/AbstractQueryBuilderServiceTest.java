package uk.gov.moj.cpp.unifiedsearch.query.builders.service;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.apache.lucene.search.join.ScoreMode.Avg;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

public abstract class AbstractQueryBuilderServiceTest {

    protected void checkNestedFilter(final QueryBuilder resultQueryBuilder) {
        assertThat(resultQueryBuilder, instanceOf(BoolQueryBuilder.class));
        final BoolQueryBuilder resultQueryBuilderAsBool = (BoolQueryBuilder) resultQueryBuilder;
        assertThat(resultQueryBuilderAsBool.filter(), hasSize(0));
        assertThat(resultQueryBuilderAsBool.should(), hasSize(0));
        assertThat(resultQueryBuilderAsBool.mustNot(), hasSize(0));
        final List<QueryBuilder> must = resultQueryBuilderAsBool.must();
        assertThat(must, hasSize(1));

        final QueryBuilder firstMust = resultQueryBuilderAsBool.must().get(0);
        assertThat(firstMust, instanceOf(NestedQueryBuilder.class));
        final NestedQueryBuilder nestedQueryBuilder = (NestedQueryBuilder) firstMust;
        assertThat(nestedQueryBuilder.getName(), is("nested"));
        assertThat(nestedQueryBuilder.scoreMode(), is(Avg));
        final QueryBuilder innerQueryBuilder = nestedQueryBuilder.query();
        assertThat(innerQueryBuilder, instanceOf(BoolQueryBuilder.class));
        final BoolQueryBuilder innerBoolQueryBuilder = (BoolQueryBuilder) innerQueryBuilder;

        assertThat(innerBoolQueryBuilder.should(), hasSize(0));
        assertThat(innerBoolQueryBuilder.filter(), hasSize(0));
        assertThat(innerBoolQueryBuilder.mustNot(), hasSize(0));
        assertThat(innerBoolQueryBuilder.must(), hasSize(1));
    }

    protected void checkExactMatchesNestedFilter(final QueryBuilder resultQueryBuilder,
                                                 final int mustSize, final int innerMust) {
        assertThat(resultQueryBuilder, instanceOf(BoolQueryBuilder.class));
        final BoolQueryBuilder resultQueryBuilderAsBool = (BoolQueryBuilder) resultQueryBuilder;
        assertThat(resultQueryBuilderAsBool.filter(), hasSize(0));
        assertThat(resultQueryBuilderAsBool.should(), hasSize(0));
        assertThat(resultQueryBuilderAsBool.mustNot(), hasSize(0));
        final List<QueryBuilder> must = resultQueryBuilderAsBool.must();
        assertThat(must, hasSize(mustSize));

        if (mustSize > 0) {
            final QueryBuilder firstMust = resultQueryBuilderAsBool.must().get(0);
            assertThat(firstMust, instanceOf(QueryBuilder.class));
            final NestedQueryBuilder nestedQueryBuilder = (NestedQueryBuilder) firstMust;
            assertThat(nestedQueryBuilder.getName(), is("nested"));
            assertThat(nestedQueryBuilder.scoreMode(), is(Avg));
            final QueryBuilder innerQueryBuilder = nestedQueryBuilder.query();
            assertThat(innerQueryBuilder, instanceOf(BoolQueryBuilder.class));
            final BoolQueryBuilder innerBoolQueryBuilder = (BoolQueryBuilder) innerQueryBuilder;

            assertThat(innerBoolQueryBuilder.should(), hasSize(0));
            assertThat(innerBoolQueryBuilder.filter(), hasSize(0));
            assertThat(innerBoolQueryBuilder.mustNot(), hasSize(0));
            assertThat(innerBoolQueryBuilder.must(), hasSize(innerMust));
        }
    }

}
