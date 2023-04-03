package com.edgar.clone.eventbrite.models;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dates {
	
	
	private LocalDateTime startDate;
	
	private LocalDateTime endDate;

}
