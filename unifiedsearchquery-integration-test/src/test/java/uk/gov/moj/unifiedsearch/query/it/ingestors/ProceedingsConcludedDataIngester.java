package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.time.ZonedDateTime.now;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCaseAsBuilder;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_CRO_NUMBER_1_987654_312_AC2;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_LAST_NAME_AC1;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_LAST_NAME_AC2;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.EXPECTED_PARTY_PNC_ID_1_2099_1234567L_AC1;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.createDefendantParty;
import static uk.gov.moj.unifiedsearch.query.it.ingestors.PartyUtil.createPartyNoProceedingsConcluded;
import static uk.gov.moj.unifiedsearch.query.it.util.StubUtils.ISO_8601_FORMATTER;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.util.ArrayList;
import java.util.List;

public class ProceedingsConcludedDataIngester {

    private final ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();

    private List<CaseDocument> indexedCaseList;

    public void loadProceedingsConcludedCases() throws Exception {
        indexedCaseList = createProceedingsConcludedCases();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    public List<CaseDocument> getExpectedCasesForProceedingsConcludedWhenFalse() {
        return asList(indexedCaseList.get(0),indexedCaseList.get(1),indexedCaseList.get(2),indexedCaseList.get(4));
    }


    public List<CaseDocument> getExpectedCasesForProceedingsConcludedWhenTrue() {
        return asList(indexedCaseList.get(3));
    }

    private final List<CaseDocument>  createProceedingsConcludedCases() {
        final List<CaseDocument.Builder> caseBuilderList = new ArrayList<>();

        final String courtProceedingsInitiated = now().format(ISO_8601_FORMATTER);
        final String masterPartyId = randomUUID().toString();
        final String partyId = randomUUID().toString();

        final PartyDocument.Builder party1 = createPartyNoProceedingsConcluded(partyId, EXPECTED_PARTY_PNC_ID_1_2099_1234567L_AC1, "123456/20L", "TEGUN", EXPECTED_PARTY_LAST_NAME_AC1, "1931-01-01", "195 Ber Stree't", courtProceedingsInitiated,  masterPartyId);
        caseBuilderList.add(defaultCaseAsBuilder("b45d409a-4e72-403d-adb0-015f15bd89c6", "63GD6253620", false, true, false));
        caseBuilderList.get(0).withParties(asList(party1));

        final PartyDocument.Builder party2 = createPartyNoProceedingsConcluded(randomUUID().toString(), "2034/1234567r", EXPECTED_PARTY_CRO_NUMBER_1_987654_312_AC2, "ROBERT", EXPECTED_PARTY_LAST_NAME_AC2, "1932-01-15", "20 Rashleigh Way", now().format(ISO_8601_FORMATTER), randomUUID().toString());
        caseBuilderList.add(defaultCaseAsBuilder("f678854b-2bc4-41b5-8853-32d5fbaf0b3c", "89GD6995420", false, true, false));
        caseBuilderList.get(1).withParties(asList(party2));

        final PartyDocument.Builder party3 = createPartyNoProceedingsConcluded(randomUUID().toString(), "1981/98765432", "123456/20t", "JACK", "SMITH", "1933-01-29", "15 Katherine Road", now().format(ISO_8601_FORMATTER), randomUUID().toString());
        caseBuilderList.add(defaultCaseAsBuilder("1559f788-e155-452c-94f0-08f4cc1b180e", "78GD1219320", false, true, false));
        caseBuilderList.get(2).withParties(asList(party3));

        final PartyDocument.Builder party4 = createDefendantParty(randomUUID().toString(), "1981/98765432", "123456/20t", "JACK", "SMITH", "1933-01-29", "15 Katherine Road", now().format(ISO_8601_FORMATTER), randomUUID().toString(), true);
        caseBuilderList.add(defaultCaseAsBuilder("2559f788-e155-452c-94f0-08f4cc1b180e", "78GD1219320", false, true, false));
        caseBuilderList.get(3).withParties(asList(party4));

        final PartyDocument.Builder party5 = createDefendantParty(randomUUID().toString(), "1981/98765432", "123456/20t", "JACK", "SMITH", "1933-01-29", "15 Katherine Road", now().format(ISO_8601_FORMATTER), randomUUID().toString(), false);
        caseBuilderList.add(defaultCaseAsBuilder("3559f788-e155-452c-94f0-08f4cc1b180e", "78GD1219320", false, true, false));
        caseBuilderList.get(4).withParties(asList(party5));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }
}
