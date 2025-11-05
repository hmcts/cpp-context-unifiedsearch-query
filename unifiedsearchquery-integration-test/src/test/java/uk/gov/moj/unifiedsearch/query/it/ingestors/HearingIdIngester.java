package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.DEFENDANT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.HearingDocumentMother.defaultHearingAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.OffenceDocumentMother.defaultOffenceDocument;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother.defaultPartyAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.RepresentationOrderDocumentMother.defaultRepresentationOrder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableList;

public class HearingIdIngester {

    public static final String HEARING_ID_1 = randomUUID().toString();
    public static final String HEARING_ID_2 = randomUUID().toString();
    public static final String HEARING_ID_3 = randomUUID().toString();


    private static ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();


    public static void loadCaseDocuments(final List<CaseDocument> caseList)  throws Exception {
        elasticSearchIndexIngestorUtil.ingestCaseData(caseList);
    }


    public static List<CaseDocument> caseListForHearingIdQuery() {

        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(3);

        caseBuilderList.get(0).withHearings(hearingWithId(HEARING_ID_1))
                .withParties(singletonList(createPartyBuilder()));
        caseBuilderList.get(1).withHearings(hearingWithId(HEARING_ID_2))
                .withParties(singletonList(createPartyBuilder()));
        caseBuilderList.get(2).withHearings(hearingWithId(HEARING_ID_3))
                .withParties(singletonList(createPartyBuilder()));

        return caseBuilderList
                .stream()
                .map(CaseDocument.Builder::build)
                .collect(
                        toList());

    }

    private static PartyDocument.Builder createPartyBuilder() {
        return defaultPartyAsBuilder()
                .withPartyType(DEFENDANT.toString())
                .withMasterPartyId(UUID.randomUUID().toString())
                .withOffences(ImmutableList.of(defaultOffenceDocument()))
                .withRepresentationOrder(defaultRepresentationOrder());
    }

    private static List<HearingDocument.Builder> hearingWithId(final String hearingId) {
        return singletonList(
                defaultHearingAsBuilder()
                        .withHearingId(hearingId));
    }



}
