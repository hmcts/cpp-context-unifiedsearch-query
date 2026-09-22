package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTIES_ASN_REFERENCE_PATH;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArrestSummonsNumberSearchQueryBuilderTest {

    private ArrestSummonsNumberSearchQueryBuilder arrestSummonsNumberSearchQueryBuilder;

    @BeforeEach
    public void setUp() {
        arrestSummonsNumberSearchQueryBuilder = new ArrestSummonsNumberSearchQueryBuilder();
    }

    @Test
    public void shouldReturnValidQueryBuilderForHearingDateFrom() {
        final String asn = "12345678ABCD";

        final Query actualQuery = arrestSummonsNumberSearchQueryBuilder.getQueryBuilderBy(asn);

        assertThat(actualQuery, is(notNullValue()));
        final TermQuery termQueryBuilder = actualQuery.term();
        assertThat(termQueryBuilder, notNullValue());

        assertThat(termQueryBuilder.field(), is(PARTIES_ASN_REFERENCE_PATH));
        assertThat(termQueryBuilder.value().stringValue(), is(asn));
    }
}