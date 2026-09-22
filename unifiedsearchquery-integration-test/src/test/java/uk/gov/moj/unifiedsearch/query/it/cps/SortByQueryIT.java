package uk.gov.moj.unifiedsearch.query.it.cps;

import static java.util.Comparator.reverseOrder;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CASE_STATUS_SORT_BY;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.COURT_HOUSE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.COURT_ROOM;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CPS_CASE_INDEX_NAME;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.CROWN_ADVOCATE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.DATE_OF_BIRTH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.DEFENDANT_LASTNAME_SORT_BY;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.HEARING_DATE_SORT_BY;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.HEARING_TYPE;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.OFFENCE_DESCRIPTION_SORT_BY;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.ORDER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.ORDER_BY;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARALEGAL_OFFICER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_LASTNAME_OR_ORGANISATIONNAME_SORT_BY;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PROSECUTOR;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.URN;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.initializeStubbing;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.CaseSearchResponse;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Hearing;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Offence;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps.Party;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexRemoverUtil;
import uk.gov.moj.unifiedsearch.query.it.ingestors.cps.SortByDataIngester;
import uk.gov.moj.unifiedsearch.query.it.util.CpsSearchApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SortByQueryIT {

    private CpsSearchApiClient searchApiClient = CpsSearchApiClient.getInstance();
    private static ElasticSearchIndexRemoverUtil elasticSearchIndexRemoverUtil = new ElasticSearchIndexRemoverUtil();
    private static SortByDataIngester sortByDataIngester = new SortByDataIngester();

    @BeforeEach
    public void setupStubs() throws Exception {
        initializeStubbing();
        elasticSearchIndexRemoverUtil.deleteAndCreateCaseIndex(CPS_CASE_INDEX_NAME);
        sortByDataIngester.loadCaseDocuments();
    }

    @Test
    public void shouldReturnCasesAsSortedDefaultAscendingByDateOfBirth() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, DATE_OF_BIRTH);

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Party> parties = flatMapPartiesFromCases(caseSearchResponse);

        final List<String> dateOfBirths = parties.stream()
                .filter(party -> nonNull(party.getDateOfBirth()))
                .map(Party::getDateOfBirth).collect(Collectors.toList());
        final ArrayList<String> expectedDateOfBirths = new ArrayList<>(dateOfBirths);
        final String[] expectedDateOfBirthsArr = expectedDateOfBirths.stream().sorted().collect(Collectors.toList()).toArray(new String[expectedDateOfBirths.size()]);

        assertThat(dateOfBirths, contains(expectedDateOfBirthsArr));
    }

    @Test
    public void shouldReturnCasesAsSortedAscendingByDateOfBirth() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, DATE_OF_BIRTH);
        parameters.putIfAbsent(ORDER, "ASC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Party> parties = flatMapPartiesFromCases(caseSearchResponse);

        final List<String> dateOfBirths = parties.stream()
                .filter(party -> nonNull(party.getDateOfBirth()))
                .map(Party::getDateOfBirth).collect(Collectors.toList());
        final ArrayList<String> expectedDateOfBirths = new ArrayList<>(dateOfBirths);
        final String[] expectedDateOfBirthsArr = expectedDateOfBirths.stream().sorted().collect(Collectors.toList()).toArray(new String[expectedDateOfBirths.size()]);

        assertThat(dateOfBirths, contains(expectedDateOfBirthsArr));
    }

    @Test
    public void shouldReturnCasesAsSortedDescendingByDateOfBirth() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, DATE_OF_BIRTH);
        parameters.putIfAbsent(ORDER, "DESC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Party> parties = flatMapPartiesFromCases(caseSearchResponse);

        final List<String> dateOfBirths = parties.stream()
                .filter(party -> nonNull(party.getDateOfBirth()))
                .map(Party::getDateOfBirth).collect(Collectors.toList());
        final ArrayList<String> expectedDateOfBirths = new ArrayList<>(dateOfBirths);
        final String[] expectedDateOfBirthsArr = expectedDateOfBirths.stream().sorted(reverseOrder()).collect(Collectors.toList()).toArray(new String[expectedDateOfBirths.size()]);

        assertThat(dateOfBirths, contains(expectedDateOfBirthsArr));
    }

    @Test
    public void shouldReturnCasesAsSortedDescendingByHearingDateTime() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, HEARING_DATE_SORT_BY);
        parameters.putIfAbsent(ORDER, "DESC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Hearing> hearings = flatMapHearingsFromCases(caseSearchResponse);
        final List<String> hearingDateTimes = hearings.stream().map(Hearing::getHearingDateTime).collect(Collectors.toList());
        final ArrayList<String> expectedHearingDateTimes = new ArrayList<>(hearingDateTimes);
        final String[] expectedHearingDateTimesArr = expectedHearingDateTimes.stream().sorted(reverseOrder()).collect(Collectors.toList()).toArray(new String[expectedHearingDateTimes.size()]);

        assertThat(hearingDateTimes, contains(expectedHearingDateTimesArr));
    }

    @Test
    public void shouldReturnCasesAsSortedAscendingByHearingDateTime() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, HEARING_DATE_SORT_BY);
        parameters.putIfAbsent(ORDER, "ASC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Hearing> hearings = flatMapHearingsFromCases(caseSearchResponse);
        final List<String> hearingDateTimes = hearings.stream().map(Hearing::getHearingDateTime).collect(Collectors.toList());
        final ArrayList<String> expectedHearingDateTimes = new ArrayList<>(hearingDateTimes);
        final String[] expectedHearingDateTimesArr = expectedHearingDateTimes.stream().sorted().collect(Collectors.toList()).toArray(new String[expectedHearingDateTimes.size()]);

        assertThat(hearingDateTimes, contains(expectedHearingDateTimesArr));
    }

    @Test
    public void shouldReturnCasesAsSortedDescendingByHearingType() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, HEARING_TYPE);
        parameters.putIfAbsent(ORDER, "DESC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Hearing> hearings = flatMapHearingsFromCases(caseSearchResponse);
        final List<String> hearingTypes = hearings.stream().map(Hearing::getHearingType).collect(Collectors.toList());
        final ArrayList<String> expectedHearingTypes = new ArrayList<>(hearingTypes);
        final String[] expectedHearingTypesArr = expectedHearingTypes.stream().sorted(reverseOrder()).collect(Collectors.toList()).toArray(new String[expectedHearingTypes.size()]);

        assertThat(hearingTypes, contains(expectedHearingTypesArr));
    }

    @Test
    public void shouldReturnCasesAsSortedAscendingByHearingType() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, HEARING_TYPE);
        parameters.putIfAbsent(ORDER, "ASC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Hearing> hearings = flatMapHearingsFromCases(caseSearchResponse);
        final List<String> hearingTypes = hearings.stream().map(Hearing::getHearingType).collect(Collectors.toList());
        final ArrayList<String> expectedHearingTypes = new ArrayList<>(hearingTypes);
        final String[] expectedHearingTypesArr = expectedHearingTypes.stream().sorted().collect(Collectors.toList()).toArray(new String[expectedHearingTypes.size()]);

        assertThat(hearingTypes, contains(expectedHearingTypesArr));
    }

    @Test
    public void shouldReturnCasesAsSortedAscendingByCourtRoom() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, COURT_ROOM);
        parameters.putIfAbsent(ORDER, "ASC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Hearing> hearings = flatMapHearingsFromCases(caseSearchResponse);
        final List<String> courtRooms = hearings.stream().map(Hearing::getCourtRoom).collect(Collectors.toList());
        final ArrayList<String> expectedCourtRooms = new ArrayList<>(courtRooms);
        final String[] expectedCourtRoomsArr = expectedCourtRooms.stream().sorted().collect(Collectors.toList()).toArray(new String[expectedCourtRooms.size()]);

        assertThat(courtRooms, contains(expectedCourtRoomsArr));
    }

    @Test
    public void shouldReturnCasesAsSortedDescendingByCourtRoom() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, COURT_ROOM);
        parameters.putIfAbsent(ORDER, "DESC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Hearing> hearings = flatMapHearingsFromCases(caseSearchResponse);
        final List<String> courtRooms = hearings.stream().map(Hearing::getCourtRoom).collect(Collectors.toList());
        final ArrayList<String> expectedCourtRooms = new ArrayList<>(courtRooms);
        final String[] expectedCourtRoomsArr = expectedCourtRooms.stream().sorted(reverseOrder()).collect(Collectors.toList()).toArray(new String[expectedCourtRooms.size()]);

        assertThat(courtRooms, contains(expectedCourtRoomsArr));
    }

    @Test
    public void shouldReturnCasesAsSortedAscendingByCourtHouse() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, COURT_HOUSE);
        parameters.putIfAbsent(ORDER, "ASC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Hearing> hearings = flatMapHearingsFromCases(caseSearchResponse);
        final List<String> courtHouses = hearings.stream().map(Hearing::getCourtHouse).collect(Collectors.toList());
        final ArrayList<String> expectedCourtHouses = new ArrayList<>(courtHouses);
        final String[] expectedCourtHousesArr = expectedCourtHouses.stream().sorted().collect(Collectors.toList()).toArray(new String[expectedCourtHouses.size()]);

        assertThat(courtHouses, contains(expectedCourtHousesArr));
    }

    @Test
    public void shouldReturnCasesAsSortedDescendingByCourtHouse() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, COURT_HOUSE);
        parameters.putIfAbsent(ORDER, "DESC");
        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Hearing> hearings = flatMapHearingsFromCases(caseSearchResponse);
        final List<String> courtHouses = hearings.stream().map(Hearing::getCourtHouse).collect(Collectors.toList());
        final ArrayList<String> expectedCourtHouses = new ArrayList<>(courtHouses);
        final String[] expectedCourtHousesArr = expectedCourtHouses.stream().sorted(reverseOrder()).collect(Collectors.toList()).toArray(new String[expectedCourtHouses.size()]);

        assertThat(courtHouses, contains(expectedCourtHousesArr));
    }

    @Test
    public void shouldReturnCasesAsSortedAscendingByProsecutor() throws Exception {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, PROSECUTOR);
        parameters.putIfAbsent(ORDER, "ASC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Case> caseListResults = caseSearchResponse.getCases();
        final List<String> prosecutors = caseListResults.stream().map(Case::getProsecutor).collect(Collectors.toList());
        final ArrayList<String> expectedProsecutors = new ArrayList<>(prosecutors);
        final String[] expectedProsecutorsArr = expectedProsecutors.stream().sorted().collect(Collectors.toList()).toArray(new String[expectedProsecutors.size()]);

        assertThat(prosecutors, contains(expectedProsecutorsArr));
    }

    @Test
    public void shouldReturnCasesAsSortedDescendingByProsecutor() throws Exception {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, PROSECUTOR);
        parameters.putIfAbsent(ORDER, "DESC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Case> caseListResults = caseSearchResponse.getCases();
        final List<String> prosecutors = caseListResults.stream().map(Case::getProsecutor).collect(Collectors.toList());
        final ArrayList<String> expectedProsecutors = new ArrayList<>(prosecutors);
        final String[] expectedProsecutorsArr = expectedProsecutors.stream().sorted(reverseOrder()).collect(Collectors.toList()).toArray(new String[expectedProsecutors.size()]);

        assertThat(prosecutors, contains(expectedProsecutorsArr));
    }

    @Test
    public void shouldReturnCasesAsSortedAscendingByParalegalOfficer() throws Exception {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, PARALEGAL_OFFICER);
        parameters.putIfAbsent(ORDER, "ASC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Case> caseListResults = caseSearchResponse.getCases();
        final List<String> paralegalOfficers = caseListResults.stream().map(Case::getParalegalOfficer).collect(Collectors.toList());
        final ArrayList<String> expectedParalegalOfficers = new ArrayList<>(paralegalOfficers);
        final String[] expectedParalegalOfficersArr = expectedParalegalOfficers.stream().sorted().collect(Collectors.toList()).toArray(new String[expectedParalegalOfficers.size()]);

        assertThat(paralegalOfficers, contains(expectedParalegalOfficersArr));
    }

    @Test
    public void shouldReturnCasesAsSortedDescendingByParalegalOfficer() throws Exception {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, PARALEGAL_OFFICER);
        parameters.putIfAbsent(ORDER, "DESC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Case> caseListResults = caseSearchResponse.getCases();
        final List<String> paralegalOfficers = caseListResults.stream().map(Case::getParalegalOfficer).collect(Collectors.toList());
        final ArrayList<String> expectedParalegalOfficers = new ArrayList<>(paralegalOfficers);
        final String[] expectedParalegalOfficersArr = expectedParalegalOfficers.stream().sorted(reverseOrder()).collect(Collectors.toList()).toArray(new String[expectedParalegalOfficers.size()]);

        assertThat(paralegalOfficers, contains(expectedParalegalOfficersArr));
    }

    @Test
    public void shouldReturnCasesAsSortedAscendingByCrownAdvocate() throws Exception {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, CROWN_ADVOCATE);
        parameters.putIfAbsent(ORDER, "ASC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Case> caseListResults = caseSearchResponse.getCases();
        final List<String> crownAdvocates = caseListResults.stream().map(Case::getCrownAdvocate).collect(Collectors.toList());
        final ArrayList<String> expectedCrownAdvocates = new ArrayList<>(crownAdvocates);
        final String[] expectedCrownAdvocatesArr = expectedCrownAdvocates.stream().sorted().collect(Collectors.toList()).toArray(new String[expectedCrownAdvocates.size()]);

        assertThat(crownAdvocates, contains(expectedCrownAdvocatesArr));
    }

    @Test
    public void shouldReturnCasesAsSortedDescendingByCrownAdvocate() throws Exception {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, CROWN_ADVOCATE);
        parameters.putIfAbsent(ORDER, "DESC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Case> caseListResults = caseSearchResponse.getCases();
        final List<String> crownAdvocates = caseListResults.stream().map(Case::getCrownAdvocate).collect(Collectors.toList());
        final ArrayList<String> expectedCrownAdvocates = new ArrayList<>(crownAdvocates);
        final String[] expectedCrownAdvocatesArr = expectedCrownAdvocates.stream().sorted(reverseOrder()).collect(Collectors.toList()).toArray(new String[expectedCrownAdvocates.size()]);

        assertThat(crownAdvocates, contains(expectedCrownAdvocatesArr));
    }

    @Test
    public void shouldReturnCasesAsSortedDescendingByUrn() throws Exception {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, URN);
        parameters.putIfAbsent(ORDER, "DESC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Case> caseListResults = caseSearchResponse.getCases();
        final List<String> urns = caseListResults.stream().map(Case::getUrn).collect(Collectors.toList());
        final ArrayList<String> expectedUrns = new ArrayList<>(urns);
        final String[] expectedUrnsArr = expectedUrns.stream().sorted(reverseOrder()).collect(Collectors.toList()).toArray(new String[expectedUrns.size()]);

        assertThat(urns, contains(expectedUrnsArr));
    }

    @Test
    public void shouldReturnCasesAsSortedAscendingByUrn() throws Exception {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, URN);
        parameters.putIfAbsent(ORDER, "ASC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Case> caseListResults = caseSearchResponse.getCases();
        final List<String> urns = caseListResults.stream().map(Case::getUrn).collect(Collectors.toList());
        final ArrayList<String> expectedUrns = new ArrayList<>(urns);
        final String[] expectedUrnsArr = expectedUrns.stream().sorted().collect(Collectors.toList()).toArray(new String[expectedUrns.size()]);

        assertThat(urns, contains(expectedUrnsArr));
    }

    @Test
    public void shouldReturnCasesAsSortedAscendingByCaseStatus() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, CASE_STATUS_SORT_BY);
        parameters.putIfAbsent(ORDER, "ASC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Case> caseListResults = caseSearchResponse.getCases();
        final List<String> caseStatusCodes = caseListResults.stream().map(Case::getCaseStatusCode).collect(Collectors.toList());
        final ArrayList<String> expectedCaseStatusCodes = new ArrayList<>(caseStatusCodes);
        final String[] expectedCaseStatusCodesArr = expectedCaseStatusCodes.stream().sorted().collect(Collectors.toList()).toArray(new String[expectedCaseStatusCodes.size()]);

        assertThat(caseStatusCodes, contains(expectedCaseStatusCodesArr));
    }

    @Test
    public void shouldReturnCasesAsSortedDescendingByCaseStatus() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, CASE_STATUS_SORT_BY);
        parameters.putIfAbsent(ORDER, "DESC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Case> caseListResults = caseSearchResponse.getCases();
        final List<String> caseStatusCodes = caseListResults.stream().map(Case::getCaseStatusCode).collect(Collectors.toList());
        final ArrayList<String> expectedCaseStatusCodes = new ArrayList<>(caseStatusCodes);
        final String[] expectedCaseStatusCodesArr = expectedCaseStatusCodes.stream()
                .sorted(reverseOrder()).collect(Collectors.toList()).toArray(new String[expectedCaseStatusCodes.size()]);

        assertThat(caseStatusCodes, contains(expectedCaseStatusCodesArr));
    }

    @Test
    public void shouldReturnCasesAsSortedAscendingByPartyLastNameOrOrganisationName() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, PARTY_LASTNAME_OR_ORGANISATIONNAME_SORT_BY);
        parameters.putIfAbsent(ORDER, "ASC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Party> parties = flatMapPartiesFromCases(caseSearchResponse);

        final List<String> lastNameOrOrganisationNameList = new ArrayList<>();

        parties.stream().forEach(party -> {
                    if (StringUtils.isNotEmpty(party.getLastName())){
                        lastNameOrOrganisationNameList.add(party.getLastName());
                    } else {
                        lastNameOrOrganisationNameList.add(party.getOrganisationName());
                    }
                });

        final ArrayList<String> expectedLastNameOrOrganisationNames = new ArrayList<>(lastNameOrOrganisationNameList);
        final String[] expectedLastNameOrOrganisationNamesArr = expectedLastNameOrOrganisationNames.stream()
                .sorted().collect(Collectors.toList()).toArray(new String[expectedLastNameOrOrganisationNames.size()]);

        assertThat(lastNameOrOrganisationNameList, contains(expectedLastNameOrOrganisationNamesArr));
    }

    @Test
    public void shouldReturnCasesAsSortedDescendingByPartyLastNameOrOrganisationName() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, PARTY_LASTNAME_OR_ORGANISATIONNAME_SORT_BY);
        parameters.putIfAbsent(ORDER, "DESC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Party> parties = flatMapPartiesFromCases(caseSearchResponse);

        final List<String> lastNameOrOrganisationNameList = new ArrayList<>();

        parties.stream().forEach(party -> {
            if (StringUtils.isNotEmpty(party.getLastName())){
                lastNameOrOrganisationNameList.add(party.getLastName());
            } else {
                lastNameOrOrganisationNameList.add(party.getOrganisationName());
            }
        });

        final ArrayList<String> expectedLastNameOrOrganisationNames = new ArrayList<>(lastNameOrOrganisationNameList);
        final String[] expectedLastNameOrOrganisationNamesArr = expectedLastNameOrOrganisationNames.stream().sorted(reverseOrder())
                .collect(Collectors.toList()).toArray(new String[expectedLastNameOrOrganisationNames.size()]);

        assertThat(lastNameOrOrganisationNameList, contains(expectedLastNameOrOrganisationNamesArr));
    }

    @Test
    public void shouldReturnCasesAsSortedAscendingByDefendantLastName() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, DEFENDANT_LASTNAME_SORT_BY);
        parameters.putIfAbsent(ORDER, "ASC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Party> parties = flatMapPartiesFromCases(caseSearchResponse);

        final List<String> defendantLastNames = parties.stream()
                .filter(party -> StringUtils.isNotEmpty(party.getLastName()))
                .map(Party::getLastName).collect(Collectors.toList());
        final ArrayList<String> expectedDefendantLastNames = new ArrayList<>(defendantLastNames);
        final String[] expectedDefendantLastNamesArr = expectedDefendantLastNames.stream().sorted().collect(Collectors.toList()).toArray(new String[expectedDefendantLastNames.size()]);

        assertThat(defendantLastNames, contains(expectedDefendantLastNamesArr));
    }

    @Test
    public void shouldReturnCasesAsSortedDescendingByDefendantLastName() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, DEFENDANT_LASTNAME_SORT_BY);
        parameters.putIfAbsent(ORDER, "DESC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Party> parties = flatMapPartiesFromCases(caseSearchResponse);

        final List<String> defendantLastNames = parties.stream()
                .filter(party -> StringUtils.isNotEmpty(party.getLastName()))
                .map(Party::getLastName).collect(Collectors.toList());
        final ArrayList<String> expectedDefendantLastNames = new ArrayList<>(defendantLastNames);
        final String[] expectedDefendantLastNamesArr = expectedDefendantLastNames.stream().sorted(reverseOrder()).collect(Collectors.toList()).toArray(new String[expectedDefendantLastNames.size()]);

        assertThat(defendantLastNames, contains(expectedDefendantLastNamesArr));
    }

    @Test
    public void shouldReturnCasesAsSortedAscendingByOffenceDescription() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, OFFENCE_DESCRIPTION_SORT_BY);
        parameters.putIfAbsent(ORDER, "ASC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Offence> offences = flatMapOffencesFromCases(caseSearchResponse);

        final List<String> offenceDescriptions = offences.stream()
                .map(Offence::getDescription).collect(Collectors.toList());
        final ArrayList<String> expectedOffenceDescriptions = new ArrayList<>(offenceDescriptions);
        final String[] expectedOffenceDescriptionArr = expectedOffenceDescriptions.stream().sorted().collect(Collectors.toList()).toArray(new String[expectedOffenceDescriptions.size()]);

        assertThat(offenceDescriptions, contains(expectedOffenceDescriptionArr));
    }

    @Test
    public void shouldReturnCasesAsSortedDescendingByOffenceDescription() throws IOException {
        final Map<String, String> parameters = new HashMap();
        parameters.putIfAbsent(ORDER_BY, OFFENCE_DESCRIPTION_SORT_BY);
        parameters.putIfAbsent(ORDER, "DESC");

        final CaseSearchResponse caseSearchResponse = searchApiClient.searchCases(parameters);

        final List<Offence> offences = flatMapOffencesFromCases(caseSearchResponse);

        final List<String> offenceDescriptions = offences.stream()
                .map(Offence::getDescription).collect(Collectors.toList());
        final ArrayList<String> expectedOffenceDescriptions = new ArrayList<>(offenceDescriptions);
        final String[] expectedOffenceDescriptionArr = expectedOffenceDescriptions.stream().sorted(reverseOrder()).collect(Collectors.toList()).toArray(new String[expectedOffenceDescriptions.size()]);

        assertThat(offenceDescriptions, contains(expectedOffenceDescriptionArr));
    }

    private List<Offence> flatMapOffencesFromCases(final CaseSearchResponse caseSearchResponse) {
        return caseSearchResponse.getCases().stream()
                .flatMap(cases -> cases.getParties().stream())
                .filter(party -> isNotEmpty(party.getOffences()))
                .flatMap(party -> party.getOffences().stream())
                .collect(Collectors.toList());
    }

    private List<Party> flatMapPartiesFromCases(final CaseSearchResponse caseSearchResponse) {
        return caseSearchResponse.getCases().stream()
                .filter(cases -> isNotEmpty(cases.getParties()))
                .flatMap(cases -> cases.getParties().stream())
                .collect(Collectors.toList());
    }

    private List<Hearing> flatMapHearingsFromCases(final CaseSearchResponse caseSearchResponse) {
        return caseSearchResponse.getCases()
                .stream()
                .flatMap(cases -> cases.getHearings().stream())
                .collect(Collectors.toList());
    }
}
