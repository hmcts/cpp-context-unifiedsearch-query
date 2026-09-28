package uk.gov.moj.cpp.unifiedsearch.query.builders.service;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.justice.services.test.utils.core.random.RandomGenerator.BOOLEAN;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.ADDRESS;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATION_STATUS;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATION_STATUS_LIST;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATION_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.ARREST_SUMMONS_NUMBER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.CASE_REFERENCE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.COURT_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.DATE_OF_BIRTH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_DATE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_BOX_HEARING;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_VIRTUAL_BOX_HEARING;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.JURISDICTION_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.NINO;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.OFFENCES_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PARTY_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.POST_CODE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PROSECUTING_AUTHORITY;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.ADDRESS_LINE_1;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.COURT_ORDER_VALIDITY_DATE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.CRO_NUMBER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.FIRST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.LAST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PNC_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.PROCEEDINGS_CONCLUDED;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.ChildScoreMode;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilderCache;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.AddressQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.ApplicationStatusQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.ArrestSummonsNumberSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.BoxHearingQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.CourtOrderValidityDateQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.CourtSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.CroNumberSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.DateOfBirthSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.DefendantAddressLine1QueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.FirstNameSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.HearingDateSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.JurisdictionTypeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.LastNameSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.NinoSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.PartyTypeSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.PncIdQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.ProceedingsConcludedQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.ProsecutingAuthoritySearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.ReferenceSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.VirtualBoxHearingQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.QueryParameters;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.builders.QueryParametersBuilder;

@ExtendWith(MockitoExtension.class)
public class CaseQueryBuilderServiceTest extends AbstractQueryBuilderServiceTest {

    @Mock
    private ElasticSearchQueryBuilderCache elasticSearchQueryBuilderCache;

    @Mock
    private ElasticSearchQueryBuilder elasticSearchQueryBuilder;

    @Mock
    private PartyTypeSearchQueryBuilder partyTypeSearchQueryBuilder;

    @Mock
    private HearingDateSearchQueryBuilder hearingDateSearchQueryBuilder;

    @Mock
    private DateOfBirthSearchQueryBuilder dateOfBirthSearchQueryBuilder;

    @Mock
    private CourtSearchQueryBuilder courtSearchQueryBuilder;

    @Mock
    private BoxHearingQueryBuilder boxHearingQueryBuilder;

    @Mock
    private Query.Builder queryBuilder;

    @Mock
    private Query query;

    @Mock
    private JurisdictionTypeQueryBuilder jurisdictionTypeQueryBuilder;

    @Mock
    private ProsecutingAuthoritySearchQueryBuilder prosecutingAuthoritySearchQueryBuilder;

    @Mock
    private NinoSearchQueryBuilder ninoSearchQueryBuilder;

    @Mock
    private ArrestSummonsNumberSearchQueryBuilder arrestSummonsNumberSearchQueryBuilder;

    @Mock
    private ProceedingsConcludedQueryBuilder proceedingsConcludedQueryBuilder;

    @Mock
    private FirstNameSearchQueryBuilder firstNameSearchQueryBuilder;

    @Mock
    private LastNameSearchQueryBuilder lastNameSearchQueryBuilder;

    @Mock
    private CroNumberSearchQueryBuilder croNumberSearchQueryBuilder;

    @Mock
    private PncIdQueryBuilder pncIdQueryBuilder;

    @Mock
    private AddressQueryBuilder addressQueryBuilder;

    @Mock
    private DefendantAddressLine1QueryBuilder addressLine1QueryBuilder;

    @Mock
    private CourtOrderValidityDateQueryBuilder courtOrderValidityDateQueryBuilder;

    @Mock
    private VirtualBoxHearingQueryBuilder virtualBoxHearingQueryBuilder;

    @InjectMocks
    private CaseQueryBuilderService caseQueryBuilderService;

    @Mock
    private ApplicationStatusQueryBuilder applicationStatusQueryBuilder;

