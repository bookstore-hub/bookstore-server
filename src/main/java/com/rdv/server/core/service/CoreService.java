package com.rdv.server.core.service;


import com.rdv.server.core.entity.Event;
import com.rdv.server.core.entity.User;
import com.rdv.server.core.entity.UserEventInvitation;
import com.rdv.server.core.entity.UserEventOwner;

import java.util.List;

/**
 * @author david.garcia
 */
public interface CoreService {

    void addEvent(User user, Event event);

    void removeEvent(User user, UserEventOwner ownedEvent);

    void takeOwnershipOfEvent(User user, Event event);

    void inviteFriendsToEvent(Event event, User userInviting, List<User> usersToInvite);

    void acceptEventInvitation(User user, Event event, UserEventInvitation invitation);

    void declineEventInvitation(UserEventInvitation userEventInvitation);

    void showInterestInEvent(User user, Event event);

    void removeInterestInEvent(User user, Event event);

}
