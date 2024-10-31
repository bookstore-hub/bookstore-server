package com.rdv.server.core.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

/**
 * @author davidgarcia
 */
@Entity
@Table(name = "user_restriction", schema="assist")
public class UserRestriction {

    @Id
    @GeneratedValue(generator = "user_restriction_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_restriction_sequence", sequenceName = "assist.user_restriction_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_user_id")
    private User sourceUser;

    @ManyToOne
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    @Column(name = "creation_date")
    private OffsetDateTime creationDate;

    @Column(name = "time_zone_offset" , length = 10)
    private String timeZoneOffset;

    /**
     * Default constructor
     */
    public UserRestriction() {
    }

    public UserRestriction(User sourceUser, User targetUser, RestrictionStatus restrictionStatus, OffsetDateTime creationDate, String timeZoneOffset) {
        this.sourceUser = sourceUser;
        this.targetUser = targetUser;
        this.creationDate = creationDate;
        this.timeZoneOffset = timeZoneOffset;
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
     * Returns the sourceUser
     *
     * @return Returns the sourceUser
     */
    public User getSourceUser() {
        return sourceUser;
    }

    /**
     * Sets the sourceUser
     *
     * @param sourceUser The sourceUser to set
     */
    public void setSourceUser(User sourceUser) {
        this.sourceUser = sourceUser;
    }

    /**
     * Returns the targetUser
     *
     * @return Returns the targetUser
     */
    public User getTargetUser() {
        return targetUser;
    }

    /**
     * Sets the targetUser
     *
     * @param targetUser The targetUser to set
     */
    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
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
     * Returns the timeZoneOffset
     *
     * @return Returns the timeZoneOffset
     */
    public String getTimeZoneOffset() {
        return timeZoneOffset;
    }

    /**
     * Sets the timeZoneOffset
     *
     * @param timeZoneOffset The timeZoneOffset to set
     */
    public void setTimeZoneOffset(String timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRestriction that = (UserRestriction) o;
        return sourceUser.equals(that.sourceUser) && targetUser.equals(that.targetUser);
    }
    
}
