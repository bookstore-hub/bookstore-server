package com.rdv.server.core.util.fuzzymatching.event;


import com.miguelfonseca.completely.AutocompleteEngine;
import com.miguelfonseca.completely.text.analyze.tokenize.WordTokenizer;
import com.miguelfonseca.completely.text.analyze.transform.LowerCaseTransformer;
import com.rdv.server.core.entity.Event;
import com.rdv.server.core.repository.EventRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * @author davidgarcia
 */
@Component
public class EventMatcher implements ApplicationListener<ApplicationReadyEvent> {

    private static final Log LOGGER = LogFactory.getLog(Event.class);

    private AutocompleteEngine<EventRecord> engine = null;
    private boolean initialized = false;

    private final EventRepository eventRepository;

    public EventMatcher(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("EventMatcher initialized start: " + new Date());
        }
        engine = new AutocompleteEngine.Builder<EventRecord>()
                .setIndex(new EventAdapter())
                .setAnalyzers(new LowerCaseTransformer(), new WordTokenizer())
                .build();

        engine.addAll(eventRepository.findAllEvents().stream().map(EventRecord::new).toList());

        initialized = true;

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("EventMatcher initialized end: " + new Date());
        }
    }

    public List<Event> collectPossibleMatches(String query) {
        if (!initialized) {
            throw new BeanInitializationException("Events not initialized yet");
        }

        List<EventRecord> matches = engine.search(query);
        List<Event> events = matches.stream().map(EventRecord::getEvent).toList();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Results for " + query + ": " + events.stream().map(Object::toString).toList());
        }

        return events;
    }

}
