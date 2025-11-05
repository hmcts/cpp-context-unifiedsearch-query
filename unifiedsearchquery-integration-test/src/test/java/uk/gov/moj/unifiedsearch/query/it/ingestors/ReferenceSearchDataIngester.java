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
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.ApplicationDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDayDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.ImmutableList;

public class ReferenceSearchDataIngester {

    public static final String TFL_CASE_REFERENCE = "TFL1234567";
    public static final String TFL_CASE2_REFERENCE = "TFL987654321";
    public static final String TVL_CASE_REFERENCE = "TVL1234567";
    public static final String TVL_CASE_2_REFERENCE = "TVL2345678";
    public static final String TVL_CASE_3_REFERENCE = "TVL3456789";
    public static final String APP_REFERENCE_PREFIX = "APPUSRS000";
    public static final String TFL_CASE8_REFERENCE = "TFL987654322";
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
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(9);

        caseBuilderList.get(0).withCaseReference(TFL_CASE_REFERENCE).withProsecutingAuthority("TFL").
                withParties(ImmutableList.of(createPartyBuilder())).
                withApplications(singletonList(createApplicationBuilder()));

        final List<ApplicationDocument.Builder> tvlApplicationList = asList(createApplicationBuilder(), createApplicationBuilder());
        caseBuilderList.get(1).withCaseReference(TVL_CASE_REFERENCE).withProsecutingAuthority("TVL")
                .withApplications(tvlApplicationList).withParties(ImmutableList.of(createPartyBuilder()));

        //standalone application
        final List<ApplicationDocument.Builder> standaloneApplicationList = asList(createApplicationBuilder(), createApplicationBuilder());
        caseBuilderList.get(2)
                .withCaseReference(null)
                .withProsecutingAuthority(null)
                .with_case_type("application")
                .withApplications(standaloneApplicationList)
                .withParties(ImmutableList.of(createPartyBuilder()));

        // TVL case without offences
        caseBuilderList.get(3).withCaseReference(TVL_CASE_2_REFERENCE).withProsecutingAuthority("TVL").withParties(ImmutableList.of(createPartyBuilderWithoutOffences()));

        // TVL case with empty offences
        caseBuilderList.get(4).withCaseReference(TVL_CASE_3_REFERENCE).withProsecutingAuthority("TVL").withParties(ImmutableList.of(createPartyBuilderWithEmptyOffences()));

        // Sort by sittingDay
        caseBuilderList.get(5).withCaseReference(TFL_CASE2_REFERENCE).withProsecutingAuthority("CASE6").withParties(of(createPartyBuilder()))
                .withHearings(of(defaultHearingAsBuilder().withHearingDays(of(defaultHearingDayDocumentAsBuilder().withSittingDay("2021-03-23T10:00:00").build(), createHearingDayDocument())),
                        defaultHearingAsBuilder().withHearingDays(of(createHearingDayDocument(), createHearingDayDocument()))));

        caseBuilderList.get(6).withCaseReference(TFL_CASE2_REFERENCE).withProsecutingAuthority("CASE7").withParties(of(createPartyBuilder()))
                .withHearings(of(defaultHearingAsBuilder().withHearingDays(of(defaultHearingDayDocumentAsBuilder().withSittingDay("2021-03-22T08:00:00").build(), createHearingDayDocument())),
                        defaultHearingAsBuilder().withHearingDays(of(createHearingDayDocument(), createHearingDayDocument()))));

        caseBuilderList.get(7).withCaseReference(TFL_CASE2_REFERENCE).withProsecutingAuthority("CASE8").withParties(of(createPartyBuilder()))
                .withHearings(of(defaultHearingAsBuilder().withHearingDays(of(defaultHearingDayDocumentAsBuilder().withSittingDay("2021-03-22T09:00:00").build(), createHearingDayDocument())),
                        defaultHearingAsBuilder().withHearingDays(of(createHearingDayDocument(), createHearingDayDocument()))));

        caseBuilderList.get(8).withCaseReference(TFL_CASE8_REFERENCE).withProsecutingAuthority("CASE8").withParties(of(createPartyBuilder()))
                .withHearings(of(defaultHearingAsBuilder().withHearingDays(of(defaultHearingDayDocumentAsBuilder().withSittingDay("2021-03-22T09:00:00").build(), createHearingDayDocument())),
                        defaultHearingAsBuilder().withHearingDays(of(createHearingDayDocument(), createHearingDayDocument()))));

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
                .withRepresentationOrder(defaultRepresentationOrder());
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
