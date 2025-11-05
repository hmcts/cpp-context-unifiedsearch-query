package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.laa;

public class VerdictType {

    private String verdictTypeId;
    private int sequence;
    private String categoryType;
    private String category;
    private String description;

    public String getVerdictTypeId() {
        return verdictTypeId;
    }

    public VerdictType setVerdictTypeId(final String verdictTypeId) {
        this.verdictTypeId = verdictTypeId;
        return this;
    }

    public int getSequence() {
        return sequence;
    }

    public VerdictType setSequence(final int sequence) {
        this.sequence = sequence;
        return this;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public VerdictType setCategoryType(final String categoryType) {
        this.categoryType = categoryType;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public VerdictType setCategory(final String category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public VerdictType setDescription(final String description) {
        this.description = description;
        return this;
    }
}
