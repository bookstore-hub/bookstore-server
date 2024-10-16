package com.rdv.server.account.controller;


import com.rdv.server.account.event.OnAccountRegistrationCompleteEvent;
import com.rdv.server.account.service.AccountService;
import com.rdv.server.account.to.GenericResponse;
import com.rdv.server.account.to.PasswordTo;
import com.rdv.server.account.to.TokenStatus;
import com.rdv.server.authentication.service.AuthenticationService;
import com.rdv.server.core.entity.User;
import com.rdv.server.core.entity.VerificationToken;
import com.rdv.server.core.repository.UserRepository;
import com.rdv.server.core.to.UserTo;
import com.rdv.server.mail.service.SendGridService;
import com.rdv.server.mail.to.MessageType;
import com.sendgrid.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;


/**
 * @author davidgarcia
 */

@Controller
@CrossOrigin
@Tag(name = "AccountController", description = "Set of endpoints to handle the account management logic")
public class AccountController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String REGISTRATION_CONFIRMATION_CALL = "/user/registrationConfirm";
    private static final String PASSWORD_RESET_CALL = "/user/enterNewPassword";
    private static final String REGISTRATION_CONFIRMATION_PAGE = "registrationConfirmation";
    private static final String PASSWORD_RESET_PAGE = "passwordReset";
    private static final String RESET_TOKEN_TAG = "resetToken";
    private static final String CONFIRMATION_TEXT_TAG = "confirmationText";
    private static final String CONNECTION_TEXT_TAG = "connectionText";
    private static final String CHANGE_PASSWORD_TITLE_TAG = "changePasswordTitle";
    private static final String NEW_PASSWORD_TEXT_TAG = "newPasswordText";
    private static final String CONFIRM_NEW_PASSWORD_TEXT_TAG = "confirmNewPassword";
    private static final String FUNCTION_SAVE_TAG = "functionSave";
    private static final String PASSWORD_CHANGED_TEXT_TAG = "passwordChanged";
    private static final String ACCOUNT_CONFIRMED = "account.confirmed";
    private static final String ACCOUNT_CONNECTION = "account.connection";
    private static final String ACCOUNT_CHANGE_PASSWORD = "account.changePassword";
    private static final String ACCOUNT_NEW_PASSWORD = "account.newPassword";
    private static final String ACCOUNT_CONFIRM_NEW_PASSWORD = "account.confirmNewPassword";
    private static final String ACCOUNT_PASSWORD_CHANGED = "account.passwordChanged";
    private static final String FUNCTION_SAVE = "function.save";
    private static final String PASSWORD_NOT_CHANGED_TEXT_TAG = "passwordNotChanged";
    private static final String ACCOUNT_PASSWORD_NOT_CHANGED = "account.passwordNotChanged";
    private static final String PASSWORD_NOT_MATCHING_TEXT_TAG = "passwordNotMatching";
    private static final String PASSWORD_NOT_MATCHING = "rule.passwordNotMatching";
    private static final String PASSWORD_FORMAT_TEXT_TAG = "passwordFormat";
    private static final String PASSWORD_FORMAT = "rule.passwordFormat";
    private static final String EMPTY_FIELD_TEXT_TAG = "emptyField";
    private static final String EMPTY_FIELD = "rule.emptyField";
    private static final String INVALID_TOKEN_PAGE = "invalidToken";
    private static final String INVALID_TOKEN_TEXT_TAG = "invalidToken";
    private static final String UNAUTHORIZED_ACCESS = "access.unauthorized";

    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";
    private final static String USER_NOT_FOUND = "userNotFound";
    private final static String INVALID_OLD_PASSWORD = "invalidOldPassword";

    private final AccountService accountService;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SendGridService sendgridService;
    private final AuthenticationService authenticationService;
    private final MessageSource messageSource;
    private final String domain;

    public AccountController(AccountService accountService, UserRepository userRepository, ApplicationEventPublisher eventPublisher,
                             SendGridService sendgridService, AuthenticationService authenticationService, MessageSource messageSource,
                             @Value("${azure.backend.domain}") String domain) {
        this.accountService = accountService;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.sendgridService = sendgridService;
        this.authenticationService = authenticationService;
        this.messageSource = messageSource;
        this.domain = domain;
    }


    /**
     * Registers a new user
     *
     * @param userInfo the user info
     * @param languageCode the language code
     */
    @Operation(description = "Registers a new user")
    @PostMapping("/user/registration")
    @ResponseBody
    public GenericResponse.GenericResponseData registerUserAccount(@Parameter (description = "The user info") @Valid @RequestBody UserTo.Creation userInfo,
                                                                   @Parameter(description = "The user language") @RequestParam String languageCode) {
        LOGGER.debug("Registering user account with information: {}", userInfo);

        User registered = accountService.registerUserAccount(userInfo, languageCode);
        if(registered != null) {
            eventPublisher.publishEvent(new OnAccountRegistrationCompleteEvent(registered, languageCode));
            return new GenericResponse.GenericResponseData(true, SUCCESS);
        } else {
            return new GenericResponse.GenericResponseData(false, FAILURE);
        }
    }

    /**
     * Updates a user account
     *
     * @param userId the user id
     * @param userInfo the user info
     */
    @Operation(description = "Updates a user account. ***Note about the user password: The update is done using the call /user/changePassword.***")
    @PutMapping(value = "/user/updateAccount")
    @ResponseBody
    private GenericResponse.GenericResponseData updateAccount(@Parameter(description = "The user id") @RequestParam Long userId,
                                          @Parameter (description = "The user profile info") @Valid @RequestBody UserTo.Update userInfo,
                                          @Parameter(description = "The user language") @RequestParam String languageCode) {

        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            User updated = accountService.updateUserAccount(user.get(), userInfo, languageCode);
            if(updated != null) {
                return new GenericResponse.GenericResponseData(true, SUCCESS);
            } else {
                return new GenericResponse.GenericResponseData(false, FAILURE);
            }
        } else {
            return new GenericResponse.GenericResponseData(false, FAILURE);
        }

    }

    /**
     * Updates the Firebase messaging token of a user
     *
     * @param userId the user id
     * @param userMessagingToken the user messaging token
     */
    @Operation(description = "Updates the Firebase messaging token of a user.")
    @PutMapping(value = "/user/updateMessagingToken")
    @ResponseBody
    private GenericResponse.GenericResponseData updateMessagingToken(@Parameter(description = "The user id") @RequestParam Long userId,
                                                 @Parameter(description = "The user messaging token") @RequestParam String userMessagingToken) {

        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            user.get().setMessagingToken(userMessagingToken);
            userRepository.save(user.get());
            return new GenericResponse.GenericResponseData(true, SUCCESS);
        } else {
            return new GenericResponse.GenericResponseData(false, FAILURE);
        }

    }

    /**
     * Removes a messaging token assigned to a user
     *
     * @param userId the user id
     */
    @Operation(description = "Removes a messaging token assigned to a user")
    @PutMapping(value = "/user/logOutMessagingToken")
    @ResponseBody
    private GenericResponse.GenericResponseData logOutMessagingToken(@Parameter(description = "The user id") @RequestParam Long userId) {

        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            user.get().setMessagingToken(null);
            userRepository.save(user.get());
            return new GenericResponse.GenericResponseData(true, SUCCESS);
        } else {
            return new GenericResponse.GenericResponseData(false, FAILURE);
        }

    }


    /**
     * Updates the preferred language of a user
     *
     * @param userId the user id
     * @param languageCode the language code
     */
    @Operation(description = "Updates the preferred language of a user")
    @PutMapping(value = "/user/updateLanguage")
    @ResponseBody
    private GenericResponse.GenericResponseData updateLanguage(@Parameter(description = "The user id") @RequestParam Long userId,
                                           @Parameter(description = "The user language") @RequestParam String languageCode) {

        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            User updatedLanguage = accountService.updateUserLanguage(user.get(), languageCode);
            if(updatedLanguage != null) {
                return new GenericResponse.GenericResponseData(true, SUCCESS);
            } else {
                return new GenericResponse.GenericResponseData(false, FAILURE);
            }
        } else {
            return new GenericResponse.GenericResponseData(false, FAILURE);
        }

    }

    /**
     * Deletes a user account
     *
     * @param userId the user id
     */
    @Operation(description = "Deletes a user account")
    @PutMapping(value = "/user/deleteAccount")
    @ResponseBody
    private GenericResponse.GenericResponseData deleteAccount(@Parameter(description = "The user id") @RequestParam Long userId) {

        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            user.get().setLastModificationDate(OffsetDateTime.now());
            accountService.deleteUserAccount(user.get());

            LOGGER.info("Sending account closure email to user " + user.get().getId());
            Locale locale = LocaleUtils.toLocale(user.get().getPreferredLanguage().name());
            Response response = sendgridService.sendMessage(user.get().getEmail(), MessageType.CLOSING_ACCOUNT, locale, user.get().getFirstName(), null, null);
            if(response != null) {
                LOGGER.info("Account closure email sent");
                return new GenericResponse.GenericResponseData(true, SUCCESS);
            } else {
                LOGGER.info("Account closure email could not be sent");
                return new GenericResponse.GenericResponseData(false, FAILURE);
            }
        } else {
            return new GenericResponse.GenericResponseData(false, FAILURE);
        }

    }

    /**
     * Resends a registration tokan
     *
     * @param userId the user id
     */
    @Operation(description = "Resends a registration token")
    @GetMapping("/user/resendRegistrationToken")
    @ResponseBody
    public GenericResponse.GenericResponseData resendRegistrationToken(@Parameter(description = "The user id") @RequestParam Long userId) {

        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            VerificationToken existingToken = accountService.findTokenByUser(user.get());
            VerificationToken newToken = accountService.generateNewVerificationToken(existingToken.getToken());

            Locale locale = LocaleUtils.toLocale(user.get().getPreferredLanguage().name());
            String confirmationUrl = domain + REGISTRATION_CONFIRMATION_CALL + "/" + user.get().getPreferredLanguage().name() + "/" + newToken;

            LOGGER.info("Sending a message of type REGISTRATION to email addresses " + user.get().getEmail());
            Response response = sendgridService.sendMessage(user.get().getEmail(), MessageType.SUBSCRIPTION, locale, user.get().getFirstName(), confirmationUrl, null);
            if(response.getStatusCode() == 200) {
                LOGGER.info("Registration email sent.");
                return new GenericResponse.GenericResponseData(true, SUCCESS);
            } else {
                LOGGER.info("Registration email not sent.");
                return new GenericResponse.GenericResponseData(false, FAILURE);
            }
        } else {
            LOGGER.info("Registration email not sent.");
            return new GenericResponse.GenericResponseData(false, FAILURE);
        }
    }

    @Operation(description = "Changes the password of a user from the user account informations")
    @PutMapping("/user/changePassword")
    @ResponseBody
    public GenericResponse.GenericResponseData changePassword(@Parameter(description = "The user id") @RequestParam Long userId,
                                          @Parameter(description = "The password data") @Valid @RequestBody PasswordTo.PasswordChangeData passwordInfo) {

        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            boolean authenticate = authenticationService.authenticate(user.get().getEmail(), passwordInfo.oldPassword());
            if (authenticate) {
                accountService.changeUserPassword(user.get(), passwordInfo.newPassword());
                return new GenericResponse.GenericResponseData(true, SUCCESS);
            } else {
                return new GenericResponse.GenericResponseData(false, INVALID_OLD_PASSWORD);
            }
        } else {
            return new GenericResponse.GenericResponseData(false, USER_NOT_FOUND);
        }
    }

    @Operation(description = "Resets the user password, when a user has forgotten his password and asks for a new one. An email will be then sent to the user with a link allowing him to enter a new password.")
    @PostMapping("/user/resetPassword")
    @ResponseBody
    public GenericResponse.GenericResponseData resetPassword(@Parameter(description = "The user email") @RequestParam  String userEmail) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userEmail));
        if (user.isPresent()) {
             String token = UUID.randomUUID().toString();
            accountService.createPasswordResetTokenForUser(user.get(), token);

            Locale locale = LocaleUtils.toLocale(user.get().getPreferredLanguage().name());
            String resetPasswordUrl = domain + PASSWORD_RESET_CALL + "/" + user.get().getPreferredLanguage().name() + "/" + token;

            LOGGER.info("Sending a message of type PASSWORD_RESET to email addresses " + user.get().getEmail());
            Response response = sendgridService.sendMessage(user.get().getEmail(), MessageType.PASSWORD_RESET, locale, user.get().getFirstName(), resetPasswordUrl, null);
            if(response.getStatusCode() == 202) {
                LOGGER.info("Password reset email sent.");
                return new GenericResponse.GenericResponseData(true, SUCCESS);
            } else {
                LOGGER.info("Password reset email not sent.");
                return new GenericResponse.GenericResponseData(false, FAILURE);
            }
        } else {
            LOGGER.info("Password reset email not sent.");
            return new GenericResponse.GenericResponseData(false, FAILURE);
        }

    }

    @Operation(description = "Checks if the user's reset password token is valid or not. This happens when the user clicks on the reset password url received by email. In case of invalidity of the token, the user won't be able to access the reset password screen.")
    @GetMapping("/user/checkResetPasswordToken")
    @ResponseBody
    public boolean checkResetPasswordToken(@Parameter(description = "The user token") @RequestParam("token") String token) {
        String result = accountService.validatePasswordResetToken(token);
        return result != null;
    }

    /**
     * Confirms a user registration
     *
     * @param languageCode the user language
     * @param token the user token
     */
    @Operation(description = "Confirms a user registration")
    @GetMapping("/user/registrationConfirm/{language}/{token}")
    public String confirmRegistration(@Parameter (description = "The user language code") @PathVariable("language") String languageCode,
                                      @Parameter (description = "The user token") @PathVariable("token") String token,
                                      @Parameter (description = "The model associated") Model model) {

        Locale locale = LocaleUtils.toLocale(languageCode);
        TokenStatus status = accountService.validateVerificationToken(token);

        if(TokenStatus.TOKEN_VALID.equals(status)) {
            model.addAttribute(CONFIRMATION_TEXT_TAG, messageSource.getMessage(ACCOUNT_CONFIRMED, null, locale));
            model.addAttribute(CONNECTION_TEXT_TAG, messageSource.getMessage(ACCOUNT_CONNECTION, null, locale));
            return REGISTRATION_CONFIRMATION_PAGE;
        } else if(TokenStatus.TOKEN_EXPIRED.equals(status)) {
            model.addAttribute(INVALID_TOKEN_TEXT_TAG, UNAUTHORIZED_ACCESS);
            return INVALID_TOKEN_PAGE;
        } else {
            model.addAttribute(INVALID_TOKEN_TEXT_TAG, UNAUTHORIZED_ACCESS);
            return INVALID_TOKEN_PAGE;
        }
    }

    @Operation(description = "Allows entering a new password")
    @GetMapping("/user/enterNewPassword/{language}/{token}")
    public String enterNewPassword(@Parameter (description = "The user language code") @PathVariable("language") String languageCode,
                                   @Parameter (description = "The password reset token") @PathVariable("token") String token,
                                   @Parameter (description = "The model associated") Model model) {

        Locale locale = LocaleUtils.toLocale(languageCode);
        String result = accountService.validatePasswordResetToken(token);
        User user = accountService.getUserByPasswordResetToken(token);

        if(result == null && user != null) {
            model.addAttribute(RESET_TOKEN_TAG, token);
            model.addAttribute(CHANGE_PASSWORD_TITLE_TAG, messageSource.getMessage(ACCOUNT_CHANGE_PASSWORD, null, locale));
            model.addAttribute(NEW_PASSWORD_TEXT_TAG, messageSource.getMessage(ACCOUNT_NEW_PASSWORD, null, locale));
            model.addAttribute(CONFIRM_NEW_PASSWORD_TEXT_TAG, messageSource.getMessage(ACCOUNT_CONFIRM_NEW_PASSWORD, null, locale));
            model.addAttribute(FUNCTION_SAVE_TAG, messageSource.getMessage(FUNCTION_SAVE, null, locale));
            model.addAttribute(PASSWORD_CHANGED_TEXT_TAG, messageSource.getMessage(ACCOUNT_PASSWORD_CHANGED, null, locale));
            model.addAttribute(PASSWORD_NOT_CHANGED_TEXT_TAG, messageSource.getMessage(ACCOUNT_PASSWORD_NOT_CHANGED, null, locale));
            model.addAttribute(PASSWORD_NOT_MATCHING_TEXT_TAG, messageSource.getMessage(PASSWORD_NOT_MATCHING, null, locale));
            model.addAttribute(PASSWORD_FORMAT_TEXT_TAG, messageSource.getMessage(PASSWORD_FORMAT, null, locale));
            model.addAttribute(EMPTY_FIELD_TEXT_TAG, messageSource.getMessage(EMPTY_FIELD, null, locale));
            return PASSWORD_RESET_PAGE;
        } else {
            model.addAttribute(INVALID_TOKEN_TEXT_TAG, UNAUTHORIZED_ACCESS);
            return INVALID_TOKEN_PAGE;
        }
    }

    @Operation(description = "Saves the new password entered by the user following his reset password request, his click on the url received by email and, if the token is valid, his filling of a new password from the reset password screen.")
    @PostMapping("/user/saveNewPassword")
    @ResponseBody
    public boolean saveNewPassword(@Parameter(description = "The password data") @Valid @RequestBody PasswordTo.PasswordResetData passwordResetData) {

        boolean passwordChanged = false;
        String result = accountService.validatePasswordResetToken(passwordResetData.token());
        User user = accountService.getUserByPasswordResetToken(passwordResetData.token());

        if(result == null && user != null && passwordResetData.password().equals(passwordResetData.confirmPassword())) {
            passwordChanged = accountService.resetUserPassword(user, passwordResetData.password(), passwordResetData.token());
        }

        return passwordChanged;
    }

}