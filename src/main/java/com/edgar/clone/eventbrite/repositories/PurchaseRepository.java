package com.edgar.clone.eventbrite.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edgar.clone.eventbrite.models.Purchase;
import com.edgar.clone.eventbrite.models.user.User;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

	List<Purchase> findByUser(User user);

//	boolean existsById(Long id);

	Optional<Purchase> findByEventName(String eventName); 

}
