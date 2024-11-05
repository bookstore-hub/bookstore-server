package com.rdv.server.core.service;


import com.rdv.server.core.entity.Event;
import com.rdv.server.core.entity.User;

import java.util.List;

/**
 * @author david.garcia
 */
public interface SearchService {


    List<Event> autoSearchEvents(String searchString);

    List<Event> fullSearchEvents(String searchString);

    List<User> autoSearchUsers(User user, String searchString);

    List<User> autoSearchFriends(User user, String searchString);

    List<User> fullSearchUsers(User user, String searchString);

    List<User> fullSearchFriends(User user, String searchString);

}
