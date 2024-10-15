package com.rdv.server.core.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "event_description", schema="rdv")
public class EventDescription {

    @Id
    @GeneratedValue(generator = "event_description_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "event_description_sequence", sequenceName = "rdv.event_description_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "event_id")
    private Event event;

    @Column(name = "description", length = 250)
    private String description;

    @Column(name = "language", length = 10)
    private Language language;

    @Column(name = "creation_date")
    private OffsetDateTime creationDate;


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
     * Returns the description
     *
     * @return Returns the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description
     *
     * @param description The description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the language
     *
     * @return Returns the language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Sets the language
     *
     * @param language The language to set
     */
    public void setLanguage(Language language) {
        this.language = language;
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