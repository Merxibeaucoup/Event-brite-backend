package com.edgar.clone.eventbrite.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Venue {
	
		
	@NotNull
	private String eventVenue;
	
	@Column(name="house_number")
	private String eventVenueAddressNumber;
	
	@Column(name="street")
	private String eventVenueAddressStreet;
	
	@Column(name="town")
	private String eventVenueAddressTown;
	
	@Column(name="state")
	private String eventVenueAddressState;
	
	@Column(name="country")
	private String eventVenueAddressCountry;
	
	@Column(name="zipcode")
	private String eventVenueAddressZipcode;
	
	
	

}
