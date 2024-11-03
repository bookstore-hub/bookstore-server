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

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


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
     * Creates a new event
     *
     * @param userId       the id of the user adding the event
     * @param eventData    the event data
     */
    @Operation(description = "Creates a new event")
    @PostMapping(value = "/create")
    public boolean createEvent(@Parameter(description = "The user id") @RequestParam Long userId,
                            @Parameter(description = "The event data") @RequestBody EventTo.CreationOrUpdate eventData) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            LOGGER.info("Creating event " + eventData.title() + " for user " + user.get().getUsername());
            Event newEvent = EventTo.mapNewEvent(eventData);
            eventService.createEvent(user.get(), newEvent);
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
    @PutMapping(value = "/remove")
    public boolean removeEvent(@Parameter(description = "The user id") @RequestParam Long userId,
                               @Parameter(description = "The event data") @RequestParam Long eventId) {
        boolean removed = false;
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventRepository.findById(eventId);

        if(user.isPresent() && event.isPresent()) {
            Optional<UserEventOwner> createdEvent = user.get().retrieveCreatedEvent(event.get());
            if(createdEvent.isPresent()) {
                LOGGER.info("Removing event " + event.get().getTitle() + " created by user " + user.get().getUsername());
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
            LOGGER.info("Giving ownership of event " + event.get().getTitle() + " to user " + user.get().getUsername());
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
            LOGGER.info("User " + userInviting.get().getUsername() + " inviting friends " +
                    usersToInvite.stream().map(User::getUsername).collect(Collectors.joining(","))+ " to event " + event.get().getTitle());

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
                LOGGER.info("User " + user.get().getUsername() + " accepting invitation to event " + event.get().getTitle());
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
                LOGGER.info("User " + user.get().getUsername() + " declining invitation to event " + event.get().getTitle());
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
            LOGGER.info("User " + user.get().getUsername() + " showing interest to event " + event.get().getTitle());
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
            LOGGER.info("User " + user.get().getUsername() + " removing interest to event " + event.get().getTitle());
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
    @PutMapping(value = "/cancel")
    public boolean cancelEvent(@Parameter(description = "The user id") @RequestParam Long userId,
                               @Parameter(description = "The event id") @RequestParam Long eventId) {
        boolean cancelled = false;
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventRepository.findById(eventId);

        if(user.isPresent() && event.isPresent() && user.get().ownsEvent(event.get())) {
            LOGGER.info("User " + user.get().getUsername() + " canceling event " + event.get().getTitle());
            eventService.cancelEvent(user.get(), event.get());
            cancelled = true;
        }

        return cancelled;
    }

    /**
     * Indicates an event as sold out
     *
     * @param userId      the user id
     * @param eventId     the event id
     */
    @Operation(description = "Indicates an event as sold out")
    @PutMapping(value = "/indicateSoldOut")
    public boolean indicateSoldOut(@Parameter(description = "The user id") @RequestParam Long userId,
                                   @Parameter(description = "The event id") @RequestParam Long eventId) {
        boolean marked = false;
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventRepository.findById(eventId);

        if(user.isPresent() && event.isPresent() && user.get().ownsEvent(event.get())) {
            LOGGER.info("User " + user.get().getUsername() + " indicating event " + event.get().getTitle() + " as sold out.");
            eventService.indicateSoldOut(event.get());
            marked = true;
        }

        return marked;
    }

    /**
     * Edits an event
     *
     * @param userId      the user id
     * @param eventId     the event id
     */
    @Operation(description = "Edits an event")
    @PutMapping(value = "/edit")
    public boolean editEvent(@Parameter(description = "The user id") @RequestParam Long userId,
                             @Parameter(description = "The event id") @RequestParam Long eventId,
                             @Parameter(description = "The event id") @RequestParam EventTo.CreationOrUpdate eventData) {
        boolean edited = false;
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventRepository.findById(eventId);

        if(user.isPresent() && event.isPresent() && user.get().ownsEvent(event.get())) {
            LOGGER.info("User " + user.get().getUsername() + " accepting invitation to event " + event.get().getTitle());

            EventTo.mapUpdatedEvent(event.get(), eventData);
            eventService.editEvent(event.get());
            edited = true;
        }

        return edited;
    }

    /**
     * Retrieve all events for a specific date
     *
     * @param userId      the user id
     * @param date        the date
     * @param category    the category (optional)
     */
    @Operation(description = "Retrieve all events for a specific date")
    @GetMapping(value = "/retrieveAll")
    public List<EventTo.ListingData> retrieveAllEvents(@Parameter(description = "The user id") @RequestParam Long userId,
                                                       @Parameter(description = "The date") @RequestParam LocalDate date,
                                                       @Parameter(description = "The category") @RequestParam(required = false) EventCategory category) {
        List<EventTo.ListingData> eventsForDate = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            LOGGER.info("Retrieving events to display on date " + date + " for category " + category);
            List<Event> events = eventService.retrieveEvents(date, category);
            eventsForDate = events.stream().sorted(Comparator.comparing(Event::getStartDate))
                    .map(event -> new EventTo.ListingData(event, user.get().getPreferredLanguage()))
                    .toList();
        }

        return eventsForDate;
    }

    /**
     * Retrieve an event details
     *
     * @param userId      the user id
     * @param eventId     the event id
     */
    @Operation(description = "Retrieve an event details")
    @GetMapping(value = "/retrieveDetails")
    public EventTo.FullData retrieveEventDetails(@Parameter(description = "The user id") @RequestParam Long userId,
                                                 @Parameter(description = "The event id") @RequestParam Long eventId) {
        EventTo.FullData eventDetails = null;
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventRepository.findById(eventId);

        if(user.isPresent() && event.isPresent()) {
            LOGGER.info("Retrieving details of event " + event.get().getTitle());
            eventDetails = new EventTo.FullData(event.get(), user.get().getPreferredLanguage(), user.get().ownsEvent(event.get()));
        }

        return eventDetails;
    }

    /**
     * Retrieve all event categories
     */
    @Operation(description = "Retrieve all event categories")
    @GetMapping(value = "/retrieveCategories")
    public List<EventCategory> retrieveEventCategories() {
        return Arrays.asList(EventCategory.values());
    }


    //Note pour + tard: ne pas reprendre les events déjà en db, juste le statut si ils ont été annulés

}
