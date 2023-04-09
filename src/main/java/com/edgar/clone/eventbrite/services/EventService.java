package com.edgar.clone.eventbrite.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edgar.clone.eventbrite.models.Event;
import com.edgar.clone.eventbrite.models.user.User;
import com.edgar.clone.eventbrite.repositories.EventRepository;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepo;

	public Event createEvent(Event event, User user) {
		event.setDateCreated(LocalDateTime.now());
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
	
	

//	private boolean isExistsEventTown(String town) {
//		if (eventRepo.existsByEventTown(town)) {
//			return true;
//		}
//
//		else {
//			return false;
//		}
//	}

}
