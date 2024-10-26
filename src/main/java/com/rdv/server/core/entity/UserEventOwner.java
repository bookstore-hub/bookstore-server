package com.rdv.server.core.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Objects;


@Entity
@Table(name = "user_event_owner", schema="rdv")
public class UserEventOwner {

    @Id
    @GeneratedValue(generator = "user_event_owner_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_event_owner_sequence", sequenceName = "rdv.user_event_owner_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private UserEventOwnerStatus status;

    @Column(name = "creation_date")
    private OffsetDateTime creationDate;


    /** Default constructor **/
    public UserEventOwner() {
    }

    public UserEventOwner(User user, Event event, UserEventOwnerStatus status, OffsetDateTime creationDate) {
        this.user = user;
        this.event = event;
        this.status = status;
        this.creationDate = creationDate;
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
     * Returns the event
     *
     * @return Returns the event
     */
    public Event getEvent() {
        return event;
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
    public UserEventOwnerStatus getStatus() {
        return status;
    }

    /**
     * Sets the status
     *
     * @param status The status to set
     */
    public void setStatus(UserEventOwnerStatus status) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEventOwner that = (UserEventOwner) o;
        return Objects.equals(id, that.id);
    }

}