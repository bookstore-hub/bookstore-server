package com.rdv.server.core.service;


import com.rdv.server.core.entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;



/**
 * @author david.garcia
 */
@Service
public class SocialServiceImpl implements SocialService {

    protected static final Log LOGGER = LogFactory.getLog(SocialServiceImpl.class);


    @Override
    public void switchFollowingStatus(User user1, User user2) {
        boolean following = user1.getFollowers().stream().anyMatch(follower -> follower.equals(user2));
        if(following) {
            LOGGER.info("User " + user1.getUsername() + " removing user " + user2 + " from his following list.");
            user1.stopFollowing(user2);
        } else {
            LOGGER.info("User " + user1.getUsername() + " adding user " + user2 + " to his following list.");
            user1.startFollowing(user2);
        }
    }

}
