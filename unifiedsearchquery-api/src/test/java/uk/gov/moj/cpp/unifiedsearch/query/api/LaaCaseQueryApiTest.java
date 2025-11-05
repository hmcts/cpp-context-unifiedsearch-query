package uk.gov.moj.cpp.unifiedsearch.query.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.messaging.Envelope.envelopeFrom;
import static uk.gov.justice.services.test.utils.core.messaging.MetadataBuilderFactory.metadataWithDefaults;

import uk.gov.justice.services.adapter.rest.exception.BadRequestException;
import uk.gov.justice.services.messaging.Envelope;
import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.moj.cpp.unifiedsearch.query.api.response.SearchResponseEnvelopeGenerator;
import uk.gov.moj.cpp.unifiedsearch.query.api.service.CaseSearchService;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.LaaQueryParameters;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.QueryParameters;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LaaCaseQueryApiTest {

    @Mock
    private CaseSearchService caseQueryService;

    @Mock
    private SearchResponseEnvelopeGenerator searchResponseEnvelopeGenerator;

    @InjectMocks
    private LaaCaseQueryApi laaCaseQueryApi;

    @Test
    public void shouldSearchCaseIndex() {
        final LaaQueryParameters laaQueryParameters = mock(LaaQueryParameters.class);
        final QueryParameters queryParameters = mock(QueryParameters.class);
        final JsonEnvelope responseEnvelope = mock(JsonEnvelope.class);
        final JsonObject queryResult = mock(JsonObject.class);
        final Envelope<LaaQueryParameters> queryEnvelope = envelopeFrom(metadataWithDefaults().withName("unifiedsearch.query.laa.cases"), laaQueryParameters);

        when(laaQueryParameters.toQueryParameters()).thenReturn(queryParameters);
        when(caseQueryService.searchLaaCases(queryParameters)).thenReturn(queryResult);
        when(searchResponseEnvelopeGenerator.createFrom("unifiedsearch.query.laa.cases", queryEnvelope.metadata(), queryResult)).thenReturn(responseEnvelope);

        assertThat(laaCaseQueryApi.searchCaseIndex(queryEnvelope), is(responseEnvelope));

        verify(caseQueryService).searchLaaCases(any(QueryParameters.class));

        verifyNoMoreInteractions(caseQueryService);
    }

    @Test
    public void shouldReturnNoResultsWhenAllSearchParametersAreEmpty() {
        final LaaQueryParameters laaQueryParameters = mock(LaaQueryParameters.class);
        final Envelope<LaaQueryParameters> envelope = envelopeFrom(metadataWithDefaults().withName("unifiedsearch.query.laa.cases"), laaQueryParameters);
        final QueryParameters queryParameters = mock(QueryParameters.class);
        when(queryParameters.isSjp()).thenReturn(null);
        when(queryParameters.isMagistrateCourt()).thenReturn(null);
        when(queryParameters.isCrownCourt()).thenReturn(null);
        when(queryParameters.isBoxWorkHearing()).thenReturn(null);
        when(queryParameters.isBoxWorkVirtualHearing()).thenReturn(null);
        when(queryParameters.isProceedingConcluded()).thenReturn(null);
        when(queryParameters.isCrownOrMagistrates()).thenReturn(null);
        when(laaQueryParameters.toQueryParameters()).thenReturn(queryParameters);

        assertThrows(BadRequestException.class, () -> laaCaseQueryApi.searchCaseIndex(envelope));
    }
}