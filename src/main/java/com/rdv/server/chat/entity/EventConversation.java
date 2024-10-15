package com.rdv.server.chat.entity;

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

    //private Event event;

    @Column(name = "creation_date")
    private OffsetDateTime creationDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "eventConversation")
    private List<UserEventConversation> usersInvolved = new ArrayList<>();


    public EventConversation() {
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
        return getUsersInvolved().stream().filter(userInConversation -> !userInConversation.getUser().getId().equals(user.getId()))
                .map(UserEventConversation::getUser).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventConversation that = (EventConversation) o;
        return id.equals(that.id);
    }

}
