package com.rdv.server.core.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;


/**
 * @author davidgarcia
 */

@RestController
@CrossOrigin
@RequestMapping("/social")
@Tag(name = "SocialController", description = "Set of endpoints to handle the RDV social logic")
public class SocialController {

    protected static final Log LOGGER = LogFactory.getLog(SocialController.class);


    //followUser()
    //unfollowUser()
    //retrieveFollowedUsers()

    //addFriend()
    //removeFriend()
    //blockFriend()
    //unblockFriend()
    //retrieveFriends()
    //retrieveBlockedFriends()

    //retrievePersonalUserInfo
    //retrieveOtherUserInfo

}
