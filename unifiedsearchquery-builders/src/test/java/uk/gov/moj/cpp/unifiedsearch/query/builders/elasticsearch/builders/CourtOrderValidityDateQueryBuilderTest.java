package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;


import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.COURT_ORDER_END_DATE_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.COURT_ORDER_START_DATE_PATH;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.junit.jupiter.api.Test;

public class CourtOrderValidityDateQueryBuilderTest {

    private CourtOrderValidityDateQueryBuilder courtOrderValidityDateQueryBuilder = new CourtOrderValidityDateQueryBuilder();

    @Test
    public void shouldReturnValidBoolQueryWhenHasActiveCourtOrder() {
        final String courtOrderValidityDate = "2021-01-01";
        final Query query = courtOrderValidityDateQueryBuilder.getQueryBuilderBy(courtOrderValidityDate);

        assertThat(query, is(notNullValue()));
        final BoolQuery actualBoolQueryBuilder = query.bool();
        assertThat(actualBoolQueryBuilder, notNullValue());


        assertThat(actualBoolQueryBuilder.should(), hasSize(0));
        assertThat(actualBoolQueryBuilder.must(), hasSize(2));
        assertThat(actualBoolQueryBuilder.mustNot(), hasSize(0));

        final Query firstMustClause = actualBoolQueryBuilder.must().get(0);
        final Query secondMustClause = actualBoolQueryBuilder.must().get(1);

        assertThat(firstMustClause.range(), notNullValue());
        assertThat(secondMustClause.range(), notNullValue());

        final RangeQuery firstRange = firstMustClause.range();
        assertThat(firstRange.untyped().field(), is(COURT_ORDER_START_DATE_PATH));
        assertThat(firstRange.untyped().gte(), nullValue());
        assertThat(firstRange.untyped().lte().toString(), is(courtOrderValidityDate));

        final RangeQuery secondRange = secondMustClause.range();
        assertThat(secondRange.untyped().field(), is(COURT_ORDER_END_DATE_PATH));
        assertThat(secondRange.untyped().gte().toString(), is(courtOrderValidityDate));
        assertThat(secondRange.untyped().lte(), nullValue());
    }
}