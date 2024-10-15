package com.rdv.server.chat.repository;

import com.rdv.server.chat.entity.EventConversation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author davidgarcia
 */

@Repository
public interface EventConversationRepository extends CrudRepository<EventConversation, Long> {

}


