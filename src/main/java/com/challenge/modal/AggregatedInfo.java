package com.challenge.modal;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="AggregatedInfo")
public class AggregatedInfo {
	
	@Id
	private String id;
	private Integer totalVisits;
	private List<String> visitorFirstNames;
	private List<String> visitorLastNames;
	
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
	
	

}
