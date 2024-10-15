package com.rdv.server.core.entity;

/**
 * @author davidgarcia
 */

public enum Location {

    MONTREAL (1);

    private final int locationCode;

    Location(int locationCode) {
        this.locationCode = locationCode;
    }

    private int getCode() {
        return locationCode;
    }

    public static Location fromCode(int locationCode) {
        return Location.MONTREAL;
    }

}
