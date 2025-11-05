package uk.gov.moj.cpp.unifiedsearch.query.api.response;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.test.utils.core.messaging.MetadataBuilderFactory.metadataWithDefaults;

import uk.gov.justice.services.common.util.UtcClock;
import uk.gov.justice.services.messaging.Metadata;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SearchResponseMetadataGeneratorTest {

    @Mock
    private UtcClock clock;

    @InjectMocks
    private SearchResponseMetadataGenerator searchResponseMetadataGenerator;

    @Test
    public void shouldCreateMetadataWithNewIdNewCreatedAtCorrectNameCorrelationIdCausationAndUserId() throws Exception {

        final UUID originalEnvelopeId = randomUUID();
        final String queryName = "some.query.name";
        final String responseName = "some.response.name";
        final UUID userId = randomUUID();
        final UUID clientCorrelationId = randomUUID();
        final UUID causation_1 = randomUUID();
        final UUID causation_2 = randomUUID();
        final ZonedDateTime createdAt = new UtcClock().now();

        final Metadata requestEnvelopeMetadata = metadataWithDefaults()
                .withName(queryName)
                .withId(originalEnvelopeId)
                .withUserId(userId.toString())
                .withClientCorrelationId(clientCorrelationId.toString())
                .withCausation(causation_1, causation_2)
                .createdAt(createdAt.minusDays(2))
                .build();


        when(clock.now()).thenReturn(createdAt);

        final Metadata responseMetadata = searchResponseMetadataGenerator.createFrom(
                        requestEnvelopeMetadata,
                        responseName)
                .build();

        assertThat(responseMetadata.name(), is(responseName));
        assertThat(responseMetadata.id(), is(notNullValue()));
        assertThat(responseMetadata.id(), is(not(originalEnvelopeId)));
        assertThat(responseMetadata.userId(), is(of(userId.toString())));
        assertThat(responseMetadata.clientCorrelationId(), is(of(clientCorrelationId.toString())));
        assertThat(responseMetadata.createdAt(), is(of(createdAt)));
        assertThat(responseMetadata.causation().size(), is(3));
        assertThat(responseMetadata.causation().get(0), is(causation_1));
        assertThat(responseMetadata.causation().get(1), is(causation_2));
        assertThat(responseMetadata.causation().get(2), is(originalEnvelopeId));
    }

    @Test
    public void shouldHandleEmptyUserId() throws Exception {

        final UUID originalEnvelopeId = randomUUID();
        final String queryName = "some.query.name";
        final String responseName = "some.response.name";
        final UUID clientCorrelationId = randomUUID();
        final UUID causation_1 = randomUUID();
        final ZonedDateTime createdAt = new UtcClock().now();

        final Metadata requestEnvelopeMetadata = metadataWithDefaults()
                .withName(queryName)
                .withId(originalEnvelopeId)
                .withClientCorrelationId(clientCorrelationId.toString())
                .withCausation(causation_1)
                .createdAt(createdAt.minusDays(2))
                .build();


        when(clock.now()).thenReturn(createdAt);

        final Metadata responseMetadata = searchResponseMetadataGenerator.createFrom(
                        requestEnvelopeMetadata,
                        responseName)
                .build();

        assertThat(responseMetadata.name(), is(responseName));
        assertThat(responseMetadata.id(), is(notNullValue()));
        assertThat(responseMetadata.id(), is(not(originalEnvelopeId)));
        assertThat(responseMetadata.userId(), is(empty()));
        assertThat(responseMetadata.clientCorrelationId(), is(of(clientCorrelationId.toString())));
        assertThat(responseMetadata.createdAt(), is(of(createdAt)));
        assertThat(responseMetadata.causation().size(), is(2));
        assertThat(responseMetadata.causation().get(0), is(causation_1));
        assertThat(responseMetadata.causation().get(1), is(originalEnvelopeId));
    }
}