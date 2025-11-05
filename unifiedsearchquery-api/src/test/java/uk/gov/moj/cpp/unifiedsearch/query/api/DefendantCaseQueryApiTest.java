package uk.gov.moj.cpp.unifiedsearch.query.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.messaging.Envelope.envelopeFrom;
import static uk.gov.justice.services.test.utils.core.messaging.MetadataBuilderFactory.metadataWithDefaults;

import uk.gov.justice.services.messaging.Envelope;
import uk.gov.justice.services.messaging.JsonEnvelope;
import uk.gov.moj.cpp.unifiedsearch.query.api.response.SearchResponseEnvelopeGenerator;
import uk.gov.moj.cpp.unifiedsearch.query.api.service.CaseSearchService;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.DefendantQueryParameters;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.QueryParameters;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefendantCaseQueryApiTest {

    @Mock
    private CaseSearchService caseQueryService;

    @Mock
    private SearchResponseEnvelopeGenerator searchResponseEnvelopeGenerator;

    @InjectMocks
    private DefendantCaseQueryApi defendantCaseQueryApi;

    @Test
    public void shouldSearchCaseIndexForDefendantCases() {

        final DefendantQueryParameters defendantQueryParameters = mock(DefendantQueryParameters.class);
        final QueryParameters queryParameters = mock(QueryParameters.class);
        final JsonEnvelope responseEnvelope = mock(JsonEnvelope.class);
        final JsonObject queryResult = mock(JsonObject.class);

        final Envelope<DefendantQueryParameters> queryEnvelope = envelopeFrom(metadataWithDefaults().withName("unifiedsearch.query.defendant.cases"), defendantQueryParameters);

        when(defendantQueryParameters.toQueryParameters()).thenReturn(queryParameters);
        when(caseQueryService.searchDefendantCases(queryParameters)).thenReturn(queryResult);
        when(searchResponseEnvelopeGenerator.createFrom("unifiedsearch.query.defendant.cases", queryEnvelope.metadata(), queryResult)).thenReturn(responseEnvelope);

        assertThat(defendantCaseQueryApi.searchCaseIndex(queryEnvelope), is(responseEnvelope));

        verify(caseQueryService).searchDefendantCases(any(QueryParameters.class));

        verifyNoMoreInteractions(caseQueryService);
    }
}