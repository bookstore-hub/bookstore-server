package com.rdv.server.chat.repository;

import com.rdv.server.chat.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author davidgarcia
 */
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {


    /**
     * Return a list of messages by order and creation date for a particular conversation id
     *
     * @param conversationId the conversation id
     * @return the messages
     */
    List<ChatMessage> findMessagesByConversationId(@Param("conversationId") Long conversationId, Pageable pageable);

    /**
     * Return all messages for a particular conversation id and author id different than the one submitted
     *
     * @param conversationId the conversation id
     * @param authorId the author id
     * @return the messages
     */
    List<ChatMessage> findMessagesByConversationIdAndAuthorIdNot(@Param("conversationId") Long conversationId, @Param("authorId") Long authorId);

}
