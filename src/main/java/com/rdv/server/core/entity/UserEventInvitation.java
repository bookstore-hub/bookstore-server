package com.rdv.server.core.entity;

import com.rdv.server.chat.entity.EventConversation;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_event_invitation", schema="rdv")
public class UserEventInvitation {

    @EmbeddedId
    private UserEventInvitationId id;

    @ManyToOne
    @JoinColumn(nullable = false, insertable=false, updatable=false, name = "invited_id")
    private User userInvited;

    @ManyToOne
    @JoinColumn(nullable = false, insertable=false, updatable=false, name = "inviting_id")
    private User userInviting;

    @ManyToOne
    @JoinColumn(nullable = false, insertable=false, updatable=false, name = "event_id")
    private Event event;

    @OneToOne
    @JoinColumn(name = "conversation_id")
    private EventConversation conversation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private UserEventInvitationStatus status;

    @Column(name = "creation_date")
    private OffsetDateTime creationDate;


    /** Default constructor **/
    public UserEventInvitation() {
    }

    public UserEventInvitation(User userInvited, User userInviting, Event event, UserEventInvitationStatus status, OffsetDateTime creationDate) {
        this.id = new UserEventInvitationId(userInvited.getId(), userInviting.getId(), event.getId());
        this.userInvited = userInvited;
        this.userInviting = userInviting;
        this.event = event;
        this.status = status;
        this.creationDate = creationDate;
    }


    /**
     * Returns the id
     *
     * @return Returns the id
     */
    public UserEventInvitationId getId() {
        return id;
    }

    /**
     * Sets the id
     *
     * @param id The id to set
     */
    public void setId(UserEventInvitationId id) {
        this.id = id;
    }

    /**
     * Returns the userInvited
     *
     * @return Returns the userInvited
     */
    public User getUserInvited() {
        return userInvited;
    }

    /**
     * Sets the userInvited
     *
     * @param userInvited The userInvited to set
     */
    public void setUserInvited(User userInvited) {
        this.userInvited = userInvited;
    }

    /**
     * Returns the userInviting
     *
     * @return Returns the userInviting
     */
    public User getUserInviting() {
        return userInviting;
    }

    /**
     * Sets the userInviting
     *
     * @param userInviting The userInviting to set
     */
    public void setUserInviting(User userInviting) {
        this.userInviting = userInviting;
    }

    /**
     * Returns the event
     *
     * @return Returns the event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Returns the conversation
     *
     * @return Returns the conversation
     */
    public EventConversation getConversation() {
        return conversation;
    }

    /**
     * Sets the conversation
     *
     * @param conversation The conversation to set
     */
    public void setConversation(EventConversation conversation) {
        this.conversation = conversation;
    }

    /**
     * Sets the event
     *
     * @param event The event to set
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Returns the status
     *
     * @return Returns the status
     */
    public UserEventInvitationStatus getStatus() {
        return status;
    }

    /**
     * Sets the status
     *
     * @param status The status to set
     */
    public void setStatus(UserEventInvitationStatus status) {
        this.status = status;
    }

    /**
     * Returns the creationDate
     *
     * @return Returns the creationDate
     */
    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creationDate
     *
     * @param creationDate The creationDate to set
     */
    public void setCreationDate(OffsetDateTime creationDate) {
        this.creationDate = creationDate;
    }

}