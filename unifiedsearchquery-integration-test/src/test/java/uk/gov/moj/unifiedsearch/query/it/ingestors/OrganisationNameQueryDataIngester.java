package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.PartyDocument;

import java.io.IOException;
import java.util.List;

public class OrganisationNameQueryDataIngester {

    private static ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();


    public static void loadCaseDocuments(final List<CaseDocument> caseList) throws IOException {
        elasticSearchIndexIngestorUtil.ingestCaseData(caseList);
    }

    public static List<CaseDocument> caseListForOrganisationNameQuery() {

        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(4);

        final PartyDocument.Builder organisationBuilder = new PartyDocument.Builder().withPartyId(randomUUID().toString()).withOrganisationName("Organisation");
        final PartyDocument.Builder organisationNameBuilder = new PartyDocument.Builder().withPartyId(randomUUID().toString()).withOrganisationName("OrganisationName");

        final PartyDocument.Builder organisation_NameBuilder = new PartyDocument.Builder().withPartyId(randomUUID().toString()).withOrganisationName("Organisation_Name");
        final PartyDocument.Builder eyBuilder = new PartyDocument.Builder().withPartyId(randomUUID().toString()).withOrganisationName("EY");

        caseBuilderList.get(0).withParties(asList(organisationBuilder));
        caseBuilderList.get(1).withParties(asList(organisationNameBuilder));
        caseBuilderList.get(2).withParties(asList(organisation_NameBuilder));
        caseBuilderList.get(3).withParties(asList(eyBuilder));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());


    }


}
