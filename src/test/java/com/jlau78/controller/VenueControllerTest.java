package com.jlau78.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jlau78.foursquare.client.PlacesApiClient;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VenueControllerTest {

	@Spy
	VenueController controller;

	@Mock
	PlacesApiClient client;

	@Before
	public void setUp() throws Exception {

		when(controller.getVenueQueryClient()).thenReturn(client);
	}

	@Test
	public void testVenueSearchSuccess() {

		Object r = controller.getVenueByLocationName("paris");

		assertNotNull(r);
	}

}
