package uk.gov.moj.cpp.unifiedsearch.query.builders.service;

import static com.google.common.collect.ImmutableMap.of;
import static java.lang.String.join;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.nestedQuery;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.ActiveCaseStatusEnum.getActiveCaseStatusEnumValues;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.ACTIVE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.ADDRESS;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATIONS_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATION_STATUS;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATION_STATUS_LIST;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATION_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.ARREST_SUMMONS_NUMBER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.CASE_REFERENCE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.CASE_STATUS_PARAM;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.COURT_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.COURT_ORDERS_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.DATE_OF_BIRTH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_DATE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_TYPE_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.INACTIVE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_BOX_HEARING;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_VIRTUAL_BOX_HEARING;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.JURISDICTION_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.NINO;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.OFFENCES_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTY_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTY_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.POST_CODE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PROSECUTING_AUTHORITY;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.ADDRESS_LINE_1;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.COURT_ORDER_VALIDITY_DATE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.CROWN_OR_MAGISTRATES;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.CRO_NUMBER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.FIRST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.LAST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PNC_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PROCEEDINGS_CONCLUDED;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.InactiveCaseStatusEnum.getInactiveCaseStatusEnumValues;

import uk.gov.moj.cpp.unifiedsearch.query.builders.UnifiedSearchQueryBuilderService;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.QueryParameters;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch.core.search.InnerHits;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections.MapUtils;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

@ApplicationScoped
public class CaseQueryBuilderService extends AbstractQueryBuilderService implements UnifiedSearchQueryBuilderService<QueryParameters> {

    public static final String CHAR_COMMA = ",";

    @Override
    public Query.Builder builder(final QueryParameters queryParameters) {

        final BoolQuery.Builder queryBuilder = new BoolQuery.Builder();
        if (queryParameters.isCrownOrMagistrates() != null) {
            addMustToQuery(CROWN_OR_MAGISTRATES, queryBuilder, queryParameters.isCrownOrMagistrates());
        }

        if (queryParameters.hasOffence() != null) {
            addMustToQuery(OFFENCES_NESTED_PATH, queryBuilder, queryParameters.hasOffence());
        }

        final String prosecutingAuthority = trimToEmpty(queryParameters.getProsecutingAuthority());

        addFilterToQueryIfPresent(prosecutingAuthority, PROSECUTING_AUTHORITY, queryBuilder);

        addJurisdictionTypeFilters(queryBuilder, queryParameters);

        addPartyQueryBuilders(queryParameters, queryBuilder);

        addHearingQueryBuilders(queryParameters, queryBuilder);

        addApplicationQueryBuilders(queryBuilder, queryParameters);

        if (queryParameters.getCaseStatus() != null && !queryParameters.getCaseStatus().contains(CHAR_COMMA)) {
            if (INACTIVE.equals(queryParameters.getCaseStatus())) {
                addMustNotToQuery(CASE_STATUS_PARAM, queryBuilder, getCaseStatusList(ACTIVE));
            } else {
                addMustToQuery(CASE_STATUS_PARAM, queryBuilder, getCaseStatusList(queryParameters.getCaseStatus()));
            }
        }

        return convertBuilder(queryBuilder);
    }

    private void addHearingQueryBuilders(final QueryParameters queryParameters, final BoolQuery.Builder queryBuilder) {
        final Boolean isBoxHearing = queryParameters.isBoxWorkHearing();
        final Boolean isVirtualBoxHearing = queryParameters.isBoxWorkVirtualHearing();
        final String hearingTypeId = trimToEmpty(queryParameters.getHearingTypeId());
        final String hearingDateFromParam = trimToEmpty(queryParameters.getHearingDateFrom());
        final String hearingDateToParam = trimToEmpty(queryParameters.getHearingDateTo());
        final String courtId = trimToEmpty(queryParameters.getCourtId());
        final String hearingId = queryParameters.getHearingId();

        final List<Query> allHearingFilters = createFilters(of(HEARING_TYPE_ID, hearingTypeId));
        if (!hearingDateFromParam.isEmpty() || !hearingDateToParam.isEmpty()) {
            allHearingFilters.add(getQueryBuilder(HEARING_DATE, hearingDateFromParam, hearingDateToParam));
        }

        if (isBoxHearing != null) {
            allHearingFilters.add(getQueryBuilder(IS_BOX_HEARING, isBoxHearing));
        }

        if (isVirtualBoxHearing != null) {
            allHearingFilters.add(getQueryBuilder(IS_VIRTUAL_BOX_HEARING, isVirtualBoxHearing));
        }

        if (!courtId.isEmpty()) {
            allHearingFilters.add(getQueryBuilder(COURT_ID, courtId));
        }

        if (isNotEmpty(hearingId)) {
            allHearingFilters.add(getQueryBuilder(HEARING_ID, hearingId));
        }

        final Query additionalHearingFilterQueries = createAdditionalMustFilter(HEARING_NESTED_PATH, allHearingFilters);
        if (additionalHearingFilterQueries != null) {
            queryBuilder.must(additionalHearingFilterQueries);
        }
    }

