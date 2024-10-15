package com.rdv.server.chat.to;


import com.rdv.server.chat.entity.ChatMessage;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author david.garcia
 */
public class ChatMessageTo {

    public record ChatMessageCreation(
        String text,
        String image,
        String author,
        Long authorId,
        Date creationDate,
        Long conversationId
    ) {}

    public record ChatMessageData(String id, String text, String image, String author, Long authorId, Date creationDate, Long conversationId) {

        public ChatMessageData(ChatMessage message) {
            this(message.getId(), message.getText(), message.getImage(), message.getAuthor(), message.getAuthorId(), message.getCreationDate(), message.getConversationId());
        }

    }

    public record ChatMessageForNotificationData(String id, String text, String image, String author, Long authorId, String creationDate, Long conversationId) {
        static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        public ChatMessageForNotificationData(ChatMessage message) {
            this(message.getId(), message.getText(), message.getImage(), message.getAuthor(), message.getAuthorId(), formatter.format(message.getCreationDate()), message.getConversationId());
        }

    }

}
