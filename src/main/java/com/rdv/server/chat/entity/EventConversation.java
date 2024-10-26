package com.rdv.server.chat.entity;

import com.rdv.server.core.entity.Event;
import com.rdv.server.core.entity.User;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author davidgarcia
 */
@Entity
@Table(name = "event_conversation", schema="rdv")
public class EventConversation {

    @Id
    @GeneratedValue(generator = "event_conversation_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "event_conversation_sequence", sequenceName = "rdv.event_conversation_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "creation_date")
    private OffsetDateTime creationDate;

    @Column(name = "end_date")
    private OffsetDateTime endDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "eventConversation")
    private List<UserEventConversation> usersInvolved = new ArrayList<>();


    /** Default constructor **/
    public EventConversation() {
    }

    public EventConversation(Event event, OffsetDateTime creationDate) {
        this.event = event;
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

    /**
     * Returns the endDate
     *
     * @return Returns the endDate
     */
    public OffsetDateTime getEndDate() {
        return endDate;
    }

    /**
     * Sets the endDate
     *
     * @param endDate The endDate to set
     */
    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns the usersInvolved
     *
     * @return Returns the usersInvolved
     */
    public List<UserEventConversation> getUsersInvolved() {
        return usersInvolved;
    }

    /**
     * Sets the usersInvolved
     *
     * @param usersInvolved The usersInvolved to set
     */
    public void setUsersInvolved(List<UserEventConversation> usersInvolved) {
        this.usersInvolved = usersInvolved;
    }


    public void addUser(UserEventConversation userInConversation) {
        userInConversation.setEventConversation(this);
        getUsersInvolved().add(userInConversation);
    }

    public List<User> getOtherParticipants(User user) {
        return getUsersInvolved().stream().map(UserEventConversation::getUser)
                .filter(userInConversation -> !userInConversation.getId().equals(user.getId())).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventConversation that = (EventConversation) o;
        return id.equals(that.id);
    }

}
