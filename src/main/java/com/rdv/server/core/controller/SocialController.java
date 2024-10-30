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
     * Declines a friend request
     *
     * @param userDecliningId      the user declining
     * @param userRequestingId      the user requesting
     */
    @Operation(description = "Declines a friend request")
    @PutMapping(value = "/declineFriendRequest")
    public boolean declineFriendRequest(@Parameter(description = "The user declining id") @RequestParam Long userDecliningId,
                                        @Parameter(description = "The user requesting id") @RequestParam Long userRequestingId) {
        boolean declined = false;
        Optional<User> userDeclining = userRepository.findById(userDecliningId);
        Optional<User> userRequesting = userRepository.findById(userRequestingId);

        if(userDeclining.isPresent() && userRequesting.isPresent()) {
            Optional<Friendship> friendRequest = userDeclining.get().getFriendRequest(userRequesting.get());
            if(friendRequest.isPresent()) {
                LOGGER.info("User " + userDeclining.get().getUsername() + " declining friend request of user " + userRequesting.get().getUsername() + ".");
                socialService.declineFriendRequest(userDeclining.get(), userRequesting.get(), friendRequest.get());
                declined = true;
            }
        }

        return declined;
    }


    //removeFriend() -> almost like decline
    //blockFriend()
    //unblockFriend()
    //retrieveFriends()
    //retrieveBlockedFriends()

    //retrievePersonalUserInfo
    //retrieveOtherUserInfo

}
