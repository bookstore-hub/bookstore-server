package com.rdv.server.account.service;

import com.rdv.server.account.entity.*;
import com.rdv.server.account.to.TokenStatus;
import com.rdv.server.core.entity.*;
import com.rdv.server.account.repository.PasswordResetTokenRepository;
import com.rdv.server.core.repository.UserRepository;
import com.rdv.server.core.repository.VerificationTokenRepository;
import com.rdv.server.core.to.UserTo;
import com.rdv.server.storage.adapter.AzureBlobAdapter;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.rdv.server.account.to.TokenStatus.*;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AzureBlobAdapter azureBlobAdapter;


    @Override
    public User registerUserAccount(final UserTo.Creation userInfo, String languageCode) {

        LOGGER.info("User registration info: Username - " + userInfo.username());

        User alreadyExistingUser = userRepository.findByEmail(userInfo.email());

        if(alreadyExistingUser != null) {
            return null;
        } else {
            final User user = new User();

            user.setUsername(userInfo.username().trim());
            user.setEmail(userInfo.email().trim());
            user.setPhoto(userInfo.photo());
            user.setBirthDate(userInfo.birthDate());
            user.setAccountCreationDateAndTime(OffsetDateTime.now());
            user.setPassword(passwordEncoder.encode(userInfo.password()));
            user.setStatus(SubscriptionStatus.PENDING);
            user.setPreferredLanguage(Language.valueOf(languageCode));
            user.setLastModificationDate(OffsetDateTime.now());

            return userRepository.save(user);
        }
    }

    @Override
    public User updateUserAccount(User user, UserTo.Update userInfo, String languageCode) {

        LOGGER.info("User update info: First name - " + userInfo.firstName() + ", Last name - " + userInfo.lastName());

        boolean profilePictureChanged = isProfilePictureChanged(user, userInfo);

        if(profilePictureChanged){
            user.setStatus(SubscriptionStatus.ASSESSING);
            handleChangeOfProfilePicture(user, user.getPhoto(), userInfo.photo());
        }

        if(userInfo.firstName() != null) {
            user.setFirstName(userInfo.firstName().trim());
        }
        if(userInfo.lastName() != null) {
            user.setLastName(userInfo.lastName().trim());
        }
        if(userInfo.username() != null) {
            user.setUsername(userInfo.username().trim());
        }
        if(userInfo.email() != null) {
            user.setEmail(userInfo.email().trim());
        }

        if(userInfo.gender() != null) {
            user.setGender(userInfo.gender());
        }
        if(userInfo.birthDate() != null) {
            user.setBirthDate(userInfo.birthDate());
        }
        if(userInfo.phoneNr() != null) {
            user.setPhoneNr(userInfo.phoneNr().trim());
        }
        if(userInfo.shortBio() != null) {
            user.setShortBio(userInfo.shortBio().trim());
        }

        user.setPreferredLanguage(Language.valueOf(languageCode));
        user.setLastModificationDate(OffsetDateTime.now());

        return userRepository.save(user);
    }

    private boolean isProfilePictureChanged(User user, UserTo.Update userInfo) {
        boolean changed = false;
        String currentProfilePicture = user.getPhoto();
        if(currentProfilePicture != null && !currentProfilePicture.equals(userInfo.photo())) {
            changed = true;
        }
        return changed;
    }

    private void handleChangeOfProfilePicture(User user, String currentProfilePicture, String newProfilePicture) {
        azureBlobAdapter.deleteProfilePicture(currentProfilePicture);
        user.setPhoto(newProfilePicture);
    }

    @Override
    public User updateUserLanguage(User user, String languageCode) {
        user.setPreferredLanguage(Language.valueOf(languageCode));
        return userRepository.save(user);
    }

    @Override
    public User getUser(final String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }

    @Override
    public User deleteUserAccount(final User user) {
        final VerificationToken verificationToken = tokenRepository.findByUser(user);
        if (verificationToken != null) {
            tokenRepository.delete(verificationToken);
        }

        final PasswordResetToken passwordToken = passwordTokenRepository.findByUser(user);
        if (passwordToken != null) {
            passwordTokenRepository.delete(passwordToken);
        }

        user.setStatus(SubscriptionStatus.ENDED);
        user.setMessagingToken(null);
        return userRepository.save(user);
    }

    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID().toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }

    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        PasswordResetToken currentPasswordToken = passwordTokenRepository.findByUser(user);
        if(currentPasswordToken != null) {
            passwordTokenRepository.delete(currentPasswordToken);
        }

        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    @Override
    public User getUserByPasswordResetToken(final String token) {
        PasswordResetToken passwordResetToken = passwordTokenRepository.findByToken(token);
        return passwordResetToken != null ? passwordResetToken.getUser() : null;
    }

    @Override
    public void changeUserPassword(User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        user.setLastModificationDate(OffsetDateTime.now());
        userRepository.save(user);
    }

    @Override
    public boolean resetUserPassword(User user, final String password, String token) {
        user.setPassword(passwordEncoder.encode(password));
        user.setLastModificationDate(OffsetDateTime.now());
        user = userRepository.save(user);
        if(user != null) {
            PasswordResetToken passwordResetToken = passwordTokenRepository.findByToken(token);
            passwordTokenRepository.delete(passwordResetToken);
            return true;
        }
        return false;
    }

    @Override
    public TokenStatus validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        if (verificationToken.getExpirationDate().isBefore(OffsetDateTime.now())) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setStatus(SubscriptionStatus.ASSESSING);
        user.setLastModificationDate(OffsetDateTime.now());
        userRepository.save(user);
        tokenRepository.delete(verificationToken);

        return TOKEN_VALID;
    }

    @Override
    public VerificationToken findTokenByUser(User user) {
        return tokenRepository.findByUser(user);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        return passToken.getExpirationDate().isBefore(OffsetDateTime.now());
    }

}