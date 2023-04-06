package com.edgar.clone.eventbrite.advice;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.edgar.clone.eventbrite.exceptions.EventDoesntExistException;
import com.edgar.clone.eventbrite.exceptions.TicketDoesntExistException;



@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {


	@ExceptionHandler(EventDoesntExistException.class)
	public ResponseEntity<Object> handleEventDoesntExist(EventDoesntExistException ex, WebRequest request) {

		return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()),
				HttpStatus.CONFLICT);
	}
	
	
	@ExceptionHandler(TicketDoesntExistException.class)
	public ResponseEntity<Object> handleTicketDoesntExist(TicketDoesntExistException ex, WebRequest request) {

		return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()),
				HttpStatus.CONFLICT);
	}
	
	
	


}

