package com.rdv.server.core.entity;

/**
 * @author davidgarcia
 */

public enum EventState {

    PLANNED,
    CANCELLED,
    ONGOING    //State not saved, determined only when sending the event data to the client


}
