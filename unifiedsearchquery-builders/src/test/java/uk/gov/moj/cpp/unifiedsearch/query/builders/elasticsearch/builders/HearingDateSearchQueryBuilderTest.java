package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.utils.HearingDateUtil.stringToDate;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_DAY_REFERENCE_PATH;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HearingDateSearchQueryBuilderTest {

    private HearingDateSearchQueryBuilder hearingDateSearchQueryBuilder;

    @BeforeEach
    public void setUp() {
        hearingDateSearchQueryBuilder = new HearingDateSearchQueryBuilder();
    }

    @Test
    public void shouldReturnValidQueryBuilderForHearingDateFrom() {
        final String hearingDateFrom = "2019-04-19";
        final String hearingDateTo = "";
        final QueryBuilder actualQueryBuilder = hearingDateSearchQueryBuilder.getQueryBuilderBy(hearingDateFrom, hearingDateTo);

        assertThat(actualQueryBuilder, is(notNullValue()));

        assertThat(actualQueryBuilder, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder termQueryBuilder = (TermQueryBuilder) actualQueryBuilder;
        assertThat(termQueryBuilder.getName(), is("term"));
        assertThat(termQueryBuilder.fieldName(), is(HEARING_DAY_REFERENCE_PATH));
        assertThat(termQueryBuilder.value(), is(hearingDateFrom));
    }

    @Test
    public void shouldReturnValidQueryBuilderForHearingDateTo() {
        final String hearingDateFrom = "";
        final String hearingDateTo = "2019-04-19";
        final QueryBuilder actualQueryBuilder = hearingDateSearchQueryBuilder.getQueryBuilderBy(hearingDateFrom, hearingDateTo);

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder termQueryBuilder = (TermQueryBuilder) actualQueryBuilder;
        assertThat(termQueryBuilder.getName(), is("term"));
        assertThat(termQueryBuilder.fieldName(), is(HEARING_DAY_REFERENCE_PATH));
        assertThat(termQueryBuilder.value(), is(hearingDateTo));
    }

    @Test
    public void shouldReturnValidQueryBuilderForHearingDateFromAndHearingDateTo() {
        final String hearingDateFrom = "2019-04-19";
        final String hearingDateTo = "2019-05-17";

        final QueryBuilder actualQueryBuilder = hearingDateSearchQueryBuilder.getQueryBuilderBy(hearingDateFrom, hearingDateTo);

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf(RangeQueryBuilder.class));

        final RangeQueryBuilder rangeQueryBuilder = (RangeQueryBuilder) actualQueryBuilder;
        assertThat(rangeQueryBuilder.getName(), is("range"));
        assertThat(rangeQueryBuilder.fieldName(), is(HEARING_DAY_REFERENCE_PATH));
        assertThat(rangeQueryBuilder.from(), is(stringToDate(hearingDateFrom)));
        assertThat(rangeQueryBuilder.to(), is(stringToDate(hearingDateTo)));
    }
}

