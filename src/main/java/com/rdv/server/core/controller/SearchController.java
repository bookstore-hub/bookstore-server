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


//Todo: Voir si possible de mettre à jour le cache (auto-complete). Peut-être déplacer vers un autre microservice ?

/**
 * @author davidgarcia
 */

@RestController
@CrossOrigin
@RequestMapping("/search")
@Tag(name = "SearchController", description = "Set of endpoints to handle the RDV search logic")
public class SearchController {

    protected static final Log LOGGER = LogFactory.getLog(SearchController.class);

    private final UserRepository userRepository;
    private final SearchService searchService;

    public SearchController(UserRepository userRepository, SearchService searchService) {
        this.userRepository = userRepository;
        this.searchService = searchService;
    }



    /******************************************************** EVENTS ***********************************************************/


    /**
     * Search events (auto-complete)
     *
     * @param userId       the searching user id
     * @param searchString   the search data
     */
    @Operation(description = "Search events (auto-complete)")
    @GetMapping(value = "/events/auto")
    public List<EventTo.MinimalData> autoSearchEvents(@Parameter(description = "The searching user id") @RequestParam Long userId,
                                                      @Parameter(description = "The search string") @RequestParam String searchString) {
        List<EventTo.MinimalData> foundData = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            LOGGER.info("Search events from partial string " + searchString);
            List<Event> events = searchService.autoSearchEvents(searchString);
            foundData = events.stream().map(EventTo.MinimalData::new).toList();
        }

        return foundData;
    }

    /**
     * Search events (full string)
     *
     * @param userId       the searching user id
     * @param searchString   the search data
     */
    @Operation(description = "Search events (full)")
    @GetMapping(value = "/events/full")
    public List<EventTo.MinimalData> fullSearchEvents(@Parameter(description = "The searching user id") @RequestParam Long userId,
                                                      @Parameter(description = "The search data") @RequestParam String searchString) {
        List<EventTo.MinimalData> foundData = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            LOGGER.info("Search events from full string " + searchString);
            List<Event> events = searchService.fullSearchEvents(searchString);
            foundData = events.stream().map(EventTo.MinimalData::new).toList();
        }

        return foundData;
    }


    /******************************************************** USERS ************************************************************/


    /**
     * Search users (auto-complete)
     *
     * @param userId       the searching user id
     * @param searchString   the search data
     */
    @Operation(description = "Search users (auto-complete)")
    @GetMapping(value = "/users/auto")
    public List<UserTo.MinimalData> autoSearchUsers(@Parameter(description = "The searching user id") @RequestParam Long userId,
                                                    @Parameter(description = "The search data") @RequestParam String searchString) {
        List<UserTo.MinimalData> foundData = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            LOGGER.info("Search users from partial string " + searchString);
            List<User> users = searchService.autoSearchUsers(searchString);
            foundData = users.stream().map(UserTo.MinimalData::new).toList();
        }

        return foundData;
    }

    /**
     * Search users (full string)
     *
     * @param userId       the searching user id
     * @param searchString   the search data
     */
    @Operation(description = "Search users (full string)")
    @GetMapping(value = "/users/full")
    public List<UserTo.MinimalData> fullSearchUsers(@Parameter(description = "The searching user id") @RequestParam Long userId,
                                                    @Parameter(description = "The search data") @RequestParam String searchString) {
        List<UserTo.MinimalData> foundData = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            LOGGER.info("Search users from full string " + searchString);
            List<User> users = searchService.fullSearchUsers(searchString);
            foundData = users.stream().map(UserTo.MinimalData::new).toList();
        }

        return foundData;
    }

}
