package com.rdv.server.core.controller;

import com.rdv.server.core.entity.Event;
import com.rdv.server.core.entity.User;
import com.rdv.server.core.repository.EventRepository;
import com.rdv.server.core.repository.UserRepository;
import com.rdv.server.core.service.SearchService;
import com.rdv.server.core.to.EventTo;
import com.rdv.server.core.to.UserTo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author davidgarcia
 */

@RestController
@CrossOrigin
@RequestMapping("/search")
@Tag(name = "SearchController", description = "Set of endpoints to handle the RDV search logic")
//Todo: tester. Ne semble pas optimal. Peut-être déplacer vers un autre microservice ?
public class SearchController {

    protected static final Log LOGGER = LogFactory.getLog(SearchController.class);

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final SearchService searchService;

    public SearchController(UserRepository userRepository, EventRepository eventRepository, SearchService searchService) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.searchService = searchService;
    }


    /**
     * Search events
     *
     * @param userId       the searching user id
     * @param searchData   the search data
     */
    @Operation(description = "Search events")
    @GetMapping(value = "/events")
    public List<EventTo.MinimalData> searchEvents(@Parameter(description = "The searching user id") @RequestParam Long userId,
                                                  @Parameter(description = "The search data") @RequestParam String searchData) {
        List<EventTo.MinimalData> foundData = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            LOGGER.info("Search events from string " + searchData);
            List<Event> events = searchService.searchEvents(searchData);
            foundData = events.stream().map(EventTo.MinimalData::new).toList();
        }

        return foundData;
    }

    /**
     * Search users
     *
     * @param userId       the searching user id
     * @param searchData   the search data
     */
    @Operation(description = "Search users")
    @GetMapping(value = "/users")
    public List<UserTo.MinimalData> searchUsers(@Parameter(description = "The searching user id") @RequestParam Long userId,
                                                 @Parameter(description = "The search data") @RequestParam String searchData) {
        List<UserTo.MinimalData> foundData = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            LOGGER.info("Search users from string " + searchData);
            List<User> users = searchService.searchUsers(searchData);
            foundData = users.stream().map(UserTo.MinimalData::new).toList();
        }

        return foundData;
    }

}
