package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Stream.of;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.apache.lucene.search.join.ScoreMode.Avg;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
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

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

public class CpsPartyNameAndTypeQueryBuilder implements ElasticSearchQueryBuilder {

    @Override
    public QueryBuilder getQueryBuilderBy(final Object... queryParams) {
        final Map<String, String> queryParameters = (Map<String, String>) queryParams[0];

        final String partyFirstName = trimToNull(queryParameters.get(PARTY_FIRST_NAME));
        final String partyLastName = trimToNull(queryParameters.get(PARTY_LAST_NAME));
        final String organisationName = trimToNull(queryParameters.get(ORGANISATION));
        final String partyTypes = trimToNull(queryParameters.get(PARTY_TYPES));

        if (of(partyFirstName, partyLastName, organisationName).anyMatch(StringUtils::isNotBlank)) {

            return ofNullable(partyTypes)
                    .map(types -> {

                        final BoolQueryBuilder boolQueryBuilder = boolQuery();
                        final List<String> requestedPartyTypes = asList(clean(types));

                        ofNullable(partyFirstName).ifPresent(firstName -> addQueryBuilderForNameSearch(boolQueryBuilder, requestedPartyTypes, firstName,
                                PARTIES_FIRST_NAME_PATH, PARTIES_FIRST_NAME_PATH_NGRAMMED, PARTIES_ALIASES_FIRST_NAME_PATH, PARTIES_ALIASES_FIRST_NAME_PATH_NGRAMMED));

                        ofNullable(partyLastName).ifPresent(lastName -> addQueryBuilderForNameSearch(boolQueryBuilder, requestedPartyTypes, lastName,
                                PARTIES_LAST_NAME_PATH, PARTIES_LAST_NAME_PATH_NGRAMMED, PARTIES_ALIASES_LAST_NAME_PATH, PARTIES_ALIASES_LAST_NAME_PATH_NGRAMMED));

                        ofNullable(organisationName).ifPresent(orgName ->
                                boolQueryBuilder.must(buildNameQuery(orgName, PARTIES_ORGANISATION_NAME_PATH, PARTIES_ORGANISATION_NAME_PATH_NGRAMMED)));

                        addQueryBuilderForTypeSearch(boolQueryBuilder, requestedPartyTypes);

                        return boolQueryBuilder;

                    }).orElseGet(() -> {

                        final BoolQueryBuilder boolQueryBuilder = boolQuery();

                        ofNullable(partyFirstName).ifPresent(firstName ->
                                boolQueryBuilder.must(buildNameQuery(firstName, PARTIES_FIRST_NAME_PATH, PARTIES_FIRST_NAME_PATH_NGRAMMED)));

                        ofNullable(partyLastName).ifPresent(lastName ->
                                boolQueryBuilder.must(buildNameQuery(lastName, PARTIES_LAST_NAME_PATH, PARTIES_LAST_NAME_PATH_NGRAMMED)));

                        ofNullable(organisationName).ifPresent(orgName ->
                                boolQueryBuilder.must(buildNameQuery(orgName, PARTIES_ORGANISATION_NAME_PATH, PARTIES_ORGANISATION_NAME_PATH_NGRAMMED)));

                        return boolQueryBuilder;
                    });
        }

        return null;
    }

    private void addQueryBuilderForTypeSearch(final BoolQueryBuilder boolQueryBuilder, final List<String> requestedPartyTypes) {
        if (!isSearchForAliasNameOnly(requestedPartyTypes)) {
            boolQueryBuilder.must(termsQuery(PARTY_TYPE_NESTED_PATH, requestedPartyTypes));
        }
    }

    private void addQueryBuilderForNameSearch(final BoolQueryBuilder boolQueryBuilder, final List<String> requestedPartyTypes, final String lastName,
                                              final String partiesLastNamePath, final String partiesLastNamePathNgrammed, final String partiesAliasesLastNamePath,
                                              final String partiesAliasesLastNamePathNgrammed) {
        if (isSearchForDefendantAliasOrPartyName(requestedPartyTypes)) {
            boolQueryBuilder.must(buildNameOrAliasQuery(lastName, partiesLastNamePath, partiesLastNamePathNgrammed,
                    partiesAliasesLastNamePath, partiesAliasesLastNamePathNgrammed));
        } else if (isSearchForAliasNameOnly(requestedPartyTypes)) {
            boolQueryBuilder.must(buildAliasQuery(lastName, partiesAliasesLastNamePath, partiesAliasesLastNamePathNgrammed));
        } else {
            boolQueryBuilder.must(buildNameQuery(lastName, partiesLastNamePath, partiesLastNamePathNgrammed));
        }
    }

    private BoolQueryBuilder buildNameOrAliasQuery(final String name, final String path, final String pathNgrammed,
                                                   final String aliasPath, final String aliasPathNgrammed) {
            return boolQuery()
                    .should(buildNameQuery(name, path, pathNgrammed))
                    .should(buildAliasQuery(name, aliasPath, aliasPathNgrammed));
    }

    private BoolQueryBuilder buildNameQuery(final String partyName, final String partiesNamePath, final String partiesNamePathNgrammed) {
        return boolQuery()
                .should(matchQuery(partiesNamePath, partyName))
                .should(matchQuery(partiesNamePathNgrammed, partyName));
    }

    private BoolQueryBuilder buildAliasQuery(final String partyName, final String partiesAliasesNamePath, final String partiesAliasesNamePathNgrammed) {
        return boolQuery()
                .must(nestedQuery(PARTIES_ALIASES_PATH, buildNameQuery(partyName, partiesAliasesNamePath, partiesAliasesNamePathNgrammed), Avg))
                .must(termQuery(PARTY_TYPE_PATH, DEFENDANT_PARTY_TYPE));
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
