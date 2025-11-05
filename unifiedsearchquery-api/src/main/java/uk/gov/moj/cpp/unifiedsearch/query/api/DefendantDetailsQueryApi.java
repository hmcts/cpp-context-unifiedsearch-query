package uk.gov.moj.cpp.unifiedsearch.query.api;

import static java.util.UUID.randomUUID;
import static uk.gov.justice.services.core.annotation.Component.QUERY_API;
import static uk.gov.justice.services.messaging.JsonEnvelope.envelopeFrom;
import static uk.gov.justice.services.messaging.JsonEnvelope.metadataBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.api.service.QueryApiPreConditions.validateQueryParameterForLAACaseQuery;

import uk.gov.justice.services.core.annotation.Component;
import uk.gov.justice.services.core.annotation.Handles;
import uk.gov.justice.services.core.annotation.ServiceComponent;
import uk.gov.justice.services.messaging.Envelope;
import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.MetadataBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.api.response.SearchResponseEnvelopeGenerator;
import uk.gov.moj.cpp.unifiedsearch.query.api.service.CaseSearchService;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.DefendantDetailsQueryParameters;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.QueryParameters;

import javax.inject.Inject;
import javax.json.JsonObject;

@ServiceComponent(QUERY_API)
public class DefendantDetailsQueryApi {

    private static final String RESPONSE_NAME = "unifiedsearch.query.probation.defendant.details";

    @Inject
    private CaseSearchService caseQueryService;

    @Inject
    private SearchResponseEnvelopeGenerator searchResponseEnvelopeGenerator;


    @Handles(RESPONSE_NAME)
    public JsonEnvelope searchDefendantDetails(final Envelope<DefendantDetailsQueryParameters> envelope) {

        final QueryParameters queryParameters = envelope.payload().toQueryParameters();

        envelope.metadata().userId().ifPresent(queryParameters::setRequestUserId);

        validateQueryParameterForLAACaseQuery(queryParameters);

        final JsonObject queryResult = caseQueryService.searchProbationDefendantDetails(queryParameters);

        return searchResponseEnvelopeGenerator.createFrom(
                RESPONSE_NAME,
                envelope.metadata(),
                queryResult);
    }
}
