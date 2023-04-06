package com.edgar.clone.eventbrite.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edgar.clone.eventbrite.exceptions.TicketDoesntExistException;
import com.edgar.clone.eventbrite.exceptions.TicketIsInactiveException;
import com.edgar.clone.eventbrite.models.Event;
import com.edgar.clone.eventbrite.models.Purchase;
import com.edgar.clone.eventbrite.models.user.User;
import com.edgar.clone.eventbrite.repositories.EventRepository;
import com.edgar.clone.eventbrite.repositories.PurchaseRepository;
import com.edgar.clone.eventbrite.requests.UseTicket;

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
	
	public List<Purchase> allBoughtByUser(User user){
		return purchaseRepository.findByUser(user);
	}
	
	
	public Purchase getOneTicketPurchasedById(Long id) {
		return purchaseRepository.findById(id).orElseThrow(()-> new TicketDoesntExistException("You  havent purchased a  ticket for this event") );
	}
	
	
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
	
	
	
	
//	quartz scheduler to also set tickets active to false if event date is passed 
	
	
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
