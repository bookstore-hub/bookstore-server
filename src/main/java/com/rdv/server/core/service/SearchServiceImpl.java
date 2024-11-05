package com.rdv.server.core.service;


import com.rdv.server.core.entity.Event;
import com.rdv.server.core.entity.Friendship;
import com.rdv.server.core.entity.FriendshipStatus;
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

import java.util.List;

import static java.util.Comparator.comparing;


/**
 * @author david.garcia
 */
@Service
public class SearchServiceImpl implements SearchService {

    protected static final Log LOGGER = LogFactory.getLog(SearchServiceImpl.class);

    private static final int MAX_RESULTS = 10;
    private static final int MAX_RESULTS_FRIENDS = 5;
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
        return matches.stream()
                .sorted(comparing(match -> new Levenshtein().distance(match.getTitle(), searchString)))
                .filter(event -> getLevenshteinScore(event.getTitle(), searchString) >= DISTANCE)
                .limit(MAX_RESULTS).toList();
    }

    @Override
    public List<Event> fullSearchEvents(String searchString) {
        List<Event> matches = eventRepository.findAllEventsMatching(searchString);
        return matches.stream()
                .sorted(comparing(match -> new Levenshtein().distance(match.getTitle(), searchString)))
                .limit(MAX_RESULTS).toList();
    }


    @Override
    public List<User> autoSearchUsers(User user, String searchString) {
        List<User> matches = userMatcher.collectPossibleMatches(searchString);
        return matches.stream()
                .sorted(comparing(match -> new Levenshtein().distance(match.getUsername(), searchString)))
                .filter(match -> !match.hasBlocked(user) && getLevenshteinScore(match.getUsername(), searchString) >= DISTANCE)
                .limit(MAX_RESULTS).toList();
    }

    @Override
    public List<User> autoSearchFriends(User user, String searchString) {
        List<User> matches = userMatcher.collectPossibleMatches(searchString);
        return matches.stream()
                .sorted(comparing(match -> new Levenshtein().distance(match.getUsername(), searchString)))
                .filter(match -> match.isFriend(user) && getLevenshteinScore(match.getUsername(), searchString) >= DISTANCE)
                .limit(MAX_RESULTS_FRIENDS).toList();
    }

    @Override
    public List<User> fullSearchUsers(User user, String searchString) {
        List<User> matches = userRepository.findAllUsersMatching(searchString);
        return matches.stream()
                .sorted(comparing(match -> new Levenshtein().distance(match.getUsername(), searchString)))
                .filter(match -> !match.hasBlocked(user))
                .limit(MAX_RESULTS).toList();
    }

    @Override
    public List<User> fullSearchFriends(User user, String searchString) {
        List<User> matches = user.getFriends().stream()
                .filter(friendship -> FriendshipStatus.CONNECTED.equals(friendship.getStatus()))
                .map(Friendship::getFriend)
                .sorted(comparing(match -> new Levenshtein().distance(match.getUsername(), searchString))).toList();
        return matches.stream().limit(MAX_RESULTS_FRIENDS).toList();
    }


    private double getLevenshteinScore(String value1, String value2) {
        return 100 - ((double) LEVENSHTEIN_DISTANCE.apply(value1, value2)) / Math.max(value1.length(), value2.length()) * 100;
    }

}
