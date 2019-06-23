package com.evbox.charging.exception;

import java.util.Date;

/**
 * Response for the default exceptions
 *
 */
public class ExceptionResponse {

	private Date timestamp;
	private String message;
	private String detail;

	public ExceptionResponse(Date time, String message, String detail) {
		super();
		this.timestamp = time;
		this.message = message;
		this.detail = detail;
	}

	public Date getTime() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetail() {
		return detail;
	}

}
