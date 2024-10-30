package com.rdv.server.core.controller;

import com.rdv.server.core.entity.Friendship;
import com.rdv.server.core.entity.User;
import com.rdv.server.core.repository.UserRepository;
import com.rdv.server.core.service.SocialService;
import com.rdv.server.core.to.UserTo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author davidgarcia
 */

@RestController
@CrossOrigin
@RequestMapping("/social")
@Tag(name = "SocialController", description = "Set of endpoints to handle the RDV social logic")
public class SocialController {

    protected static final Log LOGGER = LogFactory.getLog(SocialController.class);

    private final SocialService socialService;
    private final UserRepository userRepository;


    public SocialController(SocialService socialService, UserRepository userRepository) {
        this.socialService = socialService;
        this.userRepository = userRepository;
    }



    /**
     * Switches the following status of a user towards another
     *
     * @param userId1      the user taking action
     * @param userId2      the user subject to action
     */
    @Operation(description = "Switches the following status of a user towards another")
    @PutMapping(value = "/switchFollowingStatus")
    public boolean switchFollowingStatus(@Parameter(description = "The user taking action id") @RequestParam Long userId1,
                                         @Parameter(description = "The user subject to action id") @RequestParam Long userId2) {
        boolean switched = false;
        Optional<User> user1 = userRepository.findById(userId1);
        Optional<User> user2 = userRepository.findById(userId2);

        if(user1.isPresent() && user2.isPresent()) {
            LOGGER.info("Switch on following status between user " + user1.get().getUsername() + " and user " + user2.get().getUsername());
            socialService.switchFollowingStatus(user1.get(), user2.get());
            switched = true;
        }

        return switched;
    }


    /**
     * Retrieves a user's followers
     *
     * @param userId      the user id
     */
    @Operation(description = "Retrieves a user's followers")
    @GetMapping(value = "/retrieveFollowers")
    public List<UserTo.MinimalData> retrieveFollowers(@Parameter(description = "The user id") @RequestParam Long userId) {
        List<UserTo.MinimalData> followers = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            LOGGER.info("Retrieving followers of user " + user.get().getUsername());
            followers = user.get().getFollowers().stream().map(UserTo.MinimalData::new).toList();
        }

        return followers;
    }

    /**
     * Retrieves a user's followed users
     *
     * @param userId      the user id
     */
    @Operation(description = "Retrieves a user's followed users")
    @GetMapping(value = "/retrieveFollowedUsers")
    public List<UserTo.MinimalData> retrieveFollowedUsers(@Parameter(description = "The user id") @RequestParam Long userId) {
        List<UserTo.MinimalData> followers = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            LOGGER.info("Retrieving users followed by user " + user.get().getUsername());
            followers = user.get().getUsersFollowed().stream().map(UserTo.MinimalData::new).toList();
        }

        return followers;
    }

    /**
     * Sends a friend request
     *
     * @param userSendingId      the user sending
     * @param userReceivingId      the user receiving
     */
    @Operation(description = "Sends a friend request")
    @PutMapping(value = "/sendFriendRequest")
    public boolean sendFriendRequest(@Parameter(description = "The user sending id") @RequestParam Long userSendingId,
                                     @Parameter(description = "The user receiving id") @RequestParam Long userReceivingId) {
        boolean added = false;
        Optional<User> userSending = userRepository.findById(userSendingId);
        Optional<User> userReceiving = userRepository.findById(userReceivingId);

        if(userSending.isPresent() && userReceiving.isPresent()) {
            LOGGER.info("User " + userSending.get().getUsername() + " sending friend request to user " + userReceiving.get().getUsername() + ".");
            socialService.sendFriendRequest(userSending.get(), userReceiving.get());
            added = true;
        }

        return added;
    }

    /**
     * Accepts a friend request
     *
     * @param userAcceptingId      the user accepting
     * @param userRequestingId     the user requesting
     */
    @Operation(description = "Accepts a friend request")
    @PutMapping(value = "/acceptFriendRequest")
    public boolean acceptFriendRequest(@Parameter(description = "The user accepting id") @RequestParam Long userAcceptingId,
                                       @Parameter(description = "The user requesting id") @RequestParam Long userRequestingId) {
        boolean accepted = false;
        Optional<User> userAccepting = userRepository.findById(userAcceptingId);
        Optional<User> userRequesting = userRepository.findById(userRequestingId);

        if(userAccepting.isPresent() && userRequesting.isPresent()) {
            Optional<Friendship> friendRequest = userAccepting.get().getFriendRequest(userRequesting.get());
            if(friendRequest.isPresent()) {
                LOGGER.info("User " + userAccepting.get().getUsername() + " accepting friend request of user " + userRequesting.get().getUsername() + ".");
                socialService.acceptFriendRequest(userAccepting.get(), friendRequest.get());
                accepted = true;
            }
        }

        return accepted;
    }

    /**
     * Declines a friend request
     *
     * @param userDecliningId      the user declining
     * @param userDeclinedId      the user declined
     */
    @Operation(description = "Declines a friend request")
    @PutMapping(value = "/declineFriendRequest")
    public boolean declineFriendRequest(@Parameter(description = "The user declining id") @RequestParam Long userDecliningId,
                                        @Parameter(description = "The user declined id") @RequestParam Long userDeclinedId) {
        boolean declined = false;
        Optional<User> userDeclining = userRepository.findById(userDecliningId);
        Optional<User> userDeclined = userRepository.findById(userDeclinedId);

        if(userDeclining.isPresent() && userDeclined.isPresent()) {
            Optional<Friendship> friendRequest = userDeclining.get().getFriendRequest(userDeclined.get());
            if(friendRequest.isPresent()) {
                LOGGER.info("User " + userDeclining.get().getUsername() + " declining friend request of user " + userDeclined.get().getUsername() + ".");
                socialService.declineFriendRequest(userDeclining.get(), userDeclined.get(), friendRequest.get());
                declined = true;
            }
        }

        return declined;
    }

    /**
     * Removes a friend (also used to remove a friend request sent to someone)
     *
     * @param userRemovingId      the user removing
     * @param userRemovedId      the user removed
     */
    @Operation(description = "Removes a friend (also used to remove a friend request sent to someone)")
    @PutMapping(value = "/removeFriend")
    public boolean removeFriend(@Parameter(description = "The user declining id") @RequestParam Long userRemovingId,
                                @Parameter(description = "The user requesting id") @RequestParam Long userRemovedId) {
        boolean removed = false;
        Optional<User> userRemoving = userRepository.findById(userRemovingId);
        Optional<User> userRemoved = userRepository.findById(userRemovedId);

        if(userRemoving.isPresent() && userRemoved.isPresent()) {
            Optional<Friendship> friendship = userRemoving.get().getFriend(userRemoved.get());
            if(friendship.isPresent()) {
                LOGGER.info("User " + userRemoving.get().getUsername() + " removing user " + userRemoved.get().getUsername() + " as friend.");
                socialService.removeFriend(userRemoving.get(), userRemoved.get(), friendship.get());
                removed = true;
            }
        }

        return removed;
    }



    //blockFriend()
    //unblockFriend()
    //retrieveFriends()
    //retrieveBlockedFriends()

    //retrievePersonalUserInfo
    //retrieveOtherUserInfo

}
