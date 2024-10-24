package com.rdv.server.core.service;


import com.rdv.server.core.entity.Event;
import com.rdv.server.core.entity.User;
import com.rdv.server.core.entity.UserEventOwner;
import com.rdv.server.core.entity.UserEventOwnerStatus;
import com.rdv.server.core.repository.EventRepository;
import com.rdv.server.core.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;


/**
 * @author david.garcia
 */
@Service
public class CoreServiceImpl implements CoreService {

    protected static final Log LOGGER = LogFactory.getLog(CoreServiceImpl.class);

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public CoreServiceImpl(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }


    @Override
    public Event addEvent(User user, Event event) {
        UserEventOwner ownedEvent = new UserEventOwner();
        ownedEvent.setUser(user);
        ownedEvent.setEvent(event);
        ownedEvent.setStatus(UserEventOwnerStatus.CREATOR);
        ownedEvent.setCreationDate(OffsetDateTime.now());

        user.getOwnedEvents().add(ownedEvent);
        userRepository.save(user); //todo test without last save

        return eventRepository.save(event);
    }

}
