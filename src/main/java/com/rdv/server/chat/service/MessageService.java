package com.rdv.server.chat.service;


import com.rdv.server.chat.entity.ChatMessage;
import com.rdv.server.chat.to.ChatMessageTo;
import com.rdv.server.chat.entity.EventConversation;
import com.rdv.server.core.entity.Event;
import com.rdv.server.core.entity.User;
import com.rdv.server.chat.entity.UserEventConversation;

import java.util.List;

/**
 * @author david.garcia
 */
public interface MessageService {

    void createConversation(User userCreatingConversation, Event event);

    void endConversation(EventConversation conversation);

    void addUserToConversation(User userToAdd, EventConversation conversation);

    void removeUsersFromConversation(EventConversation conversation, List<User> usersToRemove);

    void removeConversation(UserEventConversation userConversation);

    ChatMessage saveChatMessage(ChatMessageTo.ChatMessageCreation messagedata, EventConversation assistance, User user);

    List<ChatMessage> getLatestChatMessages(Long conversationId);

    List<ChatMessage> getAllReceivedChatMessages(Long userId);

    void sendChatNotification(EventConversation conversation, User user, ChatMessage newMessage);

}
