package com.edgar.clone.eventbrite.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.edgar.clone.eventbrite.exceptions.TicketDoesntExistException;
import com.edgar.clone.eventbrite.exceptions.TicketIsInactiveException;
import com.edgar.clone.eventbrite.models.Event;
import com.edgar.clone.eventbrite.models.Purchase;
import com.edgar.clone.eventbrite.models.user.User;
import com.edgar.clone.eventbrite.repositories.EventRepository;
import com.edgar.clone.eventbrite.repositories.PurchaseRepository;
import com.edgar.clone.eventbrite.requests.UseTicket;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PurchaseService {
	
	
	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@Autowired
	private EventRepository eventRepository;
	

	
	
	
	public Purchase makePurchase(Purchase purchase, User user) {
		
		Optional<Event> event_going =  eventRepository.findByEventTitle(purchase.getEventName());
		Event event = event_going.get();
		
//		Optional<Ticket> ticket_for_event = ticketRepository.findByEvent(event);
		
		if(!isExists(purchase.getEventName())) {
			throw new RuntimeException("Event doesnt Exist");
		}
		
		else {
				
			if(event.getEventTicket().getTicketQuantity() < purchase.getQuantity()) {
				throw new RuntimeException("Tickest quantity requested not sufficient, decrease your ticket amount");
			}
			
			else {
				event.getEventTicket().setTicketQuantity(event.getEventTicket().getTicketQuantity() - purchase.getQuantity());
			}			
			/** Check if user is sending the right payment **/
			BigDecimal totalPrice = new BigDecimal("0.00");	
			BigDecimal totalPayment = new BigDecimal("0.00");
			
			totalPrice = BigDecimal.valueOf(purchase.getQuantity()).multiply(event.getEventTicket().getTicketPrice());		
			totalPayment =  BigDecimal.valueOf(purchase.getQuantity()).multiply(purchase.getAmount());
			
			if(!totalPrice.equals(totalPayment)) {				
				throw new RuntimeException("Payment not Full, submit full payment to purchase");
			}
			
			else {
				
				
				purchase.setIsTicketActive(true);
				purchase.setTicketType(event.getEventTicket().getTicketName());
				purchase.setEvent(event);
				purchase.setTicket(event.getEventTicket());
				purchase.setUser(user);		

			}
		}	
		eventRepository.save(event);
		return purchaseRepository.save(purchase);
		
	}
	/** Get All tickets bought by User **/
	public List<Purchase> allBoughtByUser(User user){
		return purchaseRepository.findByUser(user);
	}
	
	/** Get Purchased Ticket**/
	public Purchase getOneTicketPurchasedById(Long id) {
		return purchaseRepository.findById(id).orElseThrow(()-> new TicketDoesntExistException("You  havent purchased a  ticket for this event") );
	}
	
	/** CHeck in user **/
	public void CheckInTicketAtEvent(UseTicket useTicket, User user) {	
		Optional<Purchase> purchased_ticket = purchaseRepository.findById(useTicket.getPurchasedTicketid());
		Purchase purchased = purchased_ticket.get();
		
		if(isExists(useTicket.getEventName()) && isExistsTicketPurchased(useTicket.getPurchasedTicketid())) {
			
			if(purchased.getIsTicketActive() == false) {
				throw new TicketIsInactiveException("Ticket is inactive, Event might be closed or ticket has already been used ");
			}
			purchased.setIsTicketActive(false);					
			
		}
		
		purchaseRepository.save(purchased);
	}
	
	
	
	
//	quartz scheduler to also set tickets active to false if event date is passed  --> runs every minute
	
	@Scheduled(cron = "0 0/1 * * * ?") 
	public void setExpiredEventsTicketToFalse() {
		LocalDateTime time_now = LocalDateTime.now();
		 
		List<Purchase> filter_all_purchased_tickets = purchaseRepository.findAll()
				.stream()
				.filter(t -> 
				t.getEvent().getEventDateAndTime().getEndDate() !=null &&
				t.getEvent().getEventDateAndTime().getEndDate().isBefore(time_now))			
				.collect(Collectors.toList());
		
				
	if(filter_all_purchased_tickets.size() > 0) 
	
	{
		
		filter_all_purchased_tickets
		.stream()
		.forEach(e ->{			
			Optional<Purchase> purchased_ticket = purchaseRepository.findById(e.getId());
			Purchase purchase = purchased_ticket.get();
						
			purchase.setIsTicketActive(false);	
			
			purchaseRepository.save(purchase);
		});
		
		log.info("----------------expired ticket found updating ticket to inactive--------------");
		
	}
	
	}
	
	
	/** utils **/
	
	/** check if event exists **/
	private boolean isExists(String name) {
		if (eventRepository.existsByEventTitle(name)) {
			return true;
		}
		
		else 
			return false;
		
	}
	
	/** check if ticket is purchased Exists **/
	private  boolean isExistsTicketPurchased(Long id ) {
		if(purchaseRepository.existsById(id)) {
			return true ;
		}
		
		else
			return false;
	}
	
	
	
	

}
