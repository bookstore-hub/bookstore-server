package com.rdv.server.core.to;

import com.rdv.server.chat.entity.UserEventConversation;
import com.rdv.server.core.entity.*;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
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
        String messagingToken
    ) {}

    /** Update of Data **/
    public record Update(
        @Parameter(description = "The user first name")
        @Size(max = 30)
        String firstName,
        @Parameter (description = "The user last name / The organization name")
        @Size(max = 30)
        @NotNull
        String lastName,
        @Parameter (description = "The username")
        @Size(max = 30)
        @NotNull
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
        @NotNull
        String email,
        @Parameter (description = "The user phone number")
        @Size(max = 20)
        String phoneNr,
        @Parameter (description = "The user short presentation")
        @Size(max = 150)
        String shortBio,
        @Parameter (description = "The user selected location")
        Location selectedLocation
    ) {}


    /** Minimal Data **/
    public record MinimalData(Long id, String userFirstName, String userLastName, String username, String userPhoto) {
        public MinimalData(User user) {
            this(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getPhoto());
        }
    }

    /** Full Data **/
    public record FullData(Long id, String email, String userFirstName, String userLastName, String username, String userPhoto, UserGender gender, LocalDate birthDate,
                           String phoneNr, SubscriptionStatus userSubscriptionStatus, String shortBio, String messagingToken, Location selectedLocation) {

        public FullData(User user) {
            this(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getPhoto(), user.getGender(), user.getBirthDate(),
                    user.getPhoneNr(), user.getStatus(), user.getShortBio(), user.getMessagingToken(), Location.fromCode(user.getSelectedLocation()));
        }

    }

    /** Profile Data **/
    public record ProfileData(Long id, String email, String userFirstName, String userLastName, String username, String userPhoto, UserGender gender,
                              LocalDate birthDate, String phoneNr, String shortBio) {

        public ProfileData(User user) {
            this(user.getId(), null, user.getFirstName(), user.getLastName(), user.getUsername(), user.getPhoto(), user.getGender(),
                    null, null, user.getShortBio()); //todo: check if null data are public or not
        }

    }

    /** Conversation Data **/
    public record ConversationData(Long conversationId, MinimalData user, List<MinimalData> otherUsersInvolved, Date dateAndTimeOfLastUpdate) {

        public ConversationData(UserEventConversation userEventConversation, Date dateAndTimeOfLastUpdate) {
            this(userEventConversation.getEventConversation().getId(), new MinimalData(userEventConversation.getUser()),
                    determineOtherUsersInvolved(userEventConversation), dateAndTimeOfLastUpdate);
        }

        private static List<MinimalData> determineOtherUsersInvolved(UserEventConversation userEventConversation) {
            return userEventConversation.getEventConversation().getOtherParticipants(userEventConversation.getUser())
                    .stream().map(MinimalData::new).toList();
        }

    }

}
