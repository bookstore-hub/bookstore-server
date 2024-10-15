package com.rdv.server.mail.service;

import com.rdv.server.mail.to.MessageType;
import com.sendgrid.Response;

import java.time.OffsetDateTime;
import java.util.Locale;

/**
 * @author davidgarcia
 */

public interface SendGridService {

    /**
     * Sends email as an html
     *
     * @param to       the receiver
     * @param type     the message type
     * @param locale   the locale
     * @param variable the variable
     * @param url      the url
     * @param date     the date
     */
    Response sendMessage(String to, MessageType type, Locale locale, String variable, String url, OffsetDateTime date);

    /**
     * Sends email to the team
     *
     * @param subject    the subject
     * @param message  the message
     */
    public Response sendMessageToTeam(String subject, String message);

}
