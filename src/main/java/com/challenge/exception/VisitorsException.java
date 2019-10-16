package com.challenge.exception;

public class VisitorsException extends Exception {
	
	public String message;
	
	public VisitorsException(String message) {
		super(message);
		this.message = message;
	}
}