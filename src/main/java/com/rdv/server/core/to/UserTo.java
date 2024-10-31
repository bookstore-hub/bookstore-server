package com.rdv.server.core.to;

import com.rdv.server.chat.entity.UserEventConversation;
import com.rdv.server.core.entity.*;
import com.rdv.server.core.util.DateUtil;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.LocaleUtils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;


public class UserTo {

    /** Creation of Data **/
    public record Creation(
        @Parameter (description = "The username")
        @Size(max = 30)
        @NotNull
        String username,
        @Parameter (description = "The user photo")
        @Size(max = 150)
        String photo,
        @Parameter (description = "The user birthdate")
        LocalDate birthDate,
        @Parameter (description = "The user email")
        @Size(max = 50)
        @NotNull
        String email,
        @Parameter (description = "The user password")
        @Size(max = 50)
        @NotNull
        String password,
        @Parameter (description = "The user preferred language")
        @Size(max = 5)
        @NotNull
        Language preferredLanguage,
        String messagingToken
    ) {}

    /** Update of Data **/
    public record Update(
        @Parameter(description = "The user first name")
        @Size(max = 30)
        String firstName,
        @Parameter (description = "The user last name / The organization name")
        @Size(max = 30)
        String lastName,
        @Parameter (description = "The username")
        @Size(max = 30)
        String username,
        @Parameter (description = "The user photo")
        @Size(max = 150)
        String photo,
        @Parameter (description = "The user gender")
        @Size(max = 1)
        UserGender gender,
        @Parameter (description = "The user birthdate")
        LocalDate birthDate,
        @Parameter (description = "The user email")
        @Size(max = 50)
        String email,
        @Parameter (description = "The user password")
        @Size(max = 50)
        String password,
        @Parameter (description = "The user preferred language")
        @Size(max = 5)
        Language preferredLanguage,
        @Parameter (description = "The user phone number")
        @Size(max = 20)
        String phoneNr,
        @Parameter (description = "The user short presentation")
        @Size(max = 150)
        String shortBio,
        @Parameter (description = "The user selected location")
        Location selectedLocation,
        @Parameter (description = "The email visibility")
        @Size(max = 10)
        FieldVisibility visibilityEmail,
        @Parameter (description = "The phone number visibility")
        @Size(max = 10)
        FieldVisibility visibilityPhoneNr,
        @Parameter (description = "The birth date visibility")
        @Size(max = 30)
        FieldVisibility visibilityBirthDate
    ) {}


    /** Minimal Data **/
    public record MinimalData(Long id, String userFirstName, String userLastName, String username, String userPhoto) {
        public MinimalData(User user) {
            this(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getPhoto());
        }
    }


    /** Full Data **/
    public record FullData(Long id, String email, String userFirstName, String userLastName, String username, String userPhoto, UserGender gender, LocalDate birthDate,
                           String phoneNr, SubscriptionStatus subscriptionStatus, String shortBio, String messagingToken, Location selectedLocation,
                           FieldVisibility visibilityEmail, FieldVisibility visibilityPhoneNr, FieldVisibility visibilityBirthDate) {

        public FullData(User user) {
            this(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getPhoto(), user.getGender(), user.getBirthDate(),
                    user.getPhoneNr(), user.getStatus(), user.getShortBio(), user.getMessagingToken(), Location.fromCode(user.getSelectedLocation()),
                    user.getVisibilityEmail(), user.getVisibilityPhoneNr(), user.getVisibilityBirthDate());
        }

    }


