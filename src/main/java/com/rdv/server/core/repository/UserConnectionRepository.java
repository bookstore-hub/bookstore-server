package com.rdv.server.core.repository;


import com.rdv.server.core.entity.UserConnection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserConnectionRepository extends CrudRepository<UserConnection, Long> {

}
