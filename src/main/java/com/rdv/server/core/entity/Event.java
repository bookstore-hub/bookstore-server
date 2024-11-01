package com.rdv.server.core.entity;


import com.rdv.server.chat.entity.EventConversation;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author davidgarcia
 */
@Entity
@Table(name = "\"event\"", schema="rdv")
public class Event extends DomainObject {

    @Id
    @GeneratedValue(generator = "event_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "event_sequence", sequenceName = "rdv.event_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "title", length = 70)
    private String title;

    @Column(name = "creation_date")
    private OffsetDateTime creationDate;

    @Column(name = "start_date")
    private OffsetDateTime startDate;

    @Column(name = "end_date")
    private OffsetDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "category" , length = 50)
    private EventCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_audience" , length = 30)
    private EventTargetAudience targetAudience;

    @Column(name = "location_venue" , length = 50)
    private String venue;

    @Column(name = "location_district" , length = 30)
    private String district;

    @Column(name = "cost")
    private double cost;

    @Column(name = "poster" , length = 150)
    private String poster;

    @Column(name = "details_link" , length = 150)
    private String detailsLink;

    @Column(name = "ticketing_link" , length = 150)
    private String ticketingLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "state" , length = 15)
    private EventState state;

    @Enumerated(EnumType.STRING)
    @Column(name = "validation_status" , length = 20)
    private EventValidationStatus validationStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_of_information" , length = 30)
    private EventSourceOfInformation sourceOfInformation;

    @ManyToMany(mappedBy = "eventInterests")
    private Set<User> usersInterested;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "event")
    private List<EventDescription> descriptions = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "event")
    private List<UserEventInvitation> invitations = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "event")
    private List<EventConversation> conversations = new ArrayList<>();



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
     * Returns the title
     *
     * @return Returns the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title
     *
     * @param title The title to set
     */
    public void setTitle(String title) {
        this.title = title;
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
     * Returns the startDate
     *
     * @return Returns the startDate
     */
    public OffsetDateTime getStartDate() {
        return startDate;
    }

    /**
     * Sets the startDate
     *
     * @param startDate The startDate to set
     */
    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
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
     * Returns the category
     *
     * @return Returns the category
     */
    public EventCategory getCategory() {
        return category;
    }

    /**
     * Sets the category
     *
     * @param category The category to set
     */
    public void setCategory(EventCategory category) {
        this.category = category;
    }

    /**
     * Returns the targetAudience
     *
     * @return Returns the targetAudience
     */
    public EventTargetAudience getTargetAudience() {
        return targetAudience;
    }

    /**
     * Sets the targetAudience
     *
     * @param targetAudience The targetAudience to set
     */
    public void setTargetAudience(EventTargetAudience targetAudience) {
        this.targetAudience = targetAudience;
    }

    /**
     * Returns the venue
     *
     * @return Returns the venue
     */
    public String getVenue() {
        return venue;
    }

    /**
     * Sets the venue
     *
     * @param venue The venue to set
     */
    public void setVenue(String venue) {
        this.venue = venue;
    }

    /**
     * Returns the district
     *
     * @return Returns the district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * Sets the district
     *
     * @param district The district to set
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * Returns the cost
     *
     * @return Returns the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * Sets the cost
     *
     * @param cost The cost to set
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Returns the poster
     *
     * @return Returns the poster
     */
    public String getPoster() {
        return poster;
    }

    /**
     * Sets the poster
     *
     * @param poster The poster to set
     */
    public void setPoster(String poster) {
        this.poster = poster;
    }

    /**
     * Returns the detailsLink
     *
     * @return Returns the detailsLink
     */
    public String getDetailsLink() {
        return detailsLink;
    }

    /**
     * Sets the detailsLink
     *
     * @param detailsLink The detailsLink to set
     */
    public void setDetailsLink(String detailsLink) {
        this.detailsLink = detailsLink;
    }

    /**
     * Returns the ticketingLink
     *
     * @return Returns the ticketingLink
     */
    public String getTicketingLink() {
        return ticketingLink;
    }

    /**
     * Sets the ticketingLink
     *
     * @param ticketingLink The ticketingLink to set
     */
    public void setTicketingLink(String ticketingLink) {
        this.ticketingLink = ticketingLink;
    }

    /**
     * Returns the state
     *
     * @return Returns the state
     */
    public EventState getState() {
        return state;
    }

    /**
     * Sets the state
     *
     * @param state The state to set
     */
    public void setState(EventState state) {
        this.state = state;
    }

    /**
     * Returns the validationStatus
     *
     * @return Returns the validationStatus
     */
    public EventValidationStatus getValidationStatus() {
        return validationStatus;
    }

    /**
     * Sets the validationStatus
     *
     * @param validationStatus The validationStatus to set
     */
    public void setValidationStatus(EventValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
    }

    /**
     * Returns the sourceOfInformation
     *
     * @return Returns the sourceOfInformation
     */
    public EventSourceOfInformation getSourceOfInformation() {
        return sourceOfInformation;
    }

    /**
     * Sets the sourceOfInformation
     *
     * @param sourceOfInformation The sourceOfInformation to set
     */
    public void setSourceOfInformation(EventSourceOfInformation sourceOfInformation) {
        this.sourceOfInformation = sourceOfInformation;
    }

    /**
     * Returns the usersInterested
     *
     * @return Returns the usersInterested
     */
    public Set<User> getUsersInterested() {
        return usersInterested;
    }

    /**
     * Sets the usersInterested
     *
     * @param usersInterested The usersInterested to set
     */
    public void setUsersInterested(Set<User> usersInterested) {
        this.usersInterested = usersInterested;
    }

    /**
     * Returns the descriptions
     *
     * @return Returns the descriptions
     */
    public List<EventDescription> getDescriptions() {
        return descriptions;
    }

    /**
     * Sets the descriptions
     *
     * @param descriptions The descriptions to set
     */
    public void setDescriptions(List<EventDescription> descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * Returns the invitations
     *
     * @return Returns the invitations
     */
    public List<UserEventInvitation> getInvitations() {
        return invitations;
    }

    /**
     * Sets the invitations
     *
     * @param invitations The invitations to set
     */
    public void setInvitations(List<UserEventInvitation> invitations) {
        this.invitations = invitations;
    }

    /**
     * Returns the conversations
     *
     * @return Returns the conversations
     */
    public List<EventConversation> getConversations() {
        return conversations;
    }

    /**
     * Sets the conversations
     *
     * @param conversations The conversations to set
     */
    public void setConversations(List<EventConversation> conversations) {
        this.conversations = conversations;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

}
