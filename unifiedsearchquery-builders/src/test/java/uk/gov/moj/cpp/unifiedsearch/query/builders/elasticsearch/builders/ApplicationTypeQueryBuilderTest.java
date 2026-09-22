package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATION_TYPE_PATH;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import org.junit.jupiter.api.Test;

public class ApplicationTypeQueryBuilderTest {

    private ApplicationTypeQueryBuilder applicationTypeQueryBuilder = new ApplicationTypeQueryBuilder();


    @Test
    public void shouldCreateQueryBuilder() {

        final String applicationTypeToFilterBy = "Application to test the class";
        final Query query = applicationTypeQueryBuilder.getQueryBuilderBy(applicationTypeToFilterBy);

        assertThat(query, is(notNullValue()));
        final TermQuery termQueryBuilder = query.term();
        assertThat(termQueryBuilder, notNullValue());


        assertThat(termQueryBuilder.field(), is(APPLICATION_TYPE_PATH));
        assertThat(termQueryBuilder.value().stringValue(), is(applicationTypeToFilterBy));

    }

}