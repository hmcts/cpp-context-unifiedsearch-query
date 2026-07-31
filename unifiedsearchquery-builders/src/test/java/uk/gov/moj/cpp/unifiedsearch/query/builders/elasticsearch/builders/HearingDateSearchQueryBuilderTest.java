package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_DAY_REFERENCE_PATH;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;

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
        final Query actualQueryBuilder = hearingDateSearchQueryBuilder.getQueryBuilderBy(hearingDateFrom, hearingDateTo);

        assertThat(actualQueryBuilder, is(notNullValue()));

        assertThat(actualQueryBuilder.term(), notNullValue());

        final TermQuery termQueryBuilder = actualQueryBuilder.term();
        assertThat(termQueryBuilder.field(), is(HEARING_DAY_REFERENCE_PATH));
        assertThat(termQueryBuilder.value().stringValue(), is(hearingDateFrom));
    }

    @Test
    public void shouldReturnValidQueryBuilderForHearingDateTo() {
        final String hearingDateFrom = "";
        final String hearingDateTo = "2019-04-19";
        final Query actualQueryBuilder = hearingDateSearchQueryBuilder.getQueryBuilderBy(hearingDateFrom, hearingDateTo);

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder.term(), notNullValue());

        final TermQuery termQueryBuilder = actualQueryBuilder.term();
        assertThat(termQueryBuilder.field(), is(HEARING_DAY_REFERENCE_PATH));
        assertThat(termQueryBuilder.value().stringValue(), is(hearingDateTo));
    }

    @Test
    public void shouldReturnValidQueryBuilderForHearingDateFromAndHearingDateTo() {
        final String hearingDateFrom = "2019-04-19";
        final String hearingDateTo = "2019-05-17";

        final Query actualQueryBuilder = hearingDateSearchQueryBuilder.getQueryBuilderBy(hearingDateFrom, hearingDateTo);

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder.range(), notNullValue());

        final RangeQuery rangeQueryBuilder =actualQueryBuilder.range();
        assertThat(rangeQueryBuilder.untyped().field(), is(HEARING_DAY_REFERENCE_PATH));
        assertThat(rangeQueryBuilder.untyped().gte().toString(), is(hearingDateFrom));
        assertThat(rangeQueryBuilder.untyped().lte().toString(), is(hearingDateTo));
    }
}

