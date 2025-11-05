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
import uk.gov.moj.cpp.unifiedsearch.query.api.util.ApplicationTypeFilter;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.QueryParameters;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.builders.QueryParametersBuilder;

import javax.inject.Inject;
import javax.json.JsonObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CaseQueryApiTest {

    @Mock
    private CaseSearchService caseQueryService;

    @Mock
    private SearchResponseEnvelopeGenerator searchResponseEnvelopeGenerator;

    @Mock
    private ApplicationTypeFilter applicationTypeFilter;

    @InjectMocks
    private CaseQueryApi caseQueryApi;

    @Test
    public void shouldSearchCaseIndex() {

        final QueryParameters queryParameters = mock(QueryParameters.class);
        final Envelope<QueryParameters> queryEnvelope = envelopeFrom(metadataWithDefaults().withName("unifiedsearch.query.cases"), queryParameters);

        final JsonEnvelope responseEnvelope = mock(JsonEnvelope.class);
        final JsonObject queryResult = mock(JsonObject.class);

        when(caseQueryService.searchCases(queryParameters)).thenReturn(queryResult);
        when(searchResponseEnvelopeGenerator.createFrom("unifiedsearch.query.cases", queryEnvelope.metadata(), queryResult)).thenReturn(responseEnvelope);
        when(applicationTypeFilter.filter(any(), any())).thenReturn(queryResult);

        final JsonEnvelope apiResponse = caseQueryApi.searchCaseIndex(queryEnvelope);

        assertThat(apiResponse, is(responseEnvelope));

        verify(caseQueryService).searchCases(queryParameters);
        verifyNoMoreInteractions(caseQueryService);
    }

    @Test
    public void shouldReturnNoResultsWhenAllSearchParametersAreEmpty() {
        final QueryParameters queryParameters = new QueryParametersBuilder().build();
        final Envelope<QueryParameters> envelope = envelopeFrom(metadataWithDefaults().withName("unifiedsearch.query.cases"), queryParameters);
        assertThrows(BadRequestException.class, () -> caseQueryApi.searchCaseIndex(envelope));
    }

    @Test
    public void shouldSearchCaseIndexByPartyAsnWithFilteredResponse() {
        final QueryParametersBuilder queryParametersBuilder = new QueryParametersBuilder();
        final String partyArrestSummonsNumber = "asn-1";
        final QueryParameters queryParameters =
                    queryParametersBuilder.
                            withPartyArrestSummonsNumber(partyArrestSummonsNumber).build();
        final Envelope<QueryParameters> queryEnvelope =
                        envelopeFrom(metadataWithDefaults().
                                     withName("unifiedsearch.query.cases"), queryParameters);

        final JsonEnvelope responseEnvelope = mock(JsonEnvelope.class);
        final JsonObject queryResult = mock(JsonObject.class);
        final JsonObject filteredQueryResult = mock(JsonObject.class);

        when(caseQueryService.searchCases(queryParameters)).thenReturn(queryResult);
        when(searchResponseEnvelopeGenerator.createFrom("unifiedsearch.query.cases", queryEnvelope.metadata(), filteredQueryResult)).thenReturn(responseEnvelope);
        when(applicationTypeFilter.filter(any(), any())).thenReturn(filteredQueryResult);


        assertThat(queryEnvelope.payload().getPartyArrestSummonsNumber(), is(partyArrestSummonsNumber));

        final JsonEnvelope apiResponse = caseQueryApi.searchCaseIndex(queryEnvelope);

        assertThat(apiResponse, is(responseEnvelope));

        verify(caseQueryService).searchCases(queryParameters);
        verifyNoMoreInteractions(caseQueryService);
    }
}