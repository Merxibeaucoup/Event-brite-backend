package com.edgar.clone.eventbrite.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edgar.clone.eventbrite.exceptions.EventDoesntExistException;
import com.edgar.clone.eventbrite.models.Event;
import com.edgar.clone.eventbrite.models.Ticket;
import com.edgar.clone.eventbrite.models.user.User;
import com.edgar.clone.eventbrite.repositories.EventRepository;
import com.edgar.clone.eventbrite.repositories.TicketRepository;

@Service
public class TicketService {
	
	@Autowired
	private TicketRepository ticketepRepository;
	
	
	@Autowired
	private EventRepository eventRepository;
	
	public Ticket ticketsForEvent(Ticket ticket, long EventId, User user) {
		
		Optional<Event> event = eventRepository.findById(EventId);		
		if((event.get().getOrganizer().getId() == user.getId())) {				
			if(event.isPresent()) {							
				ticket.setEvent(event.get());
				ticket.setUser(user);
				ticket.setIsSaleEnded(false);	
				
				
//				/* try a quartz scheduler to update saleEnded if ended everyday*/
//				
//				LocalDateTime today = LocalDateTime.now();				
//				if(today.isAfter(ticket.getTicketSaleEndDate())) {
//					ticket.setIsSaleEnded(true);
//				}
				
				return ticketepRepository.save(ticket);
				
			}
			
			else 
				throw new EventDoesntExistException("Event doesnt exist");
			
		}
		
		else 
			throw new RuntimeException("you are not the owner of this event , cant creat tickets");
		
		
		
		
		
	}

}
