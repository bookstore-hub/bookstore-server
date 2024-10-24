package com.rdv.server.core.repository;


import com.rdv.server.core.entity.UserEventInvitation;
import com.rdv.server.core.entity.UserEventInvitationId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEventInvitationRepository extends CrudRepository<UserEventInvitation, UserEventInvitationId> {

}
