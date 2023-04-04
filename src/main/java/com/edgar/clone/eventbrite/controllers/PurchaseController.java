package com.edgar.clone.eventbrite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edgar.clone.eventbrite.models.Purchase;
import com.edgar.clone.eventbrite.models.user.User;
import com.edgar.clone.eventbrite.services.PurchaseService;

@RestController
@RequestMapping("/api/v1/purchase")
public class PurchaseController {
	
	@Autowired
	private PurchaseService purchaseService;
	
	
	@PostMapping("/buy")
	public ResponseEntity<Purchase> buy(@RequestBody Purchase purchase, @AuthenticationPrincipal User user){
		return ResponseEntity.ok(purchaseService.makePurchase(purchase, user));
	}

}
