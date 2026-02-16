package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_BOX_HEARING_PATH;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import org.junit.jupiter.api.Test;

public class BoxHearingQueryBuilderTest {

    private BoxHearingQueryBuilder boxHearingQueryBuilder = new BoxHearingQueryBuilder();


    @Test
    public void shouldCreateQueryBuilder() {

        final String isBoxHearing = "true";
        final Query query = boxHearingQueryBuilder.getQueryBuilderBy(isBoxHearing);

        assertThat(query, is(notNullValue()));
        final TermQuery termQueryBuilder = query.term();
        assertThat(termQueryBuilder, notNullValue());

        assertThat(termQueryBuilder.field(), is(IS_BOX_HEARING_PATH));
        assertThat(termQueryBuilder.value().booleanValue(), is(true));
    }

}