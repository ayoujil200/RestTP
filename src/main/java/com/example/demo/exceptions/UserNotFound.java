package com.example.demo.exceptions;

public class UserNotFound extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1165860259507150645L;

	public UserNotFound(String message) {
		super(message);
	}
}
