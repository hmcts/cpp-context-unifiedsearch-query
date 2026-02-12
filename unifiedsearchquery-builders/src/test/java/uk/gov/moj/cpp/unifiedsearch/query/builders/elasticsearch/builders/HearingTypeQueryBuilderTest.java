package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_TYPE_ID_PATH;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import org.junit.jupiter.api.Test;

public class HearingTypeQueryBuilderTest {

    private HearingTypeQueryBuilder hearingTypeQueryBuilder = new HearingTypeQueryBuilder();


    @Test
    public void shouldCreateQueryBuilder() {

        final String hearingTypeToFilterBy = "TEST HEARING TYPE";
        final Query query = hearingTypeQueryBuilder.getQueryBuilderBy(hearingTypeToFilterBy);

        assertThat(query, is(notNullValue()));
        final TermQuery termQueryBuilder = query.term();
        assertThat(termQueryBuilder, notNullValue());

        assertThat(termQueryBuilder.field(), is(HEARING_TYPE_ID_PATH));
        assertThat(termQueryBuilder.value().stringValue(), is(hearingTypeToFilterBy));

    }

}