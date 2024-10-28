package com.rdv.server.storage.to;

/**
 * @author davidgarcia
 */

public enum ContainerType {

    USER_PHOTOS("userphotos"),
    EVENT_POSTERS("eventposters");


    private String value;


    ContainerType(String value) {
        this.value = value;
    }


    /**
     * Returns the value
     *
     * @return Returns the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value
     *
     * @param value The value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
