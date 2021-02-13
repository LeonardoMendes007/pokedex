package com.pokedex.service.exception;

public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(String error) {
		super("Resource Not Found. " + error);
	}

}