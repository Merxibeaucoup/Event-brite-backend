package com.edgar.clone.eventbrite.exceptions;

public class TicketsSoldOutException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TicketsSoldOutException(String message) {
		super(message);
	}

}
