package com.rdv.server.core.service;


import com.rdv.server.core.entity.Event;
import com.rdv.server.core.repository.EventRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;


/**
 * @author david.garcia
 */
@Service
public class CoreServiceImpl implements CoreService {

    protected static final Log LOGGER = LogFactory.getLog(CoreServiceImpl.class);

    private final EventRepository eventRepository;

    public CoreServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

}
