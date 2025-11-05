package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_ID_PATH;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HearingIdQueryBuilderTest {

    @InjectMocks
    private HearingIdQueryBuilder hearingIdQueryBuilder;

    @Test
    public void shouldCreateQueryBuilder() {

        final String hearingIdFilterBy = randomUUID().toString();
        final QueryBuilder queryBuilder = hearingIdQueryBuilder.getQueryBuilderBy(hearingIdFilterBy);

        assertThat(queryBuilder, is(notNullValue()));
        assertThat(queryBuilder, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder termQueryBuilder = (TermQueryBuilder) queryBuilder;
        assertThat(termQueryBuilder.getName(), is("term"));
        assertThat(termQueryBuilder.fieldName(), is(HEARING_ID_PATH));
        assertThat(termQueryBuilder.value(), is(hearingIdFilterBy));

    }

}