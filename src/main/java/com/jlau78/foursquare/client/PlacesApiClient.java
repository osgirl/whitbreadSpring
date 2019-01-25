package com.jlau78.foursquare.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jlau78.foursquare.response.Meta;
import com.jlau78.foursquare.response.Response;
import com.jlau78.foursquare.response.VenueSearchRS;

import feign.FeignException;

// @FeignClient(value = "${service.foursquare.api.client.name:}", url =
// "${service.foursquare.api.client.url:}")
@FeignClient(value = "${service.foursquare.api.client.name}", url = "${service.foursquare.api.client.url}")
public interface PlacesApiClient {

	String QUERY = "query";
	String NEAR = "near";
	String INTENT = "intent";
	String INTENT_BROWSE = "browse";
	String API_VERSION = "v";
	String RADIUS = "radius";
	String CLIENT_ID = "client_id";
	String CLIENT_SECRET = "client_secret";

	@RequestMapping(value = "/venues/search", method = RequestMethod.GET, consumes = { MediaType.APPLICATION_JSON_VALUE })
	VenueSearchRS venueSearchByName(
	    @RequestParam(value = NEAR) String near,
	    @RequestParam(value = INTENT) String intent,
	    @RequestParam(value = API_VERSION) String apiVersion,
	    @RequestParam(value = CLIENT_ID) String clientId,
	    @RequestParam(value = CLIENT_SECRET) String clientSecret);

	@RequestMapping(value = "/venue/explore", method = RequestMethod.GET, consumes = { MediaType.APPLICATION_JSON_VALUE })
	VenueSearchRS venueRecommendationsByName(
	    @RequestParam(value = QUERY) String query,
	    @RequestParam(value = NEAR) String near,
	    @RequestParam(value = RADIUS) String radius,
	    @RequestParam(value = INTENT) String intent,
	    @RequestParam(value = API_VERSION) String apiVersion,
	    @RequestParam(value = CLIENT_ID) String clientId,
	    @RequestParam(value = CLIENT_SECRET) String clientSecret) throws FeignException;

}