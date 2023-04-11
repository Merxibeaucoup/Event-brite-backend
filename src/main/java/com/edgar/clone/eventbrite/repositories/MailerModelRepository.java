package com.edgar.clone.eventbrite.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edgar.clone.eventbrite.models.MailerModel;

@Repository
public interface MailerModelRepository extends JpaRepository<MailerModel, Long> {

}
