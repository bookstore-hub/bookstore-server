package com.rdv.server.core.controller;

import com.rdv.server.core.entity.*;
import com.rdv.server.core.repository.EventRepository;
import com.rdv.server.core.repository.UserRepository;
import com.rdv.server.core.service.CoreService;
import com.rdv.server.core.to.EventTo;
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
@Tag(name = "CoreController", description = "Set of endpoints to handle the RDV core logic")
public class CoreController {

    protected static final Log LOGGER = LogFactory.getLog(CoreController.class);

    private final CoreService coreService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public CoreController(CoreService coreService, EventRepository eventRepository, UserRepository userRepository) {
        this.coreService = coreService;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }


    /**
     * Adds a new event
     *
     * @param userId       the id of the user adding the event
     * @param eventData    the event data
     */
    @Operation(description = "Adds a new event")
    @PostMapping(value = "/event/addEvent")
    public boolean addEvent(@Parameter(description = "The user id") @RequestParam Long userId,
                            @Parameter(description = "The event data") @RequestBody EventTo.CreationOrUpdate eventData) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            Event newEvent = EventTo.mapNewEvent(eventData);
            coreService.addEvent(user.get(), newEvent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes an event
     *
     * @param eventId    the event id
     */
    @Operation(description = "Removes an event")
    @PutMapping(value = "/event/removeEvent")
    public boolean removeEvent(@Parameter(description = "The user id") @RequestParam Long userId,
                               @Parameter(description = "The event data") @RequestParam Long eventId) {
        boolean removed = false;
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventRepository.findById(eventId);

        if(user.isPresent() && event.isPresent()) {
            Optional<UserEventOwner> ownedEvent = user.get().retrieveOwnedEvent(event.get());
            if(ownedEvent.isPresent()) {
                coreService.removeEvent(user.get(), ownedEvent.get());
                removed = true;
            }
        }

        return removed;
    }

    /**
     * Invites friends to an event
     *
     * @param eventId     the event id
     * @param userId      the user inviting id
     * @param usersIds    the users to invite ids
     */
    @Operation(description = "Invites friends to an event")
    @PutMapping(value = "/event/inviteFriends")
    public boolean inviteFriendsToEvent(@Parameter(description = "The event id") @RequestParam Long eventId,
                                        @Parameter(description = "The user inviting id") @RequestParam Long userId,
                                        @Parameter(description = "The users to invite ids") @RequestBody List<Long> usersIds) {
        boolean invited = false;
        Optional<Event> event = eventRepository.findById(eventId);
        Optional<User> userInviting = userRepository.findById(userId);
        List<User> usersToInvite = retrieveUsers(usersIds);

        if(event.isPresent() && userInviting.isPresent() && !usersToInvite.isEmpty()) {
            coreService.inviteFriendsToEvent(event.get(), userInviting.get(), usersToInvite);
            invited = true;
        }

        return invited;
    }

    private List<User> retrieveUsers(List<Long> usersIds) {
        List<User> users = new ArrayList<>();
        for (Long userId : usersIds) {
            Optional<User> user = userRepository.findById(userId);
            user.ifPresent(users::add);
        }
        return users;
    }

    /**
     * Accepts an event invitation
     *
     * @param userId   the id of the user accepting the invitation
     * @param eventId    the event id
     */
    @Operation(description = "Accepts an event invitation")
    @PutMapping(value = "/event/acceptInvitation")
    public boolean acceptEventInvitation(@Parameter(description = "The user id") @RequestBody Long userId,
                                         @Parameter(description = "The event id") @RequestParam Long eventId) {
        boolean accepted = false;
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventRepository.findById(eventId);

        if(user.isPresent() && event.isPresent()) {
            Optional<UserEventInvitation> invitation = user.get().getEventInvitationsReceived().stream()
                    .filter(userEventInvitation -> userEventInvitation.getEvent().equals(event.get()))
                    .findFirst();
            if(invitation.isPresent()) {
                coreService.acceptEventInvitation(user.get(), event.get(), invitation.get());
                accepted = true;
            }
        }

        return accepted;
    }

    /**
     * Declines an event invitation
     *
     * @param userId   the id of the user declining the invitation
     * @param eventId    the event id
     */
    @Operation(description = "Declines an event invitation")
    @PutMapping(value = "/event/declineInvitation")
    public boolean declineEventInvitation(@Parameter(description = "The user id") @RequestBody Long userId,
                                          @Parameter(description = "The event id") @RequestParam Long eventId) {
        boolean declined = false;
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventRepository.findById(eventId);

        if(user.isPresent() && event.isPresent()) {
            Optional<UserEventInvitation> invitation = user.get().getEventInvitationsReceived().stream()
                    .filter(userEventInvitation -> userEventInvitation.getEvent().equals(event.get()))
                    .findFirst();
            if(invitation.isPresent()) {
                coreService.declineEventInvitation(invitation.get());
                declined = true;
            }
        }

        return declined;
    }

    //declineInvitation()
    //addInterest() ?
    //removeInterest() ?
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
