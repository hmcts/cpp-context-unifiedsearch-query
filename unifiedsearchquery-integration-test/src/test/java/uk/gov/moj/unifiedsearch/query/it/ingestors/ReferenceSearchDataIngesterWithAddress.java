package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static com.google.common.collect.ImmutableList.of;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.DEFENDANT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.ApplicationDocumentMother.defaultApplicationAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.HearingDayDocumentMother.defaultHearingDayDocumentAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.HearingDocumentMother.defaultHearingAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.OffenceDocumentMother.defaultOffenceDocument;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother.defaultPartyAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.RepresentationOrderDocumentMother.defaultRepresentationOrder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.AddressDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.ApplicationDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDayDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.ImmutableList;

public class ReferenceSearchDataIngesterWithAddress {

    private static final String TFL_CASE_REFERENCE = "TFL1234567";
    private static final String TFL_CASE2_REFERENCE = "TFL987654321";
    private static final String TVL_CASE_REFERENCE = "TVL1234567";
    private static final String TVL_CASE_2_REFERENCE = "TVL2345678";
    private static final String TVL_CASE_3_REFERENCE = "TVL3456789";
    private static final String APP_REFERENCE_PREFIX = "APPUSRS000";
    private final ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();
    private static final AtomicInteger applicationReferenceSuffix = new AtomicInteger();

    private List<CaseDocument> indexedCaseList;

    public void loadCases() throws Exception {
        indexedCaseList = caseListForReferenceSearch();
        loadCaseDocuments();
    }

    private void loadCaseDocuments() throws Exception {
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }


