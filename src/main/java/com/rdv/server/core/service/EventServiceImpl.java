package com.rdv.server.core.service;


import com.rdv.server.chat.entity.EventConversation;
import com.rdv.server.chat.entity.UserEventConversation;
import com.rdv.server.chat.entity.UserRoleInConversation;
import com.rdv.server.chat.repository.EventConversationRepository;
import com.rdv.server.chat.service.MessageService;
import com.rdv.server.core.entity.*;
import com.rdv.server.core.repository.EventRepository;
import com.rdv.server.core.repository.UserEventInvitationRepository;
import com.rdv.server.core.repository.UserRepository;
import com.rdv.server.notification.service.FirebaseMessagingService;
import com.rdv.server.notification.to.Note;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.concurrent.ExecutionException;


/**
 * @author david.garcia
 */
@Service
public class EventServiceImpl implements EventService {

    protected static final Log LOGGER = LogFactory.getLog(EventServiceImpl.class);

    private static final String EVENT_INVITATION = "EVENT_INVITATION";
    private static final String EVENT_INVITATION_MESSAGE = "event.invitation";
    private static final String EVENT_INVITATION_ACCEPTED = "EVENT_INVITATION_ACCEPTED";
    private static final String EVENT_INVITATION_ACCEPTED_MESSAGE = "event.invitation.accepted";
    private static final String EVENT_CANCELLATION = "EVENT_CANCELLATION";
    private static final String EVENT_CANCELLATION_MESSAGE = "event.cancellation";

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final UserEventInvitationRepository userEventInvitationRepository;
    private final EventConversationRepository eventConversationRepository;
    private final MessageService messageService;
    private final FirebaseMessagingService firebaseMessagingService;
    private final MessageSource messageSource;


    public EventServiceImpl(UserRepository userRepository, EventRepository eventRepository, UserEventInvitationRepository userEventInvitationRepository, EventConversationRepository eventConversationRepository,
                            MessageService messageService, FirebaseMessagingService firebaseMessagingService, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.userEventInvitationRepository = userEventInvitationRepository;
        this.eventConversationRepository = eventConversationRepository;
        this.messageService = messageService;
        this.firebaseMessagingService = firebaseMessagingService;
        this.messageSource = messageSource;
    }


    private void saveUser(User user) {
        userRepository.save(user);
    }

    private void saveEvent(Event event) {
        eventRepository.save(event);
    }

    private void saveEventInvitation(UserEventInvitation eventInvitation) {
        userEventInvitationRepository.save(eventInvitation);
    }

    private void saveEventConversation(EventConversation eventConversation) {
        eventConversationRepository.save(eventConversation);
    }


    @Override
    public void createEvent(User user, Event event) {
        UserEventOwner ownedEvent = new UserEventOwner();
        ownedEvent.setUser(user);
        ownedEvent.setEvent(event);
        ownedEvent.setStatus(UserEventOwnerStatus.PENDING_CREATOR); //todo UserEventOwnerStatus.CREATOR when validated
        ownedEvent.setCreationDate(OffsetDateTime.now());

        user.getOwnedEvents().add(ownedEvent);
        user.addEventInterest(event);
        saveUser(user);
    }

    @Override
    public void removeEvent(User user, UserEventOwner createdEvent) {
        user.removeEvent(createdEvent);
        saveUser(user);
    }

    @Override
    public void takeOwnershipOfEvent(User user, Event event) {
        UserEventOwner ownedEvent = new UserEventOwner(user, event, UserEventOwnerStatus.PENDING_ACQUIRER, OffsetDateTime.now()); //todo UserEventOwnerStatus.ACQUIRER when confirmed
        user.addOwnedEvent(ownedEvent);
        saveUser(user);
    }

    @Override
    public void inviteFriendsToEvent(Event event, User userInviting, List<User> usersToInvite) {

        for(User userInvited : usersToInvite) {
            UserEventInvitation invitation = new UserEventInvitation(userInvited, userInviting, event,
                    UserEventInvitationStatus.PENDING, OffsetDateTime.now());

            userInviting.addInvitationSent(invitation);
            userInvited.addInvitationReceived(invitation);
            saveUser(userInvited);
        }

        saveUser(userInviting);

        sendInvitationNotifications(event, userInviting, usersToInvite);
    }

    private void sendInvitationNotifications(Event event, User userInviting, List<User> usersToNotify) {

        if(!usersToNotify.isEmpty()) {
            for(User userToNotify : usersToNotify) {
                String userMessagingToken = userToNotify.getMessagingToken();
                if(userMessagingToken != null) {
                    Locale locale = LocaleUtils.toLocale(userToNotify.getPreferredLanguage().name());

                    Map<String, String> messageData = new HashMap<>();
                    messageData.put("contentType", EVENT_INVITATION);
                    Note note = new Note(messageSource.getMessage(EVENT_INVITATION_MESSAGE, new Object[]{userInviting.getUsername(), event.getTitle()}, locale), messageData);

                    try {
                        firebaseMessagingService.sendNotificationWithData(note, userMessagingToken);
                    } catch (ExecutionException | InterruptedException e) {
                        LOGGER.info("An error occurred while sending InvitationNotification to user " + userToNotify.getUsername(), e);
                    }
                }
            }
        }
    }

