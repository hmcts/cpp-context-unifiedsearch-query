package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATION_TYPE_PATH;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;

public class ApplicationTypeQueryBuilderTest {

    private ApplicationTypeQueryBuilder applicationTypeQueryBuilder = new ApplicationTypeQueryBuilder();


    @Test
    public void shouldCreateQueryBuilder() {

        final String applicationTypeToFilterBy = "Application to test the class";
        final QueryBuilder queryBuilder = applicationTypeQueryBuilder.getQueryBuilderBy(applicationTypeToFilterBy);

        assertThat(queryBuilder, is(notNullValue()));

        assertThat(queryBuilder, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder termQueryBuilder = (TermQueryBuilder) queryBuilder;
        assertThat(termQueryBuilder.getName(), is("term"));
        assertThat(termQueryBuilder.fieldName(), is(APPLICATION_TYPE_PATH));
        assertThat(termQueryBuilder.value(), is(applicationTypeToFilterBy));

    }

}