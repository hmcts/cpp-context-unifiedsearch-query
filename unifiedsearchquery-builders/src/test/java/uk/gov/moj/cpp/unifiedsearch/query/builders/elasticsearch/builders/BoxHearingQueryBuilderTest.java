package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_BOX_HEARING_PATH;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;

public class BoxHearingQueryBuilderTest {

    private BoxHearingQueryBuilder boxHearingQueryBuilder = new BoxHearingQueryBuilder();


    @Test
    public void shouldCreateQueryBuilder() {

        final String isBoxHearing = "true";
        final QueryBuilder queryBuilder = boxHearingQueryBuilder.getQueryBuilderBy(isBoxHearing);

        assertThat(queryBuilder, is(notNullValue()));

        assertThat(queryBuilder, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder termQueryBuilder = (TermQueryBuilder) queryBuilder;
        assertThat(termQueryBuilder.getName(), is("term"));
        assertThat(termQueryBuilder.fieldName(), is(IS_BOX_HEARING_PATH));
        assertThat(termQueryBuilder.value(), is(true));
    }

}