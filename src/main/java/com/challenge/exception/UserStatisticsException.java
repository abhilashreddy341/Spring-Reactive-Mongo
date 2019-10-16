package com.challenge.exception;

public class UserStatisticsException extends Exception {
	
	public String message;
	
	public UserStatisticsException(String message) {
		super(message);
		this.message = message;
	}

}
