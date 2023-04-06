package com.edgar.clone.eventbrite.exceptions;

public class TicketIsInactiveException extends RuntimeException {
	
	
	private static final long serialVersionUID = 1L;

	public TicketIsInactiveException(String message) {
		super(message);
	}

}
