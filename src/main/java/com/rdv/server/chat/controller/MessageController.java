package com.rdv.server.chat.controller;

import com.rdv.server.chat.entity.ChatMessage;
import com.rdv.server.chat.entity.EventConversation;
import com.rdv.server.chat.entity.UserEventConversation;
import com.rdv.server.chat.service.MessageService;
import com.rdv.server.chat.to.ChatMessageTo;
import com.rdv.server.core.entity.User;
import com.rdv.server.chat.repository.EventConversationRepository;
import com.rdv.server.core.repository.EventRepository;
import com.rdv.server.core.repository.UserRepository;
import com.rdv.server.core.to.UserTo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author davidgarcia
 */

@RestController
@CrossOrigin
@RequestMapping("/chat")
@Tag(name = "MessageController", description = "Set of endpoints to manage the chat messages")
public class MessageController {

    private static final Log LOGGER = LogFactory.getLog(MessageController.class);

    private final MessageService messageService;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventConversationRepository eventConversationRepository;

    public MessageController(MessageService messageService, UserRepository userRepository, EventRepository eventRepository, EventConversationRepository eventConversationRepository) {
        this.messageService = messageService;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventConversationRepository = eventConversationRepository;
    }

    /**
     * Ends a conversation
     *
     * @param conversationId    the conversation id
     */
    @Operation(description = "Ends a conversation")
    @PostMapping(value = "/endConversation")
    public boolean endConversation(@Parameter(description = "The conversation id") @RequestParam Long conversationId) {
        boolean conversationEnded = false;
        Optional<EventConversation> conversation = eventConversationRepository.findById(conversationId);

        if(conversation.isPresent()) {
            messageService.endConversation(conversation.get());
            conversationEnded = true;
        }

        return conversationEnded;
    }

    /**
     * Removes users from a conversation
     *
     * @param conversationId    the conversation id
     * @param userId      the id of the user performing the removal
     * @param usersIds    the ids of the users to remove
     */
    @Operation(description = "Removes users from a conversation")
    @PutMapping(value = "/removeUsersFromConversation")
    public boolean removeUsersFromConversation(@Parameter(description = "The conversation id") @RequestParam Long conversationId,
                                               @Parameter(description = "The user performing the removal") @RequestParam Long userId,
                                               @Parameter(description = "The users to remove from the conversation") @RequestBody List<Long> usersIds) {
        boolean usersRemoved = false;
        Optional<EventConversation> conversation = eventConversationRepository.findById(conversationId);
        Optional<User> userRemoving = userRepository.findById(userId);
        List<User> usersToRemove = retrieveUsers(usersIds);

        if(conversation.isPresent() && userRemoving.isPresent() && !usersToRemove.isEmpty()
                && userRemoving.get().hasRemovalRights(conversation.get(), usersToRemove)) {
            messageService.removeUsersFromConversation(conversation.get(), usersToRemove);
            usersRemoved = true;
        }

        return usersRemoved;
    }

    private List<User> retrieveUsers(List<Long> usersIds) {
        List<User> users = new ArrayList<>();
        for (Long userId : usersIds) {
            Optional<User> user = userRepository.findById(userId);
            user.ifPresent(users::add);
        }
        return users;
    }


    /**
     * Retrieves all conversations of a user
     *
     * @param userId    the user id
     */
    @Operation(description = "Retrieves all conversations of a user")
    @GetMapping(value = "/retrieveUserConversations")
    public List<UserTo.ConversationData> retrieveUserConversations(@Parameter(description = "The user id") @RequestParam Long userId) {

        List<UserTo.ConversationData> userConversations = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            userConversations = user.get().getParticipationInConversations().stream()
                    .filter(participation -> !participation.getConversationDeletedOnUserSide())
                    .map(participation -> new UserTo.ConversationData(participation, getLatestMessageDate(participation)))
                    .sorted(Comparator.comparing(UserTo.ConversationData::dateAndTimeOfLastUpdate).reversed()).toList();
        }

