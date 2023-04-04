package com.edgar.clone.eventbrite.services;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edgar.clone.eventbrite.models.Event;
import com.edgar.clone.eventbrite.models.Purchase;
import com.edgar.clone.eventbrite.models.user.User;
import com.edgar.clone.eventbrite.repositories.EventRepository;
import com.edgar.clone.eventbrite.repositories.PurchaseRepository;

@Service
public class PurchaseService {
	
	
	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@Autowired
	private EventRepository eventRepository;
	
	
	public Purchase makePurchase(Purchase purchase, User user) {
		
		Optional<Event> event_going =  eventRepository.findByEventTitle(purchase.getEventName());
		Event event = event_going.get();
		
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
				purchase.setEvent(event);
				purchase.setUser(user);
			}
		}
		
		
		
		eventRepository.save(event);
		return purchaseRepository.save(purchase);
		
	}
	
	
	
	
	
	/** utils **/
	private boolean isExists(String name) {
		if (eventRepository.existsByEventTitle(name)) {
			return true;
		}
		
		else 
			return false;
		
	}
	
	
	
	

}
