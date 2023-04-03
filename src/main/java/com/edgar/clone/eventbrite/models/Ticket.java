package com.edgar.clone.eventbrite.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.edgar.clone.eventbrite.enums.TicketType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
	private LocalDateTime ticketSaleStart;
	
	private LocalDateTime ticketSaleEnd;

}
