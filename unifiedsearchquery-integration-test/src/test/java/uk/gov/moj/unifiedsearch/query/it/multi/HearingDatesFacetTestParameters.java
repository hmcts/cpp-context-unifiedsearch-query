package uk.gov.moj.unifiedsearch.query.it.multi;

import static java.lang.String.format;
import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.CaseDocumentMother.defaultCasesAsBuilderList;
import static uk.gov.moj.cpp.unifiedsearch.test.util.ingest.mothers.HearingDayDocumentMother.hearingDays;
import static uk.gov.moj.unifiedsearch.query.it.util.AssertCases.hasCases;

import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Case;
import uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.Hearing;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.CaseDocument;
import uk.gov.moj.cpp.unifiedsearch.test.util.ingest.document.HearingDayDocument;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class HearingDatesFacetTestParameters implements FacetTestParameters {


    private static final String HEARING_DATE_FROM_PARAM_NAME = "hearingDateFrom";
    private static final String HEARING_DATE_TO_PARAM_NAME = "hearingDateTo";

    private static final String HEARING_DATE_FROM = "2019-04-01";
    private static final String HEARING_DATE_TO = "2019-05-01";

    private static final LocalDate START_DATE = parse(HEARING_DATE_FROM, ISO_LOCAL_DATE);
    private static final LocalDate END_DATE = parse(HEARING_DATE_TO, ISO_LOCAL_DATE);
    private final UUID courtCentreId;


    private List<HearingDayDocument> rangeOutside1;

    private List<HearingDayDocument> rangeOutside2;
    private List<HearingDayDocument> rangeOutside3;
    private List<HearingDayDocument> rangeInside1;
    private List<HearingDayDocument> rangeInside2;
    private List<HearingDayDocument> rangeInside3;


    private static final int TEST_DOCUMENT_COUNT = 10;

    private String failureMessage;

    public HearingDatesFacetTestParameters() {
        this.courtCentreId = UUID.randomUUID();
        initData();
    }

    public HearingDatesFacetTestParameters(final UUID courtCentreId) {
        this.courtCentreId = courtCentreId;
        initData();
    }

    private void initData() {
        rangeOutside1 = hearingDays(this.courtCentreId, "2019-03-01", "2019-03-19");

        rangeOutside2 = hearingDays(this.courtCentreId, "2019-01-01", "2019-02-28");
        rangeOutside3 = hearingDays(this.courtCentreId, "2019-01-01", "2019-02-28");
        rangeInside1 = hearingDays(this.courtCentreId, "2019-03-28", "2019-04-28");
        rangeInside2 = hearingDays(this.courtCentreId, "2019-04-15");
        rangeInside3 = hearingDays(this.courtCentreId, "2019-04-02", "2019-06-23");
    }

    @Override
    public void addSearchParameters(final Map<String, String> parameters) {

        parameters.put(HEARING_DATE_FROM_PARAM_NAME, HEARING_DATE_FROM);
        parameters.put(HEARING_DATE_TO_PARAM_NAME, HEARING_DATE_TO);

    }

    @Override
    public void addMatchData(final List<CaseDocument.Builder> caseBuilderList) {

        caseBuilderList.get(0).getHearingBuilders().get(0).withCourtId(courtCentreId.toString()).withHearingDays(rangeOutside1);
        caseBuilderList.get(1).getHearingBuilders().get(0).withCourtId(courtCentreId.toString()).withHearingDays(rangeOutside3);
        caseBuilderList.get(2).getHearingBuilders().get(0).withCourtId(courtCentreId.toString()).withHearingDays(rangeInside1);
        caseBuilderList.get(3).getHearingBuilders().get(0).withCourtId(courtCentreId.toString()).withHearingDays(rangeInside2);
        caseBuilderList.get(4).getHearingBuilders().get(0).withCourtId(courtCentreId.toString()).withHearingDays(rangeInside2);
        caseBuilderList.get(5).getHearingBuilders().get(0).withCourtId(courtCentreId.toString()).withHearingDays(rangeInside3);
        caseBuilderList.get(6).getHearingBuilders().get(0).withCourtId(courtCentreId.toString()).withHearingDays(rangeOutside2);
        caseBuilderList.get(7).getHearingBuilders().get(0).withCourtId(courtCentreId.toString()).withHearingDays(rangeInside2);
        caseBuilderList.get(8).getHearingBuilders().get(0).withCourtId(courtCentreId.toString()).withHearingDays(rangeOutside2);
        caseBuilderList.get(9).getHearingBuilders().get(0).withCourtId(courtCentreId.toString()).withHearingDays(rangeOutside3);

        // Clear dates in any further Cases
        if (caseBuilderList.size() > TEST_DOCUMENT_COUNT) {
            for (int i = TEST_DOCUMENT_COUNT; i < caseBuilderList.size(); i++) {
                caseBuilderList.get(i).getHearingBuilders().forEach(hb -> hb.withHearingDays(null));
            }
        }
    }

    @Override
    public void ensureCapacity(final List<CaseDocument.Builder> caseBuilderList) {

        final int requiredDocsCount = TEST_DOCUMENT_COUNT - caseBuilderList.size();

        if (requiredDocsCount > 0) {
            caseBuilderList.addAll( defaultCasesAsBuilderList(requiredDocsCount) );
        }
    }

    @Override
    public boolean hasExpectedHits(final List<Case> caseList) {

        // Other matchers may have precluded some of the above added data so we need
        // to test that however many cases are provided here, the hearings they contain
        // have expected HearingDates

        if (hasCases(caseList)) {

            if (!allMatch(caseList)) {
                failureMessage =
                        format("Unexpected HearingDate in provided case list, was expecting all CaseDocuments to have Hearing with hearing dates between [%s] and [%s]",
                                HEARING_DATE_FROM, HEARING_DATE_TO);
                return false;
            }
        }
        else {
            failureMessage = "Empty case list provided";
            return false;
        }

        return true;

    }

    private boolean allMatch(final List<Case> caseList) {
        return caseList.stream()
                .map(c -> c.getHearings().get(0))
                .allMatch(hearingDatesInRange);
    }

    private Predicate<Hearing> hearingDatesInRange = hearing -> {

        final List<String> hearingDateStrings = hearing.getHearingDates();

        return hearingDateStrings.stream()
                .map(s -> LocalDate.parse(s, ISO_LOCAL_DATE))
                .anyMatch(ld -> (START_DATE.isBefore(ld) && END_DATE.isAfter(ld)));
    };



    @Override
    public String getFailureMessage() {
        return failureMessage;
    }
}
