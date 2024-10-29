package com.rdv.server.core.util.fuzzymatching.event;

import com.miguelfonseca.completely.data.Indexable;
import com.rdv.server.core.entity.Event;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author davidgarcia
 */
@Getter
public class EventRecord implements Indexable {

    private final Event event;

    public EventRecord(Event userObject) {
        this.event = userObject;
    }

    @Override
    public List<String> getFields() {
        return Arrays.asList(event.getTitle(), event.getVenue());
    }

}
