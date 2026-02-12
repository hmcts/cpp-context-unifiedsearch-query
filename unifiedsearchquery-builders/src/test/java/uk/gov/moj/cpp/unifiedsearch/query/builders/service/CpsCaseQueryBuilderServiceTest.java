package uk.gov.moj.cpp.unifiedsearch.query.builders.service;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CASE_STATUS;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CASE_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CJS_AREA;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.COURT_HOUSE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.COURT_ROOM;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_AREA;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_UNIT;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CROWN_ADVOCATE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.DATE_OF_BIRTH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.HEARING_DATE_TIME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.JURISDICTION;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.LINKED_CASE_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OPERATION_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARALEGAL_OFFICER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_NAMES;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PROSECUTOR;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.URN;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.ElasticSearchQueryBuilderCache;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.DateOfBirthSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.PartyTypeSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CaseStatusQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CaseTypeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CjsAreaQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CourtHouseQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CourtRoomQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CpsAreaQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CpsPartyNameAndTypeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CpsUnitQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CrownAdvocateQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.ExcludeAutomaticallyLinkedCasesQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.HearingDateTimeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.JurisdictionQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.OperationNameQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.ParalegalOfficerQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.ProsecutorSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.UrnSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.CpsQueryParameters;

@ExtendWith(MockitoExtension.class)
public class CpsCaseQueryBuilderServiceTest extends AbstractQueryBuilderServiceTest {

    @Mock
    private ElasticSearchQueryBuilderCache elasticSearchQueryBuilderCache;

    @Mock
    private ElasticSearchQueryBuilder elasticSearchQueryBuilder;

    @Mock
    private UrnSearchQueryBuilder urnSearchQueryBuilder;

    @Mock
    private CaseStatusQueryBuilder caseStatusQueryBuilder;

    @Mock
    private CaseTypeQueryBuilder caseTypeQueryBuilder;

    @Mock
    private CpsUnitQueryBuilder cpsUnitQueryBuilder;

    @Mock
    private CjsAreaQueryBuilder cjsAreaQueryBuilder;

    @Mock
    private ParalegalOfficerQueryBuilder paralegalOfficerQueryBuilder;

    @Mock
    private CrownAdvocateQueryBuilder crownAdvocateQueryBuilder;

    @Mock
    private OperationNameQueryBuilder operationNameQueryBuilder;

    @Mock
    private PartyTypeSearchQueryBuilder partyTypeQueryBuilder;

    @Mock
    private HearingDateTimeQueryBuilder hearingDateTimeQueryBuilder;

    @Mock
    private DateOfBirthSearchQueryBuilder dateOfBirthSearchQueryBuilder;

    @Mock
    private CourtRoomQueryBuilder courtRoomQueryBuilder;

    @Mock
    private CourtHouseQueryBuilder courtHouseQueryBuilder;

    @Mock
    private Query query;

    @Mock
    private ProsecutorSearchQueryBuilder prosecutorSearchQueryBuilder;

    @Mock
    private JurisdictionQueryBuilder jurisdictionQueryBuilder;

    @Mock
    private CpsAreaQueryBuilder cpsAreaQueryBuilder;

    @Mock
    private ExcludeAutomaticallyLinkedCasesQueryBuilder excludeAutomaticallyLinkedCasesQueryBuilder;

    @Mock
    private CpsPartyNameAndTypeQueryBuilder cpsPartyNameAndTypeQueryBuilder;

    @InjectMocks
    private CpsCaseQueryBuilderService cpsCaseQueryBuilderService;

