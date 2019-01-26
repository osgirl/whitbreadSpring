package com.jlau78.common.exceptions;

import static org.junit.Assert.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jlau78.foursquare.response.Response;

public class ExceptionHandlerTest {

	@Spy
	ExceptionHandler testComponent;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testHandleSuccess() {

		ResponseEntity<Response> res = ExceptionHandler.handle(new AppException("Feign error encountered"));

		assertTrue(HttpStatus.BAD_REQUEST.equals(res.getStatusCode()));
		assertTrue(res.getBody().error.getMessage().contains("Feign"));
	}

	@Test
	public void testHandleBadObject() {

		ResponseEntity<Response> res = ExceptionHandler.handle(new Object());

		assertTrue(HttpStatus.BAD_REQUEST.equals(res.getStatusCode()));
		assertTrue(ExceptionHandler.UNEXPECTED_ERROR_MSG.equals(res.getBody().error.getMessage()));

	}

}
