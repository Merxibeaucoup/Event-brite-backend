package com.edgar.clone.eventbrite.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edgar.clone.eventbrite.models.Event;
import com.edgar.clone.eventbrite.models.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
	
	boolean existsByEvent(Event event);
	
	Optional<Ticket>findByEvent(Event event);

}
