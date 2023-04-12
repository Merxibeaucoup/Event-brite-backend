package com.edgar.clone.eventbrite.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.edgar.clone.eventbrite.models.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="event_name")
	@NotNull
	private String eventName;	
	
	@Nullable
	private String ticketReferenceNumber;
	
	@Column(nullable = true)
	private String ticketType;
	
	@PositiveOrZero
	private BigDecimal amount;	

	@Max(value = 1)
	@Positive
	@Column(name="event_ticket_quantity", nullable = true )
	private Integer quantity;
	
	@Column(nullable = true, name ="event_ticket_active")
	private Boolean isTicketActive;
	
	@Nullable
	private LocalDateTime purchaseDate;
	
	@OneToOne
	@JsonIgnore
	private Ticket ticket;
	
	@OneToOne
	@JsonIgnore
	private Event event ;
	
	@OneToOne
	@JsonIgnore
	private User user;

}
