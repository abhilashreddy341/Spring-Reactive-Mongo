package com.challenge.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.challenge.modal.EndPointsInfo;
import reactor.core.publisher.Flux;

/**
 * <p>This Repository is binded to the entity {@link EndPointsInfo} and provides all the functions like save(), findAll() etc to perform data operations on the collection {@link EndPointsInfo} 
 * <p>This Repository extends {@link ReactiveMongoRepository} which provides great support for reactive
 * programming and the methods provided by this interface returns either {@link Mono} or {@link Flux} to support the non-blocking 
 * @author Abhilash Reddy 
 *
 */
@Repository
public interface EndPointsRepository extends ReactiveMongoRepository<EndPointsInfo, String> {
	
}
