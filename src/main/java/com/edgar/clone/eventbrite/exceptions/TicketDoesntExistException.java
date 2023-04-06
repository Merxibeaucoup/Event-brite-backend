package com.edgar.clone.eventbrite.exceptions;

public class TicketDoesntExistException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public TicketDoesntExistException(String message) {
		super(message);
	}

}
