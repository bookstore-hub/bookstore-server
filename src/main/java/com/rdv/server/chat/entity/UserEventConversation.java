package com.rdv.server.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rdv.server.core.entity.User;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

/**
 * @author davidgarcia
 */
@Entity
@Table(name = "user_event_conversation", schema="rdv")
public class UserEventConversation {

    @Id
    @GeneratedValue(generator = "user_event_conversation_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_event_conversation_sequence", sequenceName = "rdv.user_event_conversation_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private Event event;*/

    @Column(name = "user_role_in_conversation")
    private UserRoleInConversation userRoleInConversation;

    @Column(name = "user_joining_date")
    private OffsetDateTime userJoiningDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_conversation_id")
    @JsonIgnore
    private EventConversation eventConversation;

    @Column(name = "deleted_on_user_side")
    private Boolean conversationDeletedOnUserSide;


    public UserEventConversation() {
    }
    public UserEventConversation(User user, Boolean conversationDeletedOnUserSide) {
        this.user = user;
        this.conversationDeletedOnUserSide = conversationDeletedOnUserSide;
    }

    /**
     * Returns the id
     *
     * @return Returns the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id
     *
     * @param id The id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the user
     *
     * @return Returns the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user
     *
     * @param user The user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the userRoleInConversation
     *
     * @return Returns the userRoleInConversation
     */
    public UserRoleInConversation getUserRoleInConversation() {
        return userRoleInConversation;
    }

    /**
     * Sets the userRoleInConversation
     *
     * @param userRoleInConversation The userRoleInConversation to set
     */
    public void setUserRoleInConversation(UserRoleInConversation userRoleInConversation) {
        this.userRoleInConversation = userRoleInConversation;
    }

    /**
     * Returns the userJoiningDate
     *
     * @return Returns the userJoiningDate
     */
    public OffsetDateTime getUserJoiningDate() {
        return userJoiningDate;
    }

    /**
     * Sets the userJoiningDate
     *
     * @param userJoiningDate The userJoiningDate to set
     */
    public void setUserJoiningDate(OffsetDateTime userJoiningDate) {
        this.userJoiningDate = userJoiningDate;
    }

    /**
     * Returns the eventConversation
     *
     * @return Returns the eventConversation
     */
    public EventConversation getEventConversation() {
        return eventConversation;
    }

    /**
     * Sets the eventConversation
     *
     * @param eventConversation The eventConversation to set
     */
    public void setEventConversation(EventConversation eventConversation) {
        this.eventConversation = eventConversation;
    }

    /**
     * Returns the conversationDeletedOnUserSide
     *
     * @return Returns the conversationDeletedOnUserSide
     */
    public Boolean getConversationDeletedOnUserSide() {
        return conversationDeletedOnUserSide;
    }

    /**
     * Sets the conversationDeletedOnUserSide
     *
     * @param conversationDeletedOnUserSide The conversationDeletedOnUserSide to set
     */
    public void setConversationDeletedOnUserSide(Boolean conversationDeletedOnUserSide) {
        this.conversationDeletedOnUserSide = conversationDeletedOnUserSide;
    }

}
