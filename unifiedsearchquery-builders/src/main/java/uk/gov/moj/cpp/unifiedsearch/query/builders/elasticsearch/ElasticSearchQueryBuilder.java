package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch;

import static java.lang.String.valueOf;

import java.util.Arrays;
import java.util.List;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.ChildScoreMode;
import co.elastic.clients.elasticsearch._types.query_dsl.DisMaxQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;

public interface ElasticSearchQueryBuilder {
    Query getQueryBuilderBy(final Object... queryParams);

    default String[] clean(final Object queryParam) {
        return valueOf(queryParam).replaceAll("\\s", "").split(",");
    }

    static Query.Builder termQuery(String name, String value){
        final Query.Builder builder = new Query.Builder();
        builder.term(t -> t
                .field(name)
                .value(value));
        return builder;
    }

    static Query.Builder termQuery(String name, Boolean value){
        final Query.Builder builder = new Query.Builder();
        builder.term(t -> t
                .field(name)
                .value(value));
        return builder;
    }

    static Query.Builder termsQuery(String name, String... value){
        final Query.Builder builder = new Query.Builder();
        builder.terms(t -> t
                .field(name)
                .terms(v -> v.value(
                        Arrays.stream(value)
                                .map(FieldValue::of)
                                .toList())));
        return builder;
    }

    static Query.Builder termsQuery(String name, List<String> value){
        final Query.Builder builder = new Query.Builder();
        builder.terms(t -> t
                .field(name)
                .terms(v -> v.value(
                        value.stream()
                                .map(FieldValue::of)
                                .toList())));
        return builder;
    }

    static NestedQuery.Builder nestedQuery(String path, Query.Builder query){
        final NestedQuery.Builder builder = new NestedQuery.Builder();

        builder.path(path).query(query.build()).scoreMode(ChildScoreMode.Avg);
        return builder;
    }

    static NestedQuery.Builder nestedQuery(String path, Query query){
        final NestedQuery.Builder builder = new NestedQuery.Builder();

        builder.path(path).query(query).scoreMode(ChildScoreMode.Avg);
        return builder;
    }

    static MatchQuery.Builder matchQuery(String name, String text) {
        final MatchQuery.Builder builder = new MatchQuery.Builder();
        builder.field(name).query(text);
        return builder;
    }

    static MultiMatchQuery.Builder multiMatchQuery(String text, List<String> name) {
        final MultiMatchQuery.Builder builder = new MultiMatchQuery.Builder();
        builder.fields(name).query(text);
        return builder;
    }

    static Query.Builder convertBuilder(final NestedQuery.Builder nestedQueryBuilder){
        Query.Builder builder = new Query.Builder();
        builder.nested(nestedQueryBuilder.build());
        return builder;
    }

    static Query.Builder convertBuilder(final MatchQuery.Builder matchQueryBuilder){
        Query.Builder builder = new Query.Builder();
        builder.match(matchQueryBuilder.build());
        return builder;
    }

    static Query.Builder convertBuilder(final BoolQuery.Builder matchQueryBuilder){
        Query.Builder builder = new Query.Builder();
        builder.bool(matchQueryBuilder.build());
        return builder;
    }

    static Query.Builder convertBuilder(final MultiMatchQuery.Builder multiMatchQueryBuilder){
        Query.Builder builder = new Query.Builder();
        builder.multiMatch(multiMatchQueryBuilder.build());
        return builder;
    }

    static Query.Builder convertBuilder(final DisMaxQuery.Builder disMaxQueryBuilder){
        Query.Builder builder = new Query.Builder();
        builder.disMax(disMaxQueryBuilder.build());
        return builder;
    }

    static Query.Builder convertBuilder(final TermsQuery.Builder termsQueryBuilder){
        Query.Builder builder = new Query.Builder();
        builder.terms(termsQueryBuilder.build());
        return builder;
    }

    static Query.Builder convertBuilder(final RangeQuery.Builder rangeQueryBuilder){
        Query.Builder builder = new Query.Builder();
        builder.range(rangeQueryBuilder.build());
        return builder;
    }
}
