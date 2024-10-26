package com.rdv.server.chat.service;


import com.rdv.server.chat.entity.ChatMessage;
import com.rdv.server.chat.entity.UserRoleInConversation;
import com.rdv.server.chat.repository.ChatMessageRepository;
import com.rdv.server.chat.repository.EventConversationRepository;
import com.rdv.server.chat.repository.UserEventConversationRepository;
import com.rdv.server.core.repository.UserRepository;
import com.rdv.server.core.to.UserTo;
import com.rdv.server.notification.service.FirebaseMessagingService;
import com.rdv.server.notification.to.Note;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdv.server.chat.entity.EventConversation;
import com.rdv.server.chat.entity.UserEventConversation;
import com.rdv.server.chat.to.ChatMessageTo;
import com.rdv.server.core.entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author david.garcia
 */
@Service
public class MessageServiceImpl implements MessageService {

    private static final Log LOGGER = LogFactory.getLog(MessageServiceImpl.class);

    private static final String CHAT_MESSAGE = "CHAT_MESSAGE";
    public static final long RDV_OFFICIAL_ACCOUNT_ID = 0L;


    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final EventConversationRepository eventConversationRepository;
    private final UserEventConversationRepository userEventConversationRepository;
    private final FirebaseMessagingService firebaseMessagingService;

    public MessageServiceImpl(ChatMessageRepository chatMessageRepository, UserRepository userRepository,
                              EventConversationRepository eventConversationRepository, UserEventConversationRepository userEventConversationRepository,
                              FirebaseMessagingService firebaseMessagingService) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.eventConversationRepository = eventConversationRepository;
        this.userEventConversationRepository = userEventConversationRepository;
        this.firebaseMessagingService = firebaseMessagingService;
    }


    private void saveUser(User user) {
        userRepository.save(user);
    }

    private void saveEventConversation(EventConversation eventConversation) {
        eventConversationRepository.save(eventConversation);
    }


    @Override
    public void endConversation(EventConversation conversation) {
        conversation.setEndDate(OffsetDateTime.now());
        saveEventConversation(conversation);
    }

    @Override
    public void addUserToConversation(User userToAdd, EventConversation conversation, UserRoleInConversation userRoleInConversation) {
        UserEventConversation participationInConversation = new UserEventConversation(userToAdd, false);
        participationInConversation.setUserRoleInConversation(userRoleInConversation);
        conversation.addUser(participationInConversation);
        saveEventConversation(conversation);

        userToAdd.addParticipationToConversation(participationInConversation);
        saveUser(userToAdd);
    }

    @Override
    public void removeUsersFromConversation(EventConversation conversation, List<User> usersToRemove) {
        for(User userToRemove : usersToRemove) {
            Optional<UserEventConversation> participationInConversation = conversation.getUsersInvolved().stream().filter(userInvolved -> userInvolved.getUser().equals(userToRemove)).findFirst();
            participationInConversation.ifPresent(conversation::removeUser);
        }
        saveEventConversation(conversation);
    }

    @Override
    public void removeConversationOnUserSide(UserEventConversation userInConversation) {
        userInConversation.setConversationDeletedOnUserSide(true);
        userEventConversationRepository.save(userInConversation);
    }

    @Override
    public ChatMessage saveChatMessage(ChatMessageTo.ChatMessageCreation messagedata, EventConversation conversation, User user) {
        LOGGER.info("Saving message from chat with conversation id : " + messagedata.conversationId());
        ChatMessage messageSaved = chatMessageRepository.save(new ChatMessage(messagedata.text(), messagedata.image(), messagedata.author(), messagedata.authorId(), messagedata.creationDate(), messagedata.conversationId()));
        sendChatNotification(conversation, user, messageSaved);
        return messageSaved;
    }


    @Override
    public List<ChatMessage> getLatestChatMessages(Long conversationId) {
        return chatMessageRepository.findMessagesByConversationId(conversationId, PageRequest.of(0, 5, Sort.Direction.DESC, "creationDate"));
    }

    @Override
    public List<ChatMessage> getAllReceivedChatMessages(Long userId) {
        LOGGER.info("Retrieving all received private messages for user having user id : " + userId);
        List<ChatMessage> receivedMessages = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            List<UserEventConversation> participationInConversations = user.get().getParticipationInConversations();
            for (UserEventConversation participationInConversation : participationInConversations) {
                List<ChatMessage> conversationMessages = chatMessageRepository.findMessagesByConversationIdAndAuthorIdNot(participationInConversation.getEventConversation().getId(), userId);
                receivedMessages.addAll(conversationMessages);
            }
        }

        return receivedMessages;
    }

    private void sendChatNotification(EventConversation conversation, User user, ChatMessage newMessage) {
        List<User> usersToNotify = conversation.getOtherParticipants(user);

        if(newMessage != null && usersToNotify != null && !usersToNotify.isEmpty()) {
            for(User userToNotify : usersToNotify) {
                String userMessagingToken = userToNotify.getMessagingToken();
                if(userMessagingToken != null) {

                    Map<String, String> newMessageData = new HashMap<>();
                    newMessageData.put("contentType", CHAT_MESSAGE);
                    newMessageData.put("message", writeAsJsonString(new ChatMessageTo.ChatMessageForNotificationData(newMessage)));
                    newMessageData.put("participants", writeAsJsonString(conversation.getUsersInvolved().stream().map(userInConversation -> new UserTo.MinimalData(userInConversation.getUser())).collect(Collectors.toList())));

                    Note note = new Note(user.getFirstName(), newMessage.getText(), newMessageData);

                    try {
                        firebaseMessagingService.sendChatMessageNotification(note, conversation.getId(), userMessagingToken);
                    } catch (ExecutionException | InterruptedException e) {
                        LOGGER.info("An error occurred while sending notification ", e);
                    }
                }
            }
        }
    }



    /*************** UTILS ***************/


    private String writeAsJsonString(Object object) {
        String participantsAsJsonString = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            participantsAsJsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch(Exception e) {
            LOGGER.info("An error occurred while converting the object to a JSON String ", e);
        }
        return participantsAsJsonString;
    }

}
