package com.edgar.clone.eventbrite.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@GetMapping("/id/{id}")
	public ResponseEntity<Event> getById(@PathVariable Long id){
		return ResponseEntity.ok(eventService.getEventById(id));
	}
	
	@GetMapping("/title/{title}")
	public ResponseEntity<Event> getByEventTitle(@PathVariable String title){
		return ResponseEntity.ok(eventService.getEventByTitle(title));
	}
	
	
	@GetMapping("/town/{town}")
	public ResponseEntity<List<Event>> getEventByTown(@PathVariable String town){
		return ResponseEntity.ok(eventService.findbyVenueTown(town));
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Event> updateEventById(@PathVariable Long id,@RequestBody Event event, @AuthenticationPrincipal User user){
		return ResponseEntity.ok(eventService.updateEventByid(id,event, user));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteEventByid(@PathVariable Long id, @AuthenticationPrincipal User user){		
		 eventService.deleteEventById(id);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
		
	}

}
