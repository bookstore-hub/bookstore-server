package com.rdv.server.core.service;


import com.rdv.server.core.entity.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author david.garcia
 */
public interface EventService {

    void createEvent(User user, Event event);

    void removeEvent(User user, UserEventOwner ownedEvent);

    void takeOwnershipOfEvent(User user, Event event);

    void inviteFriendsToEvent(Event event, User userInviting, List<User> usersToInvite);

    void acceptEventInvitation(User user, Event event, UserEventInvitation invitation);

    void declineEventInvitation(UserEventInvitation userEventInvitation);

    void showInterestInEvent(User user, Event event);

    void removeInterestInEvent(User user, Event event);

    void cancelEvent(User user, Event event);

    void editEvent(Event event);

    List<Event> retrieveEvents(LocalDate date, EventCategory category);

}
