package uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.ADDRESS;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.APPLICATION_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.ARREST_SUMMONS_NUMBER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.CASE_REFERENCE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.COURT_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.DATE_OF_BIRTH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_DATE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.HEARING_TYPE_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_BOX_HEARING;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.IS_VIRTUAL_BOX_HEARING;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.JURISDICTION_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.NINO;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.OFFENCES_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.POST_CODE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CaseSearchConstants.PROSECUTING_AUTHORITY;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CASE_STATUS;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CASE_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CJS_AREA;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.COURT_HOUSE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.COURT_ROOM;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_AREA;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_UNIT;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CROWN_ADVOCATE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.HEARING_DATE_TIME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.JURISDICTION;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.LINKED_CASE_ID;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OFFENCE_CODE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OIC_SHOULDER_NUMBER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OPERATION_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARALEGAL_OFFICER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_FIRST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_LAST_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PROSECUTOR;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.URN;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.WITNESS_CARE_UNIT;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.ADDRESS_LINE_1;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.COURT_ORDER_VALIDITY_DATE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.DefendantQueryParameterNamesConstants.CROWN_OR_MAGISTRATES;

import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.AddressQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.ApplicationTypeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.ArrestSummonsNumberSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.BoxHearingQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.CourtOrderValidityDateQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.CourtSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.CrownOrMagistratesQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.DateOfBirthSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.DefendantAddressLine1QueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.HasOffenceQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.HearingDateSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.HearingTypeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.JurisdictionTypeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.NameSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.NinoSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.PostCodeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.ProsecutingAuthoritySearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.ReferenceSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.VirtualBoxHearingQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CaseStatusQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CaseTypeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CjsAreaQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CourtHouseQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CourtRoomQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CpsAreaQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CpsUnitQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.CrownAdvocateQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.ExcludeAutomaticallyLinkedCasesQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.HearingDateTimeQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.JurisdictionQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.OffenceCodeSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.OicShoulderNumberQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.OperationNameQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.ParalegalOfficerQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.ProsecutorSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.UrnSearchQueryBuilder;
import uk.gov.moj.cpp.unifiedsearch.query.builders.elasticsearch.builders.cps.WitnessCareUnitQueryBuilder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ElasticSearchQueryBuilderCacheTest {

    @Mock
    private NameSearchQueryBuilder nameSearchQueryBuilder;

    @Mock
    private ReferenceSearchQueryBuilder referenceSearchQueryBuilder;

    @Mock
    private AddressQueryBuilder addressQueryBuilder;

    @Mock
    private DefendantAddressLine1QueryBuilder addressLine1QueryBuilder;

    @Mock
    private PostCodeQueryBuilder postCodeQueryBuilder;

    @Mock
    private HearingDateSearchQueryBuilder hearingDateSearchQueryBuilder;

    @Mock
    private ApplicationTypeQueryBuilder applicationTypeQueryBuilder;

    @Mock
    private HearingTypeQueryBuilder hearingTypeQueryBuilder;

    @Mock
    private DateOfBirthSearchQueryBuilder dateOfBirthSearchQueryBuilder;

    @Mock
    private ProsecutingAuthoritySearchQueryBuilder prosecutingAuthoritySearchQueryBuilder;

    @Mock
    private CourtSearchQueryBuilder courtSearchQueryBuilder;

    @Mock
    private JurisdictionTypeQueryBuilder jurisdictionTypeQueryBuilder;

    @Mock
    private BoxHearingQueryBuilder boxHearingQueryBuilder;

    @Mock
    private NinoSearchQueryBuilder ninoSearchQueryBuilder;

    @Mock
    private ArrestSummonsNumberSearchQueryBuilder arrestSummonsNumberSearchQueryBuilder;

    @Mock
    private CrownOrMagistratesQueryBuilder crownOrMagistratesQueryBuilder;

    @Mock
    private HasOffenceQueryBuilder hasOffenceQueryBuilder;

    @Mock
    private CourtOrderValidityDateQueryBuilder courtOrderValidityDateQueryBuilder;

    @Mock
    private VirtualBoxHearingQueryBuilder virtualBoxHearingQueryBuilder;

    @Mock
    private JurisdictionQueryBuilder jurisdictionQueryBuilder;

    @Mock
    private CourtHouseQueryBuilder courtHouseQueryBuilder;

    @Mock
    private CourtRoomQueryBuilder courtRoomQueryBuilder;

    @Mock
    private ProsecutorSearchQueryBuilder prosecutorSearchQueryBuilder;

    @Mock
    private CaseStatusQueryBuilder caseStatusQueryBuilder;

    @Mock
    private CaseTypeQueryBuilder caseTypeQueryBuilder;

    @Mock
    private ParalegalOfficerQueryBuilder paralegalOfficerQueryBuilder;

    @Mock
    private CrownAdvocateQueryBuilder crownAdvocateQueryBuilder;

    @Mock
    private OperationNameQueryBuilder operationNameQueryBuilder;

    @Mock
    private CpsUnitQueryBuilder cpsUnitQueryBuilder;

    @Mock
    private CjsAreaQueryBuilder cjsAreaQueryBuilder;

    @Mock
    private UrnSearchQueryBuilder urnSearchQueryBuilder;

    @Mock
    private HearingDateTimeQueryBuilder hearingDateTimeQueryBuilder;

    @Mock
    private OicShoulderNumberQueryBuilder oicShoulderNumberQueryBuilder;

    @Mock
    private OffenceCodeSearchQueryBuilder offenceCodeSearchQueryBuilder;

    @Mock
    private WitnessCareUnitQueryBuilder witnessCareUnitQueryBuilder;

    @Mock
    private CpsAreaQueryBuilder cpsAreaQueryBuilder;

    @Mock
    private ExcludeAutomaticallyLinkedCasesQueryBuilder excludeAutomaticallyLinkedCasesQueryBuilder;

    @InjectMocks
    private ElasticSearchQueryBuilderCache elasticSearchQueryBuilderCache;


    @Test
    public void shouldPopulateCache() {
        elasticSearchQueryBuilderCache.populateUnifiedSearchQueryBuilderMap();

        final ElasticSearchQueryBuilder actualSearchNameQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(NAME);
        final ElasticSearchQueryBuilder actualReferenceQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CASE_REFERENCE);
        final ElasticSearchQueryBuilder actualAddressQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(ADDRESS);
        final ElasticSearchQueryBuilder actualPostCodeQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(POST_CODE);
        final ElasticSearchQueryBuilder actualHearingDateSearchQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(HEARING_DATE);
        final ElasticSearchQueryBuilder actualApplicationTypeQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(APPLICATION_TYPE);
        final ElasticSearchQueryBuilder actualHearingTypeQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(HEARING_TYPE_ID);
        final ElasticSearchQueryBuilder actualDateOfBirthSearchQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(DATE_OF_BIRTH);
        final ElasticSearchQueryBuilder actualCourtSearchQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(COURT_ID);
        final ElasticSearchQueryBuilder actualBoxHearingQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(IS_BOX_HEARING);
        final ElasticSearchQueryBuilder actualJurisdictionTypeQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(JURISDICTION_TYPE);
        final ElasticSearchQueryBuilder actualNINOSearchQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(NINO);
        final ElasticSearchQueryBuilder actualArrestSummonsNumberSearchQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(ARREST_SUMMONS_NUMBER);
        final ElasticSearchQueryBuilder addressLine1SearchQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(ADDRESS_LINE_1);
        final ElasticSearchQueryBuilder crownOrMagistratesQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CROWN_OR_MAGISTRATES);
        final ElasticSearchQueryBuilder prosecutingAuthorityBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(PROSECUTING_AUTHORITY);
        final ElasticSearchQueryBuilder actualOffencesExistsQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(OFFENCES_NESTED_PATH);
        final ElasticSearchQueryBuilder actualCourtOrderValidityDateQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(COURT_ORDER_VALIDITY_DATE);
        final ElasticSearchQueryBuilder actualVirtualBoxHearingQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(IS_VIRTUAL_BOX_HEARING);
        final ElasticSearchQueryBuilder actualJurisdictionQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(JURISDICTION);
        final ElasticSearchQueryBuilder actualCourtHouseQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(COURT_HOUSE);
        final ElasticSearchQueryBuilder actualCourtRoomQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(COURT_ROOM);
        final ElasticSearchQueryBuilder actualProsecutorSearchQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(PROSECUTOR);
        final ElasticSearchQueryBuilder actualCaseStatusQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CASE_STATUS);
        final ElasticSearchQueryBuilder actualCaseTypeQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CASE_TYPE);
        final ElasticSearchQueryBuilder actualParalegalOfficerQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(PARALEGAL_OFFICER);
        final ElasticSearchQueryBuilder actualCrownAdvocateQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CROWN_ADVOCATE);
        final ElasticSearchQueryBuilder actualOperationNameQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(OPERATION_NAME);
        final ElasticSearchQueryBuilder actualCpsUnitQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CPS_UNIT);
        final ElasticSearchQueryBuilder actualCjsAreaQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CJS_AREA);
        final ElasticSearchQueryBuilder actualUrnSearchQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(URN);
        final ElasticSearchQueryBuilder actualHearingDateTimeQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(HEARING_DATE_TIME);
        final ElasticSearchQueryBuilder actualGivenNameSearchQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(PARTY_FIRST_NAME);
        final ElasticSearchQueryBuilder actualFamilyNameSearchQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(PARTY_LAST_NAME);
        final ElasticSearchQueryBuilder actualOicShoulderNumberQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(OIC_SHOULDER_NUMBER);
        final ElasticSearchQueryBuilder actualOffenceCodeSearchQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(OFFENCE_CODE);
        final ElasticSearchQueryBuilder actualWitnessCareUnitSearchQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(WITNESS_CARE_UNIT);
        final ElasticSearchQueryBuilder actualCpsAreaCodeSearchQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(CPS_AREA);
        final ElasticSearchQueryBuilder actualExcludeAutomaticallyLinkedCasesQueryBuilder = elasticSearchQueryBuilderCache.getQueryBuilderFromCacheBy(LINKED_CASE_ID);


        assertThat(actualSearchNameQueryBuilder, is(nameSearchQueryBuilder));
        assertThat(actualReferenceQueryBuilder, is(referenceSearchQueryBuilder));
        assertThat(actualAddressQueryBuilder, is(addressQueryBuilder));
        assertThat(actualPostCodeQueryBuilder, is(postCodeQueryBuilder));
        assertThat(actualHearingDateSearchQueryBuilder, is(hearingDateSearchQueryBuilder));
        assertThat(actualApplicationTypeQueryBuilder, is(applicationTypeQueryBuilder));
        assertThat(actualHearingTypeQueryBuilder, is(hearingTypeQueryBuilder));
        assertThat(actualDateOfBirthSearchQueryBuilder, is(dateOfBirthSearchQueryBuilder));
        assertThat(prosecutingAuthorityBuilder, is(prosecutingAuthoritySearchQueryBuilder));
        assertThat(actualCourtSearchQueryBuilder, is(courtSearchQueryBuilder));
        assertThat(actualBoxHearingQueryBuilder, is(boxHearingQueryBuilder));
        assertThat(actualJurisdictionTypeQueryBuilder, is(jurisdictionTypeQueryBuilder));
        assertThat(actualNINOSearchQueryBuilder, is(ninoSearchQueryBuilder));
        assertThat(actualArrestSummonsNumberSearchQueryBuilder, is(arrestSummonsNumberSearchQueryBuilder));
        assertThat(addressLine1SearchQueryBuilder, is(addressLine1QueryBuilder));
        assertThat(crownOrMagistratesQueryBuilder, is(this.crownOrMagistratesQueryBuilder));
        assertThat(actualOffencesExistsQueryBuilder, is(hasOffenceQueryBuilder));
        assertThat(actualCourtOrderValidityDateQueryBuilder, is(courtOrderValidityDateQueryBuilder));
        assertThat(actualVirtualBoxHearingQueryBuilder, is(virtualBoxHearingQueryBuilder));
        assertThat(actualJurisdictionQueryBuilder, is(jurisdictionQueryBuilder));
        assertThat(actualCourtHouseQueryBuilder, is(courtHouseQueryBuilder));
        assertThat(actualCourtRoomQueryBuilder, is(courtRoomQueryBuilder));
        assertThat(actualProsecutorSearchQueryBuilder, is(prosecutorSearchQueryBuilder));
        assertThat(actualCaseStatusQueryBuilder, is(caseStatusQueryBuilder));
        assertThat(actualCaseTypeQueryBuilder, is(caseTypeQueryBuilder));
        assertThat(actualParalegalOfficerQueryBuilder, is(paralegalOfficerQueryBuilder));
        assertThat(actualCrownAdvocateQueryBuilder, is(crownAdvocateQueryBuilder));
        assertThat(actualOperationNameQueryBuilder, is(operationNameQueryBuilder));
        assertThat(actualCpsUnitQueryBuilder, is(cpsUnitQueryBuilder));
        assertThat(actualCjsAreaQueryBuilder, is(cjsAreaQueryBuilder));
        assertThat(actualUrnSearchQueryBuilder, is(urnSearchQueryBuilder));
        assertThat(actualHearingDateTimeQueryBuilder, is(hearingDateTimeQueryBuilder));
        assertThat(actualOicShoulderNumberQueryBuilder, is(oicShoulderNumberQueryBuilder));
        assertThat(actualOffenceCodeSearchQueryBuilder, is(offenceCodeSearchQueryBuilder));
        assertThat(actualWitnessCareUnitSearchQueryBuilder, is(witnessCareUnitQueryBuilder));
        assertThat(actualCpsAreaCodeSearchQueryBuilder, is(cpsAreaQueryBuilder));
        assertThat(actualExcludeAutomaticallyLinkedCasesQueryBuilder, is(excludeAutomaticallyLinkedCasesQueryBuilder));

        verifyNoMoreInteractions(nameSearchQueryBuilder, referenceSearchQueryBuilder, addressQueryBuilder, postCodeQueryBuilder,
                hearingDateSearchQueryBuilder, applicationTypeQueryBuilder, hearingTypeQueryBuilder, dateOfBirthSearchQueryBuilder,
                prosecutingAuthoritySearchQueryBuilder, courtSearchQueryBuilder, boxHearingQueryBuilder, jurisdictionTypeQueryBuilder,
                ninoSearchQueryBuilder, arrestSummonsNumberSearchQueryBuilder, addressLine1QueryBuilder,
                this.crownOrMagistratesQueryBuilder, hasOffenceQueryBuilder, courtOrderValidityDateQueryBuilder, virtualBoxHearingQueryBuilder,
                jurisdictionQueryBuilder, courtHouseQueryBuilder, courtRoomQueryBuilder,
                prosecutorSearchQueryBuilder, caseStatusQueryBuilder, caseTypeQueryBuilder, paralegalOfficerQueryBuilder, crownAdvocateQueryBuilder,
                operationNameQueryBuilder, cpsUnitQueryBuilder, cjsAreaQueryBuilder, urnSearchQueryBuilder, hearingDateTimeQueryBuilder,
                oicShoulderNumberQueryBuilder, offenceCodeSearchQueryBuilder, witnessCareUnitQueryBuilder,
                cpsAreaQueryBuilder, excludeAutomaticallyLinkedCasesQueryBuilder
        );
    }
}
