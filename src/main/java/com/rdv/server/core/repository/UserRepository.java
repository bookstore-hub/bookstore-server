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


    @Query("select u from User u")
    List<User> findAllUsers();

    User findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM User u WHERE SIMILARITY(u.username, ?1) > 0.7 or SIMILARITY(u.firstName, ?1) > 0.7 or SIMILARITY(u.lastName, ?1) > 0.7", nativeQuery = true)
    List<User> findAllUsersMatching(String searchString);

}


