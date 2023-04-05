package com.edgar.clone.eventbrite.exceptions;

public class EventDoesntExistException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public EventDoesntExistException(String message) {
		super(message);
	}

}
