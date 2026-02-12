package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

import java.util.List;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
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

        final Query actualQuery = partyTypeSearchQueryBuilder.getQueryBuilderBy(" APPLICANT  , DEFENDANT  ");

        assertThat(actualQuery, is(notNullValue()));
        final TermsQuery actualTermsQuery = actualQuery.terms();
        assertThat(actualTermsQuery, notNullValue());


        assertThat(actualTermsQuery.field(), is("parties._party_type"));
        final List<String> queryValues = actualTermsQuery.terms().value().stream().map(FieldValue::stringValue).toList();

        assertThat(queryValues, hasSize(2));
        assertThat(queryValues, hasItems("APPLICANT", "DEFENDANT"));
    }
}