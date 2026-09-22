package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PNC_ID_INDEX;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import org.junit.jupiter.api.Test;

public class PncIdQueryBuilderTest {
    private PncIdQueryBuilder pncIdQueryBuilder = new PncIdQueryBuilder();


    @Test
    public void shouldCreateExactMatchQueryBuilderForPncIdNumber() {

        final String pncId = "123456";
        final Query query = pncIdQueryBuilder.getQueryBuilderBy(pncId);

        assertThat(query, is(notNullValue()));
        final TermQuery termQueryBuilder = query.term();
        assertThat(termQueryBuilder, notNullValue());

        assertThat(termQueryBuilder.field(), is(PNC_ID_INDEX));
        assertThat(termQueryBuilder.value().stringValue(), is("123456"));
    }
}