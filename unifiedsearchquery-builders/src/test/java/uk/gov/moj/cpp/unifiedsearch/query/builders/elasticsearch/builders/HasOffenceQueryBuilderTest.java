package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.withJsonPath;
import static org.apache.lucene.search.join.ScoreMode.Avg;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.OFFENCES_NESTED_PATH;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.jupiter.api.Test;

public class HasOffenceQueryBuilderTest {

    private final HasOffenceQueryBuilder target = new HasOffenceQueryBuilder();

    /**
     * // @formatter:off
     * Example nested query would look like below for the test case:
     * {
     *   "nested": {
     *     "query": {
     *       "bool": {
     *         "must": [
     *           {
     *             "exists": {
     *               "field": "parties.offences"
     *             }
     *           }
     *         ]
     *       }
     *     },
     *     "path": "parties.offences",
     *     "score_mode": "avg"
     *   }
     * }
     * // @formatter:on
     */
    @Test
    public void shouldReturnValidNestedQueryBuilderReferenceWhenOffenceExists() {
        final boolean hasOffence = true;

        final QueryBuilder actualQueryBuilder = target.getQueryBuilderBy(hasOffence);

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder.toString(), isJson(allOf(
                withJsonPath("nested.path", equalTo(OFFENCES_NESTED_PATH)),
                withJsonPath("nested.score_mode", equalTo(Avg.name().toLowerCase())),
                withJsonPath("nested.query.bool.must", hasSize(1)),
                withJsonPath("nested.query.bool.must[0].exists.field", equalTo(OFFENCES_NESTED_PATH))
        )));

        assertThat(actualQueryBuilder, is(instanceOf(NestedQueryBuilder.class)));

        final NestedQueryBuilder actualNestedQueryBuilder = (NestedQueryBuilder) actualQueryBuilder;
        assertThat(actualNestedQueryBuilder.scoreMode(), is(Avg));

        final BoolQueryBuilder innedBoolQueryBuilder = (BoolQueryBuilder) actualNestedQueryBuilder.query();
        assertThat(innedBoolQueryBuilder.mustNot(), hasSize(0));
        assertThat(innedBoolQueryBuilder.should(), hasSize(0));
        assertThat(innedBoolQueryBuilder.must(), hasSize(1));

        final QueryBuilder firstMust = innedBoolQueryBuilder.must().get(0);
        assertThat(firstMust, is(instanceOf(ExistsQueryBuilder.class)));
        final ExistsQueryBuilder actualExistsQueryBuilder = (ExistsQueryBuilder) firstMust;
        assertThat(actualExistsQueryBuilder.fieldName(), is(OFFENCES_NESTED_PATH));
    }

    /**
     * // @formatter:off
     * Example nested query would look like below for the test case:
     * {
     *   "nested": {
     *     "query": {
     *       "bool": {
     *         "must_not": [
     *           {
     *             "exists": {
     *               "field": "parties.offences"
     *             }
     *           }
     *         ]
     *       }
     *     },
     *     "path": "parties.offences",
     *     "score_mode": "avg"
     *   }
     * }
     * // @formatter:on
     */
    @Test
    public void shouldReturnValidNestedQueryBuilderReferenceWhenOffenceDoesNotExists() {
        final boolean hasOffence = false;

        final QueryBuilder actualQueryBuilder = target.getQueryBuilderBy(hasOffence);

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder.toString(), isJson(allOf(
                withJsonPath("nested.path", equalTo(OFFENCES_NESTED_PATH)),
                withJsonPath("nested.score_mode", equalTo(Avg.name().toLowerCase())),
                withJsonPath("nested.query.bool.must_not", hasSize(1)),
                withJsonPath("nested.query.bool.must_not[0].exists.field", equalTo(OFFENCES_NESTED_PATH))
        )));

        assertThat(actualQueryBuilder, is(instanceOf(NestedQueryBuilder.class)));

        final NestedQueryBuilder actualNestedQueryBuilder = (NestedQueryBuilder) actualQueryBuilder;
        assertThat(actualNestedQueryBuilder.scoreMode(), is(Avg));

        final BoolQueryBuilder innedBoolQueryBuilder = (BoolQueryBuilder) actualNestedQueryBuilder.query();
        assertThat(innedBoolQueryBuilder.mustNot(), hasSize(1));
        assertThat(innedBoolQueryBuilder.should(), hasSize(0));
        assertThat(innedBoolQueryBuilder.must(), hasSize(0));

        final QueryBuilder firstMustNot = innedBoolQueryBuilder.mustNot().get(0);
        assertThat(firstMustNot, is(instanceOf(ExistsQueryBuilder.class)));
        final ExistsQueryBuilder actualExistsQueryBuilder = (ExistsQueryBuilder) firstMustNot;
        assertThat(actualExistsQueryBuilder.fieldName(), is(OFFENCES_NESTED_PATH));
    }
}