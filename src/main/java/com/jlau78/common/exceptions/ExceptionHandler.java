package com.jlau78.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jlau78.foursquare.response.Response;

public class ExceptionHandler {

	public static ResponseEntity<Response> handle(final Exception e) {
		return handle(e.getMessage());
	}

	public static ResponseEntity<Response> handle(final String errorMessage) {
		ErrorResponse err = new ErrorResponse();
		err.setMessage(errorMessage);
		Response rs = new Response();
		rs.error = err;
		return new ResponseEntity<Response>(rs, HttpStatus.BAD_REQUEST);
	}
}
