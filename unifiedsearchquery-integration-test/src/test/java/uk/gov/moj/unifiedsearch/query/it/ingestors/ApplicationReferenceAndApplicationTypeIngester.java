package uk.gov.moj.unifiedsearch.query.it.ingestors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.ApplicationDocumentMother.defaultApplicationAsBuilder;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.ApplicationDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;

import java.util.List;

public class ApplicationReferenceAndApplicationTypeIngester {

    public static final String SEARCHED_APPLICATION_REFERENCE = "APPREF001";
    public static final String SEARCHED_APPLICATION_TYPE = "Application to vary conditions of bail";


    private final ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();

    private List<CaseDocument> indexedCaseList;

    public void loadCases() throws Exception {
        indexedCaseList = createCases();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    private List<CaseDocument> createCases() {
        final List<CaseDocument.Builder> caseBuilderList = defaultCasesAsBuilderList(3);

        caseBuilderList.get(0).withApplications(asList(createApplication(SEARCHED_APPLICATION_REFERENCE, "Fake Application Type 001"),
                createApplication("APPREF002", SEARCHED_APPLICATION_TYPE),
                createApplication("APPREFOO3", SEARCHED_APPLICATION_TYPE)));

        caseBuilderList.get(1).withApplications(singletonList(createApplication(SEARCHED_APPLICATION_REFERENCE, SEARCHED_APPLICATION_TYPE)));

        caseBuilderList.get(2).withApplications(singletonList(createApplication(SEARCHED_APPLICATION_REFERENCE, "Fake Application Type 002")));

        return caseBuilderList.stream()
                .map(CaseDocument.Builder::build)
                .collect(toList());
    }

    public List<CaseDocument> getExpectedCases() {
        return asList(indexedCaseList.get(1));
    }


    private static ApplicationDocument.Builder createApplication(final String applicationReference, final String applicationType) {
        final ApplicationDocument.Builder applicationBuilder = defaultApplicationAsBuilder();
        applicationBuilder.withApplicationReference(applicationReference);
        applicationBuilder.withApplicationType(applicationType);
        return applicationBuilder;
    }
}
