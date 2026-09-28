package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.google.common.collect.ImmutableList.of;
import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.ChildScoreMode;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.search.ScoreMode;

public class ReferenceSearchQueryBuilderTest {


    private ReferenceSearchQueryBuilder referenceSearchQueryBuilder = new ReferenceSearchQueryBuilder();


    @Test
    public void shouldReturnValidQueryBuilderReference() {
        final String caseReference = "TFF123";
        final Query actualQuery = referenceSearchQueryBuilder.getQueryBuilderBy(caseReference, emptyList());

        assertThat(actualQuery, is(notNullValue()));
        final BoolQuery actualBoolQueryBuilder = actualQuery.bool();
        assertThat(actualBoolQueryBuilder, notNullValue());


        assertThat(actualBoolQueryBuilder.must(), hasSize(0));
        assertThat(actualBoolQueryBuilder.should(), hasSize(2));

        assertThat(actualBoolQueryBuilder.filter(), hasSize(0));

        final Query firstShould = actualBoolQueryBuilder.should().get(0);

        assertThat(firstShould.term(), notNullValue());

        final TermQuery firstTermShould = firstShould.term();
        assertThat(firstTermShould.field(), is("caseReference"));
        assertThat(firstTermShould.value().stringValue(), is(caseReference));

        final Query secondShould = actualBoolQueryBuilder.should().get(1);
        assertThat(secondShould.nested(), notNullValue());
        final NestedQuery actualNestedQueryBuilder = secondShould.nested();
        assertThat(actualNestedQueryBuilder.scoreMode().name(), is(ScoreMode.Avg.name()));

        final BoolQuery innedBoolQueryBuilder = actualNestedQueryBuilder.query().bool();
        assertThat(innedBoolQueryBuilder.mustNot(), hasSize(0));
        assertThat(innedBoolQueryBuilder.should(), hasSize(0));
        assertThat(innedBoolQueryBuilder.must(), hasSize(1));
        final Query firstMust = innedBoolQueryBuilder.must().get(0);
        assertThat(firstMust.term(), notNullValue());
        final TermQuery actualNestedTermQueryBuilder = firstMust.term();
        assertThat(actualNestedTermQueryBuilder.field(), is("applications.applicationReference"));
        assertThat(actualNestedTermQueryBuilder.value().stringValue(), is(caseReference));

    }

    @Test
    public void shouldReturnValidQueryBuilderReferenceWithApplicationType() {
        final String caseReference = "TFF123";
        final Query applicationTypeQuery = new ApplicationTypeQueryBuilder().getQueryBuilderBy("Some Application Type");

        final Query actualQuery = referenceSearchQueryBuilder.getQueryBuilderBy(caseReference, of(applicationTypeQuery));

        assertThat(actualQuery, is(notNullValue()));
        assertThat(actualQuery.bool(), notNullValue());

        final BoolQuery actualBoolQueryBuilder = actualQuery.bool();
        assertThat(actualBoolQueryBuilder.must(), hasSize(0));
        assertThat(actualBoolQueryBuilder.should(), hasSize(2));

        assertThat(actualBoolQueryBuilder.filter(), hasSize(0));

        final Query firstShould = actualBoolQueryBuilder.should().get(0);

        assertThat(firstShould.term(), notNullValue());

        final TermQuery firstTermShould = firstShould.term();
        assertThat(firstTermShould.field(), is("caseReference"));
        assertThat(firstTermShould.value().stringValue(), is(caseReference));

        final Query secondShould = actualBoolQueryBuilder.should().get(1);
        assertThat(secondShould.nested(), notNullValue());
        final NestedQuery actualNestedQueryBuilder = secondShould.nested();
        assertThat(actualNestedQueryBuilder.scoreMode(), is(ChildScoreMode.Avg));

        final BoolQuery innedBoolQueryBuilder = actualNestedQueryBuilder.query().bool();
        assertThat(innedBoolQueryBuilder.mustNot(), hasSize(0));
        assertThat(innedBoolQueryBuilder.should(), hasSize(0));
        assertThat(innedBoolQueryBuilder.must(), hasSize(2));

        final Query firstMust = innedBoolQueryBuilder.must().get(0);
        assertThat(firstMust.term(), notNullValue());
        final TermQuery actualNestedTermQueryBuilder = firstMust.term();
        assertThat(actualNestedTermQueryBuilder.field(), is("applications.applicationReference"));
        assertThat(actualNestedTermQueryBuilder.value().stringValue(), is(caseReference));

        final Query secondMust = innedBoolQueryBuilder.must().get(1);

        assertThat(secondMust, is(applicationTypeQuery));

    }
}