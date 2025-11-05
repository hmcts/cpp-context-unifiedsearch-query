package uk.gov.moj.cpp.unifiedsearch.query.common.domain.response.common;

public class CourtCentre {

    private String id;
    private String name;
    private String roomId;
    private String roomName;
    private String welshName;
    private String welshRoomName;
    private Address address;
    private String code;

    public String getId() {
        return id;
    }

    public CourtCentre setId(final String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CourtCentre setName(final String name) {
        this.name = name;
        return this;
    }

    public String getRoomId() {
        return roomId;
    }

    public CourtCentre setRoomId(final String roomId) {
        this.roomId = roomId;
        return this;
    }

    public String getRoomName() {
        return roomName;
    }

    public CourtCentre setRoomName(final String roomName) {
        this.roomName = roomName;
        return this;
    }

    public String getWelshName() {
        return welshName;
    }

    public CourtCentre setWelshName(final String welshName) {
        this.welshName = welshName;
        return this;
    }

    public String getWelshRoomName() {
        return welshRoomName;
    }

    public CourtCentre setWelshRoomName(final String welshRoomName) {
        this.welshRoomName = welshRoomName;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public CourtCentre setAddress(final Address address) {
        this.address = address;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }
}
