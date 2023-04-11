package com.edgar.clone.eventbrite.advice;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.edgar.clone.eventbrite.exceptions.EventDoesntExistException;
import com.edgar.clone.eventbrite.exceptions.NotOwnerOfEventException;
import com.edgar.clone.eventbrite.exceptions.PaymentInsufficientException;
import com.edgar.clone.eventbrite.exceptions.TicketDoesntExistException;
import com.edgar.clone.eventbrite.exceptions.TicketIsInactiveException;
import com.edgar.clone.eventbrite.exceptions.TicketsSoldOutException;



@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {


	@ExceptionHandler(EventDoesntExistException.class)
	public ResponseEntity<Object> handleEventDoesntExist(EventDoesntExistException ex, WebRequest request) {

		return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()),
				HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(TicketDoesntExistException.class)
	public ResponseEntity<Object> handleTicketDoesntExist(TicketDoesntExistException ex, WebRequest request) {
		return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()),
				HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(PaymentInsufficientException.class)
	public ResponseEntity<Object> handlePaymentInsufficient(PaymentInsufficientException ex, WebRequest request) {
		return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.FORBIDDEN, LocalDateTime.now()),
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(TicketIsInactiveException.class)
	public ResponseEntity<Object> handleTicketIsInactiveException(TicketIsInactiveException ex, WebRequest request) {
		return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()),
				HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(TicketsSoldOutException.class)
	public ResponseEntity<Object> handleTicketIsSoldOutException(TicketsSoldOutException ex, WebRequest request) {
		return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.FORBIDDEN, LocalDateTime.now()),
				HttpStatus.FORBIDDEN);
	}
	
	
	@ExceptionHandler(NotOwnerOfEventException.class)
	public ResponseEntity<Object> handleNotOwnerOfEventException(NotOwnerOfEventException ex, WebRequest request) {
		return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.FORBIDDEN, LocalDateTime.now()),
				HttpStatus.FORBIDDEN);
	}
	
	
	


}

