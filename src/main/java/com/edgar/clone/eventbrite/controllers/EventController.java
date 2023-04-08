package com.edgar.clone.eventbrite.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edgar.clone.eventbrite.models.Event;
import com.edgar.clone.eventbrite.models.user.User;
import com.edgar.clone.eventbrite.services.EventService;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@PostMapping("/new")
	public ResponseEntity<Event> create(@RequestBody Event event, @AuthenticationPrincipal User user){
		return ResponseEntity.ok(eventService.createEvent(event, user));
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Event> getById(@PathVariable Long id){
		return ResponseEntity.ok(eventService.getEventById(id));
	}
	
	
	@GetMapping("/{town}")
	public ResponseEntity<List<Event>> findEventByTown(@PathVariable String town){
		return ResponseEntity.ok(eventService.findbyVenueTown(town));
	}

}
