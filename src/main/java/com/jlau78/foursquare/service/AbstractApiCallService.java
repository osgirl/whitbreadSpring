package com.jlau78.foursquare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.jlau78.foursquare.client.PlacesApiClient;

import lombok.Getter;

public abstract class AbstractApiCallService<R, Q> implements ApiCallService {

	@Getter
	@Autowired
	private PlacesApiClient venueQueryClient;

	@Value("${service.foursquare.api.api_version}")
	String API_VERSION_DEFAULT = "20190122";

	@Value("${service.foursquare.api.client_id}")
	String CLIENT_ID;

	@Value("${service.foursquare.api.client_secret}")
	String CLIENT_SECRET;

	
	
}
