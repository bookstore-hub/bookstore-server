package com.rdv.server.account.listener;

import com.rdv.server.account.event.OnAccountRegistrationCompleteEvent;
import com.rdv.server.account.service.AccountService;
import com.rdv.server.core.entity.User;
import com.rdv.server.core.repository.UserRepository;
import com.rdv.server.mail.service.SendGridService;
import com.rdv.server.mail.to.MessageType;
import com.rdv.server.notification.service.FirebaseMessagingService;
import com.rdv.server.notification.to.Note;
import com.sendgrid.Response;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
public class AccountRegistrationListener implements ApplicationListener<OnAccountRegistrationCompleteEvent> {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String REGISTRATION_CONFIRMATION_CALL = "/user/registrationConfirm";

    @Autowired
    private AccountService accountService;
    @Autowired
    private SendGridService sendgridService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    @Value("${azure.backend.domain}")
    private String domain;


    @Override
    public void onApplicationEvent(final OnAccountRegistrationCompleteEvent onAccountRegistrationCompleteEvent) {
        this.confirmRegistration(onAccountRegistrationCompleteEvent);
    }

    private void confirmRegistration(final OnAccountRegistrationCompleteEvent onAccountRegistrationCompleteEvent) {
        final User user = onAccountRegistrationCompleteEvent.getUser();
        LOGGER.info("User " + user);
        final String token = UUID.randomUUID().toString();
        accountService.createVerificationTokenForUser(user, token);

        Locale locale = LocaleUtils.toLocale(onAccountRegistrationCompleteEvent.getLanguageCode());
        String confirmationUrl = domain + REGISTRATION_CONFIRMATION_CALL + "/" + user.getPreferredLanguage().name() + "/" + token;

        LOGGER.info("Sending a message of type REGISTRATION to email addresses " + user.getEmail());
        Response response = sendgridService.sendMessage(user.getEmail(), MessageType.SUBSCRIPTION, locale, user.getFirstName() != null ? user.getFirstName() : user.getLastName(), confirmationUrl, null);
       if(response != null) {
            LOGGER.info("Registration email sent.");

           //Temporary set: Inform admin of new subscription
           String adminMessagingToken = userRepository.findByEmail("david.garcia@hotmail.be").getMessagingToken();
           String message = "NOUVELLE INSCRIPTION: " + user.getFirstName() + " " + user.getLastName() + " - " + user.getPreferredLanguage();
           Note note = new Note(message);
           try {
               firebaseMessagingService.sendNotification(note, adminMessagingToken);
           } catch (ExecutionException | InterruptedException e) {
               LOGGER.info("An error occurred while sending notification ", e);
           }

       } else {
            LOGGER.info("Registration email could not be sent.");
       }
    }

}
