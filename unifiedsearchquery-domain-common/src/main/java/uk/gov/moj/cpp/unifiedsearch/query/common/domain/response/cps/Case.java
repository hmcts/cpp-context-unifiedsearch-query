package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.cps;

import static java.util.Collections.emptyList;
import static uk.gov.justice.services.unifiedsearch.client.constant.CpsPartyType.DEFENDANT;
import static uk.gov.justice.services.unifiedsearch.client.constant.CpsPartyType.SUSPECT;

import java.util.List;
import java.util.stream.Collectors;

public class Case {

    private String caseId;
    private String urn;
    private String cpsUnit;
    private String cjsArea;
    private String caseType;
    private String caseStatusCode;
    private String operationName;
    private String paralegalOfficer;
    private String crownAdvocate;
    private String prosecutor;
    private String witnessCareUnitCode;
    private String witnessCareOfficer;
    private String unit;
    private String unitGroup;
    private List<Party> parties;
    private List<Hearing> hearings;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(final String caseId) {
        this.caseId = caseId;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(final String urn) {
        this.urn = urn;
    }

    public String getCpsUnit() {
        return cpsUnit;
    }

    public void setCpsUnit(final String cpsUnit) {
        this.cpsUnit = cpsUnit;
    }

    public String getCjsArea() {
        return cjsArea;
    }

    public void setCjsArea(final String cjsArea) {
        this.cjsArea = cjsArea;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(final String caseType) {
        this.caseType = caseType;
    }

    public String getCaseStatusCode() {
        return caseStatusCode;
    }

    public void setCaseStatusCode(final String caseStatusCode) {
        this.caseStatusCode = caseStatusCode;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(final String operationName) {
        this.operationName = operationName;
    }

    public String getParalegalOfficer() {
        return paralegalOfficer;
    }

    public void setParalegalOfficer(final String paralegalOfficer) {
        this.paralegalOfficer = paralegalOfficer;
    }

    public String getCrownAdvocate() {
        return crownAdvocate;
    }

    public void setCrownAdvocate(final String crownAdvocate) {
        this.crownAdvocate = crownAdvocate;
    }

    public String getProsecutor() {
        return prosecutor;
    }

    public void setProsecutor(final String prosecutor) {
        this.prosecutor = prosecutor;
    }

    public String getWitnessCareUnitCode() {
        return witnessCareUnitCode;
    }

    public void setWitnessCareUnitCode(final String witnessCareUnitCode) {
        this.witnessCareUnitCode = witnessCareUnitCode;
    }

    public String getWitnessCareOfficer() {
        return witnessCareOfficer;
    }

    public void setWitnessCareOfficer(final String witnessCareOfficer) {
        this.witnessCareOfficer = witnessCareOfficer;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(final String unit) {
        this.unit = unit;
    }

    public String getUnitGroup() {
        return unitGroup;
    }

    public void setUnitGroup(final String unitGroup) {
        this.unitGroup = unitGroup;
    }

    public List<Party> getParties() {
        if (parties == null) {
            return emptyList();
        }

        return parties.stream()
                .filter(party -> party.getPartyType().contains(DEFENDANT.toString()) || party.getPartyType().contains(SUSPECT.toString()))
                .collect(Collectors.toList());
    }

    public void setParties(final List<Party> parties) {
        this.parties = parties;
    }

    public List<Hearing> getHearings() {
        return hearings;
    }

    public void setHearings(final List<Hearing> hearings) {
        this.hearings = hearings;
    }

}
