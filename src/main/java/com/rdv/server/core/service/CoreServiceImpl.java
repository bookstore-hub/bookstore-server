package com.rdv.server.core.service;


import com.rdv.server.chat.entity.EventConversation;
import com.rdv.server.chat.entity.UserEventConversation;
import com.rdv.server.chat.entity.UserRoleInConversation;
import com.rdv.server.chat.service.MessageService;
import com.rdv.server.core.entity.*;
import com.rdv.server.core.repository.EventRepository;
import com.rdv.server.core.repository.UserRepository;
import com.rdv.server.notification.service.FirebaseMessagingService;
import com.rdv.server.notification.to.Note;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;


/**
 * @author david.garcia
 */
@Service
public class CoreServiceImpl implements CoreService {

    protected static final Log LOGGER = LogFactory.getLog(CoreServiceImpl.class);

    private static final String EVENT_INVITATION = "EVENT_INVITATION";
    private static final String EVENT_INVITATION_MESSAGE = "event.invitation";
    private static final String EVENT_INVITATION_ACCEPTED = "EVENT_INVITATION_ACCEPTED";
    private static final String EVENT_INVITATION_ACCEPTED_MESSAGE = "event.invitation.accepted";

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final MessageService messageService;
    private final FirebaseMessagingService firebaseMessagingService;
    private final MessageSource messageSource;


    public CoreServiceImpl(UserRepository userRepository, EventRepository eventRepository, MessageService messageService,
                           FirebaseMessagingService firebaseMessagingService, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.messageService = messageService;
        this.firebaseMessagingService = firebaseMessagingService;
        this.messageSource = messageSource;
    }


    private void saveUserChanges(User user) {
        userRepository.save(user);
    }


    @Override
    public void addEvent(User user, Event event) {
        UserEventOwner ownedEvent = new UserEventOwner();
        ownedEvent.setUser(user);
        ownedEvent.setEvent(event);
        ownedEvent.setStatus(UserEventOwnerStatus.CREATOR);
        ownedEvent.setCreationDate(OffsetDateTime.now());

        user.getOwnedEvents().add(ownedEvent);
        user.addEventInterest(event);
        saveUserChanges(user);
    }

    @Override
    public void removeEvent(User user, UserEventOwner ownedEvent) {
        user.removeEvent(ownedEvent);
        saveUserChanges(user);
    }

    @Override
    public void inviteFriendsToEvent(Event event, User userInviting, List<User> usersToInvite) {

        for(User userInvited : usersToInvite) {
            UserEventInvitation invitation = new UserEventInvitation(userInvited, userInviting, event,
                    UserEventInvitationStatus.PENDING, OffsetDateTime.now());

            userInviting.addInvitationSent(invitation);
            userInvited.addInvitationReceived(invitation);
            saveUserChanges(userInvited);
        }

        saveUserChanges(userInviting);

        sendInvitationNotifications(event, userInviting, usersToInvite);
    }

    private void sendInvitationNotifications(Event event, User userInviting, List<User> usersToNotify) {

        if(usersToNotify != null && !usersToNotify.isEmpty()) {
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
                        LOGGER.info("An error occurred while sending notification ", e);
                    }
                }
            }
        }
    }

    @Override
    public void acceptEventInvitation(User userInvited, Event event, UserEventInvitation invitation) {
        invitation.setStatus(UserEventInvitationStatus.ACCEPTED);
        userInvited.addEventInterest(event);

        User userInviting = invitation.getUserInviting();
        Optional<EventConversation> eventConversation = retrieveConversationIfExists(event, userInviting);
        if(eventConversation.isEmpty()) {
            eventConversation = Optional.of(new EventConversation(event, OffsetDateTime.now()));
            messageService.addUserToConversation(userInviting, eventConversation.get(), UserRoleInConversation.MODERATOR);
        }

        messageService.addUserToConversation(userInvited, eventConversation.get(), UserRoleInConversation.REGULAR);

        sendInvitationAcceptedNotification(userInvited, userInviting, event);
    }

    private Optional<EventConversation> retrieveConversationIfExists(Event event, User userInviting) {
        return userInviting.getUserEventConversations().stream()
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
                LOGGER.info("An error occurred while sending notification ", e);
            }
        }
    }

}
