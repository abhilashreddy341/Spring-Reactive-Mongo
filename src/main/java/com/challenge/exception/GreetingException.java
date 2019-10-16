package com.challenge.exception;

public class GreetingException extends Exception {
	
	public String message;
	
	public GreetingException(String message) {
		super(message);
		this.message = message;
	}
}
