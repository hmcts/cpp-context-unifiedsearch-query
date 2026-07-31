package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_MAGISTRATE_COURT;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_SJP;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import org.junit.jupiter.api.Test;

public class JurisdictionTypeQueryBuilderTest {

    @Test
    public void shouldCreateQueryBuilderWhenOneJurisdictionTypeIsSet() {
        final JurisdictionTypeQueryBuilder jurisdictionTypeQueryBuilder = new JurisdictionTypeQueryBuilder();

        final String isSjp = "true";
        final String isMagistrateCourt = "false";
        final String isCrownCourt = "false";
        final Query query = jurisdictionTypeQueryBuilder.getQueryBuilderBy(isSjp, isMagistrateCourt, isCrownCourt)    ;

        assertThat(query, is(notNullValue()));

        final BoolQuery actualBoolQueryBuilder = query.bool();
        assertThat(actualBoolQueryBuilder.must(), hasSize(0));
        assertThat(actualBoolQueryBuilder.should(), hasSize(1));

        final Query firstShouldClause = actualBoolQueryBuilder.should().get(0);
        final TermQuery firstTerm = firstShouldClause.term();
        assertThat(firstTerm.field(), is(IS_SJP));
        assertThat(firstTerm.value().booleanValue(), is(true));
    }

    @Test
    public void shouldCreateQueryBuilderWhenMoreThanOneJurisdictionTypeIsSet() {
        final JurisdictionTypeQueryBuilder jurisdictionTypeQueryBuilder = new JurisdictionTypeQueryBuilder();

        final String isSjp = "true";
        final String isMagistrateCourt = "true";
        final String isCrownCourt = "false";
        final Query query = jurisdictionTypeQueryBuilder.getQueryBuilderBy(isSjp, isMagistrateCourt, isCrownCourt)    ;

        assertThat(query, is(notNullValue()));

        final BoolQuery actualBoolQueryBuilder = query.bool();
        assertThat(actualBoolQueryBuilder.must(), hasSize(0));
        assertThat(actualBoolQueryBuilder.should(), hasSize(2));

        final Query firstShouldClause = actualBoolQueryBuilder.should().get(0);
        final Query secondShouldClause = actualBoolQueryBuilder.should().get(1);


        final TermQuery firstTerm =  firstShouldClause.term();
        assertThat(firstTerm.field(), is(IS_SJP));
        assertThat(firstTerm.value().booleanValue(), is(true));

        final TermQuery secondTerm = secondShouldClause.term();
        assertThat(secondTerm.field(), is(IS_MAGISTRATE_COURT));
        assertThat(secondTerm.value().booleanValue(), is(true));
    }
}