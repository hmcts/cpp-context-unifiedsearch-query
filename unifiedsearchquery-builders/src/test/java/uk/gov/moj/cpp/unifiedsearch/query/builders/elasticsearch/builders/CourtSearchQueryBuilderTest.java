package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_DAY_COURTCENTRE_REFERENCE_PATH;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CourtSearchQueryBuilderTest {

    private CourtSearchQueryBuilder courtSearchQueryBuilder;

    @BeforeEach
    public void setUp() {
        courtSearchQueryBuilder = new CourtSearchQueryBuilder();
    }

    @Test
    public void shouldReturnValidQueryBuilderForCourtQueryBuilder() {
        final String courtId = "3fdcf368-ea16-40cd-a7f2-d25b04637721";

        final QueryBuilder actualQueryBuilder = courtSearchQueryBuilder.getQueryBuilderBy(courtId);

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf((TermQueryBuilder.class)));

        final TermQueryBuilder termQueryBuilder = (TermQueryBuilder) actualQueryBuilder;
        assertThat(termQueryBuilder.getName(), is("term"));
        assertThat(termQueryBuilder.fieldName(), is(HEARING_DAY_COURTCENTRE_REFERENCE_PATH));
        assertThat(termQueryBuilder.value(), is(courtId));
    }
}