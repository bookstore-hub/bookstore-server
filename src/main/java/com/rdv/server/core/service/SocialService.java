package com.rdv.server.core.service;


import com.rdv.server.core.entity.UserConnection;
import com.rdv.server.core.entity.User;

/**
 * @author david.garcia
 */
public interface SocialService {


    void switchFollowingStatus(User user1, User user2);

    void sendFriendRequest(User userSending, User userReceiving);

    void acceptFriendRequest(User userAccepting, UserConnection connection);

    void declineFriendRequest(User userDeclining, User userDeclined, UserConnection connection);

    void removeConnection(User userRemoving, User userRemoved, UserConnection connection);

    void blockUser(UserConnection connection);

    void unblockUser(User userUnblocking, User userUnblocked, UserConnection connection);

}
