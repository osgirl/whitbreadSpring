package com.jlau78.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jlau78.foursquare.client.PlacesApiClient;
import com.jlau78.foursquare.response.Response;
import com.jlau78.foursquare.response.Venue;
import com.jlau78.foursquare.response.VenueSearchRS;

import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "VenueQueryController")
@RestController
@RequestMapping("/venue")
public class VenueController {

	@Autowired
	private PlacesApiClient venueQueryClient;

	@Value("${service.foursquare.api.api_version}")
	String API_VERSION_DEFAULT = "20190122";

	@Value("${service.foursquare.api.client_id}")
	String CLIENT_ID;

	@Value("${service.foursquare.api.client_secret}")
	String CLIENT_SECRET;
	
	@ApiOperation("Get Venue by location name")
	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> getVenueByLocationName(@RequestParam(value = "venue", required = true) String venue,
	    @RequestParam(value = "radius") String radius) {
		ResponseEntity<Boolean> response = new ResponseEntity(Boolean.TRUE, HttpStatus.OK);

		String intent = PlacesApiClient.INTENT_BROWSE;
		String apiVersion = API_VERSION_DEFAULT;

		Object r = null;
		try {
			r = venueQueryClient.venueSearchByName(venue, intent, apiVersion, CLIENT_ID, CLIENT_SECRET);
		} catch (FeignException e) {
			log.error(e.getMessage());
		}

		log.info("OK: " + r);
		return response;
	}
	
	
	private void getAllHotels(final VenueSearchRS response) {
		
		if (response != null) {
			Response r = response.getResponse();
			List<Venue> venues = r.venues;
			
			
			venues.stream()
						.map(v -> v.location )
						.collect(Collectors.toList());
						
						
						
		}
	
	}

}
