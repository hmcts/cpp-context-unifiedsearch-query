package uk.gov.moj.cpp.unifiedsearch.query.api.service;

import static java.util.Optional.empty;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;
import static uk.gov.justice.services.messaging.JsonObjects.createArrayBuilder;
import static uk.gov.justice.services.messaging.JsonObjects.createObjectBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.CRIME_CASE_INDEX_NAME;

import uk.gov.justice.services.unifiedsearch.UnifiedSearchName;
import uk.gov.justice.services.unifiedsearch.client.search.UnifiedSearchService;
import uk.gov.moj.cpp.unifiedsearch.query.api.util.LAAResultFilter;
import uk.gov.moj.cpp.unifiedsearch.query.builders.UnifiedSearchQueryBuilderService;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.QueryParameters;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.defendant.ProbationDefendantDetails;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.laa.CaseSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;


@ApplicationScoped
public class CaseSearchService implements BaseCaseSearchService {

    public static final String RESULT_HIT_NODE_NAME = "cases";
    public static final String RESULT_INNER_HIT_NODE_NAME = "parties";
    public static final String SJP_NOTICE_SERVED = "sjpNoticeServed";
    public static final String APPOINTMENT_DATE = "hearings.hearingDays.sittingDay";
    public static final String HEARINGS = "hearings";
    public static final String COURT_PROCEEDINGS_INITIATED = "parties.courtProceedingsInitiated";
    public static final String DEFENDANTS = "defendants";
    public static final String CASE_REFERENCE = "caseReference";
    public static final String STATUS = "caseStatus";
    public static final String INACTIVE = "INACTIVE";
    public static final String PROSECUTION_CASE_ID = "prosecutionCaseId";
    public static final String DEFENDANT_ID = "defendantId";
    public static final String TOTAL_RESULTS = "totalResults";

    @Inject
    private UnifiedSearchService unifiedSearchService;

    @Inject
    private LAAResultFilter laaResultFilter;


    @Inject
    @UnifiedSearchName(CRIME_CASE_INDEX_NAME)
    private UnifiedSearchQueryBuilderService unifiedSearchQueryBuilderService;

    public JsonObject searchCases(final QueryParameters queryParameters) {
        final QueryBuilder queryBuilder = unifiedSearchQueryBuilderService.builder(queryParameters);
        final Optional<FieldSortBuilder> fieldSortBuilder = getSortBuilder(queryParameters);

        return unifiedSearchService.search(queryBuilder,
                CRIME_CASE_INDEX_NAME,
                uk.gov.moj.cpp.unifiedsearch.query.api.domain.response.indexdoc2apiresponse.Case.class,
                RESULT_HIT_NODE_NAME,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                fieldSortBuilder.orElse(null));
    }


    public JsonObject searchLaaCases(final QueryParameters queryParameters) {
        final QueryBuilder queryBuilder = unifiedSearchQueryBuilderService.builder(queryParameters);

        final JsonObject results = unifiedSearchService.search(queryBuilder,
                CRIME_CASE_INDEX_NAME,
                CaseSummary.class,
                RESULT_HIT_NODE_NAME,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                null);
        return laaResultFilter.filter(results);
    }


    public JsonObject searchProbationDefendantDetails(final QueryParameters queryParameters) {
        final QueryBuilder queryBuilder = unifiedSearchQueryBuilderService.builder(queryParameters);

        final JsonObject result = unifiedSearchService.search(queryBuilder,
                CRIME_CASE_INDEX_NAME,
                ProbationDefendantDetails.class,
                RESULT_HIT_NODE_NAME,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                null);

        final JsonObjectBuilder filteredResults = createObjectBuilder();
        filteredResults.add(TOTAL_RESULTS, result.getJsonNumber(TOTAL_RESULTS));
        final JsonArrayBuilder filteredCases = createArrayBuilder();

        result.getJsonArray(RESULT_HIT_NODE_NAME).stream()
                .filter(jsonValue -> !((JsonObject) jsonValue).getString(STATUS).equals(INACTIVE))
                .forEach(filteredCases::add);

        return filteredResults.add(RESULT_HIT_NODE_NAME, filteredCases).build();


    }

