package uk.gov.moj.cpp.unifiedsearch.query.common.constant;

import static java.util.Optional.empty;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.HEARINGS_NESTED_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.HEARING_DATE_NESTED_FILTER;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.HEARING_DATE_TIME_PATH;
import static uk.gov.moj.cpp.unifiedsearch.query.common.constant.CpsCaseSearchConstants.PARTY_NESTED_PATH;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.elasticsearch.index.query.QueryBuilder;

public enum CpsCaseSortBy {

    URN("urn", "urn", empty(), empty(), empty()),
    STATUS("status", "caseStatusCode", empty(), empty(), empty()),
    DATE_OF_BIRTH("dateOfBirth", "parties.dateOfBirth", Optional.of(PARTY_NESTED_PATH), empty(), empty()),
    HEARING_DATE("hearingDate", HEARING_DATE_TIME_PATH, Optional.of(HEARINGS_NESTED_PATH), Optional.of(HEARING_DATE_NESTED_FILTER), Optional.of(1)),
    HEARING_TYPE("hearingType", "hearings.hearingType", Optional.of(HEARINGS_NESTED_PATH), Optional.of(HEARING_DATE_NESTED_FILTER), Optional.of(1)),
    COURT_HOUSE("courtHouse", "hearings.courtHouse", Optional.of(HEARINGS_NESTED_PATH), Optional.of(HEARING_DATE_NESTED_FILTER), Optional.of(1)),
    COURT_ROOM("courtRoom", "hearings.courtRoom", Optional.of(HEARINGS_NESTED_PATH), Optional.of(HEARING_DATE_NESTED_FILTER), Optional.of(1)),
    PROSECUTOR("prosecutor", "prosecutor.exact", empty(), empty(), empty()),
    PARALEGAL_OFFICER("paralegalOfficer", "paralegalOfficer.exact", empty(), empty(), empty()),
    CROWN_ADVOCATE("crownAdvocate", "crownAdvocate.exact", empty(), empty(), empty()),
    OFFENCE_DESCRIPTION("offenceDescription", "parties.offences.description.exact", Optional.of("parties.offences"), empty(), empty()),
    DEFENDANT_LASTNAME("defendantLastName", "parties.lastName.exact", Optional.of(PARTY_NESTED_PATH), empty(), empty()),
    PARTY_LASTNAME_OR_ORGANISATIONNAME("partyLastOrOrganisationName", "parties.lastOrOrganisationName", Optional.of(PARTY_NESTED_PATH), empty(), empty());

    private String keyName;

    private String fieldName;

    //Apply this filter before sorting
    private final Optional<QueryBuilder> nestedFilter;
    //How many children matching the above filter to consider for sorting
    private final Optional<Integer> nestedMaxChildren;


    private Optional<String> nestedPath;

    private static final Map<String, CpsCaseSortBy> lookUp = new ConcurrentHashMap<>();

    static {
        Arrays.stream(CpsCaseSortBy.values()).forEach(cpsCaseSortBy ->
                lookUp.put(cpsCaseSortBy.keyName, cpsCaseSortBy)
        );
    }

    CpsCaseSortBy(final String keyName, final String fieldName, final Optional<String> nestedPath, final Optional<QueryBuilder> nestedFilter, final Optional<Integer> nestedMaxChildren) {
        this.keyName = keyName;
        this.fieldName = fieldName;
        this.nestedPath = nestedPath;
        this.nestedFilter = nestedFilter;
        this.nestedMaxChildren = nestedMaxChildren;
    }

    public String getKeyName() {
        return keyName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Optional<String> getNestedPath() {
        return nestedPath;
    }

    public static CpsCaseSortBy findByKeyNameOrDefault(final String keyName) {
        return lookUp.putIfAbsent(keyName, URN);
    }

    public Optional<QueryBuilder> getNestedFilter() {
        return nestedFilter;
    }

    public Optional<Integer> getNestedMaxChildren() {
        return nestedMaxChildren;
    }

}
