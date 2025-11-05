package uk.gov.moj.cpp.unifiedsearch.query.api.response;

import static java.util.UUID.randomUUID;
import static uk.gov.justice.services.messaging.JsonEnvelope.metadataFrom;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.messaging.Metadata;
import uk.gov.justice.services.messaging.MetadataBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

public class SearchResponseMetadataGenerator {

    @Inject
    private UtcClock clock;

    public MetadataBuilder createFrom(final Metadata queryEnvelopeMetadata, final String responseName) {

        final List<UUID> causation = new ArrayList<>(queryEnvelopeMetadata.causation());
        causation.add(queryEnvelopeMetadata.id());

        final MetadataBuilder metadataBuilder = metadataFrom(queryEnvelopeMetadata)
                .withId(randomUUID())
                .withName(responseName)
                .withCausation(causation.toArray(new UUID[0]))
                .createdAt(clock.now())
                ;

        queryEnvelopeMetadata.userId().ifPresent(metadataBuilder::withUserId);

        return metadataBuilder;
    }
}
