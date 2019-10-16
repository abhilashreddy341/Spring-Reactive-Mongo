package com.challenge.modal;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import reactor.core.publisher.Flux;

@Document(collection = "UserStatistics")
public class UserStatistics {
	
	private Integer totalVisits;
	private List<String> visitorFirstNames;
	private List<String> visitorLastNames;
	private List<Visitor> visitors;
	
	public Integer getTotalVisits() {
		return totalVisits;
	}
	public void setTotalVisits(Integer totalVisits) {
		this.totalVisits = totalVisits;
	}
	public List<String> getVisitorFirstNames() {
		return visitorFirstNames;
	}
	public void setVisitorFirstNames(List<String> visitorFirstNames) {
		this.visitorFirstNames = visitorFirstNames;
	}
	public List<String> getVisitorLastNames() {
		return visitorLastNames;
	}
	public void setVisitorLastNames(List<String> visitorLastNames) {
		this.visitorLastNames = visitorLastNames;
	}
	public List<Visitor> getVisitors() {
		return visitors;
	}
	public void setVisitors(List<Visitor> visitors) {
		this.visitors = visitors;
	}
	
	@Override
	public boolean equals(Object obj) {
		UserStatistics us = (UserStatistics)obj;
		if (obj == null)
		return false;
		if (this.totalVisits.equals(us.getTotalVisits()) 
				&& this.visitorFirstNames.equals(us.getVisitorFirstNames())
				&& this.visitorLastNames.equals(us.getVisitorLastNames())
				&& this.visitors.equals(us.getVisitors()))
			return true;
		return false;
	}
}
