package com.rdv.server.core.repository;

import com.rdv.server.core.entity.User;
import com.rdv.server.core.entity.VerificationToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.stream.Stream;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

    Stream<VerificationToken> findAllByExpirationDateLessThan(Date now);

    void deleteByExpirationDateLessThan(Date now);

    @Modifying
    @Query("Delete from VerificationToken t where t.expirationDate <= ?1")
    void deleteAllExpiredSince(Date now);
}
