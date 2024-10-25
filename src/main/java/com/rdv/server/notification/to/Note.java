package com.rdv.server.notification.to;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author david.garcia
 */
@Data
@NoArgsConstructor
public class Note {

    private String subject = "Rendez-vous";
    private String content;
    private Map<String, String> data;
    private String image;


    public Note(String content) {
        this.content = content;
    }

    public Note(Map<String, String> data) {
        this.data = data;
    }

    public Note(String content, Map<String, String> data) {
        this.content = content;
        this.data = data;
    }

    public Note(String subject, String content, Map<String, String> data) {
        this.subject = subject;
        this.content = content;
        this.data = data;
    }

}
