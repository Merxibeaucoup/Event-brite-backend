package com.edgar.clone.eventbrite.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.edgar.clone.eventbrite.models.token.Token;



@Repository
public interface TokenRepository extends  JpaRepository<Token, Long>{

	
Optional<Token> findByToken(String token);
	
	
	@Query("select t from Token t inner join User u on t.user.id = u.id where u.id =:userId and (t.expired = false or t.revoked = false)")
	List<Token> findAllValidTokensByUser(Long userId);
}
