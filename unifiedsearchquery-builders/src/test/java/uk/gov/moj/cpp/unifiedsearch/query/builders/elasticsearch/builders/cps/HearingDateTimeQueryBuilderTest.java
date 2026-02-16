package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.utils.HearingDateUtil.stringToDate;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.HEARING_DATE_TIME_PATH;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.UntypedRangeQuery;

public class HearingDateTimeQueryBuilderTest {

    private HearingDateTimeQueryBuilder hearingDateTimeQueryBuilder;

    @BeforeEach
    public void setUp() {
        hearingDateTimeQueryBuilder = new HearingDateTimeQueryBuilder();
    }

    @Test
    public void shouldReturnValidQueryBuilderForHearingDateFrom() {
        final String hearingDateFrom = "2021-04-19";
        final String hearingDateTo = "";
        final Query actualQuery = hearingDateTimeQueryBuilder.getQueryBuilderBy(hearingDateFrom, hearingDateTo);

        assertThat(actualQuery, is(notNullValue()));

        final TermQuery termQueryBuilder = actualQuery.term();
        assertThat(termQueryBuilder.field(), is(HEARING_DATE_TIME_PATH));
        assertThat(termQueryBuilder.value().stringValue(), is(hearingDateFrom));
    }

    @Test
    public void shouldReturnValidQueryBuilderForHearingDateTo() {
        final String hearingDateFrom = "";
        final String hearingDateTo = "2021-04-19";
        final Query actualQuery = hearingDateTimeQueryBuilder.getQueryBuilderBy(hearingDateFrom, hearingDateTo);

        assertThat(actualQuery, is(notNullValue()));

        final TermQuery termQueryBuilder = actualQuery.term();
        assertThat(termQueryBuilder.field(), is(HEARING_DATE_TIME_PATH));
        assertThat(termQueryBuilder.value().stringValue(), is(hearingDateTo));
    }

    @Test
    public void shouldReturnValidQueryBuilderForHearingDateFromAndHearingDateTo() {
        final String hearingDateFrom = "2021-04-19";
        final String hearingDateTo = "2021-05-17";

        final Query actualQuery = hearingDateTimeQueryBuilder.getQueryBuilderBy(hearingDateFrom, hearingDateTo);

        assertThat(actualQuery, is(notNullValue()));

        final UntypedRangeQuery rangeQueryBuilder = actualQuery.range().untyped();
        assertThat(rangeQueryBuilder.field(), is(HEARING_DATE_TIME_PATH));
        assertThat(rangeQueryBuilder.gte().to(LocalDate.class), is(stringToDate(hearingDateFrom)));
        assertThat(rangeQueryBuilder.lte().to(LocalDate.class), is(stringToDate(hearingDateTo)));
    }
}

