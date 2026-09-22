package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.CRO_NUMBER_INDEX;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import org.junit.jupiter.api.Test;

public class CroNumberQueryBuilderTest {

    private CroNumberSearchQueryBuilder croNumberSearchQueryBuilder = new CroNumberSearchQueryBuilder();


    @Test
    public void shouldCreateExactMatchQueryBuilderForCroNumberNumber() {

        final String croNumber = "croNumber";
        final Query query = croNumberSearchQueryBuilder.getQueryBuilderBy(croNumber);

        assertThat(query, is(notNullValue()));
        final TermQuery termQueryBuilder = query.term();
        assertThat(termQueryBuilder, notNullValue());

        assertThat(termQueryBuilder.field(), is(CRO_NUMBER_INDEX));
        assertThat(termQueryBuilder.value().stringValue(), is(croNumber));
    }
}