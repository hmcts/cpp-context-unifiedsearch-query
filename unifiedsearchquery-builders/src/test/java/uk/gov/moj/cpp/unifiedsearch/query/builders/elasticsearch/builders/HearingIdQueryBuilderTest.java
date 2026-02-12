package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_ID_PATH;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;

@ExtendWith(MockitoExtension.class)
public class HearingIdQueryBuilderTest {

    @InjectMocks
    private HearingIdQueryBuilder hearingIdQueryBuilder;

    @Test
    public void shouldCreateQueryBuilder() {

        final String hearingIdFilterBy = randomUUID().toString();
        final Query query = hearingIdQueryBuilder.getQueryBuilderBy(hearingIdFilterBy);

        assertThat(query, is(notNullValue()));
        final TermQuery termQueryBuilder = query.term();
        assertThat(termQueryBuilder, notNullValue());

        assertThat(termQueryBuilder.field(), is(HEARING_ID_PATH));
        assertThat(termQueryBuilder.value().stringValue(), is(hearingIdFilterBy));

    }

}