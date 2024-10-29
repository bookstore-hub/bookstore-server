package com.rdv.server.core.service;


import com.rdv.server.core.entity.Event;
import com.rdv.server.core.entity.User;

import java.util.List;

/**
 * @author david.garcia
 */
public interface SearchService {


    List<Event> searchEvents(String searchData);

    List<User> searchUsers(String searchData);

}
