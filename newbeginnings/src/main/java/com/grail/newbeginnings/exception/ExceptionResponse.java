package com.grail.newbeginnings.exception;

import java.util.Date;

public class ExceptionResponse {
	
	private Date dateAndTime;
	private String message;
	private String details;
	private String status;
	
	
	public ExceptionResponse(Date dateAndTime, String message, String details, String status) {
		super();
		this.dateAndTime = dateAndTime;
		this.message = message;
		this.details = details;
		this.status = status;
	}
	
	public Date getDateAndTime() {
		return dateAndTime;
	}
	public String getMessage() {
		return message;
	}
	public String getDetails() {
		return details;
	}
	public String getStatus() {
		return status;
	}

}