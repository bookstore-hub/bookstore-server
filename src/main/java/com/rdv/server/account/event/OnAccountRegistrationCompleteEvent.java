package com.rdv.server.account.event;

import com.rdv.server.core.entity.User;
import org.springframework.context.ApplicationEvent;


public class OnAccountRegistrationCompleteEvent extends ApplicationEvent {


    private final User user;
    private final String languageCode;

    public OnAccountRegistrationCompleteEvent(User user, String languageCode) {
        super(user);
        this.user = user;
        this.languageCode = languageCode;
    }


    public User getUser() { return user; }

    public String getLanguageCode() {
        return languageCode;
    }
    
}
