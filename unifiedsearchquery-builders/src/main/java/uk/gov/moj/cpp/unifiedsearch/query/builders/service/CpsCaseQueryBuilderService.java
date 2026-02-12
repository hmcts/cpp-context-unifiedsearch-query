package uk.gov.moj.cpp.unifiedsearch.query.builders.service;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder.convertBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CASE_STATUS;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CASE_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CJS_AREA;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.COURT_HOUSE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.COURT_ROOM;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_AREA;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_UNIT;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CROWN_ADVOCATE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.DATE_OF_BIRTH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.HEARINGS_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.HEARING_DATE_TIME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.HEARING_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.JURISDICTION;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.LINKED_CASE_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OFFENCES_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OFFENCE_CODE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OIC_SHOULDER_NUMBER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OPERATION_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.ORGANISATION;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARALEGAL_OFFICER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_FIRST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_LAST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_NAMES;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_TYPES;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PNC_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PROSECUTOR;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.URN;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.WITNESS_CARE_UNIT;

import uk.gov.moj.cpp.unifiedsearch.query.builders.UnifiedSearchQueryBuilderService;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.CpsQueryParameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.google.common.collect.ImmutableMap;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

@ApplicationScoped
public class CpsCaseQueryBuilderService extends AbstractQueryBuilderService implements
        UnifiedSearchQueryBuilderService<CpsQueryParameters> {

    @Override
    public Query.Builder builder(final CpsQueryParameters queryParameters) {

        final BoolQuery.Builder queryBuilder = new BoolQuery.Builder();

        addCaseQueryBuilders(queryParameters, queryBuilder);

        addHearingQueryBuilders(queryParameters, queryBuilder);

        addPartyQueryBuilders(queryParameters, queryBuilder);

        addLinkedCaseQueryBuilders(queryParameters, queryBuilder);

        return convertBuilder(queryBuilder);
    }

    private void addCaseQueryBuilders(CpsQueryParameters queryParameters,
                                      BoolQuery.Builder queryBuilder) {
        final String urn = trimToEmpty(queryParameters.getUrn());
        final String caseStatusCode = trimToEmpty(queryParameters.getCaseStatusCode());
        final String caseType = trimToEmpty(queryParameters.getCaseType());
        final String prosecutingAuthority = trimToEmpty(queryParameters.getProsecutor());
        final String cpsUnitCode = trimToEmpty(queryParameters.getCpsUnitCode());
        final String cjsAreaCode = trimToEmpty(queryParameters.getCjsAreaCode());
        final String operationName = trimToEmpty(queryParameters.getOperationName());
        final String paralegalOfficer = trimToEmpty(queryParameters.getParalegalOfficer());
        final String crownAdvocate = trimToEmpty(queryParameters.getCrownAdvocate());
        final String witnessCareUnitCode = trimToEmpty(queryParameters.getWitnessCareUnitCode());
        final String cpsAreaCode = trimToEmpty(queryParameters.getCpsAreaCode());

        addFilterToQueryIfPresent(urn, URN, queryBuilder);
        addFilterToQueryIfPresent(caseStatusCode, CASE_STATUS, queryBuilder);
        addFilterToQueryIfPresent(caseType, CASE_TYPE, queryBuilder);
        addFilterToQueryIfPresent(prosecutingAuthority, PROSECUTOR, queryBuilder);
        addFilterToQueryIfPresent(cpsUnitCode, CPS_UNIT, queryBuilder);
        addFilterToQueryIfPresent(cjsAreaCode, CJS_AREA, queryBuilder);
        addFilterToQueryIfPresent(operationName, OPERATION_NAME, queryBuilder);
        addFilterToQueryIfPresent(paralegalOfficer, PARALEGAL_OFFICER, queryBuilder);
        addFilterToQueryIfPresent(crownAdvocate, CROWN_ADVOCATE, queryBuilder);
        addFilterToQueryIfPresent(witnessCareUnitCode, WITNESS_CARE_UNIT, queryBuilder);
        addFilterToQueryIfPresent(cpsAreaCode, CPS_AREA, queryBuilder);
      }

    private void addLinkedCaseQueryBuilders(final CpsQueryParameters queryParameters,
                                            final BoolQuery.Builder queryBuilder){
        final String excludeAutomaticallyLinkedCasesTo = trimToEmpty(queryParameters.getExcludeAutomaticallyLinkedCasesTo());
        addFilterToQueryIfPresent(excludeAutomaticallyLinkedCasesTo, LINKED_CASE_ID, queryBuilder);
    }

    private void addHearingQueryBuilders(final CpsQueryParameters queryParameters,
                                         final BoolQuery.Builder queryBuilder) {
        final String hearingType = trimToEmpty(queryParameters.getHearingType());
        final String hearingDateFromParam = trimToEmpty(queryParameters.getHearingDateFrom());
        final String hearingDateToParam = trimToEmpty(queryParameters.getHearingDateTo());
        final String courtHouse = trimToEmpty(queryParameters.getCourtHouse());
        final String courtRoom = trimToEmpty(queryParameters.getCourtRoom());
        final String jurisdictionTypes = trimToEmpty(queryParameters.getJurisdictionTypes());
        final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put(HEARING_TYPE, hearingType)
                .put(COURT_ROOM, courtRoom)
                .put(COURT_HOUSE, courtHouse)
                .put(JURISDICTION, jurisdictionTypes);

        final List<Query> allHearingFilters = createFilters(builder.build());
        if (!hearingDateFromParam.isEmpty() || !hearingDateToParam.isEmpty()) {
            allHearingFilters.add(getQueryBuilder(HEARING_DATE_TIME, hearingDateFromParam,
                    hearingDateToParam));
        }

        final Query additionalHearingFilterQueries = createAdditionalMustFilter(
                HEARINGS_NESTED_PATH, allHearingFilters);
        if (additionalHearingFilterQueries != null) {
            queryBuilder.must(additionalHearingFilterQueries);
        }
    }

    private void addPartyQueryBuilders(final CpsQueryParameters queryParameters,
                                       final BoolQuery.Builder queryBuilder) {

        final String dateOfBirth = trimToEmpty(queryParameters.getDateOfBirth());
        final String partyTypes = trimToEmpty(queryParameters.getPartyTypes());
        final String pncId = trimToEmpty(queryParameters.getPncId());
        final String partyFirstName = trimToEmpty(queryParameters.getFirstName());
        final String partyLastName = trimToEmpty(queryParameters.getLastName());
        final String oicShoulderNumber = trimToEmpty(queryParameters.getOicShoulderNumber());
        final String organisationName = trimToEmpty(queryParameters.getOrganisation());

        final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put(PNC_ID, pncId)
                .put(OIC_SHOULDER_NUMBER, oicShoulderNumber)
                .put(DATE_OF_BIRTH, dateOfBirth);


        final List<Query> allPartyFilters = createFilters(builder.build());

        addFiltersForPartyNamesAndTypes(partyTypes, partyFirstName, partyLastName,
                organisationName, allPartyFilters);

        final Query additionalPartyFilterQueries = createAdditionalMustFilter(
                PARTY_NESTED_PATH, allPartyFilters);
        if (additionalPartyFilterQueries != null) {
            queryBuilder.must(additionalPartyFilterQueries);
        }

        addOffenceQueryBuilders(queryParameters, queryBuilder);
    }

    private void addFiltersForPartyNamesAndTypes(final String partyTypes, final String partyFirstName,
                                                 final String partyLastName, final String organisationName,
                                                 final List<Query> allPartyFilters) {
        final Query query = getQueryBuilder(PARTY_NAMES, ImmutableMap.of(
                PARTY_TYPES, partyTypes,
                PARTY_FIRST_NAME, partyFirstName,
                PARTY_LAST_NAME, partyLastName,
                ORGANISATION, organisationName));

        ofNullable(query).ifPresent(allPartyFilters::add);
    }

    private void addOffenceQueryBuilders(CpsQueryParameters queryParameters,
                                         BoolQuery.Builder queryBuilder) {
        final String offence = trimToEmpty(queryParameters.getOffence());
        final Map<String, Object> partiesExactQueryBuilders = new HashMap<>();
        if (!offence.isEmpty()) {
            partiesExactQueryBuilders.putIfAbsent(OFFENCE_CODE, offence);
        }
        //TODO: Offence start and end date search

        final Query additionalPartyFilterQueries = createAdditionalMustFilter(
                OFFENCES_NESTED_PATH, createFilters(partiesExactQueryBuilders));
        if (additionalPartyFilterQueries != null) {
            queryBuilder.must(additionalPartyFilterQueries);
        }
    }

}
