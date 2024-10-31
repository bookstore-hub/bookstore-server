package com.rdv.server.core.service;


import com.rdv.server.core.entity.Event;
import com.rdv.server.core.entity.User;
import com.rdv.server.core.repository.EventRepository;
import com.rdv.server.core.repository.UserRepository;
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

    private static final int MAX_RESULTS = 10;
    public static final LevenshteinDistance LEVENSHTEIN_DISTANCE = new LevenshteinDistance();
    public static final int DISTANCE = 70;

    private final UserMatcher userMatcher;
    private final EventMatcher eventMatcher;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public SearchServiceImpl(UserMatcher userMatcher, EventMatcher eventMatcher, EventRepository eventRepository, UserRepository userRepository) {
        this.userMatcher = userMatcher;
        this.eventMatcher = eventMatcher;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }


    @Override
    public List<Event> autoSearchEvents(String searchString) {
        List<Event> matches = eventMatcher.collectPossibleMatches(searchString);
        matches.sort(Comparator.comparing(match -> new Levenshtein().distance(match.getTitle(), searchString)));
        return matches.stream().filter(event -> getLevenshteinScore(event.getTitle(), searchString) >= DISTANCE).limit(MAX_RESULTS).toList();
    }

    @Override
    public List<Event> fullSearchEvents(String searchString) {
        List<Event> matches = eventRepository.findAllEventsMatching(searchString);
        matches.sort(Comparator.comparing(match -> new Levenshtein().distance(match.getTitle(), searchString)));
        return matches.stream().limit(MAX_RESULTS).toList();
    }


    @Override
    public List<User> autoSearchUsers(User user, String searchString) {
        List<User> matches = userMatcher.collectPossibleMatches(searchString);
        matches.sort(Comparator.comparing(match -> new Levenshtein().distance(match.getUsername(), searchString)));
        return matches.stream().filter(match -> match.hasNotBlocked(user) &&
                getLevenshteinScore(match.getUsername(), searchString) >= DISTANCE).limit(MAX_RESULTS).toList();
    }

    @Override
    public List<User> fullSearchUsers(User user, String searchString) {
        List<User> matches = userRepository.findAllUsersMatching(searchString);
        matches.sort(Comparator.comparing(match -> new Levenshtein().distance(match.getUsername(), searchString)));
        return matches.stream().filter(match -> match.hasNotBlocked(user)).limit(MAX_RESULTS).toList();
    }


    private double getLevenshteinScore(String value1, String value2) {
        return 100 - ((double) LEVENSHTEIN_DISTANCE.apply(value1, value2)) / Math.max(value1.length(), value2.length()) * 100;
    }

}
