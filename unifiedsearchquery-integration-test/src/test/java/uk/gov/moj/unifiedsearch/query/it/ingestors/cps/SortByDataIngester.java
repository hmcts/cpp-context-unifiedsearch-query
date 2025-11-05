package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.justice.services.unifiedsearch.client.constant.CpsPartyType.DEFENDANT;
import static uk.gov.justice.services.unifiedsearch.client.constant.CpsPartyType.SUSPECT;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.HearingDocumentMother;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.OffenceDocumentMother;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.PartyDocumentMother;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class SortByDataIngester {

    private ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();
    private List<CaseDocument> indexedCaseList;

    public void loadCaseDocuments() throws Exception {
        indexedCaseList = createCaseDocuments();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    public List<CaseDocument> createCaseDocuments() {
        final LocalDate dateOfToday = LocalDate.now();

        final List<CaseDocument.Builder> caseBuilderList = CaseDocumentMother.defaultCasesAsBuilderList(11);
        caseBuilderList.get(0)
                .withUrn("URN001")
                .withProsecutor("Ally Henname")
                .withParalegalOfficer("John Evans")
                .withCrownAdvocate("Sam Kenny")
                .withParties(singletonList(PartyDocumentMother.defaultPartyAsBuilder()
                        .withDateOfBirth(null)
                        .withLastName("Janey")
                        .with_party_type(singletonList(DEFENDANT.name()))
                        .withOrganisationName(null)
                        .withOffences(singletonList(OffenceDocumentMother.defaultOffenceDocumentAsBuilder()
                                .withDescription("Offence Description K")
                                .build()))
                        .build()))
                .withHearings(singletonList(HearingDocumentMother.defaultHearingAsBuilder()
                        .withCourtHouse("CH A")
                        .withCourtRoom("CR Z")
                        .withHearingType("Sentence")
                        .withHearingDateTime(dateOfToday.plusDays(3).toString())
                        .build()));

        caseBuilderList.get(1)
                .withUrn("URN002")
                .withProsecutor("Ally Henname")
                .withParalegalOfficer("Jane Smith")
                .withCrownAdvocate("Michael Davidson ca")
                .withParties(singletonList(PartyDocumentMother.defaultPartyAsBuilder()
                        .with_party_type(singletonList(DEFENDANT.name()))
                        .withDateOfBirth(null)
                        .withLastName("Kareem")
                        .withOrganisationName(null)
                        .withOffences(singletonList(OffenceDocumentMother.defaultOffenceDocumentAsBuilder()
                                .withDescription("Offence Description A")
                                .build()))
                        .build()))
                .withHearings(Collections.singletonList(HearingDocumentMother.defaultHearingAsBuilder()
                        .withCourtHouse("CH D")
                        .withCourtRoom("CR K")
                        .withHearingType("Plea")
                        .withHearingDateTime(dateOfToday.plusDays(30).toString())
                        .build()));

        caseBuilderList.get(2)
                .withUrn("URN003")
                .withProsecutor("Berry Jonathan")
                .withParalegalOfficer("Arvind Shelly")
                .withCrownAdvocate("Michael Davidson")
                .withParties(singletonList(PartyDocumentMother.defaultPartyAsBuilder()
                        .with_party_type(singletonList(DEFENDANT.name()))
                        .withDateOfBirth("1988-06-06")
                        .withLastName(null)
                        .withOrganisationName("Johnsonn Ltd")
                        .withOffences(singletonList(OffenceDocumentMother.defaultOffenceDocumentAsBuilder()
                                .withDescription("Offence Description P")
                                .build()))
                        .build()))
                .withHearings(Collections.singletonList(HearingDocumentMother.defaultHearingAsBuilder()
                        .withCourtHouse("CH E")
                        .withCourtRoom("CR K8")
                        .withHearingType("Trial")
                        .withHearingDateTime(dateOfToday.plusMonths(4).toString())
                        .build()));

        caseBuilderList.get(3)
                .withUrn("URN004")
                .withProsecutor("Zeck George")
                .withParalegalOfficer("Emely Keiman")
                .withCrownAdvocate("Reikan Semon")
                .withParties(singletonList(PartyDocumentMother.defaultPartyAsBuilder()
                        .with_party_type(singletonList(DEFENDANT.name()))
                        .withDateOfBirth("1975-06-06")
                        .withLastName("Withy")
                        .withOrganisationName(null)
                        .withOffences(singletonList(OffenceDocumentMother.defaultOffenceDocumentAsBuilder()
                                .withDescription("Offence Description G")
                                .build()))
                        .build()))
                .withHearings(Collections.singletonList(HearingDocumentMother.defaultHearingAsBuilder()
                        .withCourtHouse("CH K")
                        .withCourtRoom("CR K5")
                        .withHearingType("Sentence")
                        .withHearingDateTime(dateOfToday.plusDays(23).toString())
                        .build()));

        caseBuilderList.get(4)
                .withUrn("URN004")
                .withProsecutor("Gail Wecken")
                .withParalegalOfficer("Murthy Denson")
                .withCrownAdvocate("Ben Richmond")
                .withParties(singletonList(PartyDocumentMother.defaultPartyAsBuilder()
                        .with_party_type(singletonList(SUSPECT.name()))
                        .withDateOfBirth("1955-06-06")
                        .withLastName(null)
                        .withOrganisationName("Rogers Ltd")
                        .withOffences(singletonList(OffenceDocumentMother.defaultOffenceDocumentAsBuilder()
                                .withDescription("Offence Description F")
                                .build()))
                        .build()))
                .withHearings(Collections.singletonList(HearingDocumentMother.defaultHearingAsBuilder()
                        .withCourtHouse("CH F")
                        .withCourtRoom("CR Z3")
                        .withHearingType("Trial")
                        .withHearingDateTime(dateOfToday.plusMonths(2).toString())
                        .build()));

        caseBuilderList.get(5)
                .withUrn("URN006")
                .withProsecutor("Simon Dean")
                .withParalegalOfficer("Ceyan Barry")
                .withCrownAdvocate("Sam Kenny")
                .withParties(singletonList(PartyDocumentMother.defaultPartyAsBuilder()
                        .with_party_type(singletonList(DEFENDANT.name()))
                        .withDateOfBirth("1990-06-06")
                        .withLastName("Tobares")
                        .withOrganisationName(null)
                        .withOffences(singletonList(OffenceDocumentMother.defaultOffenceDocumentAsBuilder()
                                .withDescription("Offence Description Z")
                                .build()))
                        .build()))
                .withHearings(Collections.singletonList(HearingDocumentMother.defaultHearingAsBuilder()
                        .withCourtHouse("CH B")
                        .withCourtRoom("CR 1")
                        .withHearingType("Trial")
                        .withHearingDateTime(dateOfToday.plusDays(15).toString())
                        .build()));

        caseBuilderList.get(6)
                .withUrn("URN007")
                .withProsecutor("David Kelly")
                .withParalegalOfficer("Tim Jones")
                .withCrownAdvocate("Murthy Denson")
                .withParties(singletonList(PartyDocumentMother.defaultPartyAsBuilder()
                        .with_party_type(singletonList(SUSPECT.name()))
                        .withLastName("Jolly")
                        .withOrganisationName(null)
                        .withOffences(singletonList(OffenceDocumentMother.defaultOffenceDocumentAsBuilder()
                                .withDescription("Offence Description L")
                                .build()))
                        .build()))
                .withHearings(singletonList(HearingDocumentMother.defaultHearingAsBuilder()
                        .withCourtHouse("CH Z")
                        .withCourtRoom("CR 8")
                        .withHearingType("Plea")
                        .withHearingDateTime(dateOfToday.plusMonths(4).toString())
                        .build()));

        caseBuilderList.get(7)
                .withUrn("URN008")
                .withProsecutor("Kevin Paige")
                .withParalegalOfficer("Hellan Armon")
                .withCrownAdvocate("Berry Jonathan")
                .withParties(singletonList(PartyDocumentMother.defaultPartyAsBuilder()
                        .with_party_type(singletonList(DEFENDANT.name()))
                        .withDateOfBirth("1992-06-06")
                        .withLastName(null)
                        .withOrganisationName("Ingram Ltd")
                        .build()))
                .withHearings(Collections.singletonList(HearingDocumentMother.defaultHearingAsBuilder()
                        .withCourtHouse("CH W")
                        .withCourtRoom("CR 3")
                        .withHearingType("Sentence")
                        .withHearingDateTime(dateOfToday.plusMonths(1).toString())
                        .build()));

        caseBuilderList.get(8)
                .withUrn("URN009")
                .withProsecutor("Paige Denneman")
                .withParalegalOfficer("Reikan Semon")
                .withCrownAdvocate("Jaime Carl")
                .withParties(singletonList(PartyDocumentMother.defaultPartyAsBuilder()
                        .with_party_type(singletonList(SUSPECT.name()))
                        .withDateOfBirth("1980-01-11")
                        .withLastName("Banks")
                        .withOrganisationName(null)
                        .withOffences(singletonList(OffenceDocumentMother.defaultOffenceDocumentAsBuilder()
                                .withDescription("Offence Description AB")
                                .build()))
                        .build()))
                .withHearings(Collections.singletonList(HearingDocumentMother.defaultHearingAsBuilder()
                        .withCourtHouse("CH R")
                        .withCourtRoom("CR T12")
                        .withHearingType("Plea")
                        .withHearingDateTime(dateOfToday.plusMonths(3).plusDays(3).toString())
                        .build()));

        caseBuilderList.get(9)
                .withUrn("URN009")
                .withProsecutor("Michael Davidson")
                .withParalegalOfficer("Ben Richmond")
                .withCrownAdvocate("Berry Jonathan")
                .withParties(singletonList(PartyDocumentMother.defaultPartyAsBuilder()
                        .with_party_type(singletonList(SUSPECT.name()))
                        .withDateOfBirth("1980-05-11")
                        .withLastName("Walsh")
                        .withOrganisationName(null)
                        .withOffences(singletonList(OffenceDocumentMother.defaultOffenceDocumentAsBuilder()
                                .withDescription("Offence Description W")
                                .build()))
                        .build()))
                .withHearings(Collections.singletonList(HearingDocumentMother.defaultHearingAsBuilder()
                        .withCourtHouse("CH T")
                        .withCourtRoom("CR T4")
                        .withHearingType("Plea")
                        .withHearingDateTime(dateOfToday.plusMonths(3).plusDays(1).toString())
                        .build()));

        caseBuilderList.get(10)
                .withUrn("URN009")
                .withProsecutor("Jaime Carl")
                .withParalegalOfficer("Sam Kenny")
                .withCrownAdvocate("Ally Henname")
                .withParties(singletonList(PartyDocumentMother.defaultPartyAsBuilder()
                        .with_party_type(singletonList(DEFENDANT.name()))
                        .withDateOfBirth("1975-12-27")
                        .withOrganisationName("Parker Ltd")
                        .withLastName(null)
                        .withOffences(singletonList(OffenceDocumentMother.defaultOffenceDocumentAsBuilder()
                                .withDescription("Offence Description M")
                                .build()))
                        .build()))
                .withHearings(Collections.singletonList(HearingDocumentMother.defaultHearingAsBuilder()
                        .withCourtHouse("CH N")
                        .withCourtRoom("CR T2")
                        .withHearingType("Trial")
                        .withHearingDateTime(dateOfToday.plusMonths(1).plusDays(1).toString())
                        .build()));

        return caseBuilderList
                .stream()
                .map(CaseDocument.Builder::build)
                .collect(
                        toList());

    }

    public List<CaseDocument> getIndexedCaseList() {
        return indexedCaseList;
    }

    public CaseDocument getIndexDocumentAt(int index) {
        return indexedCaseList.get(index);
    }

}
