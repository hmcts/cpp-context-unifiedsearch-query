package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.google.common.collect.ImmutableList.of;
import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;

public class ReferenceSearchQueryBuilderTest {


    private ReferenceSearchQueryBuilder referenceSearchQueryBuilder = new ReferenceSearchQueryBuilder();


    @Test
    public void shouldReturnValidQueryBuilderReference() {
        final String caseReference = "TFF123";
        final QueryBuilder actualQueryBuilder = referenceSearchQueryBuilder.getQueryBuilderBy(caseReference, emptyList());

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf((BoolQueryBuilder.class)));

        final BoolQueryBuilder actualBoolQueryBuilder = (BoolQueryBuilder) actualQueryBuilder;
        assertThat(actualBoolQueryBuilder.getName(), is("bool"));
        assertThat(actualBoolQueryBuilder.must(), hasSize(0));
        assertThat(actualBoolQueryBuilder.should(), hasSize(2));

        assertThat(actualBoolQueryBuilder.filter(), hasSize(0));

        final QueryBuilder firstShould = actualBoolQueryBuilder.should().get(0);

        assertThat(firstShould, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder firstTermShould = (TermQueryBuilder) firstShould;
        assertThat(firstTermShould.getName(), is("term"));
        assertThat(firstTermShould.fieldName(), is("caseReference"));
        assertThat(firstTermShould.value(), is(caseReference));

        final QueryBuilder secondShould = actualBoolQueryBuilder.should().get(1);
        assertThat(secondShould, instanceOf(NestedQueryBuilder.class));
        final NestedQueryBuilder actualNestedQueryBuilder = (NestedQueryBuilder) secondShould;
        assertThat(actualNestedQueryBuilder.scoreMode(), is(ScoreMode.Avg));

        final BoolQueryBuilder innedBoolQueryBuilder = (BoolQueryBuilder) actualNestedQueryBuilder.query();
        assertThat(innedBoolQueryBuilder.mustNot(), hasSize(0));
        assertThat(innedBoolQueryBuilder.should(), hasSize(0));
        assertThat(innedBoolQueryBuilder.must(), hasSize(1));
        final QueryBuilder firstMust = innedBoolQueryBuilder.must().get(0);
        assertThat(firstMust, instanceOf(TermQueryBuilder.class));
        final TermQueryBuilder actualNestedTermQueryBuilder = (TermQueryBuilder) firstMust;
        assertThat(actualNestedTermQueryBuilder.getName(), is("term"));
        assertThat(actualNestedTermQueryBuilder.fieldName(), is("applications.applicationReference"));
        assertThat(actualNestedTermQueryBuilder.value(), is(caseReference));

    }

    @Test
    public void shouldReturnValidQueryBuilderReferenceWithApplicationType() {
        final String caseReference = "TFF123";
        final QueryBuilder applicationTypeQueryBuilder = new ApplicationTypeQueryBuilder().getQueryBuilderBy("Some Application Type");

        final QueryBuilder actualQueryBuilder = referenceSearchQueryBuilder.getQueryBuilderBy(caseReference, of(applicationTypeQueryBuilder));

        assertThat(actualQueryBuilder, is(notNullValue()));
        assertThat(actualQueryBuilder, instanceOf((BoolQueryBuilder.class)));

        final BoolQueryBuilder actualBoolQueryBuilder = (BoolQueryBuilder) actualQueryBuilder;
        assertThat(actualBoolQueryBuilder.getName(), is("bool"));
        assertThat(actualBoolQueryBuilder.must(), hasSize(0));
        assertThat(actualBoolQueryBuilder.should(), hasSize(2));

        assertThat(actualBoolQueryBuilder.filter(), hasSize(0));

        final QueryBuilder firstShould = actualBoolQueryBuilder.should().get(0);

        assertThat(firstShould, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder firstTermShould = (TermQueryBuilder) firstShould;
        assertThat(firstTermShould.getName(), is("term"));
        assertThat(firstTermShould.fieldName(), is("caseReference"));
        assertThat(firstTermShould.value(), is(caseReference));

        final QueryBuilder secondShould = actualBoolQueryBuilder.should().get(1);
        assertThat(secondShould, instanceOf(NestedQueryBuilder.class));
        final NestedQueryBuilder actualNestedQueryBuilder = (NestedQueryBuilder) secondShould;
        assertThat(actualNestedQueryBuilder.scoreMode(), is(ScoreMode.Avg));

        final BoolQueryBuilder innedBoolQueryBuilder = (BoolQueryBuilder) actualNestedQueryBuilder.query();
        assertThat(innedBoolQueryBuilder.mustNot(), hasSize(0));
        assertThat(innedBoolQueryBuilder.should(), hasSize(0));
        assertThat(innedBoolQueryBuilder.must(), hasSize(2));

        final QueryBuilder firstMust = innedBoolQueryBuilder.must().get(0);
        assertThat(firstMust, instanceOf(TermQueryBuilder.class));
        final TermQueryBuilder actualNestedTermQueryBuilder = (TermQueryBuilder) firstMust;
        assertThat(actualNestedTermQueryBuilder.getName(), is("term"));
        assertThat(actualNestedTermQueryBuilder.fieldName(), is("applications.applicationReference"));
        assertThat(actualNestedTermQueryBuilder.value(), is(caseReference));

        final QueryBuilder secondMust = innedBoolQueryBuilder.must().get(1);

        assertThat(secondMust, is(applicationTypeQueryBuilder));

    }
}