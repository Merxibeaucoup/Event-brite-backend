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
	

	public List<Event> organizerEvents(User organizer){
		return eventRepo.findByOrganizer(organizer);
	}

}
