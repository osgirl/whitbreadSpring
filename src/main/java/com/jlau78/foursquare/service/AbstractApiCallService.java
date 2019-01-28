package com.jlau78.foursquare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.jlau78.foursquare.client.PlacesApiClient;
import com.jlau78.foursquare.request.VenueRequest;
import com.jlau78.foursquare.response.venue.VenueSearchRS;

import lombok.Getter;

public abstract class AbstractApiCallService<R,Q> implements ApiCallService<R, Q>  {

	@Getter
	@Autowired
	private PlacesApiClient venueQueryClient;

	@Value("${service.foursquare.api.api_version:20190222}")
	String apiVersion;

	@Value("${service.foursquare.api.client_id:}")
	String clientId;

	@Value("${service.foursquare.api.client_secret:}")
	String clientSecret;

	
	
}
