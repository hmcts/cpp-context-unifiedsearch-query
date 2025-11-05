package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PNC_ID_INDEX;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;

public class PncIdQueryBuilderTest {
    private PncIdQueryBuilder pncIdQueryBuilder = new PncIdQueryBuilder();


    @Test
    public void shouldCreateExactMatchQueryBuilderForPncIdNumber() {

        final String pncId = "123456";
        final QueryBuilder queryBuilder = pncIdQueryBuilder.getQueryBuilderBy(pncId);

        assertThat(queryBuilder, is(notNullValue()));

        assertThat(queryBuilder, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder termQueryBuilder = (TermQueryBuilder) queryBuilder;
        assertThat(termQueryBuilder.getName(), is("term"));
        assertThat(termQueryBuilder.fieldName(), is(PNC_ID_INDEX));
        assertThat(termQueryBuilder.value(), is("123456"));
    }
}