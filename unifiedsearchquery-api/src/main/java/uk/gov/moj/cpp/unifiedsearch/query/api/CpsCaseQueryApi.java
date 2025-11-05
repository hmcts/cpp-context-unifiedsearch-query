package uk.gov.moj.cpp.unifiedsearch.query.api;

import static java.util.UUID.randomUUID;
import static uk.gov.justice.services.core.annotation.Component.QUERY_API;
import static uk.gov.justice.services.messaging.JsonEnvelope.envelopeFrom;
import static uk.gov.justice.services.messaging.JsonEnvelope.metadataBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.api.service.CpsQueryApiPreConditions.validateQueryParameters;
import static uk.gov.moj.cpp.unifiedsearch.query.common.domain.CpsQueryParameters.CpsQueryParametersBuilder;
import static uk.gov.moj.cpp.unifiedsearch.query.common.domain.CpsQueryParameters.builder;

import uk.gov.justice.services.core.annotation.Component;
import uk.gov.justice.services.core.annotation.Handles;
import uk.gov.justice.services.core.annotation.ServiceComponent;
import uk.gov.justice.services.core.requester.Requester;
import uk.gov.justice.services.messaging.Envelope;
import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.Metadata;
import uk.gov.justice.services.messaging.MetadataBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.api.response.SearchResponseEnvelopeGenerator;
import uk.gov.moj.cpp.unifiedsearch.query.api.service.CpsCaseSearchService;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.CpsQueryParameters;

import javax.inject.Inject;
import javax.json.JsonObject;

@ServiceComponent(QUERY_API)
public class CpsCaseQueryApi {

    private static final String RESPONSE_NAME_CPS = "unifiedsearch.query.cps.cases";
    private static final String RESPONSE_NAME_WCU = "unifiedsearch.query.cps-wcu.cases";
    private static final String CHARGED_CASE_TYPE = "CHARGED";

    @Inject
    private CpsCaseSearchService caseQueryService;

    @Inject
    private Requester requester;

    @Inject
    private SearchResponseEnvelopeGenerator searchResponseEnvelopeGenerator;

    @Handles(RESPONSE_NAME_CPS)
    public JsonEnvelope searchCaseIndex(final Envelope<CpsQueryParameters> envelope) {

        final CpsQueryParametersBuilder cpsQueryParametersBuilder = builder().withValuesFrom(envelope.payload());
        envelope.metadata().userId().ifPresent(cpsQueryParametersBuilder::withRequestUserId);

        final CpsQueryParameters queryParameters = cpsQueryParametersBuilder.build();

        validateQueryParameters(queryParameters);

        final JsonObject queryResult = caseQueryService.searchCases(queryParameters);

        return searchResponseEnvelopeGenerator.createFrom(
                RESPONSE_NAME_CPS,
                envelope.metadata(),
                queryResult
        );
    }

    @Handles(RESPONSE_NAME_WCU)
    public JsonEnvelope searchCaseIndexForWcu(final Envelope<CpsQueryParameters> envelope) {

        final CpsQueryParametersBuilder cpsQueryParametersBuilder = builder().withValuesFrom(envelope.payload());
        envelope.metadata().userId().ifPresent(cpsQueryParametersBuilder::withRequestUserId);
        cpsQueryParametersBuilder.withCaseType(CHARGED_CASE_TYPE);

        final CpsQueryParameters queryParameters = cpsQueryParametersBuilder.build();

        validateQueryParameters(queryParameters);

        final JsonObject queryResult = caseQueryService.searchCases(queryParameters);

        return searchResponseEnvelopeGenerator.createFrom(
                RESPONSE_NAME_WCU,
                envelope.metadata(),
                queryResult);
    }
}
