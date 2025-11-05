package uk.gov.moj.unifiedsearch.query.it.ingestors.cps;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.ElasticSearchIndexIngestorUtil;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.cps.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.cps.CaseDocumentMother;

import java.util.List;

public class CpsUnitCodeDataIngester {

    private ElasticSearchIndexIngestorUtil elasticSearchIndexIngestorUtil = new ElasticSearchIndexIngestorUtil();
    private List<CaseDocument> indexedCaseList;

    public static final String DERBY_CPS_UNIT = "210";
    public static final String BRISTOL_CPS_UNIT = "30";
    public static final String BEDFORDSHIRE_CPS_UNIT = "10";
    public static final String LONDON_NORTH_CPS_UNIT = "680";
    public static final String SWANSEA_CPS_UNIT = "1410";


    public void loadCaseDocuments() throws Exception {
        indexedCaseList = createCaseCpsUnitDocuments();
        elasticSearchIndexIngestorUtil.ingestCaseData(indexedCaseList);
    }

    public List<CaseDocument> createCaseCpsUnitDocuments() {

        final List<CaseDocument.Builder> caseBuilderList = CaseDocumentMother.defaultCasesAsBuilderList(9);
        caseBuilderList.get(0).withCpsUnitCode(LONDON_NORTH_CPS_UNIT);
        caseBuilderList.get(1).withCpsUnitCode(BRISTOL_CPS_UNIT);
        caseBuilderList.get(2).withCpsUnitCode(DERBY_CPS_UNIT);

        caseBuilderList.get(3).withCpsUnitCode(BRISTOL_CPS_UNIT);
        caseBuilderList.get(4).withCpsUnitCode(LONDON_NORTH_CPS_UNIT);
        caseBuilderList.get(5).withCpsUnitCode(BEDFORDSHIRE_CPS_UNIT);

        caseBuilderList.get(6).withCpsUnitCode(SWANSEA_CPS_UNIT);
        caseBuilderList.get(7).withCpsUnitCode(BEDFORDSHIRE_CPS_UNIT);
        caseBuilderList.get(8).withCpsUnitCode(SWANSEA_CPS_UNIT);

        return caseBuilderList
                .stream()
                .map(CaseDocument.Builder::build)
                .collect(
                        toList());

    }

    public CaseDocument getIndexDocumentAt(int index) {
        return indexedCaseList.get(index);
    }

    public CaseDocument getCaseForCps002() {
        return indexedCaseList.get(2);
    }

    public List<CaseDocument> getCasesForCpsUnitLondon() {
        return asList(indexedCaseList.get(0), indexedCaseList.get(4));
    }
}
