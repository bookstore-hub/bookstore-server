package com.rdv.server.core.service;


import com.rdv.server.core.entity.Event;
import com.rdv.server.core.entity.User;
import com.rdv.server.core.util.fuzzymatching.event.EventMatcher;
import com.rdv.server.core.util.fuzzymatching.user.UserMatcher;
import info.debatty.java.stringsimilarity.Levenshtein;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;


/**
 * @author david.garcia
 */
@Service
public class SearchServiceImpl implements SearchService {

    protected static final Log LOGGER = LogFactory.getLog(SearchServiceImpl.class);

    private static final int MAX_RESULTS = 20;
    public static final LevenshteinDistance LEVENSHTEIN_DISTANCE = new LevenshteinDistance();
    public static final int DISTANCE = 70;

    private final UserMatcher userMatcher;
    private final EventMatcher eventMatcher;

    public SearchServiceImpl(UserMatcher userMatcher, EventMatcher eventMatcher) {
        this.userMatcher = userMatcher;
        this.eventMatcher = eventMatcher;
    }


    @Override
    public List<Event> searchEvents(String searchData) {
        List<Event> matches = eventMatcher.collectPossibleMatches(searchData);
        matches.sort(Comparator.comparing(match -> new Levenshtein().distance(match.getTitle(), searchData)));
        return matches.stream().filter(event -> getLevenshteinScore(event.getTitle(), searchData) >= DISTANCE).limit(MAX_RESULTS).toList();
    }

    @Override
    public List<User> searchUsers(String searchData) {
        List<User> matches = userMatcher.collectPossibleMatches(searchData);
        matches.sort(Comparator.comparing(match -> new Levenshtein().distance(match.getUsername(), searchData)));
        return matches.stream().filter(user -> getLevenshteinScore(user.getUsername(), searchData) >= DISTANCE).limit(MAX_RESULTS).toList();
    }

    private double getLevenshteinScore(String value1, String value2) {
        return 100 - ((double) LEVENSHTEIN_DISTANCE.apply(value1, value2)) / Math.max(value1.length(), value2.length()) * 100;
    }

}
