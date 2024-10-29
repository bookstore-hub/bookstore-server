package com.rdv.server.core.util.fuzzymatching.user;

import com.miguelfonseca.completely.IndexAdapter;
import com.miguelfonseca.completely.data.ScoredObject;
import com.miguelfonseca.completely.text.index.FuzzyIndex;
import com.miguelfonseca.completely.text.index.PatriciaTrie;
import com.miguelfonseca.completely.text.match.EditDistanceAutomaton;

import java.util.Collection;


/**
 * @author davidgarcia
 */
public class UserAdapter implements IndexAdapter<UserRecord> {

    private final FuzzyIndex<UserRecord> index = new PatriciaTrie<>();

    @Override
    public Collection<ScoredObject<UserRecord>> get(String token) {
        // Set threshold according to the token length
        double threshold = Math.log(Math.max(token.length() - 1, 1));
        return index.getAny(new EditDistanceAutomaton(token, threshold));
    }

    @Override
    public boolean put(String token, UserRecord value) {
        return index.put(token, value);
    }

    @Override
    public boolean remove(UserRecord value) {
        return index.remove(value);
    }

}