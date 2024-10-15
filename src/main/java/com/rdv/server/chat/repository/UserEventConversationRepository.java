package com.rdv.server.chat.repository;

import com.rdv.server.chat.entity.UserEventConversation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author davidgarcia
 */

@Repository
public interface UserEventConversationRepository extends CrudRepository<UserEventConversation, Long> {

}


