package com.jlau78.foursquare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.jlau78.common.exceptions.AppException;
import com.jlau78.foursquare.client.PlacesApiClient;
import com.jlau78.foursquare.request.VenueRequest;
import com.jlau78.foursquare.response.VenueSearchRS;

import lombok.Getter;

@Controller
public class VenueSearchCall implements ApiCallService<VenueSearchRS, VenueRequest> {

	@Getter
	@Autowired
	private PlacesApiClient apiClient;

	@Override
	public VenueSearchRS call(VenueRequest request) throws AppException {
		VenueSearchRS response = null;
		
		if (request != null) {
			response = getApiClient().venueSearchByName(request.getNear(), request.getIntent(), request.getIntent(), 
												request.getClientId(), request.getClientSecret());
		}
		return response;
	}

}
