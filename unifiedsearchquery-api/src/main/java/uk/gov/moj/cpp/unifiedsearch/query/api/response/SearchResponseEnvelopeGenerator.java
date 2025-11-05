package uk.gov.moj.cpp.unifiedsearch.query.api.response;

import static uk.gov.justice.services.messaging.JsonEnvelope.envelopeFrom;

import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.Metadata;

import javax.inject.Inject;
import javax.json.JsonObject;

public class SearchResponseEnvelopeGenerator {

    @Inject
    private SearchResponseMetadataGenerator searchResponseMetadataGenerator;

    public JsonEnvelope createFrom(
            final String responseName,
            final Metadata queryMetadata,
            final JsonObject queryResult) {

        return envelopeFrom(searchResponseMetadataGenerator.createFrom(
                        queryMetadata,
                        responseName),
                queryResult);
    }
}
