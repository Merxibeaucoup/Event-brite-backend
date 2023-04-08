package com.edgar.clone.eventbrite.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edgar.clone.eventbrite.models.Event;
import com.edgar.clone.eventbrite.models.user.User;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

	List<Event> findByOrganizer(User organizer);
	
	Optional<Event> findByEventTitle(String eventTitle);	
	boolean existsByEventTitle (String eventTitle);
	
	Optional<Event> findByEventTown(String eventTown);	
	List<Event> findByEventVenue(String eventVenueAddressTown);
	boolean existsByEventTown (String eventTown);
	


}
