package uk.gov.moj.cpp.unifiedsearch.query.builders.utils;


import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;

public class QueryBuilderUtils {
    public static MatchQuery.Builder copyQuery(MatchQuery query){
        MatchQuery.Builder mqBuilder = new MatchQuery.Builder()
                .field(query.field())
                .query(query.query());
        if (query.boost() != null) mqBuilder.boost(query.boost());
        if (query.operator() != null) mqBuilder.operator(query.operator());
        if (query.fuzziness() != null) mqBuilder.fuzziness(query.fuzziness());
        return mqBuilder;
    }
}
