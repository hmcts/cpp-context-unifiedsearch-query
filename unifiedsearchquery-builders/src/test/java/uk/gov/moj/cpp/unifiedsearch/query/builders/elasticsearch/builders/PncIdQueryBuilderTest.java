package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PNC_ID_INDEX;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class PncIdQueryBuilderTest {
    private final PncIdQueryBuilder pncIdQueryBuilder = new PncIdQueryBuilder();

    @ParameterizedTest
    @ValueSource(strings = {
            // Values that are obviously not a PNC ID.
            "123456", "TEST", "SJ123456789",
            // Values which are almost valid PNC IDs.
            "21234567T", "2012345678T", "211234567TQ",
            "2011234567T", "201712345678T", "201234567TQ",
    })
    public void shouldQueryExactMatchForUnrecognisedPncId(final String pncId) {
        final QueryBuilder queryBuilder = pncIdQueryBuilder.getQueryBuilderBy(pncId);

        assertThat(queryBuilder, is(notNullValue()));

        assertThat(queryBuilder, instanceOf(TermQueryBuilder.class));

        final TermQueryBuilder termQueryBuilder = (TermQueryBuilder) queryBuilder;
        assertThat(termQueryBuilder.getName(), is("term"));
        assertThat(termQueryBuilder.fieldName(), is(PNC_ID_INDEX));
        assertThat(termQueryBuilder.value(), is(pncId));
    }

    @ParameterizedTest
    @CsvSource({
            // Delimiter may be "/", "-", "_", ".", "'", or omitted.
            // Input does not include century; consider current and previous centuries:
            "171234567T, 19171234567T 1917/1234567T 1917-1234567T 1917_1234567T 1917.1234567T 1917'1234567T 20171234567T 2017/1234567T 2017-1234567T 2017_1234567T 2017.1234567T 2017'1234567T",
            "17/1234567T, 19171234567T 1917/1234567T 1917-1234567T 1917_1234567T 1917.1234567T 1917'1234567T 20171234567T 2017/1234567T 2017-1234567T 2017_1234567T 2017.1234567T 2017'1234567T",
            "17-1234567T, 19171234567T 1917/1234567T 1917-1234567T 1917_1234567T 1917.1234567T 1917'1234567T 20171234567T 2017/1234567T 2017-1234567T 2017_1234567T 2017.1234567T 2017'1234567T",
            "17_1234567T, 19171234567T 1917/1234567T 1917-1234567T 1917_1234567T 1917.1234567T 1917'1234567T 20171234567T 2017/1234567T 2017-1234567T 2017_1234567T 2017.1234567T 2017'1234567T",
            "17.1234567T, 19171234567T 1917/1234567T 1917-1234567T 1917_1234567T 1917.1234567T 1917'1234567T 20171234567T 2017/1234567T 2017-1234567T 2017_1234567T 2017.1234567T 2017'1234567T",
            "17'1234567T, 19171234567T 1917/1234567T 1917-1234567T 1917_1234567T 1917.1234567T 1917'1234567T 20171234567T 2017/1234567T 2017-1234567T 2017_1234567T 2017.1234567T 2017'1234567T",
            // Input is an exact year; use as-is:
            "20171234567T, 20171234567T 2017/1234567T 2017-1234567T 2017_1234567T 2017.1234567T 2017'1234567T",
            "2017/1234567T, 20171234567T 2017/1234567T 2017-1234567T 2017_1234567T 2017.1234567T 2017'1234567T",
            "2017-1234567T, 20171234567T 2017/1234567T 2017-1234567T 2017_1234567T 2017.1234567T 2017'1234567T",
            "2017_1234567T, 20171234567T 2017/1234567T 2017-1234567T 2017_1234567T 2017.1234567T 2017'1234567T",
            "2017.1234567T, 20171234567T 2017/1234567T 2017-1234567T 2017_1234567T 2017.1234567T 2017'1234567T",
            "2017'1234567T, 20171234567T 2017/1234567T 2017-1234567T 2017_1234567T 2017.1234567T 2017'1234567T",
    })
    public void shouldQueryVariantsForRecognisedPncId(final String inputPncId,
                                                      final String expectedTermsRaw) {
        final QueryBuilder queryBuilder = pncIdQueryBuilder.getQueryBuilderBy(inputPncId);

        assertThat(queryBuilder, is(notNullValue()));

        assertThat(queryBuilder, instanceOf(TermsQueryBuilder.class));

        final var expectedTerms = expectedTermsRaw.split(" ");

        final TermsQueryBuilder termQueryBuilder = (TermsQueryBuilder) queryBuilder;
        assertThat(termQueryBuilder.getName(), is("terms"));
        assertThat(termQueryBuilder.fieldName(), is(PNC_ID_INDEX));
        assertThat(termQueryBuilder.values(), hasItems(expectedTerms));
    }
}
