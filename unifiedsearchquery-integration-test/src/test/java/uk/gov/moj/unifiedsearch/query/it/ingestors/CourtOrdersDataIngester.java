package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.DEFENDANT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.ApplicationDocumentMother.defaultApplicationAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CourtOrderDocumentMother.defaultCourtOrderAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.OffenceDocumentMother.defaultOffenceDocumentAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother.defaultPartyAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.RepresentationOrderDocumentMother.defaultRepresentationOrder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.ApplicationDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.ImmutableList;

public class CourtOrdersDataIngester {

    public static final String TFL_CASE_REFERENCE = "TFL1234567";
    public static final String TVL_CASE_REFERENCE = "TVL1234567";
    public static final String TVL_CASE_2_REFERENCE = "TVL2345678";
    public static final String APP_REFERENCE_PREFIX = "APPUSRS000";
    private final ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();
    private static final AtomicInteger applicationReferenceSuffix = new AtomicInteger();

    private List<CaseDocument> indexedCaseList;

    public void loadCases() throws Exception {
        indexedCaseList = caseListForCourtOrdersSearch();
        loadCaseDocuments();
    }

    private void loadCaseDocuments() throws Exception {
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> caseListForCourtOrdersSearch() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(4);
        final LocalDate dateToday = LocalDate.now();

        caseBuilderList.get(0).withCaseReference(TFL_CASE_REFERENCE).withProsecutingAuthority("TFL")
                .with_is_crown(true)
                .withParties(ImmutableList.of(createPartyBuilderWithProceedingsConcludedAndCourtOrders(dateToday.toString(), dateToday.plusDays(10).toString(), true)));

        final List<ApplicationDocument.Builder> tvlApplicationList = asList(createApplicationBuilder(), createApplicationBuilder());
        caseBuilderList.get(1).withCaseReference(TVL_CASE_REFERENCE).withProsecutingAuthority("TVL")
                .with_is_magistrates(true)
                .withApplications(tvlApplicationList)
                .withParties(ImmutableList.of(createPartyBuilderWithProceedingsConcludedAndCourtOrders(dateToday.toString(), dateToday.plusDays(7).toString(), true)));

        //standalone application
        final List<ApplicationDocument.Builder> standaloneApplicationList = asList(createApplicationBuilder(), createApplicationBuilder());
        caseBuilderList.get(2)
                .withProsecutingAuthority(null)
                .withCaseReference(null)
                .with_is_magistrates(true)
                .with_case_type("application")
                .withApplications(standaloneApplicationList)
                .withParties(ImmutableList.of(createPartyBuilderWithProceedingsConcludedAndMultipleCourtOrders(dateToday, dateToday.plusDays(2), false)));

        // TVL case without offences
        caseBuilderList.get(3).withCaseReference(TVL_CASE_2_REFERENCE)
                .with_is_magistrates(true)
                .withProsecutingAuthority("TVL").withParties(ImmutableList.of(createPartyBuilderWithProccedingsConcludedAndNoCourtOrders()));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    public CaseDocument getIndexDocumentAt(int index) {
        return indexedCaseList.get(index);
    }

    private static ApplicationDocument.Builder createApplicationBuilder() {
        final ApplicationDocument.Builder appBuilder = defaultApplicationAsBuilder();
        final String newApplicationReference = APP_REFERENCE_PREFIX + applicationReferenceSuffix.incrementAndGet();
        appBuilder.withApplicationReference(newApplicationReference);

        return appBuilder;
    }

    private PartyDocument.Builder createPartyBuilderWithProceedingsConcludedAndCourtOrders(final String startDate, final String endDate, final boolean proccedingsConcluded) {
        return defaultPartyAsBuilder()
                .withPartyType(DEFENDANT.toString())
                .withProceedingsConcluded(proccedingsConcluded)
                .withOffences(asList(defaultOffenceDocumentAsBuilder()
                        .withCourtOrders(asList(defaultCourtOrderAsBuilder()
                                .withStartDate(startDate)
                                .withEndDate(endDate).build())).build()));
    }

    private PartyDocument.Builder createPartyBuilderWithProceedingsConcludedAndMultipleCourtOrders(final LocalDate startDate, final LocalDate endDate, final boolean proccedingsConcluded) {
        return defaultPartyAsBuilder()
                .withPartyType(DEFENDANT.toString())
                .withProceedingsConcluded(proccedingsConcluded)
                .withOffences(asList(defaultOffenceDocumentAsBuilder()
                        .withCourtOrders(asList(defaultCourtOrderAsBuilder()
                                        .withStartDate(startDate.toString())
                                        .withEndDate(endDate.toString()).build(),
                                defaultCourtOrderAsBuilder()
                                        .withStartDate(startDate.plusDays(20).toString())
                                        .withEndDate(endDate.plusDays(20).toString()).build())).build()));
    }

    private PartyDocument.Builder createPartyBuilderWithProccedingsConcludedAndNoCourtOrders() {
        return defaultPartyAsBuilder()
                .withProceedingsConcluded(true)
                .withOffences(ImmutableList.of())
                .withPartyType(DEFENDANT.toString())
                .withRepresentationOrder(defaultRepresentationOrder());
    }
}
