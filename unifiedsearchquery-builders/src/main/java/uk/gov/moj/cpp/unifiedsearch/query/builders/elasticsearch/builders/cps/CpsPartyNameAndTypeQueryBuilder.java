package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Stream.of;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;


import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.matchQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.nestedQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.termsQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTY_TYPE_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.ALIAS_PARTY_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.DEFENDANT_PARTY_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.ORGANISATION;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTIES_ALIASES_FIRST_NAME_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTIES_ALIASES_FIRST_NAME_PATH_NGRAMMED;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTIES_ALIASES_LAST_NAME_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTIES_ALIASES_LAST_NAME_PATH_NGRAMMED;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTIES_ALIASES_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTIES_FIRST_NAME_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTIES_FIRST_NAME_PATH_NGRAMMED;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTIES_LAST_NAME_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTIES_LAST_NAME_PATH_NGRAMMED;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTIES_ORGANISATION_NAME_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTIES_ORGANISATION_NAME_PATH_NGRAMMED;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_FIRST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_LAST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_TYPES;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_TYPE_PATH;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;

import java.util.List;
import java.util.Map;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import org.apache.commons.lang3.StringUtils;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class CpsPartyNameAndTypeQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public Query getQueryBuilderBy(final Object... queryParams) {
        final Map<String, String> queryParameters = (Map<String, String>) queryParams[0];

        final String partyFirstName = trimToNull(queryParameters.get(PARTY_FIRST_NAME));
        final String partyLastName = trimToNull(queryParameters.get(PARTY_LAST_NAME));
        final String organisationName = trimToNull(queryParameters.get(ORGANISATION));
        final String partyTypes = trimToNull(queryParameters.get(PARTY_TYPES));

        if (of(partyFirstName, partyLastName, organisationName).anyMatch(StringUtils::isNotBlank)) {

            return ofNullable(partyTypes)
                    .map(types -> {

                        final BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
                        final List<String> requestedPartyTypes = asList(clean(types));

                        ofNullable(partyFirstName).ifPresent(firstName -> addQueryBuilderForNameSearch(boolQueryBuilder, requestedPartyTypes, firstName,
                                PARTIES_FIRST_NAME_PATH, PARTIES_FIRST_NAME_PATH_NGRAMMED, PARTIES_ALIASES_FIRST_NAME_PATH, PARTIES_ALIASES_FIRST_NAME_PATH_NGRAMMED));

                        ofNullable(partyLastName).ifPresent(lastName -> addQueryBuilderForNameSearch(boolQueryBuilder, requestedPartyTypes, lastName,
                                PARTIES_LAST_NAME_PATH, PARTIES_LAST_NAME_PATH_NGRAMMED, PARTIES_ALIASES_LAST_NAME_PATH, PARTIES_ALIASES_LAST_NAME_PATH_NGRAMMED));

                        ofNullable(organisationName).ifPresent(orgName ->
                                boolQueryBuilder.must(buildNameQuery(orgName, PARTIES_ORGANISATION_NAME_PATH, PARTIES_ORGANISATION_NAME_PATH_NGRAMMED).build()));

                        addQueryBuilderForTypeSearch(boolQueryBuilder, requestedPartyTypes);

                        return convertBuilder(boolQueryBuilder).build();

                    }).orElseGet(() -> {

                        final BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

                        ofNullable(partyFirstName).ifPresent(firstName ->
                                boolQueryBuilder.must(buildNameQuery(firstName, PARTIES_FIRST_NAME_PATH, PARTIES_FIRST_NAME_PATH_NGRAMMED).build()));

                        ofNullable(partyLastName).ifPresent(lastName ->
                                boolQueryBuilder.must(buildNameQuery(lastName, PARTIES_LAST_NAME_PATH, PARTIES_LAST_NAME_PATH_NGRAMMED).build()));

                        ofNullable(organisationName).ifPresent(orgName ->
                                boolQueryBuilder.must(buildNameQuery(orgName, PARTIES_ORGANISATION_NAME_PATH, PARTIES_ORGANISATION_NAME_PATH_NGRAMMED).build()));

                        return convertBuilder(boolQueryBuilder).build();
                    });
        }

        return null;
    }

    private void addQueryBuilderForTypeSearch(final BoolQuery.Builder boolQueryBuilder, final List<String> requestedPartyTypes) {
        if (!isSearchForAliasNameOnly(requestedPartyTypes)) {
            boolQueryBuilder.must(termsQuery(PARTY_TYPE_NESTED_PATH, requestedPartyTypes).build());
        }
    }

    private void addQueryBuilderForNameSearch(final BoolQuery.Builder boolQueryBuilder, final List<String> requestedPartyTypes, final String lastName,
                                              final String partiesLastNamePath, final String partiesLastNamePathNgrammed, final String partiesAliasesLastNamePath,
                                              final String partiesAliasesLastNamePathNgrammed) {
        if (isSearchForDefendantAliasOrPartyName(requestedPartyTypes)) {
            boolQueryBuilder.must(buildNameOrAliasQuery(lastName, partiesLastNamePath, partiesLastNamePathNgrammed,
                    partiesAliasesLastNamePath, partiesAliasesLastNamePathNgrammed).build());
        } else if (isSearchForAliasNameOnly(requestedPartyTypes)) {
            boolQueryBuilder.must(buildAliasQuery(lastName, partiesAliasesLastNamePath, partiesAliasesLastNamePathNgrammed).build());
        } else {
            boolQueryBuilder.must(buildNameQuery(lastName, partiesLastNamePath, partiesLastNamePathNgrammed).build());
        }
    }

    private BoolQuery.Builder buildNameOrAliasQuery(final String name, final String path, final String pathNgrammed,
                                                   final String aliasPath, final String aliasPathNgrammed) {
            return (new BoolQuery.Builder())
                    .should(buildNameQuery(name, path, pathNgrammed).build())
                    .should(buildAliasQuery(name, aliasPath, aliasPathNgrammed).build());
    }

    private BoolQuery.Builder buildNameQuery(final String partyName, final String partiesNamePath, final String partiesNamePathNgrammed) {
        return (new BoolQuery.Builder()).should(matchQuery(partiesNamePath, partyName).build())
        .should(matchQuery(partiesNamePathNgrammed, partyName).build());
    }

    private BoolQuery.Builder buildAliasQuery(final String partyName, final String partiesAliasesNamePath, final String partiesAliasesNamePathNgrammed) {
        return (new BoolQuery.Builder())
                .must(nestedQuery(PARTIES_ALIASES_PATH, convertBuilder(buildNameQuery(partyName, partiesAliasesNamePath, partiesAliasesNamePathNgrammed))).build())
                .must(termQuery(PARTY_TYPE_PATH, DEFENDANT_PARTY_TYPE).build());
    }

    private boolean isSearchForAliasNameOnly(final List<String> requestedPartyTypes) {
        return isNotEmpty(requestedPartyTypes) && requestedPartyTypes.size() == 1
                && requestedPartyTypes.contains(ALIAS_PARTY_TYPE);
    }

    private boolean isSearchForDefendantAliasOrPartyName(final List<String> requestedPartyTypes) {
        return isNotEmpty(requestedPartyTypes) && requestedPartyTypes.size() > 1
                && requestedPartyTypes.contains(ALIAS_PARTY_TYPE);
    }
}
