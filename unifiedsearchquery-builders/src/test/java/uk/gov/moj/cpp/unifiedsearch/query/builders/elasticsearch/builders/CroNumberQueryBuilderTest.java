package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.CRO_NUMBER_INDEX;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;

public class CroNumberQueryBuilderTest {

    private CroNumberSearchQueryBuilder croNumberSearchQueryBuilder = new CroNumberSearchQueryBuilder();


    @Test
    public void shouldCreateExactMatchQueryBuilderForCroNumberNumber() {

        final String croNumber = "croNumber";
        final QueryBuilder queryBuilder = croNumberSearchQueryBuilder.getQueryBuilderBy(croNumber);

        assertThat(queryBuilder, is(notNullValue()));

        assertThat(queryBuilder, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder termQueryBuilder = (TermQueryBuilder) queryBuilder;
        assertThat(termQueryBuilder.getName(), is("term"));
        assertThat(termQueryBuilder.fieldName(), is(CRO_NUMBER_INDEX));
        assertThat(termQueryBuilder.value(), is(croNumber));
    }
}