package com.rdv.server.core.service;


import com.rdv.server.chat.entity.EventConversation;
import com.rdv.server.chat.service.MessageService;
import com.rdv.server.core.entity.*;
import com.rdv.server.core.repository.EventRepository;
import com.rdv.server.core.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;


/**
 * @author david.garcia
 */
@Service
public class CoreServiceImpl implements CoreService {

    protected static final Log LOGGER = LogFactory.getLog(CoreServiceImpl.class);

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final MessageService messageService;


    public CoreServiceImpl(UserRepository userRepository, EventRepository eventRepository, MessageService messageService) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.messageService = messageService;
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

        //todo send notifications
    }

    @Override
    public void acceptEventInvitation(User user, Event event, UserEventInvitation invitation) {
        invitation.setStatus(UserEventInvitationStatus.ACCEPTED);
        user.addEventInterest(event);
        EventConversation eventConversation = invitation.getConversation();

        if(eventConversation != null) {
            messageService.addUserToConversation(user, eventConversation);
        }
    }

}