    private List<CaseDocument> caseListForReferenceSearch() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(11);
        final PartyDocument.Builder partyDocumentBuilder1 = defaultPartyAsBuilder()
                .withPartyType(DEFENDANT.toString())
                .withMasterPartyId(UUID.randomUUID().toString())
                .withOffences(ImmutableList.of(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder())
                .withDefendantAddress(new AddressDocument("83 Liverpool", "Boulevard Woking Idaho",null, null, null,"W13 5TG"))
                .withPostCode("W13 5TG");
        final PartyDocument.Builder partyDocumentBuilder2 = defaultPartyAsBuilder()
                .withPartyType(DEFENDANT.toString())
                .withMasterPartyId(UUID.randomUUID().toString())
                .withOffences(ImmutableList.of(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder())
                .withDefendantAddress(new AddressDocument("130 Liverpool road", "Brighton Rhode Island",null, null, null,"TG14 4RT"));
        final PartyDocument.Builder partyDocumentBuilder3 = defaultPartyAsBuilder()
                .withPartyType(DEFENDANT.toString())
                .withMasterPartyId(UUID.randomUUID().toString())
                .withOffences(ImmutableList.of(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder())
                .withDefendantAddress(new AddressDocument("74 Liverpool Street", "Portsmouth Connecticut",null, null, null,"TG14 T67"));
        final PartyDocument.Builder partyDocumentBuilder4 = defaultPartyAsBuilder()
                .withPartyType(DEFENDANT.toString())
                .withMasterPartyId(UUID.randomUUID().toString())
                .withOffences(ImmutableList.of(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder())
                .withDefendantAddress(new AddressDocument("70 Irving Avenue", "Hemel Hemstead Liverpool",null, null, null,"TG14 15T"));
        final PartyDocument.Builder partyDocumentBuilder5 = defaultPartyAsBuilder()
                .withPartyType(DEFENDANT.toString())
                .withMasterPartyId(UUID.randomUUID().toString())
                .withOffences(ImmutableList.of(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder())
                .withDefendantAddress(new AddressDocument("130 Liverpool close", "Brighton Rhode Island",null, null, null,"YU45 5TG"));
        final PartyDocument.Builder partyDocumentBuilder6 = defaultPartyAsBuilder()
                .withPartyType(DEFENDANT.toString())
                .withMasterPartyId(UUID.randomUUID().toString())
                .withOffences(ImmutableList.of(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder())
                .withDefendantAddress(new AddressDocument("59 Baughman Plac", "Liverpool New Jersey",null, null, null,"DC4 14T"));
        final PartyDocument.Builder partyDocumentBuilder7 = defaultPartyAsBuilder()
                .withPartyType(DEFENDANT.toString())
                .withMasterPartyId(UUID.randomUUID().toString())
                .withOffences(ImmutableList.of(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder())
                .withDefendantAddress(new AddressDocument("18 Furman Avenue", "Hertford Colorado",null, null, null,"WE5 6YU"));
        final PartyDocument.Builder partyDocumentBuilder8 = defaultPartyAsBuilder()
                .withPartyType(DEFENDANT.toString())
                .withMasterPartyId(UUID.randomUUID().toString())
                .withOffences(ImmutableList.of(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder())
                .withDefendantAddress(new AddressDocument("70 Albany Avenue ", "Woking Utah",null, null, null,"W13 7JU"));
        final PartyDocument.Builder partyDocumentBuilder9 = defaultPartyAsBuilder()
                .withPartyType(DEFENDANT.toString())
                .withMasterPartyId(UUID.randomUUID().toString())
                .withOffences(ImmutableList.of(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder())
                .withDefendantAddress(new AddressDocument("71 Albany Avenue ", "Woking Utahhh",null, null, null,"W16 7JU"));
        final PartyDocument.Builder partyDocumentBuilder10 = defaultPartyAsBuilder()
                .withPartyType(DEFENDANT.toString())
                .withMasterPartyId(UUID.randomUUID().toString())
                .withOffences(ImmutableList.of(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder())
                .withDefendantAddress(new AddressDocument("72 Albany Avenue ", "Woking Utahh",null, null, null,"W18 7JU"));
        final PartyDocument.Builder partyDocumentBuilder11 = defaultPartyAsBuilder()
                .withPartyType(DEFENDANT.toString())
                .withMasterPartyId(UUID.randomUUID().toString())
                .withOffences(ImmutableList.of(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder())
                .withDefendantAddress(new AddressDocument("72 Alba Avenue ", "Woking Uth",null, null, null,"W20 7JU"));

        caseBuilderList.get(0).withCaseReference(TFL_CASE_REFERENCE)
                .withProsecutingAuthority("TFL")
                .withCaseStatus("ACTIVE")
                .withParties(asList(partyDocumentBuilder1)).
                withApplications(singletonList(createApplicationBuilder()));

        final List<ApplicationDocument.Builder> tvlApplicationList = asList(createApplicationBuilder(), createApplicationBuilder());
        caseBuilderList.get(1).withCaseReference(TVL_CASE_REFERENCE)
                .withProsecutingAuthority("TVL")
                .withCaseStatus("ACTIVE")
                .withApplications(tvlApplicationList)
                .withParties(asList(partyDocumentBuilder1));

        //standalone application
        final List<ApplicationDocument.Builder> standaloneApplicationList = asList(createApplicationBuilder(), createApplicationBuilder());
        caseBuilderList.get(2)
                .withCaseReference(null)
                .withCaseStatus("ACTIVE")
                .withProsecutingAuthority(null)
                .with_case_type("application")
                .withApplications(standaloneApplicationList)
                .withParties(asList(partyDocumentBuilder2, partyDocumentBuilder3));

        // TVL case without offences
        caseBuilderList.get(3).withCaseStatus("INACTIVE")
                .withCaseReference(TVL_CASE_2_REFERENCE)
                .withProsecutingAuthority("TVL")
                .withParties(asList(partyDocumentBuilder4));

        // TVL case with empty offences
        caseBuilderList.get(4).withCaseStatus("INACTIVE")
                .withCaseReference(TVL_CASE_3_REFERENCE)
                .withProsecutingAuthority("TVL")
                .withParties(asList(partyDocumentBuilder5));

        // Sort by sittingDay
        caseBuilderList.get(5).withCaseStatus("INACTIVE")
                .withCaseReference(TFL_CASE2_REFERENCE)
                .withProsecutingAuthority("CASE6")
                .withParties(asList(partyDocumentBuilder6))
                .withHearings(of(defaultHearingAsBuilder().withHearingDays(of(defaultHearingDayDocumentAsBuilder().withSittingDay("2021-03-23T10:00:00").build(), createHearingDayDocument())),
                        defaultHearingAsBuilder().withHearingDays(of(createHearingDayDocument(), createHearingDayDocument()))));

        caseBuilderList.get(6).withCaseStatus("INACTIVE").withCaseReference(TFL_CASE2_REFERENCE)
                .withProsecutingAuthority("CASE7")
                .withParties(asList(partyDocumentBuilder7))
                .withHearings(of(defaultHearingAsBuilder().withHearingDays(of(defaultHearingDayDocumentAsBuilder().withSittingDay("2021-03-22T08:00:00").build(), createHearingDayDocument())),
                        defaultHearingAsBuilder().withHearingDays(of(createHearingDayDocument(), createHearingDayDocument()))));

        caseBuilderList.get(7).withCaseStatus("ACTIVE")
                .withCaseReference(TFL_CASE2_REFERENCE)
                .withProsecutingAuthority("CASE8")
                .withParties(asList(partyDocumentBuilder8))
                .withHearings(of(defaultHearingAsBuilder().withHearingDays(of(defaultHearingDayDocumentAsBuilder().withSittingDay("2021-03-22T09:00:00").build(), createHearingDayDocument())),
                        defaultHearingAsBuilder().withHearingDays(of(createHearingDayDocument(), createHearingDayDocument()))));

        caseBuilderList.get(8).withCaseReference(TFL_CASE_REFERENCE)
                .withProsecutingAuthority("TFL")
                .withCaseStatus("NO_PLEA_RECEIVED_READY_FOR_DECISION")
                .withParties(asList(partyDocumentBuilder9)).
                withApplications(singletonList(createApplicationBuilder()));

        caseBuilderList.get(9).withCaseReference("TFL2222222")
                .withProsecutingAuthority("TFL")
                .withParties(asList(partyDocumentBuilder10)).
                withApplications(singletonList(createApplicationBuilder()));

        caseBuilderList.get(10).withCaseReference("TFL9876543")
                .withProsecutingAuthority("TFL")
                .withParties(asList(partyDocumentBuilder11)).
                withApplications(singletonList(createApplicationBuilder()));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    public CaseDocument getIndexDocumentAt(int index) {
        return indexedCaseList.get(index);
    }

    private HearingDayDocument createHearingDayDocument() {
        return defaultHearingDayDocumentAsBuilder().withSittingDay("2021-03-22T10:00:00").build();
    }

    private static ApplicationDocument.Builder createApplicationBuilder() {
        final ApplicationDocument.Builder appBuilder = defaultApplicationAsBuilder();
        final String newApplicationReference = APP_REFERENCE_PREFIX + applicationReferenceSuffix.incrementAndGet();
        appBuilder.withApplicationReference(newApplicationReference);

        return appBuilder;
    }

    private PartyDocument.Builder createPartyBuilder() {
        return defaultPartyAsBuilder()
                .withPartyType(DEFENDANT.toString())
                .withMasterPartyId(UUID.randomUUID().toString())
                .withOffences(ImmutableList.of(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder())
                .withAddressLines("44 Mitchley Avenue Riddlesdown Purley")
                .withPostCode("CR2 1GH");
    }

    private PartyDocument.Builder createPartyBuilderWithoutOffences() {
        return defaultPartyAsBuilder()
                .withOffences(null)
                .withPartyType(DEFENDANT.toString())
                .withRepresentationOrder(defaultRepresentationOrder());
    }

    private PartyDocument.Builder createPartyBuilderWithEmptyOffences() {
        return defaultPartyAsBuilder()
                .withOffences(ImmutableList.of())
                .withPartyType(DEFENDANT.toString())
                .withRepresentationOrder(defaultRepresentationOrder());
    }
}
