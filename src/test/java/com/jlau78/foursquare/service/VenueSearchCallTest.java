package com.jlau78.foursquare.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jlau78.common.exceptions.AppException;
import com.jlau78.foursquare.client.PlacesApiClient;
import com.jlau78.foursquare.request.VenueRequest;
import com.jlau78.foursquare.response.Response;
import com.jlau78.foursquare.response.VenueSearchRS;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VenueSearchCallTest {

	@Spy
	VenueSearchCall apiCall;

	@Mock
	PlacesApiClient apiClient;

	VenueRequest request1 = new VenueRequest();
	VenueSearchRS response1 = new VenueSearchRS();

	@Before
	public void setUp() throws Exception {
		when(apiCall.getApiClient()).thenReturn(apiClient);
		when(apiClient.venueSearchByName(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(response1);

		request1.setClientId("111111");
		request1.setClientSecret("secretkey");
		request1.setVersion("20190222");

		response1.setResponse(new Response());
	}

	@Test
	public void testCallSuccess() throws AppException {
		VenueSearchRS r = apiCall.call(request1);

		assertTrue(r == response1);
	}

	@Test
	public void testCallNullRequest() throws AppException {
		VenueSearchRS r = apiCall.call(null);

		assertNull(r);
	}

}