    @BeforeEach
    public void setUp() {
        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(PARTY_NAMES)).thenReturn(elasticSearchQueryBuilder);
    }

    @Test
    public void shouldReturnNothingIfNoParameters() {

        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .build();

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);
        final Query resultQuery = resultQueryBuilder.build();
        final BoolQuery boolQuery = resultQuery.bool();

        assertThat(boolQuery, notNullValue());
        assertThat(boolQuery.must(), hasSize(0));
    }

    @Test
    public void shouldReturnNothingIfEmptyParameters() {

        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withUrn("  ")
                .withPartyTypes("   ")
                .withProsecutor("  ")
                .build();

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);
        final BoolQuery boolQuery = resultQueryBuilder.build().bool();

        assertThat(boolQuery, notNullValue());

        assertThat(boolQuery.must(), hasSize(0));

        verifyNoMoreInteractions(urnSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForCaseUrn() {

        final String urn = "testUrn";
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withUrn(urn)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(URN))
                .thenReturn(urnSearchQueryBuilder);
        when(urnSearchQueryBuilder.getQueryBuilderBy(urn))
                .thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);
        final BoolQuery boolQuery = resultQueryBuilder.build().bool();

        assertThat(boolQuery, notNullValue());
        assertThat(boolQuery.filter(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(URN);
        verify(urnSearchQueryBuilder).getQueryBuilderBy(urn);

        verifyNoMoreInteractions(urnSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForCaseStatus() {

        final String caseStatus = "caseStatus";
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withCaseStatusCode(caseStatus)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CASE_STATUS))
                .thenReturn(caseStatusQueryBuilder);
        when(caseStatusQueryBuilder.getQueryBuilderBy(caseStatus))
                .thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);
        final BoolQuery boolQuery = resultQueryBuilder.build().bool();

        assertThat(boolQuery, notNullValue());
        assertThat(boolQuery.filter(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(CASE_STATUS);
        verify(caseStatusQueryBuilder).getQueryBuilderBy(caseStatus);

        verifyNoMoreInteractions(caseStatusQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForCaseType() {

        final String caseType = "caseType";
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withCaseType(caseType)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CASE_TYPE))
                .thenReturn(caseTypeQueryBuilder);
        when(caseTypeQueryBuilder.getQueryBuilderBy(caseType))
                .thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);
        final BoolQuery boolQuery = resultQueryBuilder.build().bool();

        assertThat(boolQuery, notNullValue());
        assertThat(boolQuery.filter(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(CASE_TYPE);
        verify(caseTypeQueryBuilder).getQueryBuilderBy(caseType);

        verifyNoMoreInteractions(caseTypeQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForCpsUnit() {

        final String cpsUnit = "cpsUnit";
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withCpsUnitCode(cpsUnit)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CPS_UNIT))
                .thenReturn(cpsUnitQueryBuilder);
        when(cpsUnitQueryBuilder.getQueryBuilderBy(cpsUnit))
                .thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);
        final BoolQuery boolQuery = resultQueryBuilder.build().bool();

        assertThat(boolQuery, notNullValue());
        assertThat(boolQuery.filter(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(CPS_UNIT);
        verify(cpsUnitQueryBuilder).getQueryBuilderBy(cpsUnit);

        verifyNoMoreInteractions(cpsUnitQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForCjsArea() {

        final String cjsArea = "cjsArea";
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withCjsAreaCode(cjsArea)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CJS_AREA))
                .thenReturn(cjsAreaQueryBuilder);
        when(cjsAreaQueryBuilder.getQueryBuilderBy(cjsArea))
                .thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);
        final BoolQuery boolQuery = resultQueryBuilder.build().bool();

        assertThat(boolQuery, notNullValue());
        assertThat(boolQuery.filter(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(CJS_AREA);
        verify(cjsAreaQueryBuilder).getQueryBuilderBy(cjsArea);

        verifyNoMoreInteractions(cjsAreaQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForOperationName() {

        final String operationName = "operationName";
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withOperationName(operationName)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(OPERATION_NAME))
                .thenReturn(operationNameQueryBuilder);
        when(operationNameQueryBuilder.getQueryBuilderBy(operationName))
                .thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);
        final BoolQuery boolQuery = resultQueryBuilder.build().bool();
        assertThat(boolQuery, notNullValue());
        assertThat(boolQuery.filter(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(OPERATION_NAME);
        verify(operationNameQueryBuilder).getQueryBuilderBy(operationName);

        verifyNoMoreInteractions(operationNameQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForParalegalOfficer() {

        final String paralegalOfficer = "paralegalOfficer";
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withParalegalOfficer(paralegalOfficer)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(PARALEGAL_OFFICER))
                .thenReturn(paralegalOfficerQueryBuilder);
        when(paralegalOfficerQueryBuilder.getQueryBuilderBy(paralegalOfficer))
                .thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);
        final Query resultQuery = resultQueryBuilder.build();
        assertThat(resultQuery.bool(), notNullValue());
        assertThat(resultQuery.bool().filter(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(PARALEGAL_OFFICER);
        verify(paralegalOfficerQueryBuilder).getQueryBuilderBy(paralegalOfficer);

        verifyNoMoreInteractions(paralegalOfficerQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForCrownAdvocate() {

        final String crownAdvocate = "crownAdvocate";
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withCrownAdvocate(crownAdvocate)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CROWN_ADVOCATE))
                .thenReturn(crownAdvocateQueryBuilder);
        when(crownAdvocateQueryBuilder.getQueryBuilderBy(crownAdvocate))
                .thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);
        final Query resultQuery = resultQueryBuilder.build();
        final BoolQuery boolQuery = resultQuery.bool();
        assertThat(boolQuery, notNullValue());
        assertThat(boolQuery.filter(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(CROWN_ADVOCATE);
        verify(crownAdvocateQueryBuilder).getQueryBuilderBy(crownAdvocate);

        verifyNoMoreInteractions(crownAdvocateQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForProsecutor() {
        final String prosecutingAuthority = "tfl";
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withProsecutor(prosecutingAuthority)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(PROSECUTOR))
                .thenReturn(prosecutorSearchQueryBuilder);
        when(prosecutorSearchQueryBuilder.getQueryBuilderBy(prosecutingAuthority))
                .thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);
        final Query resultQuery = resultQueryBuilder.build();
        final BoolQuery boolQuery = resultQuery.bool();

        assertThat(boolQuery, notNullValue());
        assertThat(boolQuery.filter(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(PROSECUTOR);
        verify(prosecutorSearchQueryBuilder).getQueryBuilderBy(prosecutingAuthority);

        verifyNoMoreInteractions(prosecutorSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForHearingDateFrom() {
        final String hearingDateFrom = "2020-01-19";
        final String hearingDateTo = "";
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withHearingDateFrom(hearingDateFrom)
                .withHearingDateTo(hearingDateTo)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(HEARING_DATE_TIME))
                .thenReturn(hearingDateTimeQueryBuilder);
        when(hearingDateTimeQueryBuilder.getQueryBuilderBy(hearingDateFrom, hearingDateTo))
                .thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkNestedFilter(resultQuery);


        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(HEARING_DATE_TIME);
        verify(hearingDateTimeQueryBuilder).getQueryBuilderBy(hearingDateFrom, hearingDateTo);

        verifyNoMoreInteractions(hearingDateTimeQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForHearingDateFromAndHearingDateTo() {
        final String hearingDateFrom = "2020-01-19";
        final String hearingDateTo = "2020-01-20";

        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withHearingDateFrom(hearingDateFrom)
                .withHearingDateTo(hearingDateTo)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(HEARING_DATE_TIME))
                .thenReturn(hearingDateTimeQueryBuilder);

        when(hearingDateTimeQueryBuilder.getQueryBuilderBy(hearingDateFrom, hearingDateTo))
                .thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkNestedFilter(resultQuery);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(HEARING_DATE_TIME);
        verify(hearingDateTimeQueryBuilder).getQueryBuilderBy(hearingDateFrom, hearingDateTo);

        verifyNoMoreInteractions(hearingDateTimeQueryBuilder);
    }

    @Test
    public void shouldNotReturnQueryBuilderForPartyTypesWithoutNameField() {
        final String partyTypes = "SUSPECT,WITNESS";
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withPartyTypes(partyTypes)
                .build();

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkExactMatchesNestedFilter(resultQuery, 0, 0);

        verifyNoMoreInteractions(partyTypeQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForDateOfBirth() {
        final String dateOfBirth = "1954-04-29";

        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withDateOfBirth(dateOfBirth)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(DATE_OF_BIRTH))
                .thenReturn(elasticSearchQueryBuilder);
        when(elasticSearchQueryBuilder.getQueryBuilderBy(dateOfBirth)).thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkNestedFilter(resultQuery);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(DATE_OF_BIRTH);
        verify(elasticSearchQueryBuilder).getQueryBuilderBy(dateOfBirth);

        verifyNoMoreInteractions(dateOfBirthSearchQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForCourtHouse() {
        final String courtHouse = "courtHouse";
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withCourtHouse(courtHouse)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(COURT_HOUSE))
                .thenReturn(courtHouseQueryBuilder);
        when(courtHouseQueryBuilder.getQueryBuilderBy(courtHouse)).thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkNestedFilter(resultQuery);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(COURT_HOUSE);
        verify(courtHouseQueryBuilder).getQueryBuilderBy(courtHouse);

        verifyNoMoreInteractions(courtHouseQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForJurisdictionType() {
        final String jurisdiction = "jurisdiction";
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withJurisdictionTypes(jurisdiction)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(JURISDICTION))
                .thenReturn(jurisdictionQueryBuilder);
        when(jurisdictionQueryBuilder.getQueryBuilderBy(jurisdiction)).thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkNestedFilter(resultQuery);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(JURISDICTION);
        verify(jurisdictionQueryBuilder).getQueryBuilderBy(jurisdiction);

        verifyNoMoreInteractions(jurisdictionQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForCourtRoom() {
        final String courtRoom = "courtRoom";
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withCourtRoom(courtRoom)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(COURT_ROOM))
                .thenReturn(courtRoomQueryBuilder);
        when(courtRoomQueryBuilder.getQueryBuilderBy(courtRoom)).thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);

        final Query resultQuery = resultQueryBuilder.build();
        checkNestedFilter(resultQuery);

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(COURT_ROOM);
        verify(courtRoomQueryBuilder).getQueryBuilderBy(courtRoom);

        verifyNoMoreInteractions(courtRoomQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForCpsArea() {

        final String cpsArea = "cpsArea";
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withCpsAreaCode(cpsArea)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CPS_AREA))
                .thenReturn(cpsAreaQueryBuilder);
        when(cpsAreaQueryBuilder.getQueryBuilderBy(cpsArea))
                .thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);
        final Query resultQuery = resultQueryBuilder.build();
        final BoolQuery boolQuery = resultQuery.bool();

        assertThat(boolQuery, notNullValue());
        assertThat(boolQuery.filter(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(CPS_AREA);
        verify(cpsAreaQueryBuilder).getQueryBuilderBy(cpsArea);

        verifyNoMoreInteractions(cpsAreaQueryBuilder);
    }

    @Test
    public void shouldReturnQueryBuilderForExcludeAutomaticallyLinkedCases() {

        final String excludeCaseId = "d3d1f509-ab71-4d8b-b7e2-fae82e5fb690";
        final CpsQueryParameters queryParameters = new CpsQueryParameters.CpsQueryParametersBuilder()
                .withExcludeAutomaticallyLinkedCasesTo(excludeCaseId)
                .build();

        when(elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(LINKED_CASE_ID))
                .thenReturn(excludeAutomaticallyLinkedCasesQueryBuilder);
        when(excludeAutomaticallyLinkedCasesQueryBuilder.getQueryBuilderBy(excludeCaseId))
                .thenReturn(query);

        final Query.Builder resultQueryBuilder = cpsCaseQueryBuilderService.builder(queryParameters);
        final Query resultQuery = resultQueryBuilder.build();
        final BoolQuery boolQuery = resultQuery.bool();

        assertThat(boolQuery, notNullValue());
        assertThat(boolQuery.filter(), hasSize(1));

        verify(elasticSearchQueryBuilderCache).getQueryBuilderFromCacheBy(LINKED_CASE_ID);
        verify(excludeAutomaticallyLinkedCasesQueryBuilder).getQueryBuilderBy(excludeCaseId);

        verifyNoMoreInteractions(excludeAutomaticallyLinkedCasesQueryBuilder);
    }
}
