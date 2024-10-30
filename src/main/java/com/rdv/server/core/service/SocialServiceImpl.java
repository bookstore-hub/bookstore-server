package com.rdv.server.core.service;


import com.rdv.server.core.entity.User;
import com.rdv.server.core.entity.Friendship;
import com.rdv.server.core.entity.FriendshipStatus;
import com.rdv.server.core.repository.UserRepository;
import com.rdv.server.notification.service.FirebaseMessagingService;
import com.rdv.server.notification.to.Note;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * @author david.garcia
 */
@Service
public class SocialServiceImpl implements SocialService {

    protected static final Log LOGGER = LogFactory.getLog(SocialServiceImpl.class);

    private static final String FRIEND_REQUEST = "FRIEND_REQUEST";
    private static final String FRIEND_REQUEST_MESSAGE = "friend.request.message";

    private final UserRepository userRepository;
    private final FirebaseMessagingService firebaseMessagingService;
    private final MessageSource messageSource;


    public SocialServiceImpl(UserRepository userRepository, FirebaseMessagingService firebaseMessagingService, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.firebaseMessagingService = firebaseMessagingService;
        this.messageSource = messageSource;
    }


    private void saveUser(User user) {
        userRepository.save(user);
    }


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

    @Override
    public void sendFriendRequest(User userSending, User userReceiving) {
        Friendship friendship = new Friendship();
        friendship.setUser(userSending);
        friendship.setFriend(userReceiving);
        friendship.setCreationDate(OffsetDateTime.now());
        friendship.setStatus(FriendshipStatus.PENDING);
        userSending.addFriendshipRelation(friendship, userReceiving);
        saveUser(userSending);
        saveUser(userReceiving);

        sendFriendRequestNotification(userSending, userReceiving);
    }

    private void sendFriendRequestNotification(User userSending, User userReceiving) {

        String userMessagingToken = userReceiving.getMessagingToken();
        if(userMessagingToken != null) {
            Locale locale = LocaleUtils.toLocale(userReceiving.getPreferredLanguage().name());

            Map<String, String> messageData = new HashMap<>();
            messageData.put("contentType", FRIEND_REQUEST);
            Note note = new Note(messageSource.getMessage(FRIEND_REQUEST_MESSAGE, new Object[]{userSending.getUsername()}, locale), messageData);

            try {
                firebaseMessagingService.sendNotificationWithData(note, userMessagingToken);
            } catch (ExecutionException | InterruptedException e) {
                LOGGER.info("An error occurred while sending FriendRequestNotification from user " +
                        userSending.getUsername() + " to user " + userReceiving.getUsername(), e);
            }
        }
    }


    @Override
    public void declineFriendRequest(User userDeclining, User userRequesting, Friendship friendship) {
        userDeclining.declineFriendRequest(friendship, userRequesting);
        saveUser(userDeclining);
        saveUser(userRequesting);
    }

}
