package com.rdv.server.chat.service;


import com.rdv.server.chat.entity.ChatMessage;
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

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author david.garcia
 */
@Service
public class MessageServiceImpl implements MessageService {

    private static final Log LOGGER = LogFactory.getLog(MessageServiceImpl.class);

    private static final String NEW_CHAT_MESSAGE = "chat.message";
    private static final String CHAT_MESSAGE = "CHAT_MESSAGE";
    private static final String PRIVATE_CHAT_MESSAGE = "CHAT_MESSAGE";
    public static final String ASSIST = "Assist";
    public static final long ASSIST_OFFICIAL_ACCOUNT_ID = 1507L;
    private static final String INITIAL_MESSAGE_1 = "assistance.initialtext1";
    private static final String INITIAL_MESSAGE_2 = "assistance.initialtext2";


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


    @Override
    public Long createConversation(EventConversation conversation, List<User> usersInvolved) {
        Long conversationId;
        for(User userInvolved : usersInvolved) {
            UserEventConversation userInConversation = new UserEventConversation(userInvolved, false);
            conversation.addUser(userInConversation);
        }
        conversationId = eventConversationRepository.save(conversation).getId();
        return conversationId;
    }

    @Override
    public void addUsersInConversation(Optional<EventConversation> conversation, List<User> usersToAdd) {
        for(User userToAdd : usersToAdd) {
            UserEventConversation userInConversation = new UserEventConversation(userToAdd, false);
            conversation.get().addUser(userInConversation);
        }
        eventConversationRepository.save(conversation.get());
    }

    @Override
    public void removeConversation(UserEventConversation userConversation) {
        userConversation.setConversationDeletedOnUserSide(true);
        userEventConversationRepository.save(userConversation);
    }

    @Override
    public ChatMessage saveChatMessage(ChatMessageTo.ChatMessageCreation messagedata, EventConversation conversation, User user) {
        LOGGER.info("Saving message from chat with conversation id : " + messagedata.conversationId());
        ChatMessage messageSaved = chatMessageRepository.save(new ChatMessage(messagedata.text(), messagedata.image(), messagedata.author(), messagedata.authorId(), messagedata.creationDate(), messagedata.conversationId()));
        if(messageSaved != null) {
            sendChatNotification(conversation, user, messageSaved);
        }
        return messageSaved;
    }


    @Override
    public List<ChatMessageTo.ChatMessageData> getLatestChatMessages(Long conversationId) {
        List<ChatMessage> latestMessages = chatMessageRepository.findMessagesByConversationId(conversationId, PageRequest.of(0, 5, Sort.Direction.DESC, "creationDate"));
        return latestMessages != null ? latestMessages.stream().map(ChatMessageTo.ChatMessageData::new).collect(Collectors.toList()) : null;
    }

    @Override
    public List<ChatMessage> getAllReceivedChatMessages(Long userId) {
        LOGGER.info("Retrieving all received private messages for user having user id : " + userId);
        List<ChatMessage> receivedMessages = new ArrayList<>();
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            List<UserEventConversation> userConversations = user.get().getUserEventConversations();
            for (UserEventConversation userConversation : userConversations) {
                List<ChatMessage> conversationMessages = chatMessageRepository.findMessagesByConversationIdAndAuthorIdNot(userConversation.getEventConversation().getId(), userId);
                receivedMessages.addAll(conversationMessages);
            }
        }

        return receivedMessages;
    }

    @Override
    public void sendChatNotification(EventConversation conversation, User user, ChatMessage newMessage) {
        List<User> usersToNotify = conversation.getOtherParticipants(user);

        if(newMessage != null && usersToNotify != null && !usersToNotify.isEmpty()) {
            for(User userToNotify : usersToNotify) {
                String userMessagingToken = userToNotify.getMessagingToken();
                if(userMessagingToken != null) {

                    Map<String, String> newMessageData = new HashMap<>();
                    newMessageData.put("contentType", PRIVATE_CHAT_MESSAGE);
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
