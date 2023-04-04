package com.edgar.clone.eventbrite.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.edgar.clone.eventbrite.enums.TicketType;
import com.edgar.clone.eventbrite.models.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String ticketName;
	
	@NotNull
	private Integer ticketQuantity;
	
	@NotNull
	private BigDecimal ticketPrice;
	
	@Enumerated(EnumType.STRING)
	private TicketType ticketType;
	
	private LocalDateTime ticketSaleStartDate;
	
	private LocalDateTime ticketSaleEndDate;
	
	@Column(nullable = true)
	private Boolean isSaleEnded ;
	
	@OneToOne(cascade= CascadeType.MERGE)
	@JsonBackReference
	private Event event;
	
	@ManyToOne
	@JsonIgnore
	private User user;

}
