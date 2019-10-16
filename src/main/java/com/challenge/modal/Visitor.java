package com.challenge.modal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.challenge.request.Name;

@Document(collection = "Visitor")
public class Visitor {
	@Id
	private String id;
	private Integer visitCount;
	private String userFirst;
	private String userLast;
	

	public Visitor(String id, Integer visitCount, String userFirst, String userLast) {
		super();
		this.id = id;
		this.visitCount = visitCount;
		this.userFirst = userFirst;
		this.userLast = userLast;
	}
	
	public Visitor () {
		
	}
	
	public Integer getVisitCount() {
		return visitCount;
	}
	public void setVisitCount(Integer visitCount) {
		this.visitCount = visitCount;
	}
	public String getUserFirst() {
		return userFirst;
	}
	public void setUserFirst(String userFirst) {
		this.userFirst = userFirst;
	}
	public String getUserLast() {
		return userLast;
	}
	public void setUserLast(String userSecond) {
		this.userLast = userSecond;
	}
	
	@Override
	public boolean equals(Object obj) { 
		  Visitor visit = (Visitor)obj;
		  if (visit == null)
			  return false;
		  else if (visit.getUserFirst().equals(this.userFirst) && visit.getUserLast().equals(this.userLast)) {
			  return true;
		  } else {
			  return false;
		  }
    } 
}
