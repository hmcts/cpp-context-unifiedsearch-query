package uk.gov.moj.cpp.unifiedsearch.query.api.service;

import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;
import static org.elasticsearch.search.sort.SortOrder.fromString;

import java.util.Optional;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.NestedSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

public interface BaseCaseSearchService {

    default Optional<FieldSortBuilder> createFieldSort(final String sortOrder, final String sortFieldName, final String sortNestedPath) {
        return createFieldSort(sortOrder, sortFieldName, sortNestedPath, null, null);
    }

    default Optional<FieldSortBuilder> createFieldSort(final String sortOrder,
                                                       final String sortFieldName,
                                                       final String sortNestedPath,
                                                       final QueryBuilder nestedFilter,
                                                       final Integer nestedMaxChildren) {

        if (nonNull(sortOrder)) {
            final SortOrder esSortOrder = fromString(sortOrder);
            final FieldSortBuilder fieldSortBuilder = fieldSort(sortFieldName)
                    .missing("_last")
                    .order(esSortOrder);

            if (isNotEmpty(sortNestedPath)) {
                final NestedSortBuilder nestedSortBuilder = new NestedSortBuilder(sortNestedPath);
                setNestedFilter(nestedFilter, nestedMaxChildren, nestedSortBuilder);

                fieldSortBuilder.setNestedSort(nestedSortBuilder);
            }


            return Optional.of(fieldSortBuilder);
        }


        return empty();
    }

    default void setNestedFilter(final QueryBuilder nestedFilter, final Integer nestedMaxChildren, final NestedSortBuilder nestedSortBuilder) {
        if (nestedFilter != null) {
            nestedSortBuilder.setFilter(nestedFilter);
            if (nestedMaxChildren != null) {
                nestedSortBuilder.setMaxChildren(nestedMaxChildren);
            }
        }
    }
}
