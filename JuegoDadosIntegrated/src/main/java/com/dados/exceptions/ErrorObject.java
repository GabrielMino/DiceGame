package com.dados.exceptions;

public class ErrorObject {
	
	private Integer statusCode;
	
	private String message;
	
	private long timestamp;
	
	
	public int getStatusCode() {
		return statusCode;
	}
	public String getMessage() {
		return message;
	}
	public long getTimeStamp() {
		return timestamp;
		}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}


	
	

}
