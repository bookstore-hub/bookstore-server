package com.rdv.server.chat.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author davidgarcia
 */
@Document(collection = "rdv_chat")
public class ChatMessage {

    @Id
    private String id;
    private String text;
    private String image;
    private String author;
    private Long authorId;
    private Date creationDate;
    private Long conversationId;

    public ChatMessage() {
    }

    public ChatMessage(String text, String image, String author, Long authorId, Date creationDate, Long conversationId) {
        this.text = text;
        this.image = image;
        this.author = author;
        this.authorId = authorId;
        this.creationDate = creationDate;
        this.conversationId = conversationId;
    }


    /**
     * Returns the id
     *
     * @return Returns the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id
     *
     * @param id The id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the text
     *
     * @return Returns the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text
     *
     * @param text The text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the image
     *
     * @return Returns the image
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the image
     *
     * @param image The image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Returns the author
     *
     * @return Returns the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author
     *
     * @param author The author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns the authorId
     *
     * @return Returns the authorId
     */
    public Long getAuthorId() {
        return authorId;
    }

    /**
     * Sets the authorId
     *
     * @param authorId The authorId to set
     */
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    /**
     * Returns the creationDate
     *
     * @return Returns the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creationDate
     *
     * @param creationDate The creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Returns the conversationId
     *
     * @return Returns the conversationId
     */
    public Long getConversationId() {
        return conversationId;
    }

    /**
     * Sets the conversationId
     *
     * @param conversationId The conversationId to set
     */
    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + '\"' +
                ",\"text\":\"" + text + '\"' +
                ",\"author\":\"" + author + '\"' +
                ",\"authorId\":\"" + authorId + '\"' +
                ",\"creationDate\":\"" + creationDate + "\"" +
                ",\"conversationId\":\"" + conversationId + "\"" +
                '}';
    }

}
