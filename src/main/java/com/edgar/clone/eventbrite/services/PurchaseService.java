package com.edgar.clone.eventbrite.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.edgar.clone.eventbrite.exceptions.EventDoesntExistException;
import com.edgar.clone.eventbrite.exceptions.PaymentInsufficientException;
import com.edgar.clone.eventbrite.exceptions.TicketDoesntExistException;
import com.edgar.clone.eventbrite.exceptions.TicketIsInactiveException;
import com.edgar.clone.eventbrite.exceptions.TicketsSoldOutException;
import com.edgar.clone.eventbrite.models.Event;
import com.edgar.clone.eventbrite.models.MailerModel;
import com.edgar.clone.eventbrite.models.Purchase;
import com.edgar.clone.eventbrite.models.user.User;
import com.edgar.clone.eventbrite.repositories.EventRepository;
import com.edgar.clone.eventbrite.repositories.PurchaseRepository;
import com.edgar.clone.eventbrite.requests.UseTicket;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PurchaseService {
	
	
	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private MailerService mailerService;
	
	
	/** for one ticket per order **/
	private final Integer TICKET_QUANTITY = 1;
	
	
	
	
	public Purchase makePurchase(Purchase purchase, User user) {
		
		Optional<Event> event_for =  eventRepository.findByEventTitle(purchase.getEventName());
		Event event = event_for.get();
		
		if(!isExists(purchase.getEventName()) && event.getEventTicket().getIsSaleEnded( ) == true && event.getIsEventEnded()==true && event.getEventTicket().getIsTicketSoldOut()==true) {
			throw new EventDoesntExistException("cant make purchase -> Event doesnt Exist, Ticket sale has ended or Tickets are soldout ");
		}
		
		else {
			purchase.setQuantity(TICKET_QUANTITY);	
			
			if(event.getEventTicket().getTicketQuantity() < purchase.getQuantity()) {
				throw new TicketsSoldOutException("Tickets sold out");
			}
			
			/* update the num of tickets left for event */
			else {
				event.getEventTicket().setTicketQuantity(event.getEventTicket().getTicketQuantity() - purchase.getQuantity());
			}			
			/** Check if user is sending the right payment **/
			BigDecimal totalPrice = new BigDecimal("0.00");	
			BigDecimal totalPayment = new BigDecimal("0.00");
			
			totalPrice = BigDecimal.valueOf(purchase.getQuantity()).multiply(event.getEventTicket().getTicketPrice());		
			totalPayment =  BigDecimal.valueOf(purchase.getQuantity()).multiply(purchase.getAmount());
			
			if(!totalPrice.equals(totalPayment)) {				
				throw new PaymentInsufficientException("Wrong purchase amount entered, submit correct payment to purchase");
			}
			
			else {
				
				purchase.setPurchaseDate(LocalDateTime.now());
				purchase.setIsTicketActive(true);
				purchase.setTicketType(event.getEventTicket().getTicketName());
				purchase.setEvent(event);
				purchase.setTicket(event.getEventTicket());
				purchase.setUser(user);		

			}
		}
		
		MailerModel mailer = new MailerModel();
		
		Integer tick_num = event.getEventTicket().getTicketQuantity()+1;
		
		mailer.setEmailFrom("clone-eventbrite@mailer.com");
		mailer.setEmailTo(user.getEmail());	
		mailer.setSubject("Thank you for your purchase, here is your ticket for : "+ event.getEventTitle());
		mailer.setMessage("\ud83e\udd73 \ud83e\udd73 \n"
				+ "your ticket number is :" + tick_num + " \n"+
				" enjoy the event \uD83E\uDD73\uD83E\uDD73\uD83E\uDD73\uD83E\uDD73\uD83E\uDD73"
				);
		
		
		mailerService.sendEmail(mailer);		
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
	
	

	
	
	
/** quartz scheduler to also set tickets active to false if event date is passed  --> runs every 2 seconds **/
	
	@Scheduled(cron ="*/2 * * * * *")
	public void setExpiredEventsTicketToFalse() {
		LocalDateTime time_now = LocalDateTime.now();
		 
		List<Purchase> filter_all_purchased_tickets = purchaseRepository.findAll()
				.stream()
				.filter(t -> 
				t.getEvent().getEventDateAndTime().getEndDate() !=null &&
				t.getIsTicketActive() == true &&
				t.getEvent().getEventDateAndTime().getEndDate().isBefore(time_now))			
				.collect(Collectors.toList());
		
		/** if event end date is past and ticket is till active add to list **/
		
				
	if(filter_all_purchased_tickets.size() > 0) 
	
	{
		
		filter_all_purchased_tickets
		.stream()
		.forEach(e ->{			
			Optional<Purchase> purchased_ticket = purchaseRepository.findById(e.getId());
			Purchase purchase = purchased_ticket.get();
				
			/** if ticket is still active then set to inactive **/		
			
				purchase.setIsTicketActive(false);	
			
			
			purchaseRepository.save(purchase);
		});
		
		log.info("---------------- ticket with ended event found updating ticket isActive to false--------------");
		
	}
	
	log.info("----------------Purchase Scheduler Running--------------");
	
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
