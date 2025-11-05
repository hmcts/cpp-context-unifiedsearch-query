package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.constant.PartyType.DEFENDANT;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.OffenceDocumentMother.defaultOffenceDocument;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.OffenceDocumentMother.defaultOffenceDocumentAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.PartyDocumentMother.defaultPartyAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.RepresentationOrderDocumentMother.defaultRepresentationOrder;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.OffenceDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableList;

public class LaaSearchShouldExcludeDeletedOffencesIngester {
    private final ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();

    private List<CaseDocument> indexedCaseList;

    public void loadCases() throws Exception {
        indexedCaseList = caseListForReferenceSearch();
        loadCaseDocuments();
    }

    private void loadCaseDocuments() throws Exception {
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }


    private List<CaseDocument> caseListForReferenceSearch() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(1);
        final OffenceDocument deletedOffence1 = defaultOffenceDocumentAsBuilder().withOffenceCode("").build();
        final OffenceDocument deletedOffence2 = defaultOffenceDocumentAsBuilder().withOffenceCode("").build();
        final PartyDocument.Builder partyDocumentBuilder1 = defaultPartyAsBuilder()
                .withPartyType(DEFENDANT.toString())
                .withNationalInsuranceNumber("nino123")
                .withMasterPartyId(UUID.randomUUID().toString())
                .withOffences(ImmutableList.of(deletedOffence1, defaultOffenceDocument(), deletedOffence2))
                .withRepresentationOrder(defaultRepresentationOrder());

        caseBuilderList.get(0).withParties(asList(partyDocumentBuilder1));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    public OffenceDocument getExpectedOffence() {
        return indexedCaseList.get(0).getParties().get(0).getOffences().get(1);
    }

}
