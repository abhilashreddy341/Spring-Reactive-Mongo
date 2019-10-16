package com.challenge.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.challenge.modal.AggregatedInfo;

/**
 * this Repository is binded to the entity {@link AggregatedInfo} and provides all the functions like save(), findAll() etc to perform data operations on the collection {@link AggregatedInfo} 
 * @author Abhilash Reddy 
 *
 */
@Repository
public interface AggregatedInfoRepository extends MongoRepository<AggregatedInfo, String>{
	
}
