package com.rdv.server.core.repository;

import com.rdv.server.core.entity.User;
import org.springframework.data.jpa.repository.Query;
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
     * Returns all users
     *
     * @return all users
     */
    @Query("select u from User u")
    List<User> findAllUsers();


    /**
     * Returns a user by email
     *
     * @param email the user email
     * @return the user
     */
    User findByEmail(@Param("email") String email);

}


