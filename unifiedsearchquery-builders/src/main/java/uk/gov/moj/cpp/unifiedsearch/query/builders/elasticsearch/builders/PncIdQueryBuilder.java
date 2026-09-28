package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders;

import java.time.Year;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termsQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PNC_ID_INDEX;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class PncIdQueryBuilder implements ElasticSearchQueryBuilder {

    private static final Pattern PNC_ID_PATTERN = Pattern.compile(
            "^(\\d{2}(\\d{2})?)[/',.\\-_]?(\\d{7}[A-HJ-NP-RT-Z])$",
            Pattern.CASE_INSENSITIVE);

    private static final String[] YEAR_DELIMITERS = { "", "/", "'", "-", "_", "." };

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final String pncId = valueOf(queryParams[0]);

        final var matcher = PNC_ID_PATTERN.matcher(pncId);
        if (matcher.matches()) {
            // Handle year variations as needed:
            // - When 2-digit year (e.g. 55) is provided, consider current and previous century (e.g. 1955 and 2055).
            // - When 4-digit year (e.g. 1955) is provided, use that exact year.
            final int currentCentury = Year.now(ZoneOffset.UTC).getValue() / 100;
            final int previousCentury = currentCentury - 1;
            final String[] yearVariations = matcher.group(1).length() == 2
                    ? new String[] { currentCentury + matcher.group(1), previousCentury + matcher.group(1) }
                    : new String[] { matcher.group(1) };

            // Handle a recognised PNC ID by searching for variants.
            final var terms = Arrays.stream(yearVariations)
                    .flatMap(year -> Arrays.stream(YEAR_DELIMITERS)
                        .map(delimiter -> year + delimiter + matcher.group(3)))
                    .collect(Collectors.toList());
            return termsQuery(PNC_ID_INDEX, terms).build();
        }
        else {
            // Handle an unrecognised PNC ID by searching as-is.
            return termQuery(PNC_ID_INDEX, pncId).build();
        }
    }
}
