package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTIES_NINO_REFERENCE_PATH;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NinoSearchQueryBuilderTest {

    private NinoSearchQueryBuilder ninoSearchQueryBuilder;

    @BeforeEach
    public void setUp() {
        ninoSearchQueryBuilder = new NinoSearchQueryBuilder();
    }

    @Test
    public void shouldReturnValidQueryBuilderForHearingDateFrom() {
        final String nino = "AB123456Z";

        final Query actualQuery = ninoSearchQueryBuilder.getQueryBuilderBy(nino);

        assertThat(actualQuery, is(notNullValue()));
        final TermQuery termQueryBuilder = actualQuery.term();
        assertThat(actualQuery, notNullValue());

        assertThat(termQueryBuilder.field(), is(PARTIES_NINO_REFERENCE_PATH));
        assertThat(termQueryBuilder.value().stringValue(), is(nino));
    }
}