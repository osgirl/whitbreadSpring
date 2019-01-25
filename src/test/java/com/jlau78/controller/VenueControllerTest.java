package com.jlau78.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jlau78.common.exceptions.AppException;
import com.jlau78.foursquare.request.VenueRequest;
import com.jlau78.foursquare.response.Response;
import com.jlau78.foursquare.response.Venue;
import com.jlau78.foursquare.response.VenueSearchRS;
import com.jlau78.foursquare.service.VenueSearchCall;

import feign.FeignException;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class VenueControllerTest {

	@Tested
	VenueController testComponent;

	@Injectable
	VenueSearchCall callService;

	@Mocked
	FeignException feignException;

	VenueSearchRS vResponse = new VenueSearchRS();
	Response response1 = new Response();

	@Before
	public void setUp() throws Exception {
		response1.venues = new ArrayList<Venue>();
	}

	@Test
	public void testVenueSearchSuccess() {

		VenueRequest rq = new VenueRequest();
		rq.setNear("paris");
		rq.setQuery("cafe");

		ResponseEntity<Response> r = testComponent.getVenueByLocationName("paris", "cafe");

		assertTrue(HttpStatus.OK.equals(r.getStatusCode()));
	}

	@Test
	public void testVenueSearchNullRequired() {
		VenueRequest rq = new VenueRequest();
		rq.setNear("");

		ResponseEntity<Response> r = testComponent.getVenueByLocationName("paris", "cafe");

		assertTrue(HttpStatus.OK.equals(r.getStatusCode()));
	}

	@Test
	public void testVenueSearchFeignException() throws AppException {
		new Expectations() {
			{
				callService.call((VenueRequest) any);
				result = feignException;
			}
		};

		ResponseEntity<Response> r = testComponent.getVenueByLocationName("paris", "cafe");

		assertNotNull(r.getBody().error);
	}

	@Test
	public void testVenueSearchAppException() throws AppException {
		new Expectations() {
			{
				callService.call((VenueRequest) any);
				result = new AppException("Application failed to run");
			}
		};

		ResponseEntity<Response> r = testComponent.getVenueByLocationName("paris", "cafe");

		assertNotNull(r.getBody().error);
	}

}
