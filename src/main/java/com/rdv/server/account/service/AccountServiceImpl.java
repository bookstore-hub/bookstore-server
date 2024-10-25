package com.rdv.server.account.service;

import com.rdv.server.account.entity.*;
import com.rdv.server.account.to.TokenStatus;
import com.rdv.server.core.entity.*;
import com.rdv.server.account.repository.PasswordResetTokenRepository;
import com.rdv.server.core.repository.UserRepository;
import com.rdv.server.core.repository.VerificationTokenRepository;
import com.rdv.server.storage.adapter.AzureBlobAdapter;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.rdv.server.account.to.TokenStatus.*;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordResetTokenRepository passwordTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AzureBlobAdapter azureBlobAdapter;

    public AccountServiceImpl(UserRepository userRepository, VerificationTokenRepository tokenRepository, PasswordResetTokenRepository passwordTokenRepository,
                              BCryptPasswordEncoder passwordEncoder, AzureBlobAdapter azureBlobAdapter) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordTokenRepository = passwordTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.azureBlobAdapter = azureBlobAdapter;
    }


    private User saveUserChanges(User user) {
        return userRepository.save(user);
    }

    @Override
    public User registerUserAccount(final User user) {
        LOGGER.info("User registration: Username - {}", user.getUsername());

        User alreadyExistingUser = userRepository.findByEmail(user.getEmail());
        if(alreadyExistingUser != null) {
            return null;
        } else {
            return saveUserChanges(user);
        }
    }

    @Override
    public void updateUserAccount(User user, User userUpdated) {
        LOGGER.info("User update: Username - {}", user.getUsername());

        boolean profilePictureChanged = isProfilePictureChanged(user, userUpdated);
        boolean shortBioChanged = isShortBioChanged(user, userUpdated);

        if(profilePictureChanged || shortBioChanged){
            user.setStatus(SubscriptionStatus.ASSESSING);
            if(profilePictureChanged) {
                handleChangeOfProfilePicture(user, user.getPhoto(), userUpdated.getPhoto());
            }
        }
    }

    private boolean isProfilePictureChanged(User user, User userUpdated) {
        boolean changed = false;
        String currentProfilePicture = user.getPhoto();
        if(currentProfilePicture != null && !currentProfilePicture.equals(userUpdated.getPhoto())) {
            changed = true;
        }
        return changed;
    }

    private boolean isShortBioChanged(User user, User userUpdated) {
        boolean changed = false;
        String currentShortBio = user.getShortBio();
        if(currentShortBio != null && !currentShortBio.equals(userUpdated.getShortBio())) {
            changed = true;
        }
        return changed;
    }

    private void handleChangeOfProfilePicture(User user, String currentProfilePicture, String newProfilePicture) {
        azureBlobAdapter.deleteProfilePicture(currentProfilePicture);
        user.setPhoto(newProfilePicture);
    }

    @Override
    public void updateUserLanguage(User user, String languageCode) {
        user.setPreferredLanguage(Language.valueOf(languageCode));
        saveUserChanges(user);
    }

    @Override
    public void updateMessagingToken(User user, String userMessagingToken) {
        user.setMessagingToken(userMessagingToken);
        saveUserChanges(user);
    }

    @Override
    public void logOutMessagingToken(User user) {
        user.setMessagingToken(null);
        saveUserChanges(user);
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
    public void deleteUserAccount(final User user) {
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
        user.setLastModificationDate(OffsetDateTime.now());
        saveUserChanges(user);
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
        saveUserChanges(user);
    }

    @Override
    public void resetUserPassword(User user, final String password, String token) {
        user.setPassword(passwordEncoder.encode(password));
        user.setLastModificationDate(OffsetDateTime.now());
        saveUserChanges(user);

        PasswordResetToken passwordResetToken = passwordTokenRepository.findByToken(token);
        passwordTokenRepository.delete(passwordResetToken);
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

        saveUserChanges(user);
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