package com.challenge.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.exception.GreetingException;
import com.challenge.exception.UserStatisticsException;
import com.challenge.exception.VisitorsException;
import com.challenge.modal.EndPointsInfo;
import com.challenge.modal.UserStatistics;
import com.challenge.modal.Visitor;
import com.challenge.request.Name;
import com.challenge.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>This Spring Boot Application is implemented using the <a href="https://projectreactor.io/">
 * Project Reactor</a> which is the implementation of 
 * event-driven non-blocking <a href="https://www.reactive-streams.org/">
 * reactive streams</a> specification which is available in Spring 5 onwards.
 * 
 * <p>the embedded server netty is utilized to support the event driven approach to efficiently utilize the servlet threads
 *  * 
 * <p> some of the routes like "/" for all routes Info and "/visitors" for all visitors information are implemented using the 
 * ReactiveMongoRepository to demonstrate the support from mongoDB for reactive programming.
 * 
 * <p> MongoDB on the cloud is utilized to persist the visitors information
 * @author Abhilash Reddy
 *
 */

@RestController
public class BaseController {
	
	 private static final Logger logger = Logger.getLogger(BaseController.class);
	
	@Autowired
	UserService userService;
	
	/**
	 * <p>this function is mapped by the route "/" to provide the information about all the routes exposed in this application
	 * <p> this function returns ResponseEntity with different statuses based on the condition and {@link Flux} 
	 * as the body to get rid of per thread request strategy
	 * <p> ResponseEntity is used to customize the response status code in case of any exception
	 * @return ResponseEntity
	 */
	@GetMapping("/")
	public ResponseEntity<Flux<EndPointsInfo>> getEndPointsInfo( ) {
		logger.info("request response cycle initaed for route {GET /}");
		Long size[] = {0L};
		Flux<EndPointsInfo> endPointsFlux = userService.findAllEndPointsInfo()
				.doOnComplete(() -> {
	        		logger.info("request response cycle ended with success for route {GET /}");
	        	})
				.onErrorMap(exec->{
					logger.error("request response cycle ended with error for route {GET /visitors}", exec);
					return new VisitorsException(exec.getMessage());
				});
			return ResponseEntity.status(HttpStatus.OK).body(endPointsFlux);
	}
	
	/**
	 * <p> this function is mapped by the route "/" statistics to fetch all the aggregated info and user specific information
	 * <p> {@link UserStatistics} contains total no of visits to the route "/greeting" and an array of visitors info about their specific count of visits.
	 * @return
	 * @throws UserStatisticsException 
	 */
	@GetMapping("/statistics")
	public Mono<ResponseEntity<UserStatistics>> getUsersStatistics( ) throws UserStatisticsException {
		logger.info("request response cycle initiated for route {GET /statistics}");
		return Mono.just(ResponseEntity.status(HttpStatus.OK).body(userService.findUserStatistics()))
				.onErrorMap(exception -> {
					logger.error("request response cycle ended with error for the route {GET /statistics}");
					return new UserStatisticsException("Exception occured while fetching user statistics");	
				});
	}
	
	/**
	 * <p> this function is mapped by the route "/greeting" to check if an user already visited the route and greet him accordingly
	 * <p> if a user already exists then he is greeted saying welcome back
	 * <p> if the user is visiting for the first time then the user is greeted saying welcome
	 * @param name
	 * @return
	 */
	@PostMapping("/greeting")
	public Mono<ResponseEntity<String>> getGreeting(@RequestBody Name name) {
		logger.info("request response cycle initiated for route {GET /statistics}");
		if ((name == null) || name.getFirstName() == null && name.getLastName() == null) {
			logger.info("request response cycle ended with error for route {GET /statistics}");
			return  Mono.empty().error(new GreetingException("please enter a valid name"));			
		}
		return Mono.create((emitter) -> {
			try {
				String response = userService.findAndUpdateUserVisits(name);
				logger.info("request response cycle ended with success for route {GET /statistics}");
				emitter.success(ResponseEntity.status(HttpStatus.OK).body(response));
			} catch (Exception e) {
				logger.error("request response cycle ended with error for route {GET /statistics}: ", e);
				emitter.error(e);
			}
		});
	}
	/**
	 * <p>this function is mapped by the "/visitors" route to return the array of users with their total no of visits to the route "/greeting"
	 * <p> this method returns the {@link Flux} to effectively utilize the non-blocking implementation of Spring Webflux
	 * @return
	 */
	@GetMapping("/visitors")
	public Flux<Visitor> getVisitors( ) {
		logger.info("request response cycle initiated for route {GET /visitors}");
		return userService.findReactiveVisitors()
				.onErrorMap(exec->{
					logger.error("request response cycle ended with error for route {GET /visitors}", exec);
					return new VisitorsException(exec.getMessage());
				});
	}
}