    public JsonObject searchDefendantCases(final QueryParameters queryParameters) {
        final QueryBuilder queryBuilder = unifiedSearchQueryBuilderService.builder(queryParameters);
        final FieldSortBuilder fieldSortBuilder = fieldSort(COURT_PROCEEDINGS_INITIATED).order(SortOrder.ASC);

        final JsonObject result = unifiedSearchService.search(queryBuilder,
                CRIME_CASE_INDEX_NAME,
                uk.gov.moj.cpp.unifiedsearch.query.api.domain.response.index2defendantcaseresponse.Case.class,
                RESULT_HIT_NODE_NAME,
                queryParameters.getPageSize(),
                queryParameters.getStartFrom(),
                fieldSortBuilder,
                uk.gov.moj.cpp.unifiedsearch.query.api.domain.response.index2defendantcaseresponse.Party.class,
                RESULT_INNER_HIT_NODE_NAME);

        return filterCasesForRequiredDefendants(result);

    }

    private JsonObject filterCasesForRequiredDefendants(final JsonObject result) {
        final JsonArray parties = result.getJsonArray(RESULT_INNER_HIT_NODE_NAME);
        final JsonArray cases = result.getJsonArray(RESULT_HIT_NODE_NAME);

        final List<String> partyIds = getRequiredDefendantIds(parties);
        final JsonObjectBuilder filteredResults = createObjectBuilder();
        filteredResults.add(TOTAL_RESULTS, result.getJsonNumber(TOTAL_RESULTS));
        final JsonArrayBuilder filteredCases = createArrayBuilder();
        for (int j = 0; j < cases.size(); j++) {
            createCase(cases, partyIds, filteredCases, j);
        }
        return filteredResults.add(RESULT_HIT_NODE_NAME, filteredCases).build();
    }

    private void createCase(JsonArray cases, List<String> partyIds, JsonArrayBuilder filteredCases, int j) {
        final JsonObjectBuilder filteredCase = createObjectBuilder();
        final JsonObject caseResult = (JsonObject) cases.get(j);
        filteredCase.add(CASE_REFERENCE, caseResult.getString(CASE_REFERENCE, EMPTY));
        filteredCase.add(PROSECUTION_CASE_ID, caseResult.get(PROSECUTION_CASE_ID));
        createDefendants(partyIds, filteredCase, caseResult);
        filteredCases.add(filteredCase);
    }

    private void createDefendants(List<String> partyIds, JsonObjectBuilder filteredCase, JsonObject caseResult) {
        final JsonArray defendants = caseResult.getJsonArray(DEFENDANTS);
        final JsonArrayBuilder filteredDefendants = createArrayBuilder();
        for (int k = 0; k < defendants.size(); k++) {
            final JsonObject defendant = (JsonObject) defendants.get(k);
            if (isDefendantToBeIncludedInResult(defendant.getString(DEFENDANT_ID), partyIds)) {
                filteredDefendants.add(defendant);
            }
        }
        filteredCase.add(DEFENDANTS, filteredDefendants);

    }

    private List<String> getRequiredDefendantIds(JsonArray parties) {
        final List<String> partyIds = new ArrayList();
        for (int j = 0; j < parties.size(); j++) {
            final JsonObject jsonObject = (JsonObject) parties.get(j);
            partyIds.add(jsonObject.getString(DEFENDANT_ID));
        }
        return partyIds;
    }

    private boolean isDefendantToBeIncludedInResult(final String defendantId,
                                                    final List<String> partyIds) {
        for (int i = 0; i < partyIds.size(); i++) {
            if (partyIds.get(i).equalsIgnoreCase(defendantId)) {
                return true;
            }
        }
        return false;
    }


    private Optional<FieldSortBuilder> getSortBuilder(final QueryParameters queryParameters) {

        final String sjpNoticeServedSortField = trimToEmpty(queryParameters.getSortBySjpNoticeServed());
        if (isNotEmpty(sjpNoticeServedSortField)) {
            return createFieldSort(sjpNoticeServedSortField, SJP_NOTICE_SERVED, null);
        }

        final String appointmentDateSortField = trimToEmpty(queryParameters.getSortByAppointmentDate());
        if (isNotEmpty(appointmentDateSortField)) {
            return createFieldSort(appointmentDateSortField, APPOINTMENT_DATE, HEARINGS);
        }

        return empty();
    }

}