    private void addPartyQueryBuilders(final QueryParameters queryParameters, final BoolQuery.Builder queryBuilder) {
        addDefendantMatchingQueryBuilders(queryBuilder, queryParameters);

        final String nameParam = trimToEmpty(queryParameters.getPartyName());
        final String partyFirstAndOrMiddleNameParam = trimToEmpty(queryParameters.getPartyFirstAndOrMiddleName());
        final String partyLastNameOrOrganisationNameParam = trimToEmpty(queryParameters.getPartyLastNameOrOrganisationName());
        final String address = trimToEmpty(queryParameters.getPartyAddress());
        final String postCode = trimToEmpty(queryParameters.getPartyPostcode());
        final String partyTypes = trimToEmpty(queryParameters.getPartyTypes());
        final String dateOfBirth = trimToEmpty(queryParameters.getPartyDateOfBirth());
        final String nino = trimToEmpty(queryParameters.getPartyNINO());
        final String arrestSummonsNumber = trimToEmpty(queryParameters.getPartyArrestSummonsNumber());

        final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put(PARTY_TYPE, partyTypes)
                .put(DATE_OF_BIRTH, dateOfBirth)
                .put(POST_CODE, postCode)
                .put(ADDRESS, address)
                .put(NINO, nino)
                .put(ARREST_SUMMONS_NUMBER, arrestSummonsNumber);

        final List<Query> allPartyFilters = createFilters(builder.build());
        if (!partyLastNameOrOrganisationNameParam.isEmpty() || !partyFirstAndOrMiddleNameParam.isEmpty()) {
            addMustToQuery(NAME, queryBuilder, partyFirstAndOrMiddleNameParam, partyLastNameOrOrganisationNameParam, allPartyFilters);
        } else if (!nameParam.isEmpty()) {
            addMustToQuery(NAME, queryBuilder, nameParam, allPartyFilters);
        } else {
            final Query additionalPartyFilterQueries = createAdditionalMustFilter(PARTY_NESTED_PATH, allPartyFilters);
            if (additionalPartyFilterQueries != null) {
                queryBuilder.must(additionalPartyFilterQueries);
            }
        }

    }

