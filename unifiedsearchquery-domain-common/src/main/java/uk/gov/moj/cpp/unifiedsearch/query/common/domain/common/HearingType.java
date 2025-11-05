package uk.gov.moj.cpp.unifiedsearch.query.common.domain.common;

public class HearingType {
    private String id;
    private String code;
    private String description;

    public String getId() {
        return id;
    }

    public HearingType setId(final String id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public HearingType setCode(final String code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public HearingType setDescription(final String description) {
        this.description = description;
        return this;
    }
}
