package com.edgar.clone.eventbrite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edgar.clone.eventbrite.models.Ticket;
import com.edgar.clone.eventbrite.models.user.User;
import com.edgar.clone.eventbrite.services.TicketService;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	
	@PostMapping("/new/{eventId}")
	public ResponseEntity<Ticket> newTickets(@RequestBody Ticket ticket,@PathVariable Long eventId, @AuthenticationPrincipal User user){		
		return ResponseEntity.ok(ticketService.ticketsForEvent(ticket, eventId, user));
	}

}