    private void addDefendantMatchingQueryBuilders(final BoolQuery.Builder queryBuilder,
                                                   final QueryParameters queryParameters) {
        final String pncIdParam = trimToEmpty(queryParameters.getPncId());
        final String croNumberParam = trimToEmpty(queryParameters.getCroNumber());
        final String firstNameParam = trimToEmpty(queryParameters.getPartyFirstName());
        final String lastNameParam = trimToEmpty(queryParameters.getPartyLastName());
        final String addressLine1Param = trimToEmpty(queryParameters.getPartyAddressLine1());
        final Boolean proceedingConcludedParam = queryParameters.isProceedingConcluded();
        final String courtOrderValidityDateParam = trimToEmpty(queryParameters.getCourtOrderValidityDate());

        final Map<String, Object> partiesExactQueryBuilders = new HashMap<>();
        final Map<String, Query> activePartiesShouldClauseQueryBuilders = new HashMap<>();

        if (proceedingConcludedParam != null) {
            activePartiesShouldClauseQueryBuilders.putIfAbsent(PARTY_NESTED_PATH, getQueryBuilder(PROCEEDINGS_CONCLUDED, proceedingConcludedParam));
        }

        if (!courtOrderValidityDateParam.isEmpty()) {
            activePartiesShouldClauseQueryBuilders.putIfAbsent(COURT_ORDERS_NESTED_PATH, getQueryBuilder(COURT_ORDER_VALIDITY_DATE, courtOrderValidityDateParam));
        }

        if (!pncIdParam.isEmpty()) {
            partiesExactQueryBuilders.putIfAbsent(PNC_ID, pncIdParam);
        }

        if (!croNumberParam.isEmpty()) {
            partiesExactQueryBuilders.putIfAbsent(CRO_NUMBER, croNumberParam);
        }

        if (!firstNameParam.isEmpty()) {
            partiesExactQueryBuilders.putIfAbsent(FIRST_NAME, firstNameParam);
        }

        if (!lastNameParam.isEmpty()) {
            partiesExactQueryBuilders.putIfAbsent(LAST_NAME, lastNameParam);
        }

        if (!addressLine1Param.isEmpty()) {
            partiesExactQueryBuilders.putIfAbsent(ADDRESS_LINE_1, addressLine1Param);
        }


        final Query additionalPartyFilterQueries = createAdditionalMustFilterWithInnerHits(PARTY_NESTED_PATH, createFilters(partiesExactQueryBuilders));

        if (additionalPartyFilterQueries != null) {
            queryBuilder.must(additionalPartyFilterQueries);
        }

        if (MapUtils.isNotEmpty(activePartiesShouldClauseQueryBuilders)) {
            final Query.Builder queriesWithClause = createAdditionalShouldFilter(activePartiesShouldClauseQueryBuilders, additionalPartyFilterQueries == null);
            queryBuilder.must(queriesWithClause.build());
        }
    }

    private void addApplicationQueryBuilders(final BoolQuery.Builder queryBuilder, final QueryParameters queryParameters) {
        final String applicationType = trimToEmpty(queryParameters.getApplicationType());
        final String referenceParam = trimToEmpty(queryParameters.getCaseReference());
        final Boolean excludeCompletedApplications = queryParameters.excludeCompletedApplications();
        final Boolean isBoxHearing = queryParameters.isBoxWorkHearing();

        final List<Query> allApplicationFilters = createFilters(of(APPLICATION_TYPE, applicationType));

        if (!referenceParam.isEmpty() && (nonNull(isBoxHearing) && isBoxHearing)) {
            final Optional<Query> additionalApplicationFilter = ofNullable(createAdditionalMustFilter(APPLICATIONS_NESTED_PATH, allApplicationFilters));
            additionalApplicationFilter.ifPresent(queryBuilder::must);

            final BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
            APPLICATION_STATUS_LIST.forEach(applicationStatus ->
                    boolQueryBuilder.should(convertBuilder(getNestedQuery(APPLICATIONS_NESTED_PATH, getQueryBuilder(APPLICATION_STATUS, applicationStatus), additionalApplicationFilter.isPresent(), join(APPLICATIONS_NESTED_PATH, applicationStatus))).build()));
            queryBuilder.must(boolQueryBuilder.build());

            addMustToQuery(CASE_REFERENCE, queryBuilder, referenceParam, allApplicationFilters);

        } else if (!referenceParam.isEmpty()) {
            addMustToQuery(CASE_REFERENCE, queryBuilder, referenceParam, allApplicationFilters);
        } else {
            final Query additionalApplicationFilter = createAdditionalMustFilter(APPLICATIONS_NESTED_PATH, allApplicationFilters);
            if (additionalApplicationFilter != null) {
                queryBuilder.must(additionalApplicationFilter);
            }

            if ((nonNull(isBoxHearing) && isBoxHearing) || (nonNull(excludeCompletedApplications) && excludeCompletedApplications)) {
                final BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

                APPLICATION_STATUS_LIST.stream().forEach(applicationStatus ->
                        boolQueryBuilder.should(Collections.singletonList(convertBuilder(getNestedQuery(APPLICATIONS_NESTED_PATH, getQueryBuilder(APPLICATION_STATUS, applicationStatus), additionalApplicationFilter == null, join(APPLICATIONS_NESTED_PATH, applicationStatus))).build())));

                queryBuilder.must(boolQueryBuilder.build());
            }
        }
    }

    @Override
    protected void addMustToQuery(final String queryBuilderCacheKey, final BoolQuery.Builder queryBuilder, final Object... params) {
        queryBuilder.must(getQueryBuilder(queryBuilderCacheKey, params));
    }

