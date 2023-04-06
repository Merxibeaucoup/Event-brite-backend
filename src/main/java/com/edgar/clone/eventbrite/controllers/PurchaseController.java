package com.edgar.clone.eventbrite.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edgar.clone.eventbrite.models.Purchase;
import com.edgar.clone.eventbrite.models.user.User;
import com.edgar.clone.eventbrite.repositories.PurchaseRepository;
import com.edgar.clone.eventbrite.requests.UseTicket;
import com.edgar.clone.eventbrite.services.PurchaseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/purchase")
public class PurchaseController {
	
	@Autowired
	private PurchaseService purchaseService;
	
	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@PostMapping("/buy")
	public ResponseEntity<Purchase> buy(@RequestBody Purchase purchase, @AuthenticationPrincipal User user){
		return ResponseEntity.ok(purchaseService.makePurchase(purchase, user));
	}
	
	
	@GetMapping("/user")
	public ResponseEntity<List<Purchase>> buy( @AuthenticationPrincipal User user){
		return ResponseEntity.ok(purchaseService.allBoughtByUser( user));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Purchase> getOne(@PathVariable Long id){
		return ResponseEntity.ok(purchaseService.getOneTicketPurchasedById(id));
	}
	
// todo:	only user with admin or Organizer role can check people in 
	@PutMapping("/checkin")
	public ResponseEntity<?> CheckInTicket(@Valid @RequestBody UseTicket useTicket, @AuthenticationPrincipal User user){
		
//		Purchase purchase = purchaseRepository.findById(useTicket.getPurchasedTicketid()).get();		
		purchaseService.CheckInTicketAtEvent(useTicket, user);	
//		purchaseRepository.save(purchase);
		
		 return new ResponseEntity<>( HttpStatus.ACCEPTED); 
	}
	
	
	

}
