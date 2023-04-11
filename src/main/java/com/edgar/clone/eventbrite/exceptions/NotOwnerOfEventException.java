package com.edgar.clone.eventbrite.exceptions;

public class NotOwnerOfEventException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotOwnerOfEventException(String message) {
		super(message);
	}

}
