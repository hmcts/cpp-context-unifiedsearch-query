package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static co.elastic.clients.elasticsearch._types.query_dsl.ChildScoreMode.Avg;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.withJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.OFFENCES_NESTED_PATH;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.ExistsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
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

        final Query actualQuery = target.getQueryBuilderBy(hasOffence);

        assertThat(actualQuery, is(notNullValue()));
        assertThat(actualQuery.toString().substring(7), isJson(allOf(
                withJsonPath("nested.path", equalTo(OFFENCES_NESTED_PATH)),
                withJsonPath("nested.score_mode", equalTo(Avg.name().toLowerCase())),
                withJsonPath("nested.query.bool.must", hasSize(1)),
                withJsonPath("nested.query.bool.must[0].exists.field", equalTo(OFFENCES_NESTED_PATH))
        )));

        assertThat(actualQuery.nested(), notNullValue());

        final NestedQuery actualNestedQueryBuilder = actualQuery.nested();
        assertThat(actualNestedQueryBuilder.scoreMode(), is(Avg));

        final BoolQuery innedBoolQueryBuilder = actualNestedQueryBuilder.query().bool();
        assertThat(innedBoolQueryBuilder.mustNot(), hasSize(0));
        assertThat(innedBoolQueryBuilder.should(), hasSize(0));
        assertThat(innedBoolQueryBuilder.must(), hasSize(1));

        final Query firstMust = innedBoolQueryBuilder.must().get(0);
        assertThat(firstMust.exists(), notNullValue());
        final ExistsQuery actualExistsQueryBuilder = firstMust.exists();
        assertThat(actualExistsQueryBuilder.field(), is(OFFENCES_NESTED_PATH));
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

        final Query actualQuery = target.getQueryBuilderBy(hasOffence);

        assertThat(actualQuery, is(notNullValue()));
        assertThat(actualQuery.toString().substring(7), isJson(allOf(
                withJsonPath("nested.path", equalTo(OFFENCES_NESTED_PATH)),
                withJsonPath("nested.score_mode", equalTo(Avg.name().toLowerCase())),
                withJsonPath("nested.query.bool.must_not", hasSize(1)),
                withJsonPath("nested.query.bool.must_not[0].exists.field", equalTo(OFFENCES_NESTED_PATH))
        )));

        assertThat(actualQuery.nested(), notNullValue());

        final NestedQuery actualNestedQueryBuilder = actualQuery.nested();
        assertThat(actualNestedQueryBuilder.scoreMode(), is(Avg));

        final BoolQuery innedBoolQueryBuilder = actualNestedQueryBuilder.query().bool();
        assertThat(innedBoolQueryBuilder.mustNot(), hasSize(1));
        assertThat(innedBoolQueryBuilder.should(), hasSize(0));
        assertThat(innedBoolQueryBuilder.must(), hasSize(0));

        final Query firstMustNot = innedBoolQueryBuilder.mustNot().get(0);
        assertThat(firstMustNot.exists(), notNullValue());
        final ExistsQuery actualExistsQueryBuilder = firstMustNot.exists();
        assertThat(actualExistsQueryBuilder.field(), is(OFFENCES_NESTED_PATH));
    }
}