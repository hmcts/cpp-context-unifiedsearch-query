package uk.gov.moj.cpp.unifiedsearch.query.api;

import static uk.gov.justice.services.core.annotation.Component.QUERY_API;
import static uk.gov.moj.cpp.unifiedsearch.query.api.service.QueryApiPreConditions.validateQueryParameters;

import uk.gov.justice.services.core.annotation.Handles;
import uk.gov.justice.services.core.annotation.ServiceComponent;
import uk.gov.justice.services.messaging.Envelope;
import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.Metadata;
import uk.gov.moj.cpp.unifiedsearch.query.api.response.SearchResponseEnvelopeGenerator;
import uk.gov.moj.cpp.unifiedsearch.query.api.service.CaseSearchService;
import uk.gov.moj.cpp.unifiedsearch.query.api.service.UsersGroupsService;
import uk.gov.moj.cpp.unifiedsearch.query.api.util.ApplicationTypeFilter;
import uk.gov.moj.cpp.unifiedsearch.query.api.util.LAAResultFilter;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.Permission;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.QueryParameters;

import java.util.List;

import javax.inject.Inject;
import javax.json.JsonObject;

@ServiceComponent(QUERY_API)
public class CaseQueryApi {

    private static final String RESPONSE_NAME = "unifiedsearch.query.cases";

    @Inject
    private CaseSearchService caseQueryService;

    @Inject
    private SearchResponseEnvelopeGenerator searchResponseEnvelopeGenerator;

    @Inject
    private ApplicationTypeFilter applicationTypeFilter;

    @Handles(RESPONSE_NAME)
    public JsonEnvelope searchCaseIndex(final Envelope<QueryParameters> envelope) {

        final QueryParameters queryParameters = envelope.payload();
        final Metadata queryMetadata = envelope.metadata();
        queryMetadata.userId().ifPresent(queryParameters::setRequestUserId);

        validateQueryParameters(queryParameters);

        final JsonObject queryResult = caseQueryService.searchCases(queryParameters);

        final JsonObject filteredResponse = applicationTypeFilter.filter(queryMetadata, queryResult);

        return searchResponseEnvelopeGenerator.createFrom(
                RESPONSE_NAME,
                queryMetadata,
                filteredResponse
        );
    }
}
