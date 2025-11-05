package uk.gov.moj.cpp.unifiedsearch.query.api.response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.justice.services.messaging.Metadata;
import uk.gov.justice.services.messaging.MetadataBuilder;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SearchResponseEnvelopeGeneratorTest {

    @Mock
    private SearchResponseMetadataGenerator searchResponseMetadataGenerator;
    
    @InjectMocks
    private SearchResponseEnvelopeGenerator searchResponseEnvelopeGenerator;

    @Test
    public void shouldCreateTheCorrectResponseEnvelopeFromTheResponseNameCorrectResponseMetadataAndQueryResultPayload() throws Exception {

        final String responseName = "some.response.name";
        final Metadata queryMetadata = mock(Metadata.class);
        final JsonObject queryResult = mock(JsonObject.class);

        final MetadataBuilder responseMetadataBuilder = mock(MetadataBuilder.class);
        final Metadata responseMetadata = mock(Metadata.class);

        when(searchResponseMetadataGenerator.createFrom(queryMetadata, responseName)).thenReturn(responseMetadataBuilder);
        when(responseMetadataBuilder.build()).thenReturn(responseMetadata);

        final JsonEnvelope responseEnvelope = searchResponseEnvelopeGenerator.createFrom(
                responseName,
                queryMetadata,
                queryResult
        );

        assertThat(responseEnvelope.metadata(), is(responseMetadata));
        assertThat(responseEnvelope.payloadAsJsonObject(), is(queryResult));
    }
}