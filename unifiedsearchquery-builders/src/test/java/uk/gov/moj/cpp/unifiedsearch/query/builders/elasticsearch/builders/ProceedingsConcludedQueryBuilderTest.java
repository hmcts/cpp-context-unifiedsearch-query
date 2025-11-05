package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PROCEEDINGS_CONCLUDED_INDEX;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;

public class ProceedingsConcludedQueryBuilderTest {

    private ProceedingsConcludedQueryBuilder proceedingsConcludedQueryBuilder = new ProceedingsConcludedQueryBuilder();


    @Test
    public void shouldCreateQueryBuilderWhenTrue() {

        final String proceedingsConcluded = "true";
        final QueryBuilder queryBuilder = proceedingsConcludedQueryBuilder.getQueryBuilderBy(proceedingsConcluded);

        assertThat(queryBuilder, is(notNullValue()));
        assertThat(queryBuilder, instanceOf(BoolQueryBuilder.class));

        final BoolQueryBuilder boolQueryBuilder = (BoolQueryBuilder) queryBuilder;
        assertThat(boolQueryBuilder.getName(), is("bool"));
        assertThat(boolQueryBuilder.should(), hasSize(0));
        assertThat(boolQueryBuilder.must(), hasSize(1));
        assertThat(boolQueryBuilder.mustNot(), hasSize(0));

        final QueryBuilder firstMustClause = boolQueryBuilder.must().get(0);
        assertThat(firstMustClause, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder firstTerm = (TermQueryBuilder) firstMustClause;
        assertThat(firstTerm.getName(), is("term"));
        assertThat(firstTerm.fieldName(), is(PROCEEDINGS_CONCLUDED_INDEX));
        assertThat(firstTerm.value(), is("true"));
    }


    @Test
    public void shouldCreateQueryBuilderWhenFalse() {

        final String proceedingsConcluded = "false";
        final QueryBuilder queryBuilder = proceedingsConcludedQueryBuilder.getQueryBuilderBy(proceedingsConcluded);

        assertThat(queryBuilder, is(notNullValue()));
        assertThat(queryBuilder, instanceOf((BoolQueryBuilder.class)));

        final BoolQueryBuilder actualBoolQueryBuilder = (BoolQueryBuilder) queryBuilder;
        assertThat(actualBoolQueryBuilder.getName(), is("bool"));
        assertThat(actualBoolQueryBuilder.should(), hasSize(2));
        assertThat(actualBoolQueryBuilder.must(), hasSize(0));
        assertThat(actualBoolQueryBuilder.mustNot(), hasSize(0));

        final QueryBuilder firstShouldClause = actualBoolQueryBuilder.should().get(0);
        final QueryBuilder secondShouldClause = actualBoolQueryBuilder.should().get(1);

        assertThat(firstShouldClause, instanceOf(TermQueryBuilder.class));
        assertThat(secondShouldClause, instanceOf(BoolQueryBuilder.class));

        final TermQueryBuilder firstTerm = (TermQueryBuilder) firstShouldClause;
        assertThat(firstTerm.getName(), is("term"));
        assertThat(firstTerm.fieldName(), is(PROCEEDINGS_CONCLUDED_INDEX));
        assertThat(firstTerm.value(), is("false"));

        final BoolQueryBuilder secondBoolQueryBuilder = (BoolQueryBuilder) secondShouldClause;
        assertThat(secondBoolQueryBuilder.mustNot().size(), is(1));
        ExistsQueryBuilder existsQueryBuilder = (ExistsQueryBuilder) secondBoolQueryBuilder.mustNot().get(0);
        assertThat(existsQueryBuilder.fieldName(), is(PROCEEDINGS_CONCLUDED_INDEX));
    }
}