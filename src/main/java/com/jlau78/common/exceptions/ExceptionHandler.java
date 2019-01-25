package com.jlau78.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jlau78.foursquare.response.Response;

/**
 * Handles error messages for return
 */
public class ExceptionHandler {

	public static final String UNEXPECTED_ERROR_MSG = "UNEXPECTED_ERROR";

	public static ResponseEntity<Response> handle(final Exception e) {
		return handle(e.getMessage());
	}

	public static ResponseEntity<Response> handle(final Object e) {
		if (e instanceof Exception) {
			return handle(((Exception) e).getMessage());
		} else {
			return handle(UNEXPECTED_ERROR_MSG);
		}
	}

	public static ResponseEntity<Response> handle(final String errorMessage) {
		ErrorResponse err = new ErrorResponse();
		err.setMessage(errorMessage);
		Response rs = new Response();
		rs.error = err;
		return new ResponseEntity<Response>(rs, HttpStatus.BAD_REQUEST);
	}
}
