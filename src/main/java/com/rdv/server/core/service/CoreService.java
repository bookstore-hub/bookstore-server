package com.rdv.server.core.service;


import com.rdv.server.core.entity.Event;
import com.rdv.server.core.entity.User;

/**
 * @author david.garcia
 */
public interface CoreService {

    Event addEvent(User user, Event event);

}
