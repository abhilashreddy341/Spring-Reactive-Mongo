package com.challenge.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.challenge.modal.Visitor;

/**
 * this Repository is binded to the entity {@link Visitor} and provides all the functions like save(), findAll() etc to perform data operations on the collection {@link Visitor} 
 * @author Abhilash Reddy 
 *
 */
@Repository
public interface VisitorsRepository extends MongoRepository<Visitor, String>  {
	/**
	 * This method will find the user based on the user first name and the last name provided and 
	 * returns the java 8 optional of {@link Visitor}
	 * @param userFirst
	 * @param userLAst
	 * @return {@link Optional}
	 */
	Optional<Visitor> findByUserFirstAndUserLast(String userFirst, String userLAst);
}
