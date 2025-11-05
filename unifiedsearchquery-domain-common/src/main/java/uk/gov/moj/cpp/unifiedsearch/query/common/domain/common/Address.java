package uk.gov.moj.cpp.unifiedsearch.query.common.domain.common;

public class Address {

    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String address5;
    private String postCode;

    public String getAddress1() {
        return address1;
    }

    public Address setAddress1(final String address1) {
        this.address1 = address1;
        return this;
    }

    public String getAddress2() {
        return address2;
    }

    public Address setAddress2(final String address2) {
        this.address2 = address2;
        return this;
    }
    public String getAddress3() {
        return address3;
    }

    public Address setAddress3(final String address3) {
        this.address3 = address3;
        return this;
    }

    public String getAddress4() {
        return address4;
    }

    public Address setAddress4(final String address4) {
        this.address4 = address4;
        return this;
    }

    public String getAddress5() {
        return address5;
    }

    public Address setAddress5(final String address5) {
        this.address5 = address5;
        return this;
    }

    public String getPostCode() {
        return postCode;
    }

    public Address setPostCode(final String postCode) {
        this.postCode = postCode;
        return this;
    }
}
