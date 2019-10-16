package com.challenge.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.controller.BaseController;
import com.challenge.exception.EndPointsException;
import com.challenge.exception.UserStatisticsException;
import com.challenge.exception.VisitorsException;
import com.challenge.modal.AggregatedInfo;
import com.challenge.modal.EndPointsInfo;
import com.challenge.modal.UserStatistics;
import com.challenge.modal.Visitor;
import com.challenge.repository.AggregatedInfoRepository;
import com.challenge.repository.EndPointsRepository;
import com.challenge.repository.ReactiveVisitorsRepo;
import com.challenge.repository.VisitorsRepository;
import com.challenge.request.Name;
import com.challenge.service.UserService;

import reactor.core.publisher.Flux;
/**
 * <p> this class acts as a service layer between the controller and the repository layer. 
 * This class is annotated with the annotation service to let spring create and inject the bean of type UserService where ever needed
 * <p> This class has various dependencies on the repository layer which will be injected using the annotation Autowired
 * 
 * @author Abhilash Reddy
 *
 */
@Service
public class UserServiceImplementation implements UserService {
	
	private static final Logger logger = Logger.getLogger(UserServiceImplementation.class);
	
	@Autowired
	EndPointsRepository endPointsRepository;
	
	@Autowired
	VisitorsRepository visitorsRepository;
	
	@Autowired
	AggregatedInfoRepository aggregatedInfoRepository;
	
	@Autowired
	ReactiveVisitorsRepo reactiveVisitorsRepo;
	
	/**
	 * This function makes a call to the endpointsRepository which is an interface that extends ReactiveMongoRepository. 
	 * Where the findAll() method will return the all the documents in the collection EndPointsInfo as a {@link Flux} 
	 * such that all the steps executed after endPointsRepository.findAll() are non-blocking 
	 */
	
	@Override
	public Flux<EndPointsInfo> findAllEndPointsInfo() {
		logger.info("started executing method: findAllEndPointsInfo()");
		Flux<EndPointsInfo> endPointsList = null;
		try {
			endPointsList = endPointsRepository.findAll()
				.doOnComplete(() -> {
					logger.info("finished executing method: findAllEndPointsInfo()");
				});
		} catch (Exception e) {
			logger.error("exception in method: findAllEndPointsInfo() : " + e.getMessage());
			endPointsList = Flux.error(new EndPointsException("error while fetching endpoints information"));
		}
		
		logger.info("finished executing method: findAllEndPointsInfo()");
		return endPointsList;
	}
	
	/**
	 * this is a utility function which fetches the document in Visitor collection and updates the 
	 * total no of visits and the information related to each user
	 * @param name
	 * @return
	 */
	private boolean visitorCalculations(Name name) {
		logger.info("started executing method: visitorCalculations()");
		boolean isUserExists = false;
		try {
			Optional<Visitor> visitorOptional = visitorsRepository.findByUserFirstAndUserLast(name.getFirstName(), name.getLastName());
			Visitor visitor = visitorOptional.orElse(new Visitor());
			String firstName = visitor.getUserFirst();
			String lastName = visitor.getUserLast();
			if ((firstName != null || lastName != null)) {
				isUserExists = true;
				visitor.setVisitCount(visitor.getVisitCount()+1);
			} else {
				visitor.setVisitCount(1);
				visitor.setUserFirst(name.getFirstName());
				visitor.setUserLast(name.getLastName());
			}
			visitorsRepository.save(visitor);
		} catch (Exception e) {
			logger.error("exception in method: visitorCalculations()" + e.getMessage());
		}
		logger.info("finished executing method: visitorCalculations()");
		return isUserExists;		
	}
	
	/**
	 * this is a utility function which fetches the document in AggregatedInfo collection and updates the 
	 * total no of visits and the lists for first names and last names
	 * @param name
	 */
	private void AggregatedInfoCalculations(Name name) {
		logger.info("started executing method: AggregatedInfoCalculations()");
		AggregatedInfo aggregatedInfo = null;
		try {
			List<AggregatedInfo> aggregatedOptional = aggregatedInfoRepository.findAll();
			aggregatedOptional = aggregatedOptional == null? new ArrayList<AggregatedInfo>() : aggregatedOptional;
			if (aggregatedOptional.size() > 0) {
				aggregatedInfo = aggregatedOptional.get(0);
				if ((name.getFirstName() != null) && !aggregatedInfo.getVisitorFirstNames().contains(name.getFirstName())) {
					aggregatedInfo.getVisitorFirstNames().add(name.getFirstName());
				}
				if ((name.getLastName() != null) && !aggregatedInfo.getVisitorLastNames().contains(name.getLastName())) {
					aggregatedInfo.getVisitorLastNames().add(name.getLastName());
				}
				aggregatedInfo.setTotalVisits(aggregatedInfo.getTotalVisits()+1);
			} else {
				aggregatedInfo = new AggregatedInfo();
				aggregatedInfo.setTotalVisits(1);
				List<String> newFirstNames = new ArrayList<String>();
				newFirstNames.add(name.getFirstName());
				List<String> newLastNames = new ArrayList<String>();
				newLastNames.add(name.getLastName());
				aggregatedInfo.setVisitorFirstNames(newFirstNames);
				aggregatedInfo.setVisitorLastNames(newLastNames);
			}
			aggregatedInfoRepository.save(aggregatedInfo);
		} catch(Exception e) {
			logger.error("exception in method: AggregatedInfoCalculations() : " + e.getMessage());
		}
		
		logger.info("finished executing method: AggregatedInfoCalculations()");
	}
	
