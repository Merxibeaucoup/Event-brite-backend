package com.edgar.clone.eventbrite.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.edgar.clone.eventbrite.enums.TicketType;
import com.edgar.clone.eventbrite.exceptions.EventDoesntExistException;
import com.edgar.clone.eventbrite.exceptions.NotOwnerOfEventException;
import com.edgar.clone.eventbrite.models.Event;
import com.edgar.clone.eventbrite.models.Ticket;
import com.edgar.clone.eventbrite.models.user.User;
import com.edgar.clone.eventbrite.repositories.EventRepository;
import com.edgar.clone.eventbrite.repositories.TicketRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TicketService {
	
	@Autowired
	private TicketRepository ticketepRepository;
		
	@Autowired
	private EventRepository eventRepository;
	
	/** for free or donation based events **/
	private final BigDecimal FREE_OR_DONATION = new BigDecimal("0.00");
	
	public Ticket ticketsForEvent(Ticket ticket, long EventId, User user) {
		
		Optional<Event> event = eventRepository.findById(EventId);		
		if((event.get().getOrganizer().getId() == user.getId())) {				
			if(event.isPresent()) {							
				ticket.setEvent(event.get());
				ticket.setUser(user);
				ticket.setIsSaleEnded(false);
				ticket.setIsTicketSoldOut(false);
				
				if(ticket.getTicketType()== TicketType.FREE || ticket.getTicketType()== TicketType.DONATION) {
					ticket.setTicketPrice(FREE_OR_DONATION);
				}
				
				
				return ticketepRepository.save(ticket);
				
			}
			
			else 
				throw new EventDoesntExistException("Event doesnt exist");
			
		}
		
		else 
			throw new NotOwnerOfEventException("you are not the owner of this event , cant create tickets");
			
	}
	
	
/** cron job to set ticket to sold out **/
	@Scheduled(cron ="*/2 * * * * *")
	public void setTicketToSoldOut() {
		List<Ticket> filter_all_tickets = ticketepRepository
				.findAll()
				.stream()
				.filter(t -> t.getTicketQuantity() != null
				&& t.getIsTicketSoldOut()== false
				&& t.getTicketQuantity() == 0)
				.collect(Collectors.toList());
		
		
		if(filter_all_tickets.size()>0) {
			
			filter_all_tickets
			.stream()
			.forEach(s ->{
				
				Optional<Ticket> event_ticket = ticketepRepository.findById(s.getId());
				Ticket ticket = event_ticket.get();
				
				
				ticket.setIsTicketSoldOut(true);				
				ticketepRepository.save(ticket);
				
				log.info("---------------- Found ticket with quantity equals 0 , setting isSoldOut to true --------------");
				
			});
		}
		log.info("----------------Ticket Scheduler `SOLD OUT` Running--------------");
	}
	
	
	/** cron job to set ticket to sale to ended **/
	@Scheduled(cron ="*/2 * * * * *")
	public void setTicketSaleEnded() {
		
		LocalDateTime today  = LocalDateTime.now();
		
		List<Ticket> filter_all_tickets = ticketepRepository
				.findAll()
				.stream()
				.filter(t -> t.getTicketSaleEndDate() !=null
				&& t.getIsSaleEnded() == false
				&& t.getTicketSaleEndDate().isBefore(today))
				.collect(Collectors.toList());	
		
		if(filter_all_tickets.size()>0) {
			filter_all_tickets
			.stream()
			.forEach(s ->{
				Optional<Ticket> ticket_found = ticketepRepository.findById(s.getId());
				Ticket ticket = ticket_found.get();	
				
				ticket.setIsSaleEnded(true);
				ticketepRepository.save(ticket);
				
				log.info("---------------- Found ticket with sale ended date , setting isSaleEnded to true --------------");
			});
		}
		log.info("----------------Ticket Scheduler `SALE ENDED` Running--------------");
		
	}

}
