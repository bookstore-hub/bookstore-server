package com.rdv.server.core.repository;

import com.rdv.server.core.entity.User;
import com.rdv.server.core.entity.SubscriptionStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


/**
 * @author davidgarcia
 */

@Repository
public interface UserRepository extends CrudRepository<User, Long> {


    /**
     * Returns a user by email
     *
     * @param email the user email
     * @return the user
     */
    User findByEmail(@Param("email") String email);

    /**
     * Returns a user by username
     *
     * @param username the username
     * @return the user
     */
    List<User> findByUsernameLike(@Param("username") String username);

    /**
     * Returns a user by first name
     *
     * @param firstName the first name
     * @return the user
     */
    List<User> findByFirstNameLike(@Param("firstName") String firstName);

    /**
     * Returns a user by last name
     *
     * @param lastName the last name
     * @return the user
     */
    List<User> findByLastNameLike(@Param("lastName") String lastName);

    /**
     * Returns a user by first name or last name
     *
     * @param firstName the first name
     * @param lastName the last name
     * @return the user
     */
    List<User> findByFirstNameLikeAndLastNameLike(@Param("firstName") String firstName, @Param("lastName") String lastName);

    /**
     * Returns the list of users who have been deactived
     */
    List<User> findByStatus(@Param("status") SubscriptionStatus status);

    /**
     * Returns all users having the lastname starting by a specific alphabet letter
     */
    List<User> findByLastNameStartsWithIgnoreCase(@Param("alphabetLetter") String alphabetLetter);

}


