package com.rdv.server.core.controller;

import com.rdv.server.core.entity.*;
import com.rdv.server.core.repository.EventRepository;
import com.rdv.server.core.repository.UserRepository;
import com.rdv.server.core.service.EventService;
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
@RequestMapping("/event")
@Tag(name = "EventController", description = "Set of endpoints to handle the RDV event logic")
public class EventController {

    protected static final Log LOGGER = LogFactory.getLog(EventController.class);

    private final EventService eventService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventController(EventService eventService, EventRepository eventRepository, UserRepository userRepository) {
        this.eventService = eventService;
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
    @PostMapping(value = "/addEvent")
    public boolean addEvent(@Parameter(description = "The user id") @RequestParam Long userId,
                            @Parameter(description = "The event data") @RequestBody EventTo.CreationOrUpdate eventData) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            Event newEvent = EventTo.mapNewEvent(eventData);
            eventService.addEvent(user.get(), newEvent);
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
    @PutMapping(value = "/removeEvent")
    public boolean removeEvent(@Parameter(description = "The user id") @RequestParam Long userId,
                               @Parameter(description = "The event data") @RequestParam Long eventId) {
        boolean removed = false;
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventRepository.findById(eventId);

        if(user.isPresent() && event.isPresent()) {
            Optional<UserEventOwner> createdEvent = user.get().retrieveCreatedEvent(event.get());
            if(createdEvent.isPresent()) {
                eventService.removeEvent(user.get(), createdEvent.get());
                removed = true;
            }
        }

        return removed;
    }

    /**
     * Takes ownership of an event
     *
     * @param userId      the user id
     * @param eventId     the event id
     */
    @Operation(description = "Takes ownership of an event")
    @PutMapping(value = "/takeOwnership")
    public boolean takeOwnershipOfEvent(@Parameter(description = "The user id") @RequestParam Long userId,
                                        @Parameter(description = "The event id") @RequestParam Long eventId) {
        boolean owned = false;
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventRepository.findById(eventId);

        if(user.isPresent() && event.isPresent()) {
            eventService.takeOwnershipOfEvent(user.get(), event.get());
            owned = true;
        }

        return owned;
    }

    /**
     * Invites friends to an event
     *
     * @param eventId     the event id
     * @param userId      the user inviting id
     * @param usersIds    the users to invite ids
     */
    @Operation(description = "Invites friends to an event")
    @PutMapping(value = "/inviteFriends")
    public boolean inviteFriendsToEvent(@Parameter(description = "The event id") @RequestParam Long eventId,
                                        @Parameter(description = "The user inviting id") @RequestParam Long userId,
                                        @Parameter(description = "The users to invite ids") @RequestBody List<Long> usersIds) {
        boolean invited = false;
        Optional<Event> event = eventRepository.findById(eventId);
        Optional<User> userInviting = userRepository.findById(userId);
        List<User> usersToInvite = retrieveUsers(usersIds);

        if(event.isPresent() && userInviting.isPresent() && !usersToInvite.isEmpty()) {
            eventService.inviteFriendsToEvent(event.get(), userInviting.get(), usersToInvite);
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
    @PutMapping(value = "/acceptInvitation")
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
                eventService.acceptEventInvitation(user.get(), event.get(), invitation.get());
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
    @PutMapping(value = "/declineInvitation")
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
                eventService.declineEventInvitation(invitation.get());
                declined = true;
            }
        }

        return declined;
    }

    /**
     * Shows interest in an event
     *
     * @param userId      the user id
     * @param eventId     the event id
     */
    @Operation(description = "Shows interest in an event")
    @PutMapping(value = "/showInterest")
    public boolean showInterestInEvent(@Parameter(description = "The user id") @RequestParam Long userId,
                                       @Parameter(description = "The event id") @RequestParam Long eventId) {
        boolean registered = false;
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventRepository.findById(eventId);

        if(user.isPresent() && event.isPresent()) {
            eventService.showInterestInEvent(user.get(), event.get());
            registered = true;
        }

        return registered;
    }

    /**
     * Removes interest in an event
     *
     * @param userId      the user id
     * @param eventId     the event id
     */
    @Operation(description = "Removes interest in an event")
    @PutMapping(value = "/removeInterest")
    public boolean removeInterestInEvent(@Parameter(description = "The user id") @RequestParam Long userId,
                                         @Parameter(description = "The event id") @RequestParam Long eventId) {
        boolean registered = false;
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventRepository.findById(eventId);

        if(user.isPresent() && event.isPresent()) {
            eventService.removeInterestInEvent(user.get(), event.get());
            registered = true;
        }

        return registered;
    }

    /**
     * Cancels an event
     *
     * @param userId      the user id
     * @param eventId     the event id
     */
    @Operation(description = "Cancels an event")
    @PutMapping(value = "/cancelEvent")
    public boolean cancelEvent(@Parameter(description = "The user id") @RequestParam Long userId,
                               @Parameter(description = "The event id") @RequestParam Long eventId) {
        boolean cancelled = false;
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventRepository.findById(eventId);

        if(user.isPresent() && event.isPresent() && user.get().ownsEvent(event.get())) {
            eventService.cancelEvent(user.get(), event.get());
            cancelled = true;
        }

        return cancelled;
    }


    //editEvent()
    //retrieveEventDetails()
    //retrieveCategories()

    //+ tard: ne pas reprendre les events déjà en db, juste le statut si ils ont été annulés

    //events jour multiples: partage des jours ? (jour 1, jour 2...)

}
