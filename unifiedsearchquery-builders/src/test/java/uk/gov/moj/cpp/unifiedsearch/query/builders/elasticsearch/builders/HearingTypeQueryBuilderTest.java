package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_TYPE_ID_PATH;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;

public class HearingTypeQueryBuilderTest {

    private HearingTypeQueryBuilder hearingTypeQueryBuilder = new HearingTypeQueryBuilder();


    @Test
    public void shouldCreateQueryBuilder() {

        final String hearingTypeToFilterBy = "TEST HEARING TYPE";
        final QueryBuilder queryBuilder = hearingTypeQueryBuilder.getQueryBuilderBy(hearingTypeToFilterBy);

        assertThat(queryBuilder, is(notNullValue()));
        assertThat(queryBuilder, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder termQueryBuilder = (TermQueryBuilder) queryBuilder;
        assertThat(termQueryBuilder.getName(), is("term"));
        assertThat(termQueryBuilder.fieldName(), is(HEARING_TYPE_ID_PATH));
        assertThat(termQueryBuilder.value(), is(hearingTypeToFilterBy));

    }

}