package com.challenge.service;

import com.challenge.exception.UserStatisticsException;
import com.challenge.modal.EndPointsInfo;
import com.challenge.modal.UserStatistics;
import com.challenge.modal.Visitor;
import com.challenge.request.Name;

import reactor.core.publisher.Flux;


public interface UserService {
	
	public Flux<EndPointsInfo> findAllEndPointsInfo();
	
	public UserStatistics findUserStatistics() throws UserStatisticsException;
	
	public String findAndUpdateUserVisits(Name name) throws UserStatisticsException, UserStatisticsException;
	
	public Flux<Visitor> findReactiveVisitors();
}
