package com.rdv.server.account.repository;

import com.rdv.server.account.entity.PasswordResetToken;
import com.rdv.server.core.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.stream.Stream;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);

    Stream<PasswordResetToken> findAllByExpirationDateLessThan(Date now);

    void deleteByExpirationDateLessThan(Date now);

    @Modifying
    @Query("Delete from PasswordResetToken t where t.expirationDate <= ?1")
    void deleteAllExpiredSince(Date now);
}