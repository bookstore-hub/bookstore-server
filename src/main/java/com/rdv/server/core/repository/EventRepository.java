package com.rdv.server.core.repository;


import com.rdv.server.core.entity.Event;
import com.rdv.server.core.entity.EventCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {


    @Query("select e from Event e")
    List<Event> findAllEvents();

    List<Event> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(@Param("endDate") OffsetDateTime endDate, @Param("startDate") OffsetDateTime startDate);

    List<Event> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndCategory(
            @Param("endDate") OffsetDateTime dateEndOfDay, @Param("startDate") OffsetDateTime dateBeginningOfDay, @Param("category") EventCategory category);

    @Query(value = "SELECT * FROM Event e WHERE SIMILARITY(e.title, ?1) > 0.7 or SIMILARITY(e.location_venue, ?1) > 0.7", nativeQuery = true)
    List<Event> findAllEventsMatching(String searchString);

}
