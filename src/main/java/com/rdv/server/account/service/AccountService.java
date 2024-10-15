package com.rdv.server.account.service;

import com.rdv.server.account.to.TokenStatus;
import com.rdv.server.core.entity.User;
import com.rdv.server.core.entity.VerificationToken;
import com.rdv.server.core.to.UserTo;


public interface AccountService {

    User registerUserAccount(UserTo.Creation userInfo, String languageCode);

    User updateUserAccount(User user, UserTo.Update userInfo, String languageCode);

    User updateUserLanguage(User user, String languageCode);

    User getUser(String verificationToken);

    User deleteUserAccount(User user);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken generateNewVerificationToken(String token);

    void createPasswordResetTokenForUser(User user, String token);

    User getUserByPasswordResetToken(String token);

    void changeUserPassword(User user, String password);

    boolean resetUserPassword(User user, String password, String token);

    TokenStatus validateVerificationToken(String token);

    VerificationToken findTokenByUser(User user);

    String validatePasswordResetToken(String token);

}