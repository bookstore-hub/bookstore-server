package com.rdv.server.core.service;


import com.rdv.server.core.entity.User;

/**
 * @author david.garcia
 */
public interface SocialService {


    void switchFollowingStatus(User user1, User user2);

}
