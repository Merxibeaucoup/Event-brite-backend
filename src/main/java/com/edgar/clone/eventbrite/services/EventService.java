package com.edgar.clone.eventbrite.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.edgar.clone.eventbrite.models.Event;
import com.edgar.clone.eventbrite.models.user.User;
import com.edgar.clone.eventbrite.repositories.EventRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventService {

	@Autowired
	private EventRepository eventRepo;

	public Event createEvent(Event event, User user) {
		event.setDateCreated(LocalDateTime.now());
		event.setIsEventEnded(false);
		event.setOrganizer(user);
		return eventRepo.save(event);
	}

	public Event getEventById(long id) {
		return eventRepo.findById(id).get();
	}

	public List<Event> organizerEvents(User organizer) {
		return eventRepo.findByOrganizer(organizer);
	}

	public List<Event> findbyVenueTown(String town) {		
		return eventRepo.findByEventVenue_EventVenueAddressTown(town);
	}
	
	/**
	 * to do : add another scheduler to set all ended events state to inactive
	 * **/
	
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
