package com.rdv.server.account.service;

import com.rdv.server.account.to.TokenStatus;
import com.rdv.server.core.entity.User;
import com.rdv.server.core.entity.VerificationToken;


public interface AccountService {

    User registerUserAccount(User user);

    void updateUserAccount(User user, User userUpdated);

    void updateUserLanguage(User user, String languageCode);

    void updateMessagingToken(User user, String userMessagingToken);

    void logOutMessagingToken(User user);

    User getUser(String verificationToken);

    void deleteUserAccount(User user);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken generateNewVerificationToken(String token);

    void createPasswordResetTokenForUser(User user, String token);

    User getUserByPasswordResetToken(String token);

    void changeUserPassword(User user, String password);

    void resetUserPassword(User user, String password, String token);

    TokenStatus validateVerificationToken(String token);

    VerificationToken findTokenByUser(User user);

    String validatePasswordResetToken(String token);

}