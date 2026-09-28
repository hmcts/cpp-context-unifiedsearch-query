package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PNC_ID_INDEX;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;

import java.util.List;

public class PncIdQueryBuilderTest {
    private final PncIdQueryBuilder pncIdQueryBuilder = new PncIdQueryBuilder();

    @ParameterizedTest
    @ValueSource(strings = {
            // Values that are obviously not a PNC ID.
            "123456", "TEST", "SJ123456789",
            // Values which are almost valid PNC IDs.
            "21234567T", "2012345678T", "211234567TQ", "171234567I", "171234567O", "171234567S",
            "2011234567T", "201712345678T", "201234567TQ", "20171234567I", "20171234567O", "20171234567S",
    })
    public void shouldQueryExactMatchForUnrecognisedPncId(final String inputPncId) {
        final Query actualQuery = pncIdQueryBuilder.getQueryBuilderBy(inputPncId);
        assertThat(actualQuery, is(notNullValue()));

        final TermQuery actualTermQuery = actualQuery.term();
        assertThat(actualTermQuery, notNullValue());

        assertThat(actualTermQuery.field(), is(PNC_ID_INDEX));
        assertThat(actualTermQuery.value().stringValue(), is(inputPncId));
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
        final Query actualQuery = pncIdQueryBuilder.getQueryBuilderBy(inputPncId);
        assertThat(actualQuery, is(notNullValue()));

        final TermsQuery actualTermsQuery = actualQuery.terms();
        assertThat(actualTermsQuery, is(notNullValue()));

        assertThat(actualTermsQuery.field(), is(PNC_ID_INDEX));
        final List<String> queryValues = actualTermsQuery.terms().value().stream().map(FieldValue::stringValue).toList();

        final var expectedTerms = expectedTermsRaw.split(" ");
        assertThat(queryValues, hasSize(expectedTerms.length));
        assertThat(queryValues, hasItems(expectedTerms));
    }
}
