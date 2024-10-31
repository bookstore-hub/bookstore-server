package com.rdv.server.core.entity;


import com.rdv.server.chat.entity.EventConversation;
import com.rdv.server.chat.entity.UserEventConversation;
import com.rdv.server.chat.entity.UserRoleInConversation;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

/**
 * @author davidgarcia
 */
@Entity
@Table(name = "\"user\"", schema="rdv")
public class User extends DomainObject {

    @Id
    @GeneratedValue(generator = "user_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_sequence", sequenceName = "rdv.user_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "account_creation_date")
    private OffsetDateTime accountCreationDateAndTime;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email" , length = 50)
    private String email;

    @Column(name = "first_name" , length = 30)
    private String firstName;

    @Column(name = "last_name" , length = 30)
    private String lastName;

    @Column(name = "username" , length = 30)
    private String username;

    @Column(name = "user_photo" , length = 150)
    private String photo;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender" , length = 2)
    private UserGender gender;

    @Column(name = "password" , length = 100)
    private String password;

    @Column(name = "phone_nr" , length = 20)
    private String phoneNr;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_status" , length = 20)
    private SubscriptionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_language" , length = 20)
    private Language preferredLanguage;

    @Column(name = "short_bio" , length = 150)
    private String shortBio;

    @Column(name = "messaging_token", length = 200)
    private String messagingToken;

    @Column(name = "selected_location")
    private int selectedLocation;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="user")
    private Set<UserConnection> connections = new HashSet<>();

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="connectedUser")
    private Set<UserConnection> friendRequests = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_event_interest",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> eventInterests;

    @ManyToMany
    @JoinTable(name = "user_follower",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private List<User> followers = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_following",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"))
    private List<User> usersFollowed = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility_email", length = 10)
    private FieldVisibility visibilityEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility_phone_nr", length = 10)
    private FieldVisibility visibilityPhoneNr;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility_birth_date", length = 30)
    private FieldVisibility visibilityBirthDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserEventOwner> ownedEvents = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userInvited")
    private List<UserEventInvitation> eventInvitationsReceived = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userInviting")
    private List<UserEventInvitation> eventInvitationsSent = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserEventConversation> participationInConversations = new ArrayList<>();


    public User() {
    }

    /**
     * For default authentication
     */
    public User(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
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
     * Returns the accountCreationDateAndTime
     *
     * @return Returns the accountCreationDateAndTime
     */
    public OffsetDateTime getAccountCreationDateAndTime() {
        return accountCreationDateAndTime;
    }

    /**
     * Sets the accountCreationDateAndTime
     *
     * @param accountCreationDateAndTime The accountCreationDateAndTime to set
     */
    public void setAccountCreationDateAndTime(OffsetDateTime accountCreationDateAndTime) {
        this.accountCreationDateAndTime = accountCreationDateAndTime;
    }

    /**
     * Returns the birthDate
     *
     * @return Returns the birthDate
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birthDate
     *
     * @param birthDate The birthDate to set
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Returns the email
     *
     * @return Returns the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email
     *
     * @param email The email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the firstName
     *
     * @return Returns the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the firstName
     *
     * @param firstName The firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the lastName
     *
     * @return Returns the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the lastName
     *
     * @param lastName The lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the username
     *
     * @return Returns the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username
     *
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the photo
     *
     * @return Returns the photo
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Sets the photo
     *
     * @param photo The photo to set
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Returns the gender
     *
     * @return Returns the gender
     */
    public UserGender getGender() {
        return gender;
    }

    /**
     * Sets the gender
     *
     * @param gender The gender to set
     */
    public void setGender(UserGender gender) {
        this.gender = gender;
    }

    /**
     * Returns the password
     *
     * @return Returns the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password
     *
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the phoneNr
     *
     * @return Returns the phoneNr
     */
    public String getPhoneNr() {
        return phoneNr;
    }

    /**
     * Sets the phoneNr
     *
     * @param phoneNr The phoneNr to set
     */
    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    /**
     * Returns the status
     *
     * @return Returns the status
     */
    public SubscriptionStatus getStatus() {
        return status;
    }

    /**
     * Sets the status
     *
     * @param status The status to set
     */
    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    /**
     * Returns the preferredLanguage
     *
     * @return Returns the preferredLanguage
     */
    public Language getPreferredLanguage() {
        return preferredLanguage;
    }

    /**
     * Sets the preferredLanguage
     *
     * @param preferredLanguage The preferredLanguage to set
     */
    public void setPreferredLanguage(Language preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    /**
     * Returns the shortBio
     *
     * @return Returns the shortBio
     */
    public String getShortBio() {
        return shortBio;
    }

    /**
     * Sets the shortBio
     *
     * @param shortBio The shortBio to set
     */
    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    /**
     * Returns the messagingToken
     *
     * @return Returns the messagingToken
     */
    public String getMessagingToken() {
        return messagingToken;
    }

    /**
     * Sets the messagingToken
     *
     * @param messagingToken The messagingToken to set
     */
    public void setMessagingToken(String messagingToken) {
        this.messagingToken = messagingToken;
    }

    /**
     * Returns the selectedLocation
     *
     * @return Returns the selectedLocation
     */
    public int getSelectedLocation() {
        return selectedLocation;
    }

    /**
     * Sets the selectedLocation
     *
     * @param selectedLocation The selectedLocation to set
     */
    public void setSelectedLocation(int selectedLocation) {
        this.selectedLocation = selectedLocation;
    }

    /**
     * Returns the connections
     *
     * @return Returns the connections
     */
    public Set<UserConnection> getConnections() {
        return connections;
    }

    /**
     * Sets the connections
     *
     * @param connections The connections to set
     */
    public void setConnections(Set<UserConnection> connections) {
        this.connections = connections;
    }

    /**
     * Returns the friendRequests
     *
     * @return Returns the friendRequests
     */
    public Set<UserConnection> getFriendRequests() {
        return friendRequests;
    }

    /**
     * Sets the friendRequests
     *
     * @param friendRequests The friendRequests to set
     */
    public void setFriendRequests(Set<UserConnection> friendRequests) {
        this.friendRequests = friendRequests;
    }

    /**
     * Returns the eventInterests
     *
     * @return Returns the eventInterests
     */
    public Set<Event> getEventInterests() {
        return eventInterests;
    }

    /**
     * Sets the eventInterests
     *
     * @param eventInterests The eventInterests to set
     */
    public void setEventInterests(Set<Event> eventInterests) {
        this.eventInterests = eventInterests;
    }

    /**
     * Returns the followers
     *
     * @return Returns the followers
     */
    public List<User> getFollowers() {
        return followers;
    }

    /**
     * Sets the followers
     *
     * @param followers The followers to set
     */
    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    /**
     * Returns the usersFollowed
     *
     * @return Returns the usersFollowed
     */
    public List<User> getUsersFollowed() {
        return usersFollowed;
    }

    /**
     * Sets the usersFollowed
     *
     * @param usersFollowed The usersFollowed to set
     */
    public void setUsersFollowed(List<User> usersFollowed) {
        this.usersFollowed = usersFollowed;
    }

    /**
     * Returns the visibilityEmail
     *
     * @return Returns the visibilityEmail
     */
    public FieldVisibility getVisibilityEmail() {
        return visibilityEmail;
    }

    /**
     * Sets the visibilityEmail
     *
     * @param visibilityEmail The visibilityEmail to set
     */
    public void setVisibilityEmail(FieldVisibility visibilityEmail) {
        this.visibilityEmail = visibilityEmail;
    }

    /**
     * Returns the visibilityPhoneNr
     *
     * @return Returns the visibilityPhoneNr
     */
    public FieldVisibility getVisibilityPhoneNr() {
        return visibilityPhoneNr;
    }

    /**
     * Sets the visibilityPhoneNr
     *
     * @param visibilityPhoneNr The visibilityPhoneNr to set
     */
    public void setVisibilityPhoneNr(FieldVisibility visibilityPhoneNr) {
        this.visibilityPhoneNr = visibilityPhoneNr;
    }

    /**
     * Returns the visibilityBirthDate
     *
     * @return Returns the visibilityBirthDate
     */
    public FieldVisibility getVisibilityBirthDate() {
        return visibilityBirthDate;
    }

    /**
     * Sets the visibilityBirthDate
     *
     * @param visibilityBirthDate The visibilityBirthDate to set
     */
    public void setVisibilityBirthDate(FieldVisibility visibilityBirthDate) {
        this.visibilityBirthDate = visibilityBirthDate;
    }

    /**
     * Returns the eventInvitationsReceived
     *
     * @return Returns the eventInvitationsReceived
     */
    public List<UserEventInvitation> getEventInvitationsReceived() {
        return eventInvitationsReceived;
    }

    /**
     * Returns the ownedEvents
     *
     * @return Returns the ownedEvents
     */
    public List<UserEventOwner> getOwnedEvents() {
        return ownedEvents;
    }

    /**
     * Sets the ownedEvents
     *
     * @param ownedEvents The ownedEvents to set
     */
    public void setOwnedEvents(List<UserEventOwner> ownedEvents) {
        this.ownedEvents = ownedEvents;
    }

    /**
     * Sets the eventInvitationsReceived
     *
     * @param eventInvitationsReceived The eventInvitationsReceived to set
     */
    public void setEventInvitationsReceived(List<UserEventInvitation> eventInvitationsReceived) {
        this.eventInvitationsReceived = eventInvitationsReceived;
    }

    /**
     * Returns the eventInvitationsSent
     *
     * @return Returns the eventInvitationsSent
     */
    public List<UserEventInvitation> getEventInvitationsSent() {
        return eventInvitationsSent;
    }

    /**
     * Sets the eventInvitationsSent
     *
     * @param eventInvitationsSent The eventInvitationsSent to set
     */
    public void setEventInvitationsSent(List<UserEventInvitation> eventInvitationsSent) {
        this.eventInvitationsSent = eventInvitationsSent;
    }

    /**
     * Returns the participationInConversations
     *
     * @return Returns the participationInConversations
     */
    public List<UserEventConversation> getParticipationInConversations() {
        return participationInConversations;
    }

    /**
     * Sets the participationInConversations
     *
     * @param participationInConversations The participationInConversations to set
     */
    public void setParticipationInConversations(List<UserEventConversation> participationInConversations) {
        this.participationInConversations = participationInConversations;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }


    public boolean isEnabled() {
        return switch (getStatus()) {
            case ASSESSING, CONFIRMED, VERIFIED, TESTER, ADMIN -> true;
            default -> false;
        };
    }

    public Optional<UserEventOwner> retrieveCreatedEvent(Event event) {
        return ownedEvents.stream()
                .filter(ownedEvent -> ownedEvent.getEvent().equals(event) && UserEventOwnerStatus.CREATOR.equals(ownedEvent.getStatus()))
                .findAny();
    }

    public void removeEvent(UserEventOwner ownedEvent) {
        ownedEvent.setUser(null);
        getOwnedEvents().remove(ownedEvent);
    }

    public void addInvitationSent(UserEventInvitation invitationSent) {
        invitationSent.setUserInviting(this);
        getEventInvitationsSent().add(invitationSent);
    }

    public void addInvitationReceived(UserEventInvitation invitationReceived) {
        invitationReceived.setUserInvited(this);
        getEventInvitationsReceived().add(invitationReceived);
    }

    public void addEventInterest(Event event) {
        getEventInterests().add(event);
    }

    public void removeEventInterest(Event event) {
        getEventInterests().remove(event);
    }

    public void addParticipationToConversation(UserEventConversation participationInConversation) {
        getParticipationInConversations().add(participationInConversation);
    }

    public boolean hasRemovalRights(EventConversation eventConversation, List<User> usersToRemove) {
        return eventConversation.getUsersInvolved().stream()
                .anyMatch(userInvolved -> isModerator(eventConversation)
                        || (usersToRemove.size() == 1 && usersToRemove.getFirst().getId().equals(getId())));
    }

    public boolean isModerator(EventConversation eventConversation) {
        return eventConversation.getUsersInvolved().stream()
                .anyMatch(userInvolved ->
                        userInvolved.getId().equals(getId()) && UserRoleInConversation.MODERATOR.equals(userInvolved.getUserRoleInConversation()));
    }

    public void addOwnedEvent(UserEventOwner ownedEvent) {
        getOwnedEvents().add(ownedEvent);
    }

    public boolean ownsEvent(Event event) {
        return getOwnedEvents().stream()
                .anyMatch(ownedEvent ->
                        ownedEvent.getEvent().equals(event)
                        && (ownedEvent.getStatus().equals(UserEventOwnerStatus.CREATOR) || ownedEvent.getStatus().equals(UserEventOwnerStatus.ACQUIRER)));
    }

    public void startFollowing(User user) {
        getUsersFollowed().add(user);
        user.getFollowers().add(this);
    }

    public void stopFollowing(User user) {
        getUsersFollowed().remove(user);
        user.getFollowers().remove(this);
    }

    public Optional<UserConnection> getConnection(User user) {
        return getConnections().stream().filter(friend -> friend.getConnectedUser().equals(user)).findFirst();
    }

    public void addFriendRequest(User userAdded, UserConnection connection) {
        getConnections().add(connection);
        userAdded.getFriendRequests().add(connection);
    }

    public Optional<UserConnection> getFriendRequest(User user) {
        return getFriendRequests().stream().filter(friendRequest -> friendRequest.getUser().equals(user)).findFirst();
    }

    public void acceptFriendRequest(UserConnection connection) {
        getConnections().add(connection);
        getFriendRequests().remove(connection);
    }

    public void declineFriendRequest(User userDeclined, UserConnection connection) {
        getFriendRequests().remove(connection);
        userDeclined.getConnections().remove(connection);
    }

    public void removeConnection(User userRemoved, UserConnection connection) {
        getConnections().remove(connection);

        if(UserConnectionStatus.FRIEND.equals(connection.getStatus())) {
            userRemoved.getConnections().remove(connection);
        } else if(UserConnectionStatus.PENDING.equals(connection.getStatus())) {
            userRemoved.getFriendRequests().remove(connection);
        }
    }

    public boolean isFriend(User user) {
        Optional<UserConnection> connection = getConnection(user);
        return connection.isPresent() && UserConnectionStatus.FRIEND.equals(connection.get().getStatus());
    }

    public boolean hasBlocked(User user) { //todo revoir la logique !! (pour filtrer les bloqués en recherche entre autre-
        Optional<UserConnection> connection = getConnection(user);
        return connection.isPresent() && UserConnectionStatus.BLOCKED.equals(connection.get().getStatus());
    }

}
