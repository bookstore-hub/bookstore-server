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
@Tag(name = "CoreController", description = "Set of endpoints to handle the RDV core logic")
public class CoreController {

    protected static final Log LOGGER = LogFactory.getLog(CoreController.class);

    //addEvent()
    //removeEvent()
    //ownEvent()
    //editEvent()
    //retrieveEventDetails()
    //retrieveCategories()
    //selectEvent()
    //dropEvent()
    //followUser()
    //unfollowUser()
    //retrieveFollowedUsers()
    //addFriend()
    //removeFriend()
    //blockFriend()
    //unblockFriend()
    //retrieveFriends()
    //retrieveBlockedFriends()
    //search
    //createConversation (chat)
    //closeConversation (chat)
    //addFriendToConversation (chat)
    //removeFriendFromConversation (chat)
    //retrievePersonalUserInfo
    //retrieveOtherUserInfo

}
