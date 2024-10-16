package com.rdv.server.mail.controller;

import com.rdv.server.mail.service.SendGridService;
import com.rdv.server.core.entity.Language;
import com.rdv.server.mail.to.MessageType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Locale;

/**
 * @author davidgarcia
 */

@RestController
@CrossOrigin
@RequestMapping("/mail")
@Tag(name = "MailController", description = "Set of endpoints to handle the email logic")
public class MailController {

    private static final Log LOGGER = LogFactory.getLog(MailController.class);

    private final SendGridService sendgridService;

    public MailController(SendGridService sendgridService) {
        this.sendgridService = sendgridService;
    }


    /**
     * Sends a test email
     *
     * @param email    the user email
     * @param type    the message type
     * @param language    the language
     * @param parameter    a parameter to add to the email content
     */
    @GetMapping(value = "/sendEmail")
    @Operation(description = "Sends a test email to a user")
    public void sendEmail(@Parameter(description = "The user's email")
                          @RequestParam String email,
                          @Parameter(description = "The message type")
                          @RequestParam MessageType type,
                          @Parameter(description = "The language")
                          @RequestParam(defaultValue = "fr") Language language,
                          @Parameter(description = "A parameter to include in the message")
                          @RequestParam(required = false) String parameter,
                          @Parameter(description = "A url to include in the message")
                          @RequestParam(required = false) String url,
                          @Parameter(description = "A date to include in the message")
                          @RequestParam(required = false) OffsetDateTime date) {

        Locale locale = LocaleUtils.toLocale(language.name());

        LOGGER.info("Sending message with type " + type + " to email address " + email);
        sendgridService.sendMessage(email, type, locale, parameter, url, date);
        LOGGER.info("Email sent to address " + email + ".");
    }

}
