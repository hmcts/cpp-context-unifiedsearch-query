package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;


import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.COURT_ORDER_END_DATE_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.COURT_ORDER_START_DATE_PATH;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.junit.jupiter.api.Test;

public class CourtOrderValidityDateQueryBuilderTest {

    private CourtOrderValidityDateQueryBuilder courtOrderValidityDateQueryBuilder = new CourtOrderValidityDateQueryBuilder();

    @Test
    public void shouldReturnValidBoolQueryWhenHasActiveCourtOrder() {
        final String courtOrderValidityDate = "2021-01-01";
        final QueryBuilder queryBuilder = courtOrderValidityDateQueryBuilder.getQueryBuilderBy(courtOrderValidityDate);

        assertThat(queryBuilder, is(notNullValue()));
        assertThat(queryBuilder, instanceOf((BoolQueryBuilder.class)));

        final BoolQueryBuilder actualBoolQueryBuilder = (BoolQueryBuilder) queryBuilder;
        assertThat(actualBoolQueryBuilder.getName(), is("bool"));
        assertThat(actualBoolQueryBuilder.should(), hasSize(0));
        assertThat(actualBoolQueryBuilder.must(), hasSize(2));
        assertThat(actualBoolQueryBuilder.mustNot(), hasSize(0));

        final QueryBuilder firstMustClause = actualBoolQueryBuilder.must().get(0);
        final QueryBuilder secondMustClause = actualBoolQueryBuilder.must().get(1);

        assertThat(firstMustClause, instanceOf(RangeQueryBuilder.class));
        assertThat(secondMustClause, instanceOf(RangeQueryBuilder.class));

        final RangeQueryBuilder firstRange = (RangeQueryBuilder) firstMustClause;
        assertThat(firstRange.getName(), is("range"));
        assertThat(firstRange.fieldName(), is(COURT_ORDER_START_DATE_PATH));
        assertThat(firstRange.from(), nullValue());
        assertThat(firstRange.to(), is(courtOrderValidityDate));
        assertThat(firstRange.includeLower(), is(true));
        assertThat(firstRange.includeUpper(), is(true));

        final RangeQueryBuilder secondRange = (RangeQueryBuilder) secondMustClause;
        assertThat(secondRange.getName(), is("range"));
        assertThat(secondRange.fieldName(), is(COURT_ORDER_END_DATE_PATH));
        assertThat(secondRange.from(), is(courtOrderValidityDate));
        assertThat(secondRange.to(), nullValue());
        assertThat(secondRange.includeLower(), is(true));
        assertThat(secondRange.includeUpper(), is(true));
    }
}