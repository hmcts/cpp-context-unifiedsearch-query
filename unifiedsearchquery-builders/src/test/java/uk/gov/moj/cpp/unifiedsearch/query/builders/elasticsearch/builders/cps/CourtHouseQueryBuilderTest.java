package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.COURT_HOUSE_PATH;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CourtHouseQueryBuilderTest {

    private CourtHouseQueryBuilder courtHouseQueryBuilder;

    @BeforeEach
    public void setUp() {
        courtHouseQueryBuilder = new CourtHouseQueryBuilder();
    }

    @Test
    public void shouldReturnValidQueryBuilderForHearingDateTo() {
        final String courtHouse = "C43OX00";
        final QueryBuilder actualQueryBuilder = courtHouseQueryBuilder.getQueryBuilderBy(courtHouse);

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder termQueryBuilder = (TermQueryBuilder) actualQueryBuilder;
        assertThat(termQueryBuilder.getName(), is("term"));
        assertThat(termQueryBuilder.fieldName(), is(COURT_HOUSE_PATH));
        assertThat(termQueryBuilder.value(), is(courtHouse));
    }
}

