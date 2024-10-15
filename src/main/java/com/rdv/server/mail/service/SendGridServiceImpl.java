package com.rdv.server.mail.service;

import com.rdv.server.core.util.DateUtil;
import com.rdv.server.mail.to.MailContent;
import com.rdv.server.mail.to.MessageType;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author davidgarcia
 */

@Service
public class SendGridServiceImpl implements SendGridService {

    private static final Log LOGGER = LogFactory.getLog(SendGridServiceImpl.class);

    private static final String DOT = ".";
    private static final String SUBJECT = "subject";
    private static final String CONTENT = "content";
    private static final String CONTENT_2 = "content2";
    private static final String CONTENT_3 = "content3";
    private static final String CONTENT_4 = "content4";
    private static final String LINK_TEXT = "link_text";
    private static final String LINK = "link";
    private static final String SIGNATURE = "signature";


    @Value("${sender.email}")
    String senderEmail;

    @Value("${sender.email2}")
    String senderEmail2;

    @Value("${sender.name}")
    String senderName;

    @Value("${email.template1}")
    private String TEMPLATE_1;

    @Value("${email.template2}")
    private String TEMPLATE_2;

    @Value("${email.template3}")
    private String TEMPLATE_3;

    @Autowired
    MessageSource messageSource;

    private SendGrid sendGridClient;

    @Autowired
    public SendGridServiceImpl(SendGrid sendGridClient) {
        this.sendGridClient = sendGridClient;
    }


    /**
     * @see SendGridService#sendMessage(String, MessageType, Locale, String, String, OffsetDateTime)
     */
    @Override
    public Response sendMessage(String to, MessageType type, Locale locale, String parameter, String url, OffsetDateTime date) {
        MailContent mailContent = retrieveMailContent(type, locale, parameter, url, date);
        Response response = sendEmail(to, mailContent, type);
        LOGGER.info("Status Code: " + response.getStatusCode() + ", Body: " + response.getBody() + ", Headers: "
                + response.getHeaders());
        return response;
    }

    /**
     * @see SendGridService#sendMessageToTeam(String, String)
     */
    @Override
    public Response sendMessageToTeam(String subject, String message) {
        //Message from sender to sender
        Response response = sendEmail(senderEmail, new MailContent(subject, message), MessageType.CONTACT_US);
        LOGGER.info("Status Code: " + response.getStatusCode() + ", Body: " + response.getBody() + ", Headers: "
                + response.getHeaders());
        return response;
    }

    /**
     * Sends an email
     *
     * @param to the receiver
     * @param mailContent  the mail content
     */
    private Response sendEmail(String to, MailContent mailContent, MessageType type) {
        LOGGER.info("Sending email with subject " + mailContent.getSubject() + " to address " + to);
        Mail mail = new Mail();
        mail.setFrom(new Email(!MessageType.INVITATION.equals(type) ? senderEmail : senderEmail2, senderName));

        String templateId = getTemplateId(type);

        if(templateId != null) {
            mail.setTemplateId(templateId);
            Personalization personalization = new Personalization();
            personalization.addDynamicTemplateData(SUBJECT, mailContent.getSubject());
            personalization.addDynamicTemplateData(CONTENT, mailContent.getContent());
            personalization.addDynamicTemplateData(CONTENT_2, mailContent.getContent2());
            personalization.addDynamicTemplateData(CONTENT_3, mailContent.getContent3());
            personalization.addDynamicTemplateData(CONTENT_4, mailContent.getContent4());
            personalization.addDynamicTemplateData(LINK_TEXT, mailContent.getTextLink());
            personalization.addDynamicTemplateData(LINK, mailContent.getLink());

            if(!MessageType.CONTACT_US.equals(type)) {
                personalization.addDynamicTemplateData(SIGNATURE, mailContent.getSignature());
            }

            personalization.addTo(new Email(to));
            mail.addPersonalization(personalization);
        }

        Request request = new Request();
        Response response = null;
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            response = sendGridClient.api(request);

        } catch (IOException ex) {
            LOGGER.debug("An error occured while sending an email to " + to + " : " + ex);
        }
        return response;
    }

    /**
     * Retrieves an email content for a specific type in the language targeted
     * @param type The message type
     * @param locale The locale
     * @param parameter An optional parameter
     * @param url An optional url
     * @param date An optional date
     *
     */
    public MailContent retrieveMailContent(MessageType type, Locale locale, String parameter, String url, OffsetDateTime date){
        LOGGER.info("Retrieving mail content for type " + type);

        List<Object> objects = Arrays.asList(parameter, date != null ? DateUtil.formatLongDateAndTime(date, Locale.FRENCH) : null);

        Locale localeToApply = Locale.forLanguageTag("mtl"); //MTL : French followed by English. For now we only use this one

        MailContent mailContent = new MailContent();

        mailContent.setSubject(messageSource.getMessage(type.getCode() + DOT + SUBJECT, objects.toArray(Object[]::new), localeToApply));
        mailContent.setContent(messageSource.getMessage(type.getCode() + DOT + CONTENT, objects.toArray(Object[]::new), localeToApply));

        if(MessageType.INVITATION.equals(type)) {
            mailContent.setContent2(messageSource.getMessage(type.getCode() + DOT + CONTENT_2, null, localeToApply));
            mailContent.setContent3(messageSource.getMessage(type.getCode() + DOT + CONTENT_3, null, localeToApply));
            mailContent.setContent3(messageSource.getMessage(type.getCode() + DOT + CONTENT_3, null, localeToApply));
            mailContent.setContent4(messageSource.getMessage(type.getCode() + DOT + CONTENT_4, null, localeToApply));
        }

        if(MessageType.SUBSCRIPTION.equals(type) || MessageType.PASSWORD_RESET.equals(type) || MessageType.INVITATION.equals(type) || MessageType.ADVERT_VALIDATED_TO_PAY.equals(type)) {
            mailContent.setTextLink(messageSource.getMessage(type.getCode() + DOT + LINK_TEXT, null, localeToApply));
            mailContent.setLink(url);
        }

        if(!MessageType.INVITATION.equals(type)) {
            mailContent.setSignature(messageSource.getMessage(SIGNATURE, null, localeToApply));
        }

        return mailContent;
    }


    private String getTemplateId(MessageType type) {
        switch(type) {
            case SUBSCRIPTION:
            case PASSWORD_RESET:
            case ADVERT_VALIDATED_TO_PAY:
                return TEMPLATE_1;
            case INVITATION: return TEMPLATE_2;
            default: return TEMPLATE_3;
        }
    }

}
