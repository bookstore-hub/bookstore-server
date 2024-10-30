package com.rdv.server.core.controller;

import com.rdv.server.core.entity.Event;
import com.rdv.server.core.entity.User;
import com.rdv.server.core.repository.UserRepository;
import com.rdv.server.core.service.SocialService;
import com.rdv.server.core.to.EventTo;
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


    //addFriend()
    //removeFriend()
    //blockFriend()
    //unblockFriend()
    //retrieveFriends()
    //retrieveBlockedFriends()

    //retrievePersonalUserInfo
    //retrieveOtherUserInfo

}
