package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PartyTypeSearchQueryBuilderTest {

    private PartyTypeSearchQueryBuilder partyTypeSearchQueryBuilder;

    @BeforeEach
    public void setUp() {
        this.partyTypeSearchQueryBuilder = new PartyTypeSearchQueryBuilder();
    }


    @Test
    public void shouldGetQueryBuilderBy() {

        final QueryBuilder actualQueryBuilder = partyTypeSearchQueryBuilder.getQueryBuilderBy(" APPLICANT  , DEFENDANT  ");

        assertThat(actualQueryBuilder, is(notNullValue()));

        assertThat(actualQueryBuilder, instanceOf(TermsQueryBuilder.class));

        final TermsQueryBuilder actualTermsQuery = (TermsQueryBuilder) actualQueryBuilder;

        assertThat(actualTermsQuery.fieldName(), is("parties._party_type"));
        final List<Object> queryValues = actualTermsQuery.values();

        assertThat(queryValues, hasSize(2));
        assertThat(queryValues, hasItems("APPLICANT", "DEFENDANT"));
    }
}