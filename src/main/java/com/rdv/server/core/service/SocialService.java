package com.rdv.server.core.service;


import com.rdv.server.core.entity.Friendship;
import com.rdv.server.core.entity.User;

/**
 * @author david.garcia
 */
public interface SocialService {


    void switchFollowingStatus(User user1, User user2);

    void sendFriendRequest(User userSending, User userReceiving);

    void declineFriendRequest(User userDeclining, User userRequesting, Friendship friendship);

}