    @Override
    protected void addMustNotToQuery(final String queryBuilderCacheKey, final BoolQuery.Builder queryBuilder, final Object... params) {
        queryBuilder.mustNot(getQueryBuilder(queryBuilderCacheKey, params));
    }

    @Override
    protected void addFilterToQuery(final String queryBuilderCacheKey, final BoolQuery.Builder queryBuilder, final Object... params) {
        queryBuilder.filter(getQueryBuilder(queryBuilderCacheKey, params));
    }

    @Override
    protected void addFilterToQueryIfPresent(final String param, final String queryBuilderCacheKey, final BoolQuery.Builder queryBuilder) {
        if (!param.isEmpty()) {
            queryBuilder.filter(getQueryBuilder(queryBuilderCacheKey, param));
        }
    }

    @Override
    protected Query getQueryBuilder(final String queryBuilderCacheKey, final Object... params) {
        final ElasticSearchQueryBuilder unifiedSearchQueryBuilderBy = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(queryBuilderCacheKey);
        return unifiedSearchQueryBuilderBy.getQueryBuilderBy(params);
    }

    @Override
    protected Query createAdditionalMustFilterWithInnerHits(final String nestedPath, final List<Query> allInnerQueries) {
        if (allInnerQueries.isEmpty()) {
            return null;
        }

        final BoolQuery.Builder innerBooleanQueryBuilder = new BoolQuery.Builder();
        allInnerQueries.forEach(innerBooleanQueryBuilder::must);
        return convertBuilder(getNestedQuery(nestedPath, convertBuilder(innerBooleanQueryBuilder).build(), true, "parties")).build();
    }

    @Override
    protected Query createAdditionalMustFilter(final String nestedPath, final List<Query> allInnerQueries) {
        if (allInnerQueries.isEmpty()) {
            return null;
        }

        final BoolQuery.Builder innerBooleanQueryBuilder = new BoolQuery.Builder();
        allInnerQueries.forEach(innerBooleanQueryBuilder::must);
        return convertBuilder(nestedQuery(nestedPath, convertBuilder(innerBooleanQueryBuilder))).build();
    }

    private Query.Builder createAdditionalShouldFilter(final Map<String, Query> innerQueries, final boolean withInnerHits) {
        if (MapUtils.isEmpty(innerQueries)) {
            return null;
        }

        final BoolQuery.Builder booleanQueryBuilder = new BoolQuery.Builder();

        for (final Map.Entry<String, Query> innerQuery : innerQueries.entrySet()) {
            booleanQueryBuilder.should(Collections.singletonList(convertBuilder(getNestedQuery(innerQuery.getKey(), innerQuery.getValue(), withInnerHits, innerQuery.getKey())).build()));
        }
        return convertBuilder(booleanQueryBuilder);
    }

    private NestedQuery.Builder getNestedQuery(final String key, final Query value, final boolean withInnerHits, final String innerHitKey) {
        return withInnerHits ? nestedQuery(key, value).innerHits(InnerHits.of(i -> i.name(innerHitKey))) :
                nestedQuery(key, value);
    }

    @Override
    protected List<Query> createFilters(final Map<String, Object> parameterAndValues) {
        return parameterAndValues.entrySet().stream().
                filter(entry -> !hasNoValue(entry.getValue())).
                map(entry -> getQueryBuilder(entry.getKey(), entry.getValue())).
                collect(toList());
    }

    private void addJurisdictionTypeFilters(final BoolQuery.Builder queryBuilder, final QueryParameters queryParameters) {
        final Boolean isSjp = queryParameters.isSjp();
        final Boolean isMagistrateCourt = queryParameters.isMagistrateCourt();
        final Boolean isCrownCourt = queryParameters.isCrownCourt();
        if (isSjp != null || isMagistrateCourt != null || isCrownCourt != null) {
            addFilterToQuery(JURISDICTION_TYPE, queryBuilder, isSjp, isMagistrateCourt, isCrownCourt);
        }
    }

    private List<String> getCaseStatusList(final String caseStatusQueryParam) {
        if (INACTIVE.equalsIgnoreCase(caseStatusQueryParam)) {
            return getInactiveCaseStatusEnumValues();
        }
        return getActiveCaseStatusEnumValues();
    }
}
