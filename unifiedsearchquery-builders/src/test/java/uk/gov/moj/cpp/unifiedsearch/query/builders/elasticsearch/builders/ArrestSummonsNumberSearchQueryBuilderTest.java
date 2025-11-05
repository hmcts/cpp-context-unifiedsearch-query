package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTIES_ASN_REFERENCE_PATH;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
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

        final QueryBuilder actualQueryBuilder = arrestSummonsNumberSearchQueryBuilder.getQueryBuilderBy(asn);

        assertThat(actualQueryBuilder, is(notNullValue()));

        assertThat(actualQueryBuilder, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder termQueryBuilder = (TermQueryBuilder) actualQueryBuilder;
        assertThat(termQueryBuilder.getName(), is("term"));
        assertThat(termQueryBuilder.fieldName(), is(PARTIES_ASN_REFERENCE_PATH));
        assertThat(termQueryBuilder.value(), is(asn));
    }
}