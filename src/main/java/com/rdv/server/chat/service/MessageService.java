package com.rdv.server.chat.service;


import com.rdv.server.chat.entity.ChatMessage;
import com.rdv.server.chat.to.ChatMessageTo;
import com.rdv.server.chat.entity.EventConversation;
import com.rdv.server.core.entity.User;
import com.rdv.server.chat.entity.UserEventConversation;

import java.util.List;
import java.util.Optional;

/**
 * @author david.garcia
 */
public interface MessageService {

    Long createConversation(EventConversation conversation, List<User> usersInvolved);

    void addUsersInConversation(Optional<EventConversation> conversation, List<User> usersToAdd);

    void removeConversation(UserEventConversation userConversation);

    ChatMessage saveChatMessage(ChatMessageTo.ChatMessageCreation messagedata, EventConversation assistance, User user);

    List<ChatMessageTo.ChatMessageData> getLatestChatMessages(Long conversationId);

    List<ChatMessage> getAllReceivedChatMessages(Long userId);

    void sendChatNotification(EventConversation conversation, User user, ChatMessage newMessage);

}
