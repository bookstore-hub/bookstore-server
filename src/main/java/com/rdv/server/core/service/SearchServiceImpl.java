package com.rdv.server.core.service;


import com.rdv.server.core.entity.Event;
import com.rdv.server.core.entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author david.garcia
 */
@Service
public class SearchServiceImpl implements SearchService {

    protected static final Log LOGGER = LogFactory.getLog(SearchServiceImpl.class);


    @Override
    public List<Event> searchEvents(String searchData) {
        return List.of();
    }

    @Override
    public List<User> searchUsers(String searchData) {
        return List.of();
    }

}