	/**
	 *<p>this function checks if an user exists in the database and updates his count in the database
	 *<p>Here two collections are maintained to calculate and aggregate the visitor Statistics 1. {@link VisitorsInfo} and 2. {@link AggregatedInfo}
	 *<p>a Threadpool of size 2 is created as the operation on two collections is independent so that the database operation on 
	 * one collection is not blocked until one database operation is finished.
	 */
	
	@Override
	public String findAndUpdateUserVisits(Name name) throws UserStatisticsException {
		logger.info("started executing method: findAndUpdateUserVisits()");
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(()->AggregatedInfoCalculations(name));
		Future<Boolean> visitorFuture = executor.submit(() ->visitorCalculations(name));
		String nameString = (name.getFirstName() == null ? "":name.getFirstName())  + " " + (name.getLastName() == null ? "": name.getLastName());
		try {
			if (visitorFuture.get()) {
				return "Welcome back "+ nameString;
			}
			executor.shutdown();
		} catch (InterruptedException e) {
			logger.error("InterruptedException in method: findAndUpdateUserVisits() : "+e.getMessage());
			throw new UserStatisticsException(e.getMessage());
		} catch (ExecutionException e) {
			logger.error("ExecutionException in method: findAndUpdateUserVisits() : "+e.getMessage());
			throw new UserStatisticsException(e.getMessage());
		}
		logger.info("finished executing method: findAndUpdateUserVisits()");
		return "Welcome "+ nameString;
	}
		
	/**
	 * <p>This method will fetch the documents from both {@link AggregatedInfo} and {@link VisitorsInfo} and creates a new instance for UserStatistics
	 * <p>In this method {@link Executorservice} is used to create the threadpool of size 2 as fetching the documents from two collections is independent
	 * @throws UserStatisticsException 
	 */
	@Override
	public UserStatistics findUserStatistics() throws UserStatisticsException {
		logger.info("started executing method: findUserStatistics()");
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Future<AggregatedInfo> aggregatedFuture = executor.submit(()->{
			List<AggregatedInfo> aggregatedInfoList = aggregatedInfoRepository.findAll();
			return aggregatedInfoList == null ? new AggregatedInfo(): (aggregatedInfoList.size() != 0 ? aggregatedInfoList.get(0) : new AggregatedInfo());
		});
		
		Future<List<Visitor>> visitorsFuture = executor.submit(()->{
			List<Visitor> visitorList = visitorsRepository.findAll();
			List<Visitor> emptyList = new ArrayList<Visitor>();
			emptyList.add(new Visitor());
			return visitorList == null ? emptyList: (visitorList.size() != 0 ? visitorList : emptyList);
		});
		
		UserStatistics userStatics = new UserStatistics();
		
		try {
			AggregatedInfo aggregatedInfo= aggregatedFuture.get();
			userStatics.setTotalVisits(aggregatedInfo.getTotalVisits());
			userStatics.setVisitorFirstNames(aggregatedInfo.getVisitorFirstNames());
			userStatics.setVisitorLastNames(aggregatedInfo.getVisitorLastNames());

			List<Visitor> visitorsList = visitorsFuture.get();
			userStatics.setVisitors(visitorsList);

		} catch (InterruptedException e) {
			logger.error("InterruptedException in method: findUserStatistics() : "+e.getMessage());
			throw new UserStatisticsException("exception while fetching visitors statistics");
		} catch (ExecutionException e) {
			logger.error("ExecutionException in method: findUserStatistics() : "+e.getMessage());
			throw new UserStatisticsException("exception while fetching visitors statistics");
		}

		executor.shutdown();
		logger.info("finished executing method: findUserStatistics()");
		return userStatics;
	}
	
	/**
	 * this method will fetch all the documents in the repository Visitors using 
	 * ReactiveMongoRepository which returns {@link Flux} which is the non-blocking operation
	 */
	@Override
	public Flux<Visitor> findReactiveVisitors() {
		logger.info("started executing method: findReactiveVisitors()");
		Flux<Visitor> visitorFlux = null;
		try {
			visitorFlux = reactiveVisitorsRepo.findAll()
					.doOnComplete(() -> {
						logger.info("finished executing method: findReactiveVisitors()");
					});
		}catch (Exception e) {
			logger.error("Exception in method: findReactiveVisitors() : "+e.getMessage());
			visitorFlux = Flux.error(new VisitorsException("error while fetching visitors info"));
		}
		return visitorFlux;
	}
	
}