        return userConversations;
    }

    private Date getLatestMessageDate(UserEventConversation participationInConversation) {
        Date latestMessageDate = null;
        Long conversationId = participationInConversation.getEventConversation().getId();
        List<ChatMessageTo.ChatMessageData> latestConversationMessages = getChatMessages(conversationId);

        if(latestConversationMessages != null && !latestConversationMessages.isEmpty()) {
            latestMessageDate = latestConversationMessages.stream().findFirst().get().creationDate();
        }

        return latestMessageDate;
    }


    /**
     * Removes a conversation (on the user side)
     *
     * @param userId    the user id
     * @param conversationId  the conversationId
     */
    @Operation(description = "Removes a conversation (on the user side)")
    @PutMapping(value = "/removeConversation")
    public boolean removeConversation(@Parameter(description = "The user id") @RequestBody Long userId,
                                      @Parameter(description = "The conversation id") @RequestBody Long conversationId) {

        boolean conversationRemovedOnUserSide = false;
        Optional<User> user = userRepository.findById(userId);
        Optional<EventConversation> conversation = eventConversationRepository.findById(conversationId);

        if(user.isPresent() && conversation.isPresent()) {
            UserEventConversation participationInConversation = user.get().getParticipationInConversations().stream()
                    .filter(participation -> participation.getEventConversation().equals(conversation.get())).findAny().orElse(null);

            if(participationInConversation != null) {
                messageService.removeConversationOnUserSide(participationInConversation);
                conversationRemovedOnUserSide = true;
            }
        }

        return conversationRemovedOnUserSide;
    }


    /**
     * Saves a new message in a chat
     *
     * @param messageData    the message to save
     */
    @Operation(description = "Saves a new message in a chat")
    @PostMapping(value = "/chatMessage")
    @MessageMapping("/newChatMessage")
    @SendTo("/topic/newChatMessage")
    public List<ChatMessageTo.ChatMessageData> saveChatMessage(@Parameter(description = "The message to send") @RequestBody ChatMessageTo.ChatMessageCreation messageData) {
        Optional<EventConversation> conversation = eventConversationRepository.findById(messageData.conversationId());
        Optional<User> user = userRepository.findById(messageData.authorId());

        ChatMessage savedMessage = null;
        if(conversation.isPresent() && user.isPresent()) {
            savedMessage = messageService.saveChatMessage(messageData, conversation.get(), user.get());
        }

        if(savedMessage != null && savedMessage.getId() != null) {
            List<ChatMessage> latestMessages = messageService.getLatestChatMessages(savedMessage.getConversationId());
            return latestMessages.stream().map(ChatMessageTo.ChatMessageData::new).toList();
        } else {
            return null;
        }

    }


    /**
     * Retrieves messages from a chat
     *
     * @param conversationId    the conversation id related
     */
    @Operation(description = "Get conversation messages from a chat")
    @GetMapping(value = "/chatMessages")
    public List<ChatMessageTo.ChatMessageData> getChatMessages(@Parameter(description = "The conversation id") @RequestParam Long conversationId) {
        LOGGER.info("Retrieving messages for chat having conversation id : " + conversationId);
        List<ChatMessage> latestMessages = messageService.getLatestChatMessages(conversationId);
        return latestMessages.stream().map(ChatMessageTo.ChatMessageData::new).toList();
    }


    /**
     * Retrieves all received messages from all conversations
     *
     * @param userId    the user id related
     */
    @Operation(description = "Get all received chat messages from all conversations")
    @GetMapping(value = "/allReceivedMessages")
    public List<ChatMessageTo.ChatMessageData> allReceivedChatMessages(@Parameter(description = "The user id") @RequestParam Long userId) {
        LOGGER.info("Retrieving all received messages from all conversations for user with id: " + userId);
        List<ChatMessage> messages = messageService.getAllReceivedChatMessages(userId);
        return messages.stream().sorted(Comparator.comparing(ChatMessage::getCreationDate)).map(ChatMessageTo.ChatMessageData::new).toList();
    }

}
