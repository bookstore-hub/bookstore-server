package com.rdv.server.core.service;


import com.rdv.server.core.entity.Friendship;
import com.rdv.server.core.entity.User;
import com.rdv.server.core.entity.UserRestriction;

import java.util.List;

/**
 * @author david.garcia
 */
public interface SocialService {


    void switchFollowingStatus(User user1, User user2);

    void sendFriendRequest(User userSending, User userReceiving);

    void acceptFriendRequest(User userAccepting, Friendship friendship);

    void declineFriendRequest(User userDeclining, User userDeclined, Friendship friendship);

    void removeFriend(User userRemoving, User userRemoved, Friendship friendship);

    void blockUser(User userBlocking, User userBlocked, Friendship friendship);

    void unblockUser(User userUnblocking, User userUnblocked, UserRestriction restriction);

    List<User> retrieveFriends(User user);

    List<User> retrieveFriendRequests(User user);

}
