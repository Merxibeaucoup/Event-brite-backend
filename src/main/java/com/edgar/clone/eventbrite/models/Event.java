package com.edgar.clone.eventbrite.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.edgar.clone.eventbrite.enums.EventCategory;
import com.edgar.clone.eventbrite.enums.EventType;
import com.edgar.clone.eventbrite.models.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="EVENT_ID")
	private Long id;
	
	@Column(nullable = true)
	private String eventImageUrl;
	
	@NotNull
	private String eventTitle;
	
	@Enumerated(EnumType.STRING)
	private EventType eventType; 
	
	@Enumerated(EnumType.STRING)
	private EventCategory eventCategory;
	
	@Column(columnDefinition = "TEXT")
	private String eventDescription;
	
	@Column(nullable = true)
	private Boolean isEventEnded;
	
	@Column(nullable = true)
	private List<String> tags;
	
	@CreationTimestamp
	private LocalDateTime dateCreated;
	
	@Embedded
	private Venue eventVenue;
	
	@Embedded
	private Dates eventDateAndTime;
	
	
	@OneToOne(cascade = CascadeType.MERGE, mappedBy = "event")
	@JsonManagedReference
	private Ticket eventTicket;	
	
		
	@ManyToOne
	@JsonIgnore
	private User organizer;

}
