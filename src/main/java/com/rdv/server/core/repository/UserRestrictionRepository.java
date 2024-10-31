package com.rdv.server.core.repository;


import com.rdv.server.core.entity.UserRestriction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRestrictionRepository extends CrudRepository<UserRestriction, Long> {

}