    @Override
    public void acceptEventInvitation(User userInvited, Event event, UserEventInvitation invitation) {
        invitation.setStatus(UserEventInvitationStatus.ACCEPTED);
        saveEventInvitation(invitation);

        User userInviting = invitation.getUserInviting();
        Optional<EventConversation> eventConversation = retrieveConversationIfExists(userInviting, event);
        if(eventConversation.isEmpty()) {
            eventConversation = Optional.of(new EventConversation(event, OffsetDateTime.now()));
            messageService.addUserToConversation(userInviting, eventConversation.get(), UserRoleInConversation.MODERATOR);
        }

        userInvited.addEventInterest(event);
        messageService.addUserToConversation(userInvited, eventConversation.get(), UserRoleInConversation.REGULAR);

        sendInvitationAcceptedNotification(userInvited, userInviting, event);
    }

    private Optional<EventConversation> retrieveConversationIfExists(User user, Event event) {
        return user.getParticipationInConversations().stream()
                .map(UserEventConversation::getEventConversation)
                .filter(eventConversation -> eventConversation.getEvent().equals(event))
                .findFirst();
    }

    private void sendInvitationAcceptedNotification(User userInvited, User userInviting, Event event) {

        String userMessagingToken = userInviting.getMessagingToken();
        if(userMessagingToken != null) {
            Locale locale = LocaleUtils.toLocale(userInviting.getPreferredLanguage().name());

            Map<String, String> messageData = new HashMap<>();
            messageData.put("contentType", EVENT_INVITATION_ACCEPTED);
            Note note = new Note(messageSource.getMessage(EVENT_INVITATION_ACCEPTED_MESSAGE, new Object[]{userInvited.getUsername(), event.getTitle()}, locale), messageData);

            try {
                firebaseMessagingService.sendNotificationWithData(note, userMessagingToken);
            } catch (ExecutionException | InterruptedException e) {
                LOGGER.info("An error occurred while sending InvitationAcceptedNotification to user " + userInviting.getUsername(), e);
            }
        }
    }

    @Override
    public void declineEventInvitation(UserEventInvitation invitation) {
        invitation.setStatus(UserEventInvitationStatus.DECLINED);
        saveEventInvitation(invitation);
    }

    @Override
    public void showInterestInEvent(User user, Event event) {
        user.addEventInterest(event);
        saveUser(user);
    }

    @Override
    public void removeInterestInEvent(User user, Event event) {
        Optional<EventConversation> eventConversation = retrieveConversationIfExists(user, event);
        if(eventConversation.isPresent()) {
            messageService.removeUsersFromConversation(eventConversation.get(), List.of(user));
            if(user.isModerator(eventConversation.get())) {
                eventConversation.get().setEndDate(OffsetDateTime.now());
                saveEventConversation(eventConversation.get());
            }
        }
        user.removeEventInterest(event);
        saveUser(user);
    }

    @Override
    public void cancelEvent(User user, Event event) {
        event.setState(EventState.CANCELLED);
        saveEvent(event);

        sendEventCancelledNotification(user, event);
    }

    @Override
    public void indicateSoldOut(Event event) {
        event.setState(EventState.SCHEDULED_SOLD_OUT);
        saveEvent(event);
    }

    private void sendEventCancelledNotification(User owner, Event event) {
        Set<User> usersToNotify = event.getUsersInterested();
        usersToNotify.remove(owner);

        if(!usersToNotify.isEmpty()) {
            for(User userToNotify : usersToNotify) {
                String userMessagingToken = userToNotify.getMessagingToken();
                if(userMessagingToken != null) {
                    Locale locale = LocaleUtils.toLocale(userToNotify.getPreferredLanguage().name());

                    Map<String, String> messageData = new HashMap<>();
                    messageData.put("contentType", EVENT_CANCELLATION);
                    Note note = new Note(messageSource.getMessage(EVENT_CANCELLATION_MESSAGE, new Object[]{event.getTitle()}, locale), messageData);

                    try {
                        firebaseMessagingService.sendNotificationWithData(note, userMessagingToken);
                    } catch (ExecutionException | InterruptedException e) {
                        LOGGER.info("An error occurred while sending EventCancelledNotification to user " + userToNotify.getUsername(), e);
                    }
                }
            }
        }
    }

    @Override
    public void editEvent(Event event) {
        saveEvent(event);
    }

    @Override
    public List<Event> retrieveEvents(LocalDate date, EventCategory category) {
        List<Event> eventsFound;

        ZoneOffset montrealStandardOffset = getZoneOffset();
        OffsetDateTime dateBeginningOfDay = date.atTime(OffsetTime.MIN).withOffsetSameLocal(montrealStandardOffset);
        OffsetDateTime dateEndOfDay = date.atTime(OffsetTime.MAX).withOffsetSameLocal(montrealStandardOffset);

        if(category == null) {
            eventsFound = eventRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(dateEndOfDay, dateBeginningOfDay);
        } else {
            eventsFound = eventRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndCategory(dateEndOfDay, dateBeginningOfDay, category);
        }

        return eventsFound;
    }

    private static ZoneOffset getZoneOffset() {
        ZoneId zoneId = ZoneId.of("America/Montreal");
        Instant now = Instant.now();
        return zoneId.getRules().getStandardOffset(now);
    }

}