    /** Profile Data **/
    public record ProfileData(Long id, String email, String userFirstName, String userLastName, String username, String userPhoto, UserGender gender,
                              String birthDate, String phoneNr, String shortBio) {

        public ProfileData(User userTargeted, User userRequesting) {
            this(userTargeted.getId(), resolveEmailDisplay(userTargeted, userRequesting), userTargeted.getFirstName(), userTargeted.getLastName(),
                    userTargeted.getUsername(), userTargeted.getPhoto(), userTargeted.getGender(), resolveBirthDateDisplay(userTargeted, userRequesting),
                    resolvePhoneNumberDisplay(userTargeted, userRequesting), userTargeted.getShortBio());
        }


        private static String resolveEmailDisplay(User userTargeted, User userRequesting) {
            String email = null;
            if(!userRequesting.hasBlocked(userTargeted)) {
                FieldVisibility emailVisibility = userTargeted.getVisibilityEmail();
                if(FieldVisibility.PUBLIC.equals(emailVisibility)
                        || (FieldVisibility.FRIENDS.equals(emailVisibility) && userTargeted.isFriend(userRequesting))) {
                    email = userTargeted.getEmail();
                }
            }
            return email;
        }

        private static String resolveBirthDateDisplay(User userTargeted, User userRequesting) {
            String birthDateAsString = null;
            LocalDate birthDate = userTargeted.getBirthDate();

            if(!userRequesting.hasBlocked(userTargeted)) {
                FieldVisibility birthDateVisibility = userTargeted.getVisibilityBirthDate();
                if(FieldVisibility.PUBLIC.equals(birthDateVisibility)
                        || (FieldVisibility.FRIENDS.equals(birthDateVisibility) && userTargeted.isFriend(userRequesting))) {
                    birthDateAsString = DateUtil.formatDate(birthDate, LocaleUtils.toLocale(userRequesting.getPreferredLanguage().name()));
                } else if(FieldVisibility.DAY_AND_MONTH_PUBLIC.equals(birthDateVisibility)
                        || (FieldVisibility.DAY_AND_MONTH_FRIENDS.equals(birthDateVisibility) && userTargeted.isFriend(userRequesting))) {
                    birthDateAsString = DateUtil.formatDateWithDayAndMonth(birthDate, LocaleUtils.toLocale(userRequesting.getPreferredLanguage().name()));

                }
            }

            return birthDateAsString;
        }

        private static String resolvePhoneNumberDisplay(User userTargeted, User userRequesting) {
            String phoneNr = null;
            if(!userRequesting.hasBlocked(userTargeted)) {
                FieldVisibility phoneNrVisibility = userTargeted.getVisibilityPhoneNr();
                if(FieldVisibility.PUBLIC.equals(phoneNrVisibility)
                        || (FieldVisibility.FRIENDS.equals(phoneNrVisibility) && userTargeted.isFriend(userRequesting))) {
                    phoneNr = userTargeted.getEmail();
                }
            }
            return phoneNr;
        }

    }


    /** Conversation Data **/
    public record ConversationData(Long conversationId, MinimalData user, List<MinimalData> otherUsersInvolved, Date dateAndTimeOfLastUpdate,
                                   Long eventId, String eventTitle) {

        public ConversationData(UserEventConversation userInConversation, Date dateAndTimeOfLastUpdate) {
            this(userInConversation.getEventConversation().getId(), new MinimalData(userInConversation.getUser()),
                    determineOtherUsersInvolved(userInConversation), dateAndTimeOfLastUpdate,
                    userInConversation.getEventConversation().getEvent().getId(), userInConversation.getEventConversation().getEvent().getTitle());
        }

        private static List<MinimalData> determineOtherUsersInvolved(UserEventConversation userEventConversation) {
            return userEventConversation.getEventConversation().getOtherParticipants(userEventConversation.getUser())
                    .stream().map(MinimalData::new).toList();
        }

    }

    /** Mapping of new User **/
    public static User mapNewUser(UserTo.Creation userData, String passwordEncoded) {
        User user = new User();
        user.setUsername(userData.username().trim());
        user.setPhoto(userData.photo());
        user.setBirthDate(userData.birthDate());
        user.setEmail(userData.email());
        user.setPassword(passwordEncoded);
        user.setPreferredLanguage(userData.preferredLanguage());
        user.setMessagingToken(userData.messagingToken());
        user.setAccountCreationDateAndTime(OffsetDateTime.now());
        user.setLastModificationDate(OffsetDateTime.now());
        user.setStatus(SubscriptionStatus.PENDING);

        return user;
    }

    /** Mapping of updated User **/
    public static User mapUpdatedUser(User user, UserTo.Update userData) {
        user.setUsername(userData.username());
        user.setPhoto(userData.photo());
        user.setBirthDate(userData.birthDate());
        user.setEmail(userData.email());
        user.setPreferredLanguage(userData.preferredLanguage());
        user.setFirstName(userData.firstName());
        user.setLastName(userData.lastName());
        user.setGender(userData.gender());
        user.setPhoneNr(userData.phoneNr());
        user.setShortBio(userData.shortBio());
        user.setSelectedLocation(1); //MTL only for now
        user.setVisibilityEmail(userData.visibilityEmail());
        user.setVisibilityPhoneNr(userData.visibilityPhoneNr());
        user.setVisibilityBirthDate(userData.visibilityBirthDate());
        user.setLastModificationDate(OffsetDateTime.now());

        return user;
    }

}
