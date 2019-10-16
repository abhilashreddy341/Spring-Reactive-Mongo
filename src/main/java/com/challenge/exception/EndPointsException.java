package com.challenge.exception;

public class EndPointsException extends Exception {
	
	public String message;
	
	public EndPointsException(String message) {
		super(message);
		this.message = message;
	}
}
