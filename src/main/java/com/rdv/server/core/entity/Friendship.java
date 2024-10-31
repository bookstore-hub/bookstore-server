package com.rdv.server.core.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "friendship", schema="rdv")
public class Friendship {

    @Id
    @GeneratedValue(generator = "friendship_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "friendship_sequence", sequenceName = "rdv.friendship_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false, name = "friend_id")
    private User friend;

    @Column(name = "creation_date")
    private OffsetDateTime creationDate;

    @Column(name = "status", length = 20)
    private FriendshipStatus status;



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
     * Returns the friend
     *
     * @return Returns the friend
     */
    public User getFriend() {
        return friend;
    }

    /**
     * Sets the friend
     *
     * @param friend The friend to set
     */
    public void setFriend(User friend) {
        this.friend = friend;
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
     * Returns the status
     *
     * @return Returns the status
     */
    public FriendshipStatus getStatus() {
        return status;
    }

    /**
     * Sets the status
     *
     * @param status The status to set
     */
    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

}