    private final ReferenceSearchQueryBuilder referenceSearchQueryBuilder = new ReferenceSearchQueryBuilder();

    @Test
    public void shouldReturnNothingIfNoParameters() {

        final QueryParameters queryParameters = new QueryParametersBuilder().build();

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());

        assertThat(resultQuery.bool().must(), hasSize(0));

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache);
    }

    @Test
    public void shouldReturnNothingIfEmptyParameters() {

        final QueryParameters queryParameters = new QueryParametersBuilder().
                withPartyName(" ").
                withCaseReference("  ")
                .withPartyTypes("   ")
                .withProsecutingAuthority("  ")
                .build();

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());

        assertThat(resultQuery.bool().must(), hasSize(0));

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, partyTypeSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForPartyName() {

        final String queryValue = "testName";
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withPartyName(queryValue)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(NAME)).thenReturn(elasticSearchQueryBuilder);
        when(elasticSearchQueryBuilder.getQueryBuilderBy(queryValue, emptyList())).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());

        assertThat(resultQuery.bool().must(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(NAME);
        verify(elasticSearchQueryBuilder).getQueryBuilderBy(queryValue, emptyList());

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, partyTypeSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForPartyNameWithAliasFlagSetToTrue() {

        final String queryValue = "testName";
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withPartyName(queryValue)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(NAME)).thenReturn(elasticSearchQueryBuilder);
        when(elasticSearchQueryBuilder.getQueryBuilderBy(queryValue, emptyList())).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());

        assertThat(resultQuery.bool().must(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(NAME);
        verify(elasticSearchQueryBuilder).getQueryBuilderBy(queryValue, emptyList());

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, partyTypeSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForPartyNameAndPartyType() {

        final String queryValue = "testName";
        final String partyTypes = "DEFENDANT,APPLICANT";
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withPartyName(queryValue)
                .withPartyTypes(partyTypes)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(NAME)).thenReturn(elasticSearchQueryBuilder);
        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(PARTY_TYPE)).thenReturn(partyTypeSearchQueryBuilder);
        when(elasticSearchQueryBuilder.getQueryBuilderBy(eq(queryValue), anyList())).thenReturn(query);
        when(partyTypeSearchQueryBuilder.getQueryBuilderBy(partyTypes)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());

        assertThat(resultQuery.bool().must(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(NAME);
        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(PARTY_TYPE);
        verify(elasticSearchQueryBuilder).getQueryBuilderBy(eq(queryValue), anyList());
        verify(partyTypeSearchQueryBuilder).getQueryBuilderBy(partyTypes);

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, partyTypeSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForCaseReference() {

        final String caseReference = "TVL1234";
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withCaseReference(caseReference)
                .build();
        final List<Query.Builder> applicationTypeQueryBuilder = emptyList();
        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CASE_REFERENCE)).thenReturn(elasticSearchQueryBuilder);
        when(elasticSearchQueryBuilder.getQueryBuilderBy(caseReference, applicationTypeQueryBuilder)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());
        assertThat(resultQuery.bool().must(), hasSize(1));
        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(CASE_REFERENCE);
        verify(elasticSearchQueryBuilder).getQueryBuilderBy(caseReference, applicationTypeQueryBuilder);

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, partyTypeSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForAddress() {

        final String address = "123 test address";
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withPartyAddress(address)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(ADDRESS)).thenReturn(elasticSearchQueryBuilder);
        when(elasticSearchQueryBuilder.getQueryBuilderBy(address)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkNestedFilter(resultQuery);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(ADDRESS);
        verify(elasticSearchQueryBuilder).getQueryBuilderBy(address);

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, partyTypeSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForPostCode() {

        final String postCode = "w12 7qd";
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withPartyPostCode(postCode)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(POST_CODE)).thenReturn(elasticSearchQueryBuilder);
        when(elasticSearchQueryBuilder.getQueryBuilderBy(postCode)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkNestedFilter(resultQuery);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(POST_CODE);
        verify(elasticSearchQueryBuilder).getQueryBuilderBy(postCode);

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, partyTypeSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForHearingDateFrom() {
        final String hearingDateFrom = "2019-04-19";
        final String hearingDateTo = "";
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withHearingDateFrom(hearingDateFrom)
                .withHearingDateTo(hearingDateTo)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(HEARING_DATE)).thenReturn(elasticSearchQueryBuilder);
        when(elasticSearchQueryBuilder.getQueryBuilderBy(hearingDateFrom, hearingDateTo)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkNestedFilter(resultQuery);


        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(HEARING_DATE);
        verify(elasticSearchQueryBuilder).getQueryBuilderBy(hearingDateFrom, hearingDateTo);

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, hearingDateSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForHearingDateFromAndHearingDateTo() {
        final String hearingDateFrom = "2019-04-19";
        final String hearingDateTo = "2019-05-17";

        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withHearingDateFrom(hearingDateFrom)
                .withHearingDateTo(hearingDateTo)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(HEARING_DATE)).thenReturn(elasticSearchQueryBuilder);

        when(elasticSearchQueryBuilder.getQueryBuilderBy(hearingDateFrom, hearingDateTo)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkNestedFilter(resultQuery);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(HEARING_DATE);
        verify(elasticSearchQueryBuilder).getQueryBuilderBy(hearingDateFrom, hearingDateTo);

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, hearingDateSearchQueryBuilder);
    }


    @Test
    public void shouldReturnQueryBuilderForApplicationType() {

        final String applicationType = "Application for testing";
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withApplicationType(applicationType)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(APPLICATION_TYPE)).thenReturn(elasticSearchQueryBuilder);
        when(elasticSearchQueryBuilder.getQueryBuilderBy(applicationType)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());
        checkNestedFilter(resultQuery);
        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(APPLICATION_TYPE);
        verify(elasticSearchQueryBuilder).getQueryBuilderBy(applicationType);

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, partyTypeSearchQueryBuilder);
        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache);
    }

    @Test
    public void shouldReturnQueryBuilderForPartyTypes() {
        final String partyTypes = "APPLICANT,DEFENDANT";
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withPartyTypes(partyTypes)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(PARTY_TYPE)).thenReturn(partyTypeSearchQueryBuilder);
        when(partyTypeSearchQueryBuilder.getQueryBuilderBy(partyTypes)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkNestedFilter(resultQuery);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(PARTY_TYPE);
        verify(partyTypeSearchQueryBuilder).getQueryBuilderBy(partyTypes);

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, partyTypeSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForDateOfBirth() {
        final String dateOfBirth = "1954-04-29";

        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withPartyDateOfBirth(dateOfBirth)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(DATE_OF_BIRTH)).thenReturn(elasticSearchQueryBuilder);
        when(elasticSearchQueryBuilder.getQueryBuilderBy(dateOfBirth)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkNestedFilter(resultQuery);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(DATE_OF_BIRTH);
        verify(elasticSearchQueryBuilder).getQueryBuilderBy(dateOfBirth);

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, dateOfBirthSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForProsecutingAuthority() {
        final String prosecutingAuthority = "tfl";
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withProsecutingAuthority(prosecutingAuthority)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(PROSECUTING_AUTHORITY)).thenReturn(prosecutingAuthoritySearchQueryBuilder);
        when(prosecutingAuthoritySearchQueryBuilder.getQueryBuilderBy(prosecutingAuthority)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());
        assertThat(resultQuery.bool().filter(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(PROSECUTING_AUTHORITY);
        verify(prosecutingAuthoritySearchQueryBuilder).getQueryBuilderBy(prosecutingAuthority);

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, partyTypeSearchQueryBuilder, prosecutingAuthoritySearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForCourtId() {
        final String courtId = "3fdcf368-ea16-40cd-a7f2-d25b04637721";
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withCourtId(courtId)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(COURT_ID)).thenReturn(courtSearchQueryBuilder);
        when(courtSearchQueryBuilder.getQueryBuilderBy(courtId)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());
        checkNestedFilter(resultQuery);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(COURT_ID);
        verify(courtSearchQueryBuilder).getQueryBuilderBy(courtId);

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, courtSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForAtLeastOneJurisdictionTypeSetToTrue() {
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withSjp(true)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(JURISDICTION_TYPE)).thenReturn(jurisdictionTypeQueryBuilder);
        when(jurisdictionTypeQueryBuilder.getQueryBuilderBy(true, null, null)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());
        assertThat(resultQuery.bool().filter(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(JURISDICTION_TYPE);
        verify(jurisdictionTypeQueryBuilder).getQueryBuilderBy(true, null, null);

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, jurisdictionTypeQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForIsBoxHearing() {

        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withBoxWorkHearing(TRUE)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(IS_BOX_HEARING)).thenReturn(boxHearingQueryBuilder);
        when(boxHearingQueryBuilder.getQueryBuilderBy(true)).thenReturn(query);
        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(APPLICATION_STATUS)).thenReturn(applicationStatusQueryBuilder);
        APPLICATION_STATUS_LIST.stream().forEach(applicationStatus ->
                when(applicationStatusQueryBuilder.getQueryBuilderBy(applicationStatus)).thenReturn(query));

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());

        checkExactMatchesNestedFilter(resultQuery, 2, 1);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(IS_BOX_HEARING);
        verify(boxHearingQueryBuilder).getQueryBuilderBy(true);
        verify(elasticSearchQueryBuilderCache, atLeast(3)).getQueryBuilderFromCacheBy(APPLICATION_STATUS);
        APPLICATION_STATUS_LIST.stream().forEach(applicationStatus ->
                verify(applicationStatusQueryBuilder).getQueryBuilderBy(applicationStatus));

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, boxHearingQueryBuilder, applicationStatusQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForIsBoxHearingAndCaseReference() {

        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withBoxWorkHearing(TRUE)
                .withCaseReference("caseUrn")
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(IS_BOX_HEARING)).thenReturn(boxHearingQueryBuilder);
        when(boxHearingQueryBuilder.getQueryBuilderBy(true)).thenReturn(query);
        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CASE_REFERENCE)).thenReturn(referenceSearchQueryBuilder);
        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(APPLICATION_STATUS)).thenReturn(new ApplicationStatusQueryBuilder());

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());

        checkExactMatchesNestedFilter(resultQuery, 3, 1);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(IS_BOX_HEARING);
        verify(boxHearingQueryBuilder).getQueryBuilderBy(true);
        verify(elasticSearchQueryBuilderCache, atLeast(3)).getQueryBuilderFromCacheBy(APPLICATION_STATUS);
    }

    @Test
    public void shouldReturnQueryBuilderForIsVirtualBoxHearing() {

        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withVirtualBoxWorkHearing(TRUE)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(IS_VIRTUAL_BOX_HEARING)).thenReturn(virtualBoxHearingQueryBuilder);
        when(virtualBoxHearingQueryBuilder.getQueryBuilderBy(true)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());

        checkNestedFilter(resultQuery);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(IS_VIRTUAL_BOX_HEARING);
        verify(virtualBoxHearingQueryBuilder).getQueryBuilderBy(true);

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, virtualBoxHearingQueryBuilder);
    }


    @Test
    public void shouldReturnQueryBuilderForPartyNino() {

        final String queryValue = "AB123456Z";
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withPartyNINO(queryValue)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(NINO)).thenReturn(elasticSearchQueryBuilder);
        when(elasticSearchQueryBuilder.getQueryBuilderBy(queryValue)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());

        checkNestedFilter(resultQuery);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(NINO);
        verify(elasticSearchQueryBuilder).getQueryBuilderBy(queryValue);

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, ninoSearchQueryBuilder);
    }


    @Test
    public void shouldReturnQueryBuilderForASN() {

        final String queryValue = "123456ABCD";
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withPartyArrestSummonsNumber(queryValue)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(ARREST_SUMMONS_NUMBER)).thenReturn(elasticSearchQueryBuilder);
        when(elasticSearchQueryBuilder.getQueryBuilderBy(queryValue)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkNestedFilter(resultQuery);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(ARREST_SUMMONS_NUMBER);
        verify(elasticSearchQueryBuilder).getQueryBuilderBy(queryValue);

        verifyNoMoreInteractions(elasticSearchQueryBuilder, elasticSearchQueryBuilderCache, arrestSummonsNumberSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForIsCrownAndProceedingsConluded() {
        final boolean isProceedingsIncluded = false;

        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withProceedingsConcluded(isProceedingsIncluded)
                .build();

        final Query.Builder multiCriteriaQueryBuilder = new Query.Builder();
        multiCriteriaQueryBuilder.bool(BoolQuery.of( b -> b.must(query ) ) ) ;

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(PROCEEDINGS_CONCLUDED)).thenReturn(proceedingsConcludedQueryBuilder);
        when(proceedingsConcludedQueryBuilder.getQueryBuilderBy(false)).thenReturn(multiCriteriaQueryBuilder.build());

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkMatchesNestedFilter(resultQuery, 1, 1, 1);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(PROCEEDINGS_CONCLUDED);
        verify(proceedingsConcludedQueryBuilder).getQueryBuilderBy(false);

        verifyNoMoreInteractions(elasticSearchQueryBuilderCache, proceedingsConcludedQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForIsCrownAndHasActiveCourtOrder() {
        final String courtOrderValidityDate = "2021-01-01";

        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withCourtOrderValidityDate(courtOrderValidityDate)
                .build();

        final Query.Builder multiCriteriaQueryBuilder = new Query.Builder();
        multiCriteriaQueryBuilder.bool( BoolQuery.of( b -> b.must(query)));

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(COURT_ORDER_VALIDITY_DATE)).thenReturn(courtOrderValidityDateQueryBuilder);
        when(courtOrderValidityDateQueryBuilder.getQueryBuilderBy(courtOrderValidityDate)).thenReturn(multiCriteriaQueryBuilder.build());

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkMatchesNestedFilter(resultQuery, 1, 1, 1);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(COURT_ORDER_VALIDITY_DATE);
        verify(courtOrderValidityDateQueryBuilder).getQueryBuilderBy(courtOrderValidityDate);

        verifyNoMoreInteractions(elasticSearchQueryBuilderCache, courtOrderValidityDateQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForExactMatchesForFirstName() {
        final String firstNameParam = "firstName";

        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withPartyFirstName(firstNameParam)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(FIRST_NAME)).thenReturn(firstNameSearchQueryBuilder);
        when(firstNameSearchQueryBuilder.getQueryBuilderBy(firstNameParam)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkExactMatchesNestedFilter(resultQuery, 1, 1);
        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(FIRST_NAME);
        verify(firstNameSearchQueryBuilder).getQueryBuilderBy(firstNameParam);

        verifyNoMoreInteractions(queryBuilder, elasticSearchQueryBuilderCache, firstNameSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForExactMatchesForLastName() {
        final String lastNameParam = "lastName";

        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withPartyLastName(lastNameParam)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(LAST_NAME)).thenReturn(lastNameSearchQueryBuilder);
        when(lastNameSearchQueryBuilder.getQueryBuilderBy(lastNameParam)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkExactMatchesNestedFilter(resultQuery, 1, 1);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(LAST_NAME);
        verify(lastNameSearchQueryBuilder).getQueryBuilderBy(lastNameParam);

        verifyNoMoreInteractions(queryBuilder, elasticSearchQueryBuilderCache, lastNameSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForExactMatchesForPncId() {
        final String pncIdParam = "pncId";

        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withPncId(pncIdParam)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(PNC_ID)).thenReturn(pncIdQueryBuilder);
        when(pncIdQueryBuilder.getQueryBuilderBy(pncIdParam)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkExactMatchesNestedFilter(resultQuery, 1, 1);
        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(PNC_ID);
        verify(pncIdQueryBuilder).getQueryBuilderBy(PNC_ID);

        verifyNoMoreInteractions(queryBuilder, elasticSearchQueryBuilderCache, pncIdQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForExactMatchesForCroNumber() {
        final String croNumberParam = "croNumber";

        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withCroNumber(croNumberParam)
                .build();

        when(croNumberSearchQueryBuilder.getQueryBuilderBy(croNumberParam)).thenReturn(query);
        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CRO_NUMBER)).thenReturn(croNumberSearchQueryBuilder);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkExactMatchesNestedFilter(resultQuery, 1, 1);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(CRO_NUMBER);
        verify(croNumberSearchQueryBuilder).getQueryBuilderBy(croNumberParam);

        verifyNoMoreInteractions(queryBuilder, elasticSearchQueryBuilderCache, croNumberSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForExactMatchesForAddressLine1() {
        final String addressLine1Param = "addressLine1";

        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withPartyAddressLine1(addressLine1Param)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(ADDRESS_LINE_1)).thenReturn(addressLine1QueryBuilder);
        when(addressLine1QueryBuilder.getQueryBuilderBy(ADDRESS_LINE_1)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkExactMatchesNestedFilter(resultQuery, 1, 1);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(ADDRESS_LINE_1);
        verify(addressLine1QueryBuilder).getQueryBuilderBy(addressLine1Param);

        verifyNoMoreInteractions(queryBuilder, elasticSearchQueryBuilderCache, addressLine1QueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderWhenHasOffenceFlagIsSet() {
        final boolean hasOffence = BOOLEAN.next();
        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withHasOffence(hasOffence)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(OFFENCES_NESTED_PATH)).thenReturn(elasticSearchQueryBuilder);
        when(elasticSearchQueryBuilder.getQueryBuilderBy(hasOffence)).thenReturn(query);

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());

        assertThat(resultQuery.bool().must(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(eq(OFFENCES_NESTED_PATH));
        verify(elasticSearchQueryBuilder).getQueryBuilderBy(eq(hasOffence));

        verifyNoMoreInteractions(elasticSearchQueryBuilderCache, elasticSearchQueryBuilder, queryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForExcludeCompletedApplications() {
        final boolean excludeCompletedApplications = true;

        final QueryParameters queryParameters = new QueryParametersBuilder()
                .withExcludeCompletedApplications(excludeCompletedApplications)
                .build();
        final Query.Builder multiCriteriaQueryBuilder = new Query.Builder();
        multiCriteriaQueryBuilder.bool(BoolQuery.of(b -> b.must(query)));


        final Query multiCriteriaQuery = multiCriteriaQueryBuilder.build();
        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(APPLICATION_STATUS)).thenReturn(applicationStatusQueryBuilder);
        APPLICATION_STATUS_LIST.stream().forEach(applicationStatus ->
            when(applicationStatusQueryBuilder.getQueryBuilderBy(applicationStatus)).thenReturn(multiCriteriaQuery));

        final Query.Builder resultQueryBuilder = caseQueryBuilderService.builder(queryParameters);
        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());
        final BoolQuery resultQueryBuilderAsBool = resultQuery.bool();
        assertThat(resultQueryBuilderAsBool.must(), hasSize(1));
        final List<Query> mustClause = resultQueryBuilderAsBool.must();
        assertThat(mustClause, hasSize(1));

        verify(elasticSearchQueryBuilderCache, atLeast(3)).getQueryBuilderFromCacheBy(APPLICATION_STATUS);
        APPLICATION_STATUS_LIST.stream().forEach(applicationStatus ->
            verify(applicationStatusQueryBuilder).getQueryBuilderBy(applicationStatus));
        verifyNoMoreInteractions(elasticSearchQueryBuilderCache, applicationStatusQueryBuilder);
    }

    @Override
    protected void checkNestedFilter(final Query resultQuery) {
        assertThat(resultQuery.bool(), notNullValue());
        final BoolQuery resultQueryBuilderAsBool = resultQuery.bool();
        assertThat(resultQueryBuilderAsBool.filter(), hasSize(0));
        assertThat(resultQueryBuilderAsBool.should(), hasSize(0));
        assertThat(resultQueryBuilderAsBool.mustNot(), hasSize(0));
        final List<Query> must = resultQueryBuilderAsBool.must();
        assertThat(must, hasSize(1));

        final Query firstMust = resultQueryBuilderAsBool.must().get(0);
        assertThat(firstMust.nested(), notNullValue());
        final NestedQuery nestedQueryBuilder = firstMust.nested();
        assertThat(nestedQueryBuilder.scoreMode(), is(ChildScoreMode.Avg));
        final Query innerQueryBuilder = nestedQueryBuilder.query();
        assertThat(innerQueryBuilder.bool(), notNullValue());
        final BoolQuery innerBoolQueryBuilder = innerQueryBuilder.bool();

        assertThat(innerBoolQueryBuilder.should(), hasSize(0));
        assertThat(innerBoolQueryBuilder.filter(), hasSize(0));
        assertThat(innerBoolQueryBuilder.mustNot(), hasSize(0));
        assertThat(innerBoolQueryBuilder.must(), hasSize(1));
    }

    @Override
    protected void checkExactMatchesNestedFilter(final Query resultQuery, final int mustSize, final int innerMust) {
        assertThat(resultQuery.bool(), notNullValue());
        final BoolQuery resultQueryBuilderAsBool = resultQuery.bool();
        assertThat(resultQueryBuilderAsBool.filter(), hasSize(0));
        assertThat(resultQueryBuilderAsBool.should(), hasSize(0));
        assertThat(resultQueryBuilderAsBool.mustNot(), hasSize(0));
        final List<Query> must = resultQueryBuilderAsBool.must();
        assertThat(must, hasSize(mustSize));

        final Query firstMust = resultQueryBuilderAsBool.must().get(0);
        assertThat(firstMust.nested(), notNullValue());
        final NestedQuery nestedQueryBuilder = firstMust.nested();
        assertThat(nestedQueryBuilder.scoreMode(), is(ChildScoreMode.Avg));
        final Query innerQueryBuilder = nestedQueryBuilder.query();
        assertThat(innerQueryBuilder.bool(), notNullValue());
        final BoolQuery innerBoolQueryBuilder = innerQueryBuilder.bool();

        assertThat(innerBoolQueryBuilder.should(), hasSize(0));
        assertThat(innerBoolQueryBuilder.filter(), hasSize(0));
        assertThat(innerBoolQueryBuilder.mustNot(), hasSize(0));
        assertThat(innerBoolQueryBuilder.must(), hasSize(innerMust));
    }

    private void checkMatchesNestedFilter(final Query resultQuery, final int mustSize, final int shouldSize, final int innerShould) {
        assertThat(resultQuery.bool(), notNullValue());
        final BoolQuery resultQueryBuilderAsBool = resultQuery.bool();
        assertThat(resultQueryBuilderAsBool.filter(), hasSize(0));
        assertThat(resultQueryBuilderAsBool.should(), hasSize(0));
        assertThat(resultQueryBuilderAsBool.mustNot(), hasSize(0));
        final List<Query> must = resultQueryBuilderAsBool.must();
        assertThat(must, hasSize(mustSize));

        final Query firstMust = must.get(0);
        assertThat(firstMust.bool(), notNullValue());
        final BoolQuery boolQueryBuilder = firstMust.bool();
        final List<Query> should = boolQueryBuilder.should();
        assertThat(should, hasSize(shouldSize));

        final Query firstShould = should.get(0);
        assertThat(firstShould.nested(), notNullValue());
        final NestedQuery nestedQueryBuilder = firstShould.nested();
        assertThat(nestedQueryBuilder.scoreMode(), is(ChildScoreMode.Avg));
        final Query innerQueryBuilder = nestedQueryBuilder.query();
        assertThat(innerQueryBuilder.bool(), notNullValue());
    }

}
