package com.rdv.server.core.service;


import com.rdv.server.core.entity.User;
import com.rdv.server.core.entity.UserConnection;
import com.rdv.server.core.entity.UserConnectionStatus;
import com.rdv.server.core.repository.UserConnectionRepository;
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
    private final UserConnectionRepository userConnectionRepository;
    private final FirebaseMessagingService firebaseMessagingService;
    private final MessageSource messageSource;


    public SocialServiceImpl(UserRepository userRepository, UserConnectionRepository userConnectionRepository,
                             FirebaseMessagingService firebaseMessagingService, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.userConnectionRepository = userConnectionRepository;
        this.firebaseMessagingService = firebaseMessagingService;
        this.messageSource = messageSource;
    }


    private void saveUser(User user) {
        userRepository.save(user);
    }

    private void saveUserConnection(UserConnection userConnection) {
        userConnectionRepository.save(userConnection);
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
        UserConnection connection = new UserConnection();
        connection.setUser(userSending);
        connection.setConnectedUser(userReceiving);
        connection.setCreationDate(OffsetDateTime.now());
        connection.setStatus(UserConnectionStatus.PENDING);

        userSending.addFriendRequest(userReceiving, connection);

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
    public void acceptFriendRequest(User userAccepting, UserConnection connection) {
        connection.setStatus(UserConnectionStatus.FRIEND);
        userAccepting.acceptFriendRequest(connection);

        saveUserConnection(connection);
        saveUser(userAccepting);
    }

    @Override
    public void declineFriendRequest(User userDeclining, User userDeclined, UserConnection connection) {
        userDeclining.declineFriendRequest(userDeclined, connection);
        saveUser(userDeclining);
        saveUser(userDeclined);
    }

    @Override
    public void removeConnection(User userRemoving, User userRemoved, UserConnection connection) {
        userRemoving.removeConnection(userRemoved, connection);
        saveUser(userRemoving);
        saveUser(userRemoved);
    }

    @Override
    public void blockUser(UserConnection connection) {
        connection.setStatus(UserConnectionStatus.BLOCKED);
        saveUserConnection(connection);
    }

    @Override
    public void unblockUser(User userUnblocking, User userUnblocked, UserConnection connection) {
        // In practice, unblocking a user means removing the connection with him/her.
        // The unblocked user will be able to see the profile of the user who had previously blocked him/her,
        // but they will no longer be friends or linked by any connection.
        removeConnection(userUnblocking, userUnblocked, connection);
    }

}
