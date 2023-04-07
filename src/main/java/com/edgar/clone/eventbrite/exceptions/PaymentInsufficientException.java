package com.edgar.clone.eventbrite.exceptions;

public class PaymentInsufficientException extends RuntimeException {
	
	
	private static final long serialVersionUID = 1L;

	public PaymentInsufficientException(String message) {
		super(message);
	}

}
