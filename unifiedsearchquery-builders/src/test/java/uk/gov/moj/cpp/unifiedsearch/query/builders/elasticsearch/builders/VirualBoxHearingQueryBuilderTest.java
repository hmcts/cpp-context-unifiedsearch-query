package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_VIRTUAL_BOX_HEARING_PATH;

import org.junit.jupiter.api.Test;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;

public class VirualBoxHearingQueryBuilderTest {
    private VirtualBoxHearingQueryBuilder virtualBoxHearingQueryBuilder = new VirtualBoxHearingQueryBuilder();

    @Test
    public void shouldCreateQueryBuilder() {

        final String isVirtualBoxHearing = "true";
        final Query query = virtualBoxHearingQueryBuilder.getQueryBuilderBy(isVirtualBoxHearing);

        assertThat(query, is(notNullValue()));

        assertThat(query.term(), notNullValue());

        final TermQuery termQueryBuilder =  query.term();
        assertThat(termQueryBuilder.field(), is(IS_VIRTUAL_BOX_HEARING_PATH));
        assertThat(termQueryBuilder.value().booleanValue(), is(true));
    }

}