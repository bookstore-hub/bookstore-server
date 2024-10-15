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
     * Returns the type
     *
     * @return Returns the type
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the type
     *
     * @param value The type to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
