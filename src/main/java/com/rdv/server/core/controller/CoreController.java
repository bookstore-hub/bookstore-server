package com.rdv.server.core.controller;

import com.rdv.server.core.entity.Event;
import com.rdv.server.core.service.CoreService;
import com.rdv.server.core.to.EventTo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;


/**
 * @author davidgarcia
 */

@RestController
@CrossOrigin
@Tag(name = "CoreController", description = "Set of endpoints to handle the RDV core logic")
public class CoreController {

    protected static final Log LOGGER = LogFactory.getLog(CoreController.class);

    private final CoreService coreService;

    public CoreController(CoreService coreService) {
        this.coreService = coreService;
    }


    /**
     * Adds a new event
     *
     * @param eventData    the event data
     */
    @Operation(description = "Adds a new event")
    @PostMapping(value = "/event/addEvent")
    public boolean addEvent(@Parameter(description = "The event data") @RequestBody EventTo.CreationOrUpdate eventData) {
        Event newEvent = EventTo.mapNewEvent(eventData);
        return coreService.addEvent(newEvent) != null;
    }


    //removeEvent()
    //ownEvent()
    //editEvent()
    //retrieveEventDetails()
    //retrieveCategories()
    //selectEvent()
    //dropEvent()
    //followUser()
    //unfollowUser()
    //retrieveFollowedUsers()
    //addFriend()
    //removeFriend()
    //blockFriend()
    //unblockFriend()
    //retrieveFriends()
    //retrieveBlockedFriends()
    //search
    //retrievePersonalUserInfo
    //retrieveOtherUserInfo

}
