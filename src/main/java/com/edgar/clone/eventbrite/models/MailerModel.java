package com.edgar.clone.eventbrite.models;

import java.time.LocalDateTime;

import com.edgar.clone.eventbrite.enums.EmailStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailerModel {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String emailFrom;
	
	@Column(nullable = false)
	private String emailTo;
	
	@Column(nullable = true)
	private String subject;
	
	@Column(columnDefinition = "TEXT")
	private String message;
	
	private LocalDateTime SentDateTime;

	private EmailStatus status;

}
