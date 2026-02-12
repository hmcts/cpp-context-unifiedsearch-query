package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_CROWN_COURT;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_MAGISTRATE_COURT;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import org.junit.jupiter.api.Test;

public class CrownOrMagistratesQueryBuilderTest {

    @Test
    public void shouldCreateQueryBuilderWhenCrownOrMagistratesIsSet() {
        final CrownOrMagistratesQueryBuilder crownOrMagistratesQueryBuilder = new CrownOrMagistratesQueryBuilder();

        final String crownOrMagistratesParam = "true";
        final Query query = crownOrMagistratesQueryBuilder.getQueryBuilderBy(crownOrMagistratesParam);

        assertThat(query, is(notNullValue()));
        final BoolQuery actualBoolQueryBuilder = query.bool();
        assertThat(actualBoolQueryBuilder, instanceOf((BoolQuery.class)));


        assertThat(actualBoolQueryBuilder.must(), hasSize(0));
        assertThat(actualBoolQueryBuilder.should(), hasSize(2));

        final Query firstShouldClause = actualBoolQueryBuilder.should().get(0);
        final Query secondShouldClause = actualBoolQueryBuilder.should().get(1);

        assertThat(firstShouldClause.term(), instanceOf(TermQuery.class));
        assertThat(secondShouldClause.term(), instanceOf(TermQuery.class));

        final TermQuery firstTerm = firstShouldClause.term();
        assertThat(firstTerm.field(), is(IS_CROWN_COURT));
        assertThat(firstTerm.value().booleanValue(), is(true));

        final TermQuery secondTerm = secondShouldClause.term();
        assertThat(secondTerm.field(), is(IS_MAGISTRATE_COURT));
        assertThat(secondTerm.value().booleanValue(), is(true));
    }

    @Test
    public void shouldCreateQueryBuilderWhenCrownOrMagistratesIsFalseNotSet() {
        final CrownOrMagistratesQueryBuilder crownOrMagistratesQueryBuilder = new CrownOrMagistratesQueryBuilder();

        final String crownOrMagistratesParam = "false";
        final Query query = crownOrMagistratesQueryBuilder.getQueryBuilderBy(crownOrMagistratesParam);
        assertThat(query, is(notNullValue()));

        final BoolQuery actualBoolQueryBuilder = query.bool();
        assertThat(actualBoolQueryBuilder.must(), hasSize(2));
        assertThat(actualBoolQueryBuilder.should(), hasSize(0));

        final Query firstMustClause = actualBoolQueryBuilder.must().get(0);
        final Query secondShouldClause = actualBoolQueryBuilder.must().get(1);

        assertThat(firstMustClause.term(), instanceOf(TermQuery.class));
        assertThat(secondShouldClause.term(), instanceOf(TermQuery.class));

        final TermQuery firstTerm = firstMustClause.term();
        assertThat(firstTerm.field(), is(IS_CROWN_COURT));
        assertThat(firstTerm.value().booleanValue(), is(false));

        final TermQuery secondTerm = secondShouldClause.term();
        assertThat(secondTerm.field(), is(IS_MAGISTRATE_COURT));
        assertThat(secondTerm.value().booleanValue(), is(false));
    }

}