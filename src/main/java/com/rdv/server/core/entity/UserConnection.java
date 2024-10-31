package com.rdv.server.core.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_connection", schema="rdv")
public class UserConnection {

    @Id
    @GeneratedValue(generator = "user_connection_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_connection_sequence", sequenceName = "rdv.user_connection_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false, name = "connected_user_id")
    private User connectedUser;

    @Column(name = "creation_date")
    private OffsetDateTime creationDate;

    @Column(name = "status", length = 20)
    private UserConnectionStatus status;



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
     * Returns the connectedUser
     *
     * @return Returns the connectedUser
     */
    public User getConnectedUser() {
        return connectedUser;
    }

    /**
     * Sets the connectedUser
     *
     * @param connectedUser The connectedUser to set
     */
    public void setConnectedUser(User connectedUser) {
        this.connectedUser = connectedUser;
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
    public UserConnectionStatus getStatus() {
        return status;
    }

    /**
     * Sets the status
     *
     * @param status The status to set
     */
    public void setStatus(UserConnectionStatus status) {
        this.status = status;
    }

}