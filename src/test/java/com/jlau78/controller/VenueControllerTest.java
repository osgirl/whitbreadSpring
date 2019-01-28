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
import com.jlau78.foursquare.response.venue.Response;
import com.jlau78.foursquare.response.venue.SearchResponse;
import com.jlau78.foursquare.response.venue.Venue;
import com.jlau78.foursquare.response.venue.VenueSearchRS;
import com.jlau78.foursquare.service.VenueDetailsCall;
import com.jlau78.foursquare.service.VenueRecommendationCall;
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

	@Injectable
	VenueRecommendationCall recommendService;

	@Injectable
	VenueDetailsCall detailsService;

	@Mocked
	FeignException feignException;

	VenueSearchRS vResponse = new VenueSearchRS();
	SearchResponse response1 = new SearchResponse();

	@Before
	public void setUp() throws Exception {
		response1.venues = new ArrayList<Venue>();
	}

	@Test
	public void testVenueSearchSuccess() {

		VenueRequest rq = new VenueRequest("paris");
		rq.setQuery("cafe");

		ResponseEntity<SearchResponse> r = testComponent.getVenueByLocationName("paris", "cafe");

		assertTrue(HttpStatus.OK.equals(r.getStatusCode()));
	}

	@Test
	public void testVenueSearchNullRequired() {
		VenueRequest rq = new VenueRequest("paris");
		rq.setNear("");

		ResponseEntity<SearchResponse> r = testComponent.getVenueByLocationName("paris", "cafe");

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

		ResponseEntity<SearchResponse> r = testComponent.getVenueByLocationName("paris", "cafe");

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

		ResponseEntity<SearchResponse> r = testComponent.getVenueByLocationName("paris", "cafe");

		assertNotNull(r.getBody().error);
	}

}
