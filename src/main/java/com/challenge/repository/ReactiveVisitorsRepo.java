package com.challenge.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.challenge.modal.Visitor;

/**
 * this Repository is binded to the entity {@link Visitor} and provides all the functions like save(), findAll() etc to perform data operations on the collection {@link Visitor} 
 * This Repository extends {@link ReactiveMongoRepository} which provides great support for reactive
 * programming and the methods provided by this interface returns either {@link Mono} or {@link Flux} to support the non-blocking 
 * @author Abhilash Reddy 
 *
 */
@Repository
public interface ReactiveVisitorsRepo extends ReactiveMongoRepository<Visitor, String> {

}
