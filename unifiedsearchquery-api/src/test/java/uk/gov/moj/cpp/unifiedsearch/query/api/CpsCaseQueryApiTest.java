package uk.gov.moj.cpp.unifiedsearch.query.api;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.messaging.Envelope.envelopeFrom;
import static uk.gov.justice.services.test.utils.core.messaging.MetadataBuilderFactory.metadataWithDefaults;

import uk.gov.justice.services.core.requester.Requester;
import uk.gov.justice.services.messaging.Envelope;
import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.moj.cpp.unifiedsearch.query.api.response.SearchResponseEnvelopeGenerator;
import uk.gov.moj.cpp.unifiedsearch.query.api.service.CpsCaseSearchService;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.CpsQueryParameters;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CpsCaseQueryApiTest {

    @Mock
    private CpsCaseSearchService caseQueryService;

    @Mock
    private SearchResponseEnvelopeGenerator searchResponseEnvelopeGenerator;

    @Captor
    private ArgumentCaptor<CpsQueryParameters> queryParametersCaptor;

    @InjectMocks
    private CpsCaseQueryApi caseQueryApi;

    @Test
    public void shouldSearchCaseIndex() {
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder().withUrn(randomUUID().toString()).build();
        final String requestUserId = randomUUID().toString();

        final JsonEnvelope responseEnvelope = mock(JsonEnvelope.class);
        final JsonObject queryResult = mock(JsonObject.class);

        final Envelope<CpsQueryParameters> queryEnvelope = envelopeFrom(metadataWithDefaults()
                .withUserId(requestUserId).withName("unifiedsearch.query.cps.cases"), queryParameters);

        when(caseQueryService.searchCases(any(CpsQueryParameters.class))).thenReturn(queryResult);
        when(searchResponseEnvelopeGenerator.createFrom("unifiedsearch.query.cps.cases", queryEnvelope.metadata(), queryResult)).thenReturn(responseEnvelope);

        assertThat(caseQueryApi.searchCaseIndex(queryEnvelope), is(responseEnvelope));

        verify(caseQueryService).searchCases(queryParametersCaptor.capture());
        verifyNoMoreInteractions(caseQueryService);

        final CpsQueryParameters cpsQueryParameters = queryParametersCaptor.getValue();
        assertThat(cpsQueryParameters.getRequestUserId(), is(requestUserId));
        assertThat(cpsQueryParameters.getCaseType(), is(nullValue()));
    }

    @Test
    public void shouldSearchCaseIndexForWcu() {
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder().withUrn(randomUUID().toString()).build();
        final String requestUserId = randomUUID().toString();
        final Envelope<CpsQueryParameters> queryEnvelope = envelopeFrom(metadataWithDefaults()
                .withUserId(requestUserId).withName("unifiedsearch.query.cps-wcu.cases"), queryParameters);
        final JsonEnvelope responseEnvelope = mock(JsonEnvelope.class);
        final JsonObject queryResult = mock(JsonObject.class);

        when(caseQueryService.searchCases(any(CpsQueryParameters.class))).thenReturn(queryResult);
        when(searchResponseEnvelopeGenerator.createFrom("unifiedsearch.query.cps-wcu.cases", queryEnvelope.metadata(), queryResult)).thenReturn(responseEnvelope);

        assertThat(caseQueryApi.searchCaseIndexForWcu(queryEnvelope), is(responseEnvelope));

        verify(caseQueryService).searchCases(queryParametersCaptor.capture());
        verifyNoMoreInteractions(caseQueryService);

        final CpsQueryParameters cpsQueryParameters = queryParametersCaptor.getValue();
        assertThat(cpsQueryParameters.getRequestUserId(), is(requestUserId));
        assertThat(cpsQueryParameters.getCaseType(), is("CHARGED"));
    }
}