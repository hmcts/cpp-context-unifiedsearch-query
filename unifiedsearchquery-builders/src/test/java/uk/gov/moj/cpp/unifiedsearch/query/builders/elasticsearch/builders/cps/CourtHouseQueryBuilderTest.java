package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.COURT_HOUSE_PATH;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;

public class CourtHouseQueryBuilderTest {

    private CourtHouseQueryBuilder courtHouseQueryBuilder;

    @BeforeEach
    public void setUp() {
        courtHouseQueryBuilder = new CourtHouseQueryBuilder();
    }

    @Test
    public void shouldReturnValidQueryBuilderForHearingDateTo() {
        final String courtHouse = "C43OX00";
        final Query actualQuery = courtHouseQueryBuilder.getQueryBuilderBy(courtHouse);

        assertThat(actualQuery, is(notNullValue()));
        assertThat(actualQuery.term(), notNullValue());

        final TermQuery termQueryBuilder = actualQuery.term();
        assertThat(termQueryBuilder.field(), is(COURT_HOUSE_PATH));
        assertThat(termQueryBuilder.value().stringValue(), is(courtHouse));
    }
}

