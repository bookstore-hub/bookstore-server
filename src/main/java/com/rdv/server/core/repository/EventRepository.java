package com.rdv.server.core.repository;


import com.rdv.server.core.entity.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long> {


}
