package com.edgar.clone.eventbrite.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.edgar.clone.eventbrite.enums.EmailStatus;
import com.edgar.clone.eventbrite.models.MailerModel;
import com.edgar.clone.eventbrite.repositories.MailerModelRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailerService {
	
	@Autowired
	private JavaMailSender mailSender; 
	
	@Autowired
	private MailerModelRepository mailerModelRepository;
	
	
	
	public void sendEmail(MailerModel mailerModel) {
		
		// message time
		mailerModel.setSentDateTime(LocalDateTime.now()); 
		
		
		try {
			SimpleMailMessage message = new SimpleMailMessage(); 
			
			message.setFrom(mailerModel.getEmailFrom());
			message.setTo(mailerModel.getEmailTo());
			message.setSubject(mailerModel.getSubject());
			message.setText(mailerModel.getMessage());
			mailSender.send(message); 
			mailerModel.setStatus(EmailStatus.SENT);
			
			log.info("----------------- Email Reciept SENT ----------------");
			
		}
		catch(Exception e) {
			mailerModel.setStatus(EmailStatus.FAILED);
			log.info("----------- Couldnt Send Email -----------");
		}
		
		mailerModelRepository.save(mailerModel);
		
	}

}
