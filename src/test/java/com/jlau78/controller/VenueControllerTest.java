package com.jlau78.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import static org.mockito.Mockito.anyString;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jlau78.foursquare.client.PlacesApiClient;
import com.jlau78.foursquare.request.VenueRequest;
import com.jlau78.foursquare.response.Response;
import com.jlau78.foursquare.response.Venue;
import com.jlau78.foursquare.response.VenueSearchRS;
import com.jlau78.foursquare.service.VenueSearchCall;

@RunWith(SpringRunner.class)
// @SpringBootTest
public class VenueControllerTest {

	@Spy
	VenueController controller;

	@Mock
	VenueSearchCall callService;

	VenueSearchRS vResponse = new VenueSearchRS();
	Response response1 = new Response();

	@Before
	public void setUp() throws Exception {
		response1.venues = new ArrayList<Venue>();
		when(controller.getVenueSearchCall()).thenReturn(callService);
		when(callService.call(Mockito.anyObject())).thenReturn(vResponse);
	}

	@Test
	public void testVenueSearchSuccess() {
		VenueRequest rq = new VenueRequest();
		rq.setNear("paris");
		rq.setQuery("cafe");

		Object r = controller.getVenueByLocationName("paris", "cafe");

		assertNotNull(r);
	}

}
