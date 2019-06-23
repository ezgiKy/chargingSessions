package com.evbox.charging.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -9054724507398243555L;

	public DataNotFoundException() {
		super();
	}

	public DataNotFoundException(String arg0) {
		super(arg0);
	}

}
