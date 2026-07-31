package uk.gov.moj.cpp.unifiedsearch.query.api.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.Optional;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;


public interface BaseCaseSearchService {

    default Optional<SortOptions> createFieldSort(final String sortOrder, final String sortFieldName, final String sortNestedPath) {
        return createFieldSort(sortOrder, sortFieldName, sortNestedPath, null, null);
    }

    default Optional<SortOptions> createFieldSort(final String sortOrder,
                                                  final String sortFieldName,
                                                  final String sortNestedPath,
                                                  final Query nestedFilter,
                                                  final Integer nestedMaxChildren) {

        if (isNull(sortOrder)){
            return empty();
        }


        final SortOrder esSortOrder = SortOrder.valueOf(uk.gov.moj.cpp.unifiedsearch.query.common.constant.SortOrder.valueOf(sortOrder).getOrder());

        return Optional.of(
                SortOptions.of(s -> s
                        .field(f -> {
                            f.field(sortFieldName)
                                    .order(esSortOrder)
                                    .missing("_last");

                            if (sortNestedPath != null && !sortNestedPath.isEmpty()) {
                                f.nested(n -> {
                                    n.path(sortNestedPath);

                                    if (nestedFilter != null) {
                                        n.filter(nestedFilter);
                                    }

                                    if (nestedMaxChildren != null) {
                                        n.maxChildren(nestedMaxChildren);
                                    }

                                    return n;
                                });
                            }

                            return f;
                        })
                )
        );

    }
}
