package uk.gov.moj.unifiedsearch.query.it;

import static java.util.Arrays.asList;
import static java.util.UUID.fromString;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.CaseDocumentIngester.loadCaseDocuments;
import static uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient.getInstance;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.unifiedsearch.query.it.multi.AddressFacetTestParameters;
import uk.gov.moj.unifiedsearch.query.it.multi.ApplicationTypeFacetTestParameters;
import uk.gov.moj.unifiedsearch.query.it.multi.CourtFacetTestParameters;
import uk.gov.moj.unifiedsearch.query.it.multi.DateOfBirthFacetTestParameters;
import uk.gov.moj.unifiedsearch.query.it.multi.FacetTestParameters;
import uk.gov.moj.unifiedsearch.query.it.multi.HearingDatesFacetTestParameters;
import uk.gov.moj.unifiedsearch.query.it.multi.HearingTypeFacetTestParameters;
import uk.gov.moj.unifiedsearch.query.it.multi.IsBoxHearingFacetTestParameters;
import uk.gov.moj.unifiedsearch.query.it.multi.IsVirtualBoxHearingFacetTestParameters;
import uk.gov.moj.unifiedsearch.query.it.multi.NameFacetTestParameters;
import uk.gov.moj.unifiedsearch.query.it.multi.PartyTypeFacetTestParameters;
import uk.gov.moj.unifiedsearch.query.it.multi.PostcodeFacetTestParameters;
import uk.gov.moj.unifiedsearch.query.it.multi.ReferenceFacetTestParameters;
import uk.gov.moj.unifiedsearch.query.it.util.SearchApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MultiQueryIT {

    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();

    private SearchApiClient searchApiClient = getInstance();

    @BeforeAll
    public static void setupStubs() throws Exception {
        initializeStubbing();
    }

    @BeforeEach
    public void setup() throws Exception {
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex();
    }

    @Test
    public void shouldQueryByNameAndPostCode() throws Exception {

        multiQueryTest(
                asList(
                        new NameFacetTestParameters(),
                        new PostcodeFacetTestParameters())
        );

    }

    @Test
    public void shouldQueryByNameAndApplicationType() throws Exception {

        multiQueryTest(
                asList(
                        new NameFacetTestParameters(),
                        new ApplicationTypeFacetTestParameters())
        );

    }

    @Test
    public void shouldQueryByNameAndApplicationTypeAndPostcode() throws Exception {

        multiQueryTest(
                asList(
                        new NameFacetTestParameters(),
                        new ApplicationTypeFacetTestParameters(),
                        new PostcodeFacetTestParameters())
        );

    }

    @Test
    public void shouldQueryByHearingDatesAndApplicationTypeAndPostcode() throws Exception {

        multiQueryTest(
                asList(
                        new HearingDatesFacetTestParameters(),
                        new ApplicationTypeFacetTestParameters(),
                        new PostcodeFacetTestParameters(),
                        new NameFacetTestParameters())
        );

    }


    @Test
    public void shouldQueryByNameAndHearingType() throws Exception {

        multiQueryTest(
                asList(
                        new NameFacetTestParameters(),
                        new HearingTypeFacetTestParameters())
        );

    }

    @Test
    public void shouldQueryByHearingTypeAndDOB() throws Exception {

        multiQueryTest(
                asList(
                        new HearingTypeFacetTestParameters(),
                        new DateOfBirthFacetTestParameters())
        );

    }


    @Test
    public void shouldQueryByNameAndHearingTypeAndDOB() throws Exception {

        multiQueryTest(
                asList(
                        new NameFacetTestParameters(),
                        new HearingTypeFacetTestParameters(),
                        new DateOfBirthFacetTestParameters())
        );

    }


    @Test
    public void shouldQueryByNameAndCaseReference() throws Exception {

        multiQueryTest(
                asList(
                        new NameFacetTestParameters(),
                        new ReferenceFacetTestParameters())
        );

    }

    @Test
    public void shouldQueryByHearingTypeAndDOBAndCaseReference() throws Exception {

        multiQueryTest(
                asList(
                        new HearingTypeFacetTestParameters(),
                        new DateOfBirthFacetTestParameters(),
                        new ReferenceFacetTestParameters())
        );

    }


    @Test
    public void shouldQueryByNameAndHearingTypeAndDOBAndCaseReference() throws Exception {

        multiQueryTest(
                asList(
                        new NameFacetTestParameters(),
                        new HearingTypeFacetTestParameters(),
                        new DateOfBirthFacetTestParameters(),
                        new ReferenceFacetTestParameters())
        );

    }

    @Test
    public void shouldQueryByPostcodeAndHearingTypeAndDOBAndCaseReference() throws Exception {

        multiQueryTest(
                asList(
                        new PostcodeFacetTestParameters(),
                        new HearingTypeFacetTestParameters(),
                        new DateOfBirthFacetTestParameters(),
                        new ReferenceFacetTestParameters())
        );

    }


    @Test
    public void shouldQueryByNameAndPartyType() throws Exception {

        multiQueryTest(
                asList(
                        new NameFacetTestParameters(),
                        new PartyTypeFacetTestParameters())
        );

    }


    @Test
    public void shouldQueryByHearingDates() throws Exception {

        multiQueryTest(
                asList(
                        new HearingDatesFacetTestParameters())
        );

    }


    @Test
    public void shouldQueryByName() throws Exception {

        multiQueryTest(
                asList(
                        new NameFacetTestParameters())
        );

    }

    @Test
    public void shouldQueryByNameAndAddress() throws Exception {

        multiQueryTest(
                asList(
                        new AddressFacetTestParameters(),
                        new NameFacetTestParameters())
        );

    }

    @Test
    public void shouldQueryByAddressAndDOB() throws Exception {

        multiQueryTest(
                asList(
                        new AddressFacetTestParameters(),
                        new DateOfBirthFacetTestParameters())
        );

    }

    @Test
    public void shouldQueryByCourtIdAndHearingDates() throws Exception {
        /*
        Here, the search is happening all in the hearingDays ....
        so we need to have both courtCentreId and sittingDay populated
         */
        final CourtFacetTestParameters courtFacetParam = new CourtFacetTestParameters();
        multiQueryTest(
                asList(
                        courtFacetParam,
                        new HearingDatesFacetTestParameters(fromString(courtFacetParam.getCourtIdToQuery())))
        );

    }

    @Test
    public void shouldQueryByNameAndIsBoxHearingAndHearingDates() throws Exception {

        multiQueryTest(
                asList(
                        new NameFacetTestParameters(),
                        new IsBoxHearingFacetTestParameters(),
                        new HearingDatesFacetTestParameters())
        );

    }

    @Test
    public void shouldQueryByNameAndIsVirtualBoxHearingAndHearingDates() throws Exception {

        multiQueryTest(
                asList(
                        new NameFacetTestParameters(),
                        new IsVirtualBoxHearingFacetTestParameters(),
                        new HearingDatesFacetTestParameters())
        );

    }

    private void multiQueryTest(final List<FacetTestParameters> facetTestParametersList) throws Exception {

        final List<CaseDocument.Builder> caseBuilderList = new ArrayList<>();
        final Map<String, String> parameterMap = new HashMap<>();

        facetTestParametersList
                .forEach(provider -> {
                    provider.ensureCapacity(caseBuilderList);
                    provider.addMatchData(caseBuilderList);
                    provider.addSearchParameters(parameterMap);
                });


        final List<CaseDocument> caseDocumentList = caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(
                        toList());

        loadCaseDocuments(caseDocumentList);

        final List<Case> results = submitSearch(parameterMap);

        assertThat(

                facetTestParametersList.stream()
                        .filter(provider -> provider.hasExpectedHits(results) == false)
                        .map(FacetTestParameters::getFailureMessage)
                        .collect(toList()),

                is(empty())
        );


    }


    private List<Case> submitSearch(final Map<String, String> parameterMap) throws Exception {

        parameterMap.put("pageSize", "30");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameterMap);

        return caseSearchResponse.getCases();

    }
}
