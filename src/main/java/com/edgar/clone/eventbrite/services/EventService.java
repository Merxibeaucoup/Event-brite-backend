package com.edgar.clone.eventbrite.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.edgar.clone.eventbrite.exceptions.EventDoesntExistException;
import com.edgar.clone.eventbrite.exceptions.NotOwnerOfEventException;
import com.edgar.clone.eventbrite.models.Event;
import com.edgar.clone.eventbrite.models.user.User;
import com.edgar.clone.eventbrite.repositories.EventRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventService {

	@Autowired
	private EventRepository eventRepo;
	
	/** create event **/
	public Event createEvent(Event event, User user) {
		event.setDateCreated(LocalDateTime.now());
		event.setIsEventEnded(false);
		event.setOrganizer(user);
		return eventRepo.save(event);
	}

	/** get event by id **/
	public Event getEventById(Long id) {
		return eventRepo.findById(id).get();
	}
	
	/** get event by title **/
	public Event getEventByTitle(String eventTitle) {	
		Event event = eventRepo.findByEventTitle(eventTitle)
				.orElseThrow(()-> new EventDoesntExistException("Event with name :: "+ eventTitle +" does not exist"));
		return event;	
	}
	
	
	/** update event by id **/
	public Event updateEventByid(Long id,Event event, User user) {	
										
		if(eventRepo.findById(id).isPresent()) {
			return eventRepo.save(event);		
		}
		else 
			throw new EventDoesntExistException("Event with id :: "+ id +" does not exist");	
	
	}
	
	
	
	/** delete event by id **/
	public void deleteEventById(Long id, User user) {
				
		if(eventRepo.findById(id).isPresent()) {
			eventRepo.deleteById(id);
		}
		else {			
			throw new NotOwnerOfEventException("Cant delete event, you are not an ADMIN or the owner of the event with id :: "+ id );		
		}
			
	}

	
	/** get event belonging to organizer **/
	public List<Event> organizerEvents(User organizer) {
		return eventRepo.findByOrganizer(organizer);
	}
	
	
	/** get list of events by event town name **/
	public List<Event> findbyVenueTown(String town) {		
		return eventRepo.findByEventVenue_EventVenueAddressTown(town);
	}
	
	/** scheduler to set event isEnded to true if -> event end date passed **/	
	@Scheduled(cron ="*/3 * * * * *")
	public void setEventsToInactive() {
		
		LocalDateTime today = LocalDateTime.now();
		
		List <Event> filter_all_events_endDate = eventRepo
				.findAll()
				.stream()
				.filter(e -> e.getEventDateAndTime().getEndDate() != null 
				&& e.getIsEventEnded()== false 
				&& e.getEventDateAndTime().getEndDate().isBefore(today)
						).collect(Collectors.toList());
		
		if(filter_all_events_endDate.size() > 0) {			
			filter_all_events_endDate
			.stream()
			.forEach(f -> {
				Optional<Event> event_ended = eventRepo.findById(f.getId());
				Event event = event_ended.get();
				
				event.setIsEventEnded(true);			
				eventRepo.save(event);
				
				
				log.info("----------------ended event found updating isEnded to true--------------");
							
			});
			
		}
		log.info("----------------Event Scheduler Running--------------");
	}
	
	

//	public boolean isExistsEventTown(String town) {
//		if (eventRepo.existsByEventTown(town)) {
//			return true;
//		}
//
//		else {
//			return false;
//		}
//	}

}
