package com.rdv.server.core.util.fuzzymatching.event;

import com.miguelfonseca.completely.IndexAdapter;
import com.miguelfonseca.completely.data.ScoredObject;
import com.miguelfonseca.completely.text.index.FuzzyIndex;
import com.miguelfonseca.completely.text.index.PatriciaTrie;
import com.miguelfonseca.completely.text.match.EditDistanceAutomaton;

import java.util.Collection;


/**
 * @author davidgarcia
 */
public class EventAdapter implements IndexAdapter<EventRecord> {

    private final FuzzyIndex<EventRecord> index = new PatriciaTrie<>();

    @Override
    public Collection<ScoredObject<EventRecord>> get(String token) {
        // Set threshold according to the token length
        double threshold = Math.log(Math.max(token.length() - 1, 1));
        return index.getAny(new EditDistanceAutomaton(token, threshold));
    }

    @Override
    public boolean put(String token, EventRecord value) {
        return index.put(token, value);
    }

    @Override
    public boolean remove(EventRecord value) {
        return index.remove(value);
    }

}