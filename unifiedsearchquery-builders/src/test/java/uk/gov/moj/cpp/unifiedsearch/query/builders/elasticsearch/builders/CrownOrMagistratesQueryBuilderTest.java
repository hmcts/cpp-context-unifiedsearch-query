package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_CROWN_COURT;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_MAGISTRATE_COURT;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;

public class CrownOrMagistratesQueryBuilderTest {

    @Test
    public void shouldCreateQueryBuilderWhenCrownOrMagistratesIsSet() {
        final CrownOrMagistratesQueryBuilder crownOrMagistratesQueryBuilder = new CrownOrMagistratesQueryBuilder();

        final String crownOrMagistratesParam = "true";
        final QueryBuilder queryBuilder = crownOrMagistratesQueryBuilder.getQueryBuilderBy(crownOrMagistratesParam);

        assertThat(queryBuilder, is(notNullValue()));
        assertThat(queryBuilder, instanceOf((BoolQueryBuilder.class)));

        final BoolQueryBuilder actualBoolQueryBuilder = (BoolQueryBuilder) queryBuilder;
        assertThat(actualBoolQueryBuilder.getName(), is("bool"));
        assertThat(actualBoolQueryBuilder.must(), hasSize(0));
        assertThat(actualBoolQueryBuilder.should(), hasSize(2));

        final QueryBuilder firstShouldClause = actualBoolQueryBuilder.should().get(0);
        final QueryBuilder secondShouldClause = actualBoolQueryBuilder.should().get(1);

        assertThat(firstShouldClause, instanceOf(TermQueryBuilder.class));
        assertThat(secondShouldClause, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder firstTerm = (TermQueryBuilder) firstShouldClause;
        assertThat(firstTerm.getName(), is("term"));
        assertThat(firstTerm.fieldName(), is(IS_CROWN_COURT));
        assertThat(firstTerm.value(), is(true));

        final TermQueryBuilder secondTerm = (TermQueryBuilder) secondShouldClause;
        assertThat(secondTerm.getName(), is("term"));
        assertThat(secondTerm.fieldName(), is(IS_MAGISTRATE_COURT));
        assertThat(secondTerm.value(), is(true));
    }

    @Test
    public void shouldCreateQueryBuilderWhenCrownOrMagistratesIsFalseNotSet() {
        final CrownOrMagistratesQueryBuilder crownOrMagistratesQueryBuilder = new CrownOrMagistratesQueryBuilder();

        final String crownOrMagistratesParam = "false";
        final QueryBuilder queryBuilder = crownOrMagistratesQueryBuilder.getQueryBuilderBy(crownOrMagistratesParam);
        assertThat(queryBuilder, is(notNullValue()));
        assertThat(queryBuilder, instanceOf((BoolQueryBuilder.class)));

        final BoolQueryBuilder actualBoolQueryBuilder = (BoolQueryBuilder) queryBuilder;
        assertThat(actualBoolQueryBuilder.getName(), is("bool"));
        assertThat(actualBoolQueryBuilder.must(), hasSize(2));
        assertThat(actualBoolQueryBuilder.should(), hasSize(0));

        final QueryBuilder firstMustClause = actualBoolQueryBuilder.must().get(0);
        final QueryBuilder secondShouldClause = actualBoolQueryBuilder.must().get(1);

        assertThat(firstMustClause, instanceOf(TermQueryBuilder.class));
        assertThat(secondShouldClause, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder firstTerm = (TermQueryBuilder) firstMustClause;
        assertThat(firstTerm.getName(), is("term"));
        assertThat(firstTerm.fieldName(), is(IS_CROWN_COURT));
        assertThat(firstTerm.value(), is(false));

        final TermQueryBuilder secondTerm = (TermQueryBuilder) secondShouldClause;
        assertThat(secondTerm.getName(), is("term"));
        assertThat(secondTerm.fieldName(), is(IS_MAGISTRATE_COURT));
        assertThat(secondTerm.value(), is(false));
    }

}