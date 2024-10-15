package com.rdv.server.mail.to;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author davidgarcia
 */

@Data
@NoArgsConstructor
public class MailContent {

    private String subject;
    private String content;
    private String content2;
    private String content3;
    private String content4;
    private String link;
    private String textLink;
    private String signature;


    public MailContent(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

}
