package com.rdv.server.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserEventInvitationId implements Serializable {

    @Column(name = "invited_id")
    private Long userInvitedId;

    @Column(name = "inviting_id")
    private Long userInvitingId;

    @Column(name = "event_id")
    private Long eventId;


    /** Default constructor **/
    public UserEventInvitationId() {
    }

    public UserEventInvitationId(Long userInvitedId, Long userInvitingId, Long eventId) {
        this.userInvitedId = userInvitedId;
        this.userInvitingId = userInvitingId;
        this.eventId = eventId;
    }

    /**
     * Returns the userInvitedId
     *
     * @return Returns the userInvitedId
     */
    public Long getUserInvitedId() {
        return userInvitedId;
    }

    /**
     * Sets the userInvitedId
     *
     * @param userInvitedId The userInvitedId to set
     */
    public void setUserInvitedId(Long userInvitedId) {
        this.userInvitedId = userInvitedId;
    }

    /**
     * Returns the userInvitingId
     *
     * @return Returns the userInvitingId
     */
    public Long getUserInvitingId() {
        return userInvitingId;
    }

    /**
     * Sets the userInvitingId
     *
     * @param userInvitingId The userInvitingId to set
     */
    public void setUserInvitingId(Long userInvitingId) {
        this.userInvitingId = userInvitingId;
    }

    /**
     * Returns the eventId
     *
     * @return Returns the eventId
     */
    public Long getEventId() {
        return eventId;
    }

    /**
     * Sets the eventId
     *
     * @param eventId The eventId to set
     */
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

}
