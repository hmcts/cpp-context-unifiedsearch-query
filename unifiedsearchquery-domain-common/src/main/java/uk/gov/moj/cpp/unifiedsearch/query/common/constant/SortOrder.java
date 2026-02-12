package uk.gov.moj.cpp.unifiedsearch.query.common.constant;

public enum SortOrder {
    ASC("Asc"),
    DESC("Desc"),
    Asc("Asc"),
    Desc("Desc"),
    asc("Asc"),
    desc("Desc");

    private String order;

    private SortOrder(final String order){ this.order = order;}

    public String getOrder(){ return this.order;}

}